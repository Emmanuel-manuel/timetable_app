/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package JFrames;

//import MiniFrames.*;
import java.awt.Color;
import java.awt.event.ActionEvent;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import javax.swing.JOptionPane;
import javax.swing.Timer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;
import timetable_app.*;

public class ManageFacilitators extends javax.swing.JFrame {

    // Global Variables
    String userName, email, contact;
    String userId;
    DefaultTableModel model;

//    Initialize components
//    ManageInventory manageInventory = new ManageInventory();

    // Gets the window's screen position
    int xx, xy;

    //Global variable for Hover Effect
    Color mouseEnterColor = new Color(255, 153, 0);
    Color mouseExitColor = new Color(51, 51, 51);

    public ManageFacilitators() {
        initComponents();
        init();
        setUserDetailsToTable();
    }

    //to pull the users' details from the db to the table
    public void setUserDetailsToTable() {

        try {
            Connection con = DBConnection.getConnection();

            Statement st = con.createStatement();
            ResultSet rs = st.executeQuery("select * from employee_details");

            while (rs.next()) {
                String userId = rs.getString("user_id");
                String userName = rs.getString("name");
                String email = rs.getString("email");
                String contact = rs.getString("contact");

                Object[] obj = {userId, userName, email, contact};
                model = (DefaultTableModel) tbl_userDetails.getModel();
                //adds a row array
                model.addRow(obj);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    //to add user to the database in users table
    public boolean addUser() {

        boolean isAdded = false;

        userId = txt_userId.getText();
        userName = txt_userName.getText();
        email = txt_email.getText();
        contact = txt_contact.getText();

        try {
//            Connection con = DBConnection.getConnection();
//            String sql = "insert into employee_details values(?,?,?,?)";
//            PreparedStatement pst = con.prepareStatement(sql);

            Connection con = DBConnection.getConnection();
            String sql = "insert into employee_details (user_id, name, email, contact) values(?, ?, ?, ?)";
            PreparedStatement pst = con.prepareStatement(sql);

            //sets the values from the textfield to the colums in the db
            pst.setString(1, userId);
            pst.setString(2, userName);
            pst.setString(3, email);
            pst.setString(4, contact);

            //If a database row is added to output a success message
            int rowCount = pst.executeUpdate();

            if (rowCount > 0) {
                isAdded = true;
            } else {
                isAdded = false;
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        //returns the 'isAdded' variable value
        return isAdded;

    }

    //method to Update the user details
    public boolean updateUser() {

        boolean isUpdated = false;

        userId = txt_userId.getText();
        userName = txt_userName.getText();
        email = txt_email.getText();
        contact = txt_contact.getText();

        try {
            Connection con = DBConnection.getConnection();
            String sql = "update employee_details set name = ?, email = ?, contact = ? where user_id = ?";
            PreparedStatement pst = con.prepareStatement(sql);

            //sets the values from the textfield to the colums in the db
            pst.setString(1, userName);
            pst.setString(2, email);
            pst.setString(3, contact);
            pst.setString(4, userId);

            //If a database row is added to output a success message
            int rowCount = pst.executeUpdate();

            if (rowCount > 0) {
                isUpdated = true;
            } else {
                isUpdated = false;
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        //returns the 'isAdded' variable value
        return isUpdated;

    }

    //method to delete user detail
    public boolean deleteUser() {

        boolean isDeleted = false;

//        studentId = Integer.parseInt(txt_studentId.getText());
        userId = txt_userId.getText();

        try {
            Connection con = DBConnection.getConnection();
            String sql = "delete from employee_details where user_id = ?";
            PreparedStatement pst = con.prepareStatement(sql);

            //sets the values from the textfield to the colums in the db
            pst.setString(1, userId);

            //If a database row is added to output a success message
            int rowCount = pst.executeUpdate();

            if (rowCount > 0) {
                isDeleted = true;
            } else {
                isDeleted = false;
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        //returns the 'isAdded' variable value
        return isDeleted;
    }

    //method to clear jtable before adding new data on it
    public void clearTable() {
        DefaultTableModel model = (DefaultTableModel) tbl_userDetails.getModel();
        model.setRowCount(0);
    }
    
    private void clearComponents() {
        txt_userId.setText("");
        txt_userName.setText("");
        txt_email.setText("");
        txt_contact.setText("");
//        jDateDOB.setDate(null);
//        cboGender.setSelectedIndex(0);
//        jlabelimage.setIcon(null);
//        tbl_details.clearSelection();
//        imagePath = null;
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel1 = new javax.swing.JPanel();
        jPanel2 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        lbl_close = new javax.swing.JLabel();
        txtTime = new javax.swing.JLabel();
        txtDate = new javax.swing.JLabel();
        jPanel3 = new javax.swing.JPanel();
        lbl_menu = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        parentPanel = new javax.swing.JPanel();
        panel_display = new javax.swing.JPanel();
        jPanel15 = new javax.swing.JPanel();
        jLabel9 = new javax.swing.JLabel();
        txt_userId = new app.bolivia.swing.JCTextField();
        jLabel5 = new javax.swing.JLabel();
        jLabel6 = new javax.swing.JLabel();
        jLabel10 = new javax.swing.JLabel();
        txt_userName = new app.bolivia.swing.JCTextField();
        btn_delete = new rojerusan.RSMaterialButtonCircle();
        btn_add = new rojerusan.RSMaterialButtonCircle();
        btn_update = new rojerusan.RSMaterialButtonCircle();
        jLabel13 = new javax.swing.JLabel();
        txt_email = new app.bolivia.swing.JCTextField();
        jLabel16 = new javax.swing.JLabel();
        jLabel15 = new javax.swing.JLabel();
        txt_contact = new app.bolivia.swing.JCTextField();
        jLabel14 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        jPanel16 = new javax.swing.JPanel();
        jLabel17 = new javax.swing.JLabel();
        jLabel18 = new javax.swing.JLabel();
        jLabel19 = new javax.swing.JLabel();
        jLabel20 = new javax.swing.JLabel();
        cbo_grade = new rojerusan.RSComboMetro();
        cbo_learningArea = new rojerusan.RSComboMetro();
        jScrollPane2 = new javax.swing.JScrollPane();
        tbl_userDetails = new rojeru_san.complementos.RSTableMetro();
        panel_menu = new javax.swing.JPanel();
        jPanel6 = new javax.swing.JPanel();
        jLabel4 = new javax.swing.JLabel();
        jPanel7 = new javax.swing.JPanel();
        lbl_manageSubjects = new javax.swing.JLabel();
        jPanel8 = new javax.swing.JPanel();
        lbl_dashboard = new javax.swing.JLabel();
        jPanel11 = new javax.swing.JPanel();
        lbl_manageTutors = new javax.swing.JLabel();
        jPanel17 = new javax.swing.JPanel();
        lbl_logout = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setMinimumSize(new java.awt.Dimension(1370, 764));
        setUndecorated(true);
        setPreferredSize(new java.awt.Dimension(1370, 764));
        getContentPane().setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jPanel1.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jPanel2.setBackground(new java.awt.Color(102, 153, 255));
        jPanel2.addMouseMotionListener(new java.awt.event.MouseMotionAdapter() {
            public void mouseDragged(java.awt.event.MouseEvent evt) {
                jPanel2MouseDragged(evt);
            }
        });
        jPanel2.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jPanel2MouseClicked(evt);
            }
        });
        jPanel2.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jLabel1.setFont(new java.awt.Font("Times New Roman", 1, 36)); // NOI18N
        jLabel1.setForeground(new java.awt.Color(255, 255, 255));
        jLabel1.setText("TIME-TABLE APPLICATION");
        jPanel2.add(jLabel1, new org.netbeans.lib.awtextra.AbsoluteConstraints(520, 10, 500, -1));

        lbl_close.setFont(new java.awt.Font("Arial", 1, 48)); // NOI18N
        lbl_close.setForeground(new java.awt.Color(255, 255, 255));
        lbl_close.setText("X");
        lbl_close.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                lbl_closeMouseClicked(evt);
            }
        });
        jPanel2.add(lbl_close, new org.netbeans.lib.awtextra.AbsoluteConstraints(1320, 10, 40, 40));

        txtTime.setFont(new java.awt.Font("Times New Roman", 1, 20)); // NOI18N
        txtTime.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jPanel2.add(txtTime, new org.netbeans.lib.awtextra.AbsoluteConstraints(250, 10, 180, 30));

        txtDate.setFont(new java.awt.Font("Times New Roman", 0, 18)); // NOI18N
        txtDate.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jPanel2.add(txtDate, new org.netbeans.lib.awtextra.AbsoluteConstraints(250, 40, 180, 30));

        jPanel3.setBackground(new java.awt.Color(51, 51, 51));

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 5, Short.MAX_VALUE)
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 50, Short.MAX_VALUE)
        );

        jPanel2.add(jPanel3, new org.netbeans.lib.awtextra.AbsoluteConstraints(100, 10, 5, 50));

        lbl_menu.setIcon(new javax.swing.ImageIcon(getClass().getResource("/res/icons/icons8_menu_48px_1.png"))); // NOI18N
        lbl_menu.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                lbl_menuMouseClicked(evt);
            }
        });
        jPanel2.add(lbl_menu, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 10, 70, 50));

        jLabel2.setFont(new java.awt.Font("Verdana", 0, 20)); // NOI18N
        jLabel2.setForeground(new java.awt.Color(255, 255, 255));
        jLabel2.setIcon(new javax.swing.ImageIcon(getClass().getResource("/res/icons/male_user_50px.png"))); // NOI18N
        jLabel2.setText("Welcome, Admin");
        jPanel2.add(jLabel2, new org.netbeans.lib.awtextra.AbsoluteConstraints(1070, 10, -1, -1));

        jPanel1.add(jPanel2, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 1370, 70));

        getContentPane().add(jPanel1, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 1370, -1));

        parentPanel.setLayout(null);

        panel_display.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jPanel15.setBackground(new java.awt.Color(102, 153, 255));
        jPanel15.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(255, 255, 255), 2));
        jPanel15.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jLabel9.setFont(new java.awt.Font("Verdana", 0, 20)); // NOI18N
        jLabel9.setForeground(new java.awt.Color(255, 255, 255));
        jLabel9.setText("Enter User Id");
        jPanel15.add(jLabel9, new org.netbeans.lib.awtextra.AbsoluteConstraints(80, 70, 180, -1));

        txt_userId.setBackground(new java.awt.Color(102, 153, 255));
        txt_userId.setBorder(javax.swing.BorderFactory.createMatteBorder(0, 0, 2, 0, new java.awt.Color(255, 255, 255)));
        txt_userId.setFont(new java.awt.Font("Tahoma", 0, 17)); // NOI18N
        txt_userId.setPlaceholder("Enter User Id ....");
        txt_userId.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                txt_userIdFocusLost(evt);
            }
        });
        txt_userId.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txt_userIdActionPerformed(evt);
            }
        });
        txt_userId.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                txt_userIdKeyTyped(evt);
            }
        });
        jPanel15.add(txt_userId, new org.netbeans.lib.awtextra.AbsoluteConstraints(80, 90, 260, 40));

        jLabel5.setFont(new java.awt.Font("Verdana", 0, 20)); // NOI18N
        jLabel5.setForeground(new java.awt.Color(255, 255, 255));
        jLabel5.setIcon(new javax.swing.ImageIcon(getClass().getResource("/res/icons/icons8_Contact_26px.png"))); // NOI18N
        jPanel15.add(jLabel5, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 80, 50, 50));

        jLabel6.setFont(new java.awt.Font("Verdana", 0, 20)); // NOI18N
        jLabel6.setForeground(new java.awt.Color(255, 255, 255));
        jLabel6.setIcon(new javax.swing.ImageIcon(getClass().getResource("/res/icons/icons8_Collaborator_Male_26px.png"))); // NOI18N
        jPanel15.add(jLabel6, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 160, 50, 50));

        jLabel10.setFont(new java.awt.Font("Verdana", 0, 20)); // NOI18N
        jLabel10.setForeground(new java.awt.Color(255, 255, 255));
        jLabel10.setText("Enter User Name");
        jPanel15.add(jLabel10, new org.netbeans.lib.awtextra.AbsoluteConstraints(80, 150, 180, -1));

        txt_userName.setBackground(new java.awt.Color(102, 153, 255));
        txt_userName.setBorder(javax.swing.BorderFactory.createMatteBorder(0, 0, 2, 0, new java.awt.Color(255, 255, 255)));
        txt_userName.setFont(new java.awt.Font("Tahoma", 0, 17)); // NOI18N
        txt_userName.setPlaceholder("Enter User Name ....");
        txt_userName.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                txt_userNameFocusLost(evt);
            }
        });
        txt_userName.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txt_userNameActionPerformed(evt);
            }
        });
        jPanel15.add(txt_userName, new org.netbeans.lib.awtextra.AbsoluteConstraints(80, 170, 260, 40));

        btn_delete.setBackground(new java.awt.Color(255, 102, 51));
        btn_delete.setText("DELETE");
        btn_delete.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_deleteActionPerformed(evt);
            }
        });
        jPanel15.add(btn_delete, new org.netbeans.lib.awtextra.AbsoluteConstraints(260, 610, 110, 60));

        btn_add.setBackground(new java.awt.Color(255, 102, 51));
        btn_add.setText("ADD");
        btn_add.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_addActionPerformed(evt);
            }
        });
        jPanel15.add(btn_add, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 610, 110, 60));

        btn_update.setBackground(new java.awt.Color(255, 102, 51));
        btn_update.setText("UPDATE");
        btn_update.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_updateActionPerformed(evt);
            }
        });
        jPanel15.add(btn_update, new org.netbeans.lib.awtextra.AbsoluteConstraints(130, 610, 110, 60));

        jLabel13.setFont(new java.awt.Font("Verdana", 0, 20)); // NOI18N
        jLabel13.setForeground(new java.awt.Color(255, 255, 255));
        jLabel13.setText("Email");
        jPanel15.add(jLabel13, new org.netbeans.lib.awtextra.AbsoluteConstraints(80, 230, 180, -1));

        txt_email.setBackground(new java.awt.Color(102, 153, 255));
        txt_email.setBorder(javax.swing.BorderFactory.createMatteBorder(0, 0, 2, 0, new java.awt.Color(255, 255, 255)));
        txt_email.setFont(new java.awt.Font("Tahoma", 0, 17)); // NOI18N
        txt_email.setPlaceholder("Please Enter Email ....");
        txt_email.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txt_emailActionPerformed(evt);
            }
        });
        jPanel15.add(txt_email, new org.netbeans.lib.awtextra.AbsoluteConstraints(80, 250, 270, 40));

        jLabel16.setFont(new java.awt.Font("Verdana", 0, 20)); // NOI18N
        jLabel16.setForeground(new java.awt.Color(255, 255, 255));
        jLabel16.setIcon(new javax.swing.ImageIcon(getClass().getResource("/res/icons/icons8_Secured_Letter_50px.png"))); // NOI18N
        jPanel15.add(jLabel16, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 240, 60, -1));

        jLabel15.setFont(new java.awt.Font("Verdana", 0, 20)); // NOI18N
        jLabel15.setForeground(new java.awt.Color(255, 255, 255));
        jLabel15.setText("Contact");
        jPanel15.add(jLabel15, new org.netbeans.lib.awtextra.AbsoluteConstraints(80, 310, 180, -1));

        txt_contact.setBackground(new java.awt.Color(102, 153, 255));
        txt_contact.setBorder(javax.swing.BorderFactory.createMatteBorder(0, 0, 2, 0, new java.awt.Color(255, 255, 255)));
        txt_contact.setFont(new java.awt.Font("Tahoma", 0, 17)); // NOI18N
        txt_contact.setPlaceholder("Please Enter Phone Number ....");
        txt_contact.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txt_contactActionPerformed(evt);
            }
        });
        txt_contact.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                txt_contactKeyTyped(evt);
            }
        });
        jPanel15.add(txt_contact, new org.netbeans.lib.awtextra.AbsoluteConstraints(80, 330, 270, 40));

        jLabel14.setFont(new java.awt.Font("Verdana", 0, 20)); // NOI18N
        jLabel14.setForeground(new java.awt.Color(255, 255, 255));
        jLabel14.setIcon(new javax.swing.ImageIcon(getClass().getResource("/res/icons/icons8_Google_Mobile_50px.png"))); // NOI18N
        jPanel15.add(jLabel14, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 320, 60, 50));

        jLabel3.setFont(new java.awt.Font("Tahoma", 0, 30)); // NOI18N
        jLabel3.setForeground(new java.awt.Color(255, 255, 255));
        jLabel3.setText("  Manage Facilitators'");
        jPanel15.add(jLabel3, new org.netbeans.lib.awtextra.AbsoluteConstraints(40, 10, 300, -1));

        jPanel16.setBackground(new java.awt.Color(255, 255, 255));
        jPanel16.setForeground(new java.awt.Color(255, 255, 255));

        javax.swing.GroupLayout jPanel16Layout = new javax.swing.GroupLayout(jPanel16);
        jPanel16.setLayout(jPanel16Layout);
        jPanel16Layout.setHorizontalGroup(
            jPanel16Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 259, Short.MAX_VALUE)
        );
        jPanel16Layout.setVerticalGroup(
            jPanel16Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 3, Short.MAX_VALUE)
        );

        jPanel15.add(jPanel16, new org.netbeans.lib.awtextra.AbsoluteConstraints(60, 50, 259, 3));

        jLabel17.setFont(new java.awt.Font("Verdana", 0, 20)); // NOI18N
        jLabel17.setForeground(new java.awt.Color(255, 255, 255));
        jLabel17.setIcon(new javax.swing.ImageIcon(getClass().getResource("/res/icons/icons8_Unit_26px.png"))); // NOI18N
        jPanel15.add(jLabel17, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 500, 40, 50));

        jLabel18.setFont(new java.awt.Font("Verdana", 0, 20)); // NOI18N
        jLabel18.setForeground(new java.awt.Color(255, 255, 255));
        jLabel18.setText("Learning Area");
        jPanel15.add(jLabel18, new org.netbeans.lib.awtextra.AbsoluteConstraints(80, 490, 180, -1));

        jLabel19.setFont(new java.awt.Font("Verdana", 0, 20)); // NOI18N
        jLabel19.setForeground(new java.awt.Color(255, 255, 255));
        jLabel19.setText("Grade");
        jPanel15.add(jLabel19, new org.netbeans.lib.awtextra.AbsoluteConstraints(80, 400, 80, -1));

        jLabel20.setFont(new java.awt.Font("Verdana", 0, 20)); // NOI18N
        jLabel20.setForeground(new java.awt.Color(255, 255, 255));
        jLabel20.setIcon(new javax.swing.ImageIcon(getClass().getResource("/res/icons/icons8_Library_32px.png"))); // NOI18N
        jPanel15.add(jLabel20, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 410, 50, 50));

        cbo_grade.setForeground(new java.awt.Color(0, 0, 0));
        cbo_grade.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Grade 1", "Grade 2 ", "Grade 3", "Grade 4", "Grade 5", "Grade 6", "Grade 7", "Grade 8", "Grade 9" }));
        cbo_grade.setColorBorde(new java.awt.Color(102, 153, 255));
        cbo_grade.setColorFondo(new java.awt.Color(255, 153, 0));
        cbo_grade.setFont(new java.awt.Font("Times New Roman", 1, 18)); // NOI18N
        jPanel15.add(cbo_grade, new org.netbeans.lib.awtextra.AbsoluteConstraints(80, 430, 190, 30));

        cbo_learningArea.setForeground(new java.awt.Color(0, 0, 0));
        cbo_learningArea.setColorBorde(new java.awt.Color(102, 153, 255));
        cbo_learningArea.setColorFondo(new java.awt.Color(255, 153, 0));
        cbo_learningArea.setFont(new java.awt.Font("Times New Roman", 1, 18)); // NOI18N
        cbo_learningArea.setPreferredSize(new java.awt.Dimension(200, 30));
        jPanel15.add(cbo_learningArea, new org.netbeans.lib.awtextra.AbsoluteConstraints(80, 520, 280, 35));

        panel_display.add(jPanel15, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 370, 690));

        tbl_userDetails.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "User Id", "Name", "Email", "Contact"
            }
        ));
        tbl_userDetails.setColorBackgoundHead(new java.awt.Color(102, 153, 255));
        tbl_userDetails.setColorBordeFilas(new java.awt.Color(102, 153, 255));
        tbl_userDetails.setColorFilasBackgound2(new java.awt.Color(255, 255, 255));
        tbl_userDetails.setColorSelBackgound(new java.awt.Color(255, 153, 0));
        tbl_userDetails.setFont(new java.awt.Font("Yu Gothic UI Light", 0, 25)); // NOI18N
        tbl_userDetails.setFuenteFilas(new java.awt.Font("Yu Gothic UI Semibold", 0, 18)); // NOI18N
        tbl_userDetails.setFuenteFilasSelect(new java.awt.Font("Yu Gothic UI", 1, 20)); // NOI18N
        tbl_userDetails.setFuenteHead(new java.awt.Font("Yu Gothic UI Semibold", 1, 20)); // NOI18N
        tbl_userDetails.setIntercellSpacing(new java.awt.Dimension(0, 0));
        tbl_userDetails.setRowHeight(22);
        tbl_userDetails.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tbl_userDetailsMouseClicked(evt);
            }
        });
        jScrollPane2.setViewportView(tbl_userDetails);

        panel_display.add(jScrollPane2, new org.netbeans.lib.awtextra.AbsoluteConstraints(370, 0, 750, 680));

        parentPanel.add(panel_display);
        panel_display.setBounds(250, 0, 1120, 700);

        panel_menu.setBackground(new java.awt.Color(51, 51, 51));
        panel_menu.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jPanel6.setBackground(new java.awt.Color(51, 51, 51));
        jPanel6.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jLabel4.setFont(new java.awt.Font("Yu Gothic UI Semilight", 1, 18)); // NOI18N
        jLabel4.setForeground(new java.awt.Color(255, 255, 255));
        jLabel4.setText("Features");
        jPanel6.add(jLabel4, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 10, 120, 30));

        panel_menu.add(jPanel6, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 250, 60));

        jPanel7.setBackground(new java.awt.Color(51, 51, 51));
        jPanel7.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        lbl_manageSubjects.setFont(new java.awt.Font("Yu Gothic UI Semilight", 1, 18)); // NOI18N
        lbl_manageSubjects.setForeground(new java.awt.Color(153, 153, 153));
        lbl_manageSubjects.setIcon(new javax.swing.ImageIcon(getClass().getResource("/res/icons/icons8_Unit_26px.png"))); // NOI18N
        lbl_manageSubjects.setText(" Manage Learning Areas");
        lbl_manageSubjects.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                lbl_manageSubjectsMouseClicked(evt);
            }
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                lbl_manageSubjectsMouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                lbl_manageSubjectsMouseExited(evt);
            }
        });
        jPanel7.add(lbl_manageSubjects, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 10, 250, 40));

        panel_menu.add(jPanel7, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 120, 250, 60));

        jPanel8.setBackground(new java.awt.Color(51, 51, 51));
        jPanel8.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        lbl_dashboard.setFont(new java.awt.Font("Yu Gothic UI Semilight", 1, 18)); // NOI18N
        lbl_dashboard.setForeground(new java.awt.Color(153, 153, 153));
        lbl_dashboard.setIcon(new javax.swing.ImageIcon(getClass().getResource("/res/icons/icons8_Library_32px.png"))); // NOI18N
        lbl_dashboard.setText("Dashboard");
        lbl_dashboard.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                lbl_dashboardMouseClicked(evt);
            }
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                lbl_dashboardMouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                lbl_dashboardMouseExited(evt);
            }
        });
        jPanel8.add(lbl_dashboard, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 10, 250, 40));

        panel_menu.add(jPanel8, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 60, 250, 60));

        jPanel11.setBackground(new java.awt.Color(255, 153, 0));
        jPanel11.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        lbl_manageTutors.setFont(new java.awt.Font("Yu Gothic UI Semilight", 1, 18)); // NOI18N
        lbl_manageTutors.setForeground(new java.awt.Color(153, 153, 153));
        lbl_manageTutors.setIcon(new javax.swing.ImageIcon(getClass().getResource("/res/icons/icons8_Collaborator_Male_26px.png"))); // NOI18N
        lbl_manageTutors.setText(" Manage Facilitators");
        lbl_manageTutors.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                lbl_manageTutorsMouseClicked(evt);
            }
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                lbl_manageTutorsMouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                lbl_manageTutorsMouseExited(evt);
            }
        });
        jPanel11.add(lbl_manageTutors, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 20, 240, 30));

        panel_menu.add(jPanel11, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 180, 250, 60));

        jPanel17.setBackground(new java.awt.Color(153, 51, 0));
        jPanel17.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        lbl_logout.setFont(new java.awt.Font("Yu Gothic UI Semilight", 1, 18)); // NOI18N
        lbl_logout.setForeground(new java.awt.Color(255, 255, 255));
        lbl_logout.setText("  Log Out");
        lbl_logout.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                lbl_logoutMouseClicked(evt);
            }
        });
        jPanel17.add(lbl_logout, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 20, 160, 30));

        panel_menu.add(jPanel17, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 240, 250, 60));

        parentPanel.add(panel_menu);
        panel_menu.setBounds(0, 0, 250, 310);

        getContentPane().add(parentPanel, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 70, 1370, 690));

        setSize(new java.awt.Dimension(1366, 757));
        setLocationRelativeTo(null);
    }// </editor-fold>//GEN-END:initComponents

    public void init() {
        Time.setTime(txtTime, txtDate);  // Calling the setTime method from the Time class

    }

//    for seamless JFrame migration, this method causes a 2-seconds delay before disposing the previous JFrame
    public void delayBeforeClosingPreviousJframe() {
        // Create a Timer with a 4000ms (4 seconds) delay
        Timer timer = new Timer(2000, (ActionEvent e) -> {
            dispose(); // Dispose the LandingPage JFrame after 4 seconds
        });

        timer.setRepeats(false); // Ensure the timer only fires once
        timer.start(); // Start the timer
    }
    
    

    private void lbl_closeMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_lbl_closeMouseClicked
        int a = JOptionPane.showConfirmDialog(null, "Do you really want to Close Application?", "Select", JOptionPane.YES_NO_OPTION);
        if (a == 0) {
            System.exit(0);
        }
    }//GEN-LAST:event_lbl_closeMouseClicked

    private void jPanel2MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jPanel2MouseClicked

        xx = evt.getX();
        xy = evt.getY();
    }//GEN-LAST:event_jPanel2MouseClicked

    private void jPanel2MouseDragged(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jPanel2MouseDragged

        int x = evt.getXOnScreen();
        int y = evt.getYOnScreen();
        this.setLocation(x - xx, y - xy);
    }//GEN-LAST:event_jPanel2MouseDragged

    private void lbl_menuMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_lbl_menuMouseClicked

        if (panel_menu.isVisible()) {
            panel_menu.setVisible(false);
            // Adjust panel_display when panel_menu disappears
            panel_display.setBounds(0, 0, parentPanel.getWidth(), parentPanel.getHeight());

        } else {
            panel_menu.setVisible(true);
            // Adjust panel_display when panel_menu reappears
            panel_display.setBounds(panel_menu.getWidth(), 0, parentPanel.getWidth() - panel_menu.getWidth(), 700);

        }
        // Force panel_display to re-layout its components
        parentPanel.repaint();
    }//GEN-LAST:event_lbl_menuMouseClicked

    private void txt_userIdFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txt_userIdFocusLost

    }//GEN-LAST:event_txt_userIdFocusLost

    private void txt_userIdActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txt_userIdActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txt_userIdActionPerformed

    private void txt_userNameFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txt_userNameFocusLost
        // TODO add your handling code here:
    }//GEN-LAST:event_txt_userNameFocusLost

    private void txt_userNameActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txt_userNameActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txt_userNameActionPerformed

    private void btn_deleteActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_deleteActionPerformed
        if (deleteUser() == true) {
            JOptionPane.showMessageDialog(this, "User Deleted Successfully...");
            clearTable();
            setUserDetailsToTable();
        } else {
            JOptionPane.showMessageDialog(this, "User Deletion failed, Please check your Database Connection...");
        }
    }//GEN-LAST:event_btn_deleteActionPerformed

    private void btn_addActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_addActionPerformed
        if (addUser() == true) {
            JOptionPane.showMessageDialog(this, "User Added Successfully...");
            clearTable();
            setUserDetailsToTable();
            clearComponents();
        } else {
            JOptionPane.showMessageDialog(this, "User Addition failed, Please check your Database Connection...");
        }
    }//GEN-LAST:event_btn_addActionPerformed

    private void btn_updateActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_updateActionPerformed
        if (updateUser() == true) {
            JOptionPane.showMessageDialog(this, "User Updated Successfully...");
            clearTable();
            setUserDetailsToTable();
            clearComponents();
        } else {
            JOptionPane.showMessageDialog(this, "User Updation failed, Please check your Database Connection...");
        }
    }//GEN-LAST:event_btn_updateActionPerformed

    private void txt_contactActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txt_contactActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txt_contactActionPerformed

    private void txt_emailActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txt_emailActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txt_emailActionPerformed

    private void tbl_userDetailsMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tbl_userDetailsMouseClicked

        int rowNo = tbl_userDetails.getSelectedRow();
        TableModel model = tbl_userDetails.getModel();

        txt_userId.setText(model.getValueAt(rowNo, 0).toString());
        txt_userName.setText(model.getValueAt(rowNo, 1).toString());
        txt_email.setText(model.getValueAt(rowNo, 2).toString());
        txt_contact.setText(model.getValueAt(rowNo, 3).toString());

    }//GEN-LAST:event_tbl_userDetailsMouseClicked

    private void txt_userIdKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txt_userIdKeyTyped
        if (!Character.isDigit(evt.getKeyChar())) {
            evt.consume();
        }
    }//GEN-LAST:event_txt_userIdKeyTyped

    private void txt_contactKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txt_contactKeyTyped
        if (!Character.isDigit(evt.getKeyChar())) {
            evt.consume();
        }
    }//GEN-LAST:event_txt_contactKeyTyped

    private void lbl_manageSubjectsMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_lbl_manageSubjectsMouseEntered
        jPanel7.setBackground(mouseEnterColor);
    }//GEN-LAST:event_lbl_manageSubjectsMouseEntered

    private void lbl_manageSubjectsMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_lbl_manageSubjectsMouseExited
                jPanel7.setBackground(mouseExitColor);
    }//GEN-LAST:event_lbl_manageSubjectsMouseExited

    private void lbl_dashboardMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_lbl_dashboardMouseClicked
        LandingPage land = new LandingPage();
        land.setVisible(true);

        delayBeforeClosingPreviousJframe();
    }//GEN-LAST:event_lbl_dashboardMouseClicked

    private void lbl_dashboardMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_lbl_dashboardMouseEntered
        jPanel8.setBackground(mouseEnterColor);
    }//GEN-LAST:event_lbl_dashboardMouseEntered

    private void lbl_dashboardMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_lbl_dashboardMouseExited
        jPanel6.setBackground(mouseExitColor);
    }//GEN-LAST:event_lbl_dashboardMouseExited

    private void lbl_manageTutorsMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_lbl_manageTutorsMouseClicked
        ManageFacilitators users = new ManageFacilitators();
        users.setVisible(true);

        delayBeforeClosingPreviousJframe();
    }//GEN-LAST:event_lbl_manageTutorsMouseClicked

    private void lbl_manageTutorsMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_lbl_manageTutorsMouseEntered
        jPanel11.setBackground(mouseEnterColor);
    }//GEN-LAST:event_lbl_manageTutorsMouseEntered

    private void lbl_manageTutorsMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_lbl_manageTutorsMouseExited
//        jPanel11.setBackground(mouseExitColor);
    }//GEN-LAST:event_lbl_manageTutorsMouseExited

    private void lbl_logoutMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_lbl_logoutMouseClicked
        LoginPage login = new LoginPage();
        login.setVisible(true);

        delayBeforeClosingPreviousJframe();
    }//GEN-LAST:event_lbl_logoutMouseClicked

    private void lbl_manageSubjectsMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_lbl_manageSubjectsMouseClicked
        ManageLearningArea mla = new ManageLearningArea();
        mla.setVisible(true);

        delayBeforeClosingPreviousJframe();
    }//GEN-LAST:event_lbl_manageSubjectsMouseClicked

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(ManageFacilitators.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(ManageFacilitators.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(ManageFacilitators.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(ManageFacilitators.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new ManageFacilitators().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private rojerusan.RSMaterialButtonCircle btn_add;
    private rojerusan.RSMaterialButtonCircle btn_delete;
    private rojerusan.RSMaterialButtonCircle btn_update;
    private rojerusan.RSComboMetro cbo_grade;
    private rojerusan.RSComboMetro cbo_learningArea;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel13;
    private javax.swing.JLabel jLabel14;
    private javax.swing.JLabel jLabel15;
    private javax.swing.JLabel jLabel16;
    private javax.swing.JLabel jLabel17;
    private javax.swing.JLabel jLabel18;
    private javax.swing.JLabel jLabel19;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel20;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel11;
    private javax.swing.JPanel jPanel15;
    private javax.swing.JPanel jPanel16;
    private javax.swing.JPanel jPanel17;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel6;
    private javax.swing.JPanel jPanel7;
    private javax.swing.JPanel jPanel8;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JLabel lbl_close;
    private javax.swing.JLabel lbl_dashboard;
    private javax.swing.JLabel lbl_logout;
    private javax.swing.JLabel lbl_manageSubjects;
    private javax.swing.JLabel lbl_manageTutors;
    private javax.swing.JLabel lbl_menu;
    private javax.swing.JPanel panel_display;
    private javax.swing.JPanel panel_menu;
    private javax.swing.JPanel parentPanel;
    private rojeru_san.complementos.RSTableMetro tbl_userDetails;
    private javax.swing.JLabel txtDate;
    private javax.swing.JLabel txtTime;
    private app.bolivia.swing.JCTextField txt_contact;
    private app.bolivia.swing.JCTextField txt_email;
    private app.bolivia.swing.JCTextField txt_userId;
    private app.bolivia.swing.JCTextField txt_userName;
    // End of variables declaration//GEN-END:variables
}
