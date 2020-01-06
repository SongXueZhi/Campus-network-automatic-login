package seleniumtest;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Properties;
import java.util.TimerTask;
import java.util.concurrent.TimeUnit;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;

/*
 * 定时登录
 * */
public class LoginTimerTask extends TimerTask {

    private static List<Integer> loginTimes; //登录时刻表
    private static String key;               //配置文件中的url
    private final String DONGHUA = "东华大学";
    private final String BAIDU = "百度一下";
    static Properties prop = new Properties();

    /*
     * 静态初始化
     * */
    static {

        try (
                InputStream inStream = new FileInputStream(new File("url.properties"));) {
            prop.load(inStream);
        } catch (IOException ex) {
            System.out.println(ex.getMessage());
        }
        key = prop.getProperty("url");
//
//        initEatTimes();
    }

//    /*
//     * 初始化时间
//     * */
//    private static void initEatTimes() {
//        loginTimes = new ArrayList<Integer>();
//        loginTimes.add(6);
//        loginTimes.add(14);
//        loginTimes.add(18);
//    }

    /*
     * 执行
     * */
    @Override
    public void run() {
            loginTask();
    }

    //登录任务
    public void loginTask() {

        System.setProperty("webdriver.chrome.driver", "chromedriver.exe");//chromedriver服务地址
        WebDriver driver = new ChromeDriver(); //新建一个WebDriver 的对象，但是new 的是chrome的驱动
        driver.get("http:\\www.baidu.com/");
        driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
        //如果成功打开百度就退出
        if (driver.getTitle().contains(BAIDU)) {           
           driver.quit();
           return;
        } else if (driver.getTitle().contains(DONGHUA)) { //否则成功进入东华大学界面
             prop.setProperty("url", driver.getCurrentUrl()); //记录成功转向后的url，以备使用
            //根据html标签 执行登录事件
            driver.findElement(By.id("userphone")).sendKeys(new String[]{"2181791"});
            driver.findElement(By.id("password")).sendKeys(new String[]{"password"});
            driver.findElement(By.id("mobilelogin_submit")).click();
            
            //将成功打开的url存入配置文件
            try (
                    FileOutputStream oFile = new FileOutputStream(new File("url.properties"));) {
                prop.store(oFile, "new url");
            } catch (IOException ex) {
                System.out.println(ex.getMessage());
            }
        } else { //没有进入正常的界面，根据配置文件强制转入登录
            driver.navigate().to(key);
            driver.manage().timeouts().implicitlyWait(20, TimeUnit.SECONDS);
            int testnum=0;  //计数转向登录失败次数
            while (true) {               
                if (driver.getTitle().contains(DONGHUA)) {   //如果成功转入，则正常登陆
                    driver.findElement(By.id("userphone")).sendKeys(new String[]{"2181791"});
                    driver.findElement(By.id("password")).sendKeys(new String[]{"password"});
                    driver.findElement(By.id("mobilelogin_submit")).click();
                    break;
                } else {                
                  if(testnum>3){ //尝试4次 
                  System.out.println("登录失败");
                  break;                     
                  }
                  //刷新
                  driver.navigate().refresh();
                  ++testnum;
//                  //等待2分钟加载
                driver.manage().timeouts().implicitlyWait(120, TimeUnit.SECONDS);
                  //如果加载成功
                  if (driver.getTitle().contains(DONGHUA)) {
                    driver.findElement(By.id("userphone")).sendKeys(new String[]{"2181791"});
                    driver.findElement(By.id("password")).sendKeys(new String[]{"password"});
                    driver.findElement(By.id("mobilelogin_submit")).click();
                    break;
                } 
                }
            }
        }
        try {
            driver.manage().timeouts().implicitlyWait(20, TimeUnit.SECONDS);
        } catch (Exception e) {
            e.printStackTrace();
        }
        driver.quit();
    }

}
