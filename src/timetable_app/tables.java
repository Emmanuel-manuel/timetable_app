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
            //statement to create learning_area table
//            st.executeUpdate("create table learning_area_tbl (init int(11) AUTO_INCREMENT PRIMARY KEY, learning_area_id int(20),learning_area varchar(200),grade varchar(200) )");

            //statement to create facilitator table
//            st.executeUpdate("create table facilitator_tbl (init int(11) AUTO_INCREMENT PRIMARY KEY, facilitator_id int(20),username varchar(200),email varchar(200),contact int(10),grade varchar(200),learning_area varchar(200) )");
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
