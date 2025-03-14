/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package timetable_app;

import java.sql.Connection;
import java.sql.Statement;
import javax.swing.JOptionPane;

/**
 *
 * @author Lenovo-x130
 */
public class tables {

    public static void main(String[] args) {
        Connection con = null;
        Statement st = null;
        try {
            con = DBConnection.getConnection();
            st = con.createStatement();
//            st.executeUpdate("create table learning_area_tbl (init int(11) AUTO_INCREMENT PRIMARY KEY, learning_area_id int(20),learning_area varchar(200),grade varchar(200) )");



//            st.executeUpdate("create table employee_details (id int(11) AUTO_INCREMENT PRIMARY KEY, user_id int(20),name varchar(200),email varchar(100),contact int(13) )");
            
//            st.executeUpdate("create table products (id int(11) AUTO_INCREMENT PRIMARY KEY,product_name varchar(200),b_price int(11),s_price int(11),profit int(11),perc_profit DOUBLE )");

//            st.executeUpdate("create table inventory (id int(11) AUTO_INCREMENT PRIMARY KEY,product_name varchar(200), price_per_product int(20),qty_delivered int(20),yesterday_bal int(20),total_qty int(20),total_price int(20), today_rem int(20), date varchar(100) )");

//            st.executeUpdate("create table issued_goods (id int(11) AUTO_INCREMENT PRIMARY KEY,employee_name varchar(200),product_name varchar(200), price_per_product int(20),qty_given int(20),total_price int(20), date varchar(100) )");
            
//            st.executeUpdate("create table return_goods (id int(11) AUTO_INCREMENT PRIMARY KEY,employee_name varchar(200),product_name varchar(200), price_per_product int(20),qty_returned int(20),total_price int(20), date varchar(100) )");

            JOptionPane.showMessageDialog(null, "Table Created Successfully");
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, e);
        } finally {
            try {
                con.close();
                st.close();

            } catch (Exception e) {
            }
        }
    }

}
