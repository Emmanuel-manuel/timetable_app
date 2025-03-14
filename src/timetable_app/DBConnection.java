/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package timetable_app;

import java.sql.Connection;
import java.sql.DriverManager;

/**
 *
 * @author EMMANUEL
 */
public class DBConnection {
    
    static Connection con = null;
    
    public static Connection getConnection(){
        try{
            Class.forName("com.mysql.jdbc.Driver"); //Register MySql driver
            con = DriverManager.getConnection("jdbc:mysql://localhost:3306/timetable","root","");
            
        } catch (Exception e){
            // handles error by printing where the error is emanating from
            
            e.printStackTrace();
            
        }
        // this returns the connection
        return con;
    }
    
}
