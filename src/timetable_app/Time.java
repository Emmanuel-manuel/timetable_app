/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package timetable_app;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JLabel;

/**
 *
 * @author Lenovo-x130
 */
public class Time {
    //    Displays Current Date & Time
    public static void setTime(JLabel txtTime, JLabel txtDate) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                while (true) {
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException ex) {
                        Logger.getLogger(Time.class.getName()).log(Level.SEVERE, null, ex);
                    }

                    Date date = new Date();
                    SimpleDateFormat tf = new SimpleDateFormat("h:mm:ss aa");
                    SimpleDateFormat dd = new SimpleDateFormat("yyyy-MM-dd");
                   
//                    SimpleDateFormat dd = new SimpleDateFormat("EEEE, dd/MM/yyyy");

                    String time = tf.format(date);
                    txtTime.setText(time.split(" ")[0] + " " + time.split(" ")[1]);
                    txtDate.setText(dd.format(date));
                }
            }
        }).start();
    }
}
