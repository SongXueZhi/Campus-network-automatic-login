/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package seleniumtest;
import java.util.Calendar;
import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;
/**
 *
 * @author sxz
 */
public class Seleniumtest {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) { 
        
        // TODO code application logic here
       TimerTask task = new LoginTimerTask();
        Calendar  calendar= Calendar.getInstance();    
        
        
        Date firstTime = calendar.getTime();
        //间隔：1小时
        long period = 1000 * 60 * 60;    
        //测试时间每分钟一次
        //long period = 1000 * 60;        
        
        Timer timer = new Timer();        
        timer.schedule(task, firstTime, period);
    }
    
}
