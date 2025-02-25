/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package JFrames;

//import MiniFrames.*;
import java.awt.Color;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;
import timetable_app.*;

public class ManageUsers extends javax.swing.JFrame {

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

    public ManageUsers() {
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
        panel_menu = new javax.swing.JPanel();
        jPanel4 = new javax.swing.JPanel();
        jLabel4 = new javax.swing.JLabel();
        jPanel5 = new javax.swing.JPanel();
        lbl_homePage = new javax.swing.JLabel();
        jPanel6 = new javax.swing.JPanel();
        lbl_dashboard = new javax.swing.JLabel();
        jPanel7 = new javax.swing.JPanel();
        lbl_viewIssuedGood = new javax.swing.JLabel();
        jPanel8 = new javax.swing.JPanel();
        lbl_manageInventory = new javax.swing.JLabel();
        jPanel9 = new javax.swing.JPanel();
        lbl_manageUsers = new javax.swing.JLabel();
        jPanel10 = new javax.swing.JPanel();
        lbl_issueGood = new javax.swing.JLabel();
        jPanel11 = new javax.swing.JPanel();
        lbl_returnGood = new javax.swing.JLabel();
        jPanel12 = new javax.swing.JPanel();
        lbl_viewRecords = new javax.swing.JLabel();
        jPanel14 = new javax.swing.JPanel();
        lbl_logout = new javax.swing.JLabel();
        jPanel13 = new javax.swing.JPanel();
        lbl_defaulterList = new javax.swing.JLabel();
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
        jScrollPane2 = new javax.swing.JScrollPane();
        tbl_userDetails = new rojeru_san.complementos.RSTableMetro();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setUndecorated(true);
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
        jLabel1.setText("BLESSED SHOP");
        jPanel2.add(jLabel1, new org.netbeans.lib.awtextra.AbsoluteConstraints(560, 10, 280, -1));

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

        lbl_menu.setIcon(new javax.swing.ImageIcon(getClass().getResource("/res/adminIcons/icons8_menu_48px_1.png"))); // NOI18N
        lbl_menu.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                lbl_menuMouseClicked(evt);
            }
        });
        jPanel2.add(lbl_menu, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 10, 70, 50));

        jLabel2.setFont(new java.awt.Font("Verdana", 0, 20)); // NOI18N
        jLabel2.setForeground(new java.awt.Color(255, 255, 255));
        jLabel2.setIcon(new javax.swing.ImageIcon(getClass().getResource("/res/adminIcons/male_user_50px.png"))); // NOI18N
        jLabel2.setText("Welcome, Admin");
        jPanel2.add(jLabel2, new org.netbeans.lib.awtextra.AbsoluteConstraints(1070, 10, -1, -1));

        jPanel1.add(jPanel2, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 1370, 70));

        getContentPane().add(jPanel1, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 1370, -1));

        parentPanel.setLayout(null);

        panel_menu.setBackground(new java.awt.Color(51, 51, 51));
        panel_menu.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jPanel4.setBackground(new java.awt.Color(51, 51, 51));
        jPanel4.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jLabel4.setFont(new java.awt.Font("Yu Gothic UI Semilight", 1, 18)); // NOI18N
        jLabel4.setForeground(new java.awt.Color(255, 255, 255));
        jLabel4.setText("Features");
        jPanel4.add(jLabel4, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 16, 230, 30));

        panel_menu.add(jPanel4, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 130, 220, 60));

        jPanel5.setBackground(new java.awt.Color(51, 51, 51));
        jPanel5.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        lbl_homePage.setFont(new java.awt.Font("Yu Gothic UI Semilight", 1, 18)); // NOI18N
        lbl_homePage.setForeground(new java.awt.Color(153, 153, 153));
        lbl_homePage.setIcon(new javax.swing.ImageIcon(getClass().getResource("/res/adminIcons/icons8_Home_26px_2.png"))); // NOI18N
        lbl_homePage.setText("   Home Page");
        lbl_homePage.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                lbl_homePageMouseClicked(evt);
            }
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                lbl_homePageMouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                lbl_homePageMouseExited(evt);
            }
        });
        jPanel5.add(lbl_homePage, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 16, 190, 30));

        panel_menu.add(jPanel5, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 10, 250, 60));

        jPanel6.setBackground(new java.awt.Color(51, 51, 51));
        jPanel6.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        lbl_dashboard.setFont(new java.awt.Font("Yu Gothic UI Semilight", 1, 18)); // NOI18N
        lbl_dashboard.setForeground(new java.awt.Color(153, 153, 153));
        lbl_dashboard.setIcon(new javax.swing.ImageIcon(getClass().getResource("/res/adminIcons/icons8_Library_32px.png"))); // NOI18N
        lbl_dashboard.setText("  Dashboard");
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
        jPanel6.add(lbl_dashboard, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 16, 230, 30));

        panel_menu.add(jPanel6, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 70, 250, 60));

        jPanel7.setBackground(new java.awt.Color(51, 51, 51));
        jPanel7.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        lbl_viewIssuedGood.setFont(new java.awt.Font("Yu Gothic UI Semilight", 1, 18)); // NOI18N
        lbl_viewIssuedGood.setForeground(new java.awt.Color(153, 153, 153));
        lbl_viewIssuedGood.setIcon(new javax.swing.ImageIcon(getClass().getResource("/res/adminIcons/icons8_List_of_Thumbnails_50px.png"))); // NOI18N
        lbl_viewIssuedGood.setText("View Issued Goods");
        lbl_viewIssuedGood.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                lbl_viewIssuedGoodMouseClicked(evt);
            }
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                lbl_viewIssuedGoodMouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                lbl_viewIssuedGoodMouseExited(evt);
            }
        });
        jPanel7.add(lbl_viewIssuedGood, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 6, 240, 40));

        panel_menu.add(jPanel7, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 490, 250, 60));

        jPanel8.setBackground(new java.awt.Color(51, 51, 51));
        jPanel8.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        lbl_manageInventory.setFont(new java.awt.Font("Yu Gothic UI Semilight", 1, 18)); // NOI18N
        lbl_manageInventory.setForeground(new java.awt.Color(153, 153, 153));
        lbl_manageInventory.setIcon(new javax.swing.ImageIcon(getClass().getResource("/res/adminIcons/icons8_List_of_Thumbnails_50px.png"))); // NOI18N
        lbl_manageInventory.setText("Manage Inventory");
        lbl_manageInventory.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                lbl_manageInventoryMouseClicked(evt);
            }
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                lbl_manageInventoryMouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                lbl_manageInventoryMouseExited(evt);
            }
        });
        jPanel8.add(lbl_manageInventory, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 6, 240, 40));

        panel_menu.add(jPanel8, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 190, 250, 60));

        jPanel9.setBackground(new java.awt.Color(255, 153, 0));
        jPanel9.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        lbl_manageUsers.setFont(new java.awt.Font("Yu Gothic UI Semilight", 1, 18)); // NOI18N
        lbl_manageUsers.setForeground(new java.awt.Color(153, 153, 153));
        lbl_manageUsers.setIcon(new javax.swing.ImageIcon(getClass().getResource("/res/adminIcons/icons8_Read_Online_26px.png"))); // NOI18N
        lbl_manageUsers.setText("  Manage Users");
        jPanel9.add(lbl_manageUsers, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 16, 230, 30));

        panel_menu.add(jPanel9, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 250, 250, 60));

        jPanel10.setBackground(new java.awt.Color(51, 51, 51));
        jPanel10.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        lbl_issueGood.setFont(new java.awt.Font("Yu Gothic UI Semilight", 1, 18)); // NOI18N
        lbl_issueGood.setForeground(new java.awt.Color(153, 153, 153));
        lbl_issueGood.setIcon(new javax.swing.ImageIcon(getClass().getResource("/res/adminIcons/icons8_Sell_26px.png"))); // NOI18N
        lbl_issueGood.setText("  Issue Goods");
        lbl_issueGood.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                lbl_issueGoodMouseClicked(evt);
            }
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                lbl_issueGoodMouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                lbl_issueGoodMouseExited(evt);
            }
        });
        jPanel10.add(lbl_issueGood, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 16, 230, 30));

        panel_menu.add(jPanel10, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 310, 250, 60));

        jPanel11.setBackground(new java.awt.Color(51, 51, 51));
        jPanel11.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        lbl_returnGood.setFont(new java.awt.Font("Yu Gothic UI Semilight", 1, 18)); // NOI18N
        lbl_returnGood.setForeground(new java.awt.Color(153, 153, 153));
        lbl_returnGood.setIcon(new javax.swing.ImageIcon(getClass().getResource("/res/adminIcons/icons8_Return_Purchase_26px.png"))); // NOI18N
        lbl_returnGood.setText("  Return Goods");
        lbl_returnGood.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                lbl_returnGoodMouseClicked(evt);
            }
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                lbl_returnGoodMouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                lbl_returnGoodMouseExited(evt);
            }
        });
        jPanel11.add(lbl_returnGood, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 16, 230, 30));

        panel_menu.add(jPanel11, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 370, 250, 60));

        jPanel12.setBackground(new java.awt.Color(51, 51, 51));
        jPanel12.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        lbl_viewRecords.setFont(new java.awt.Font("Yu Gothic UI Semilight", 1, 18)); // NOI18N
        lbl_viewRecords.setForeground(new java.awt.Color(153, 153, 153));
        lbl_viewRecords.setIcon(new javax.swing.ImageIcon(getClass().getResource("/res/adminIcons/icons8_View_Details_26px.png"))); // NOI18N
        lbl_viewRecords.setText("  View Records");
        lbl_viewRecords.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                lbl_viewRecordsMouseClicked(evt);
            }
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                lbl_viewRecordsMouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                lbl_viewRecordsMouseExited(evt);
            }
        });
        jPanel12.add(lbl_viewRecords, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 16, 230, 30));

        panel_menu.add(jPanel12, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 430, 250, 60));

        jPanel14.setBackground(new java.awt.Color(153, 51, 0));
        jPanel14.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        lbl_logout.setFont(new java.awt.Font("Yu Gothic UI Semilight", 1, 18)); // NOI18N
        lbl_logout.setForeground(new java.awt.Color(255, 255, 255));
        lbl_logout.setIcon(new javax.swing.ImageIcon(getClass().getResource("/res/adminIcons/icons8_Exit_26px_2.png"))); // NOI18N
        lbl_logout.setText("  Log Out");
        lbl_logout.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                lbl_logoutMouseClicked(evt);
            }
        });
        jPanel14.add(lbl_logout, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 16, 230, 30));

        panel_menu.add(jPanel14, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 610, 250, 60));

        jPanel13.setBackground(new java.awt.Color(51, 51, 51));
        jPanel13.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        lbl_defaulterList.setFont(new java.awt.Font("Yu Gothic UI Semilight", 1, 18)); // NOI18N
        lbl_defaulterList.setForeground(new java.awt.Color(153, 153, 153));
        lbl_defaulterList.setIcon(new javax.swing.ImageIcon(getClass().getResource("/res/adminIcons/icons8_Conference_26px.png"))); // NOI18N
        lbl_defaulterList.setText("  Defaulter List");
        lbl_defaulterList.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                lbl_defaulterListMouseClicked(evt);
            }
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                lbl_defaulterListMouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                lbl_defaulterListMouseExited(evt);
            }
        });
        jPanel13.add(lbl_defaulterList, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 16, 230, 30));

        panel_menu.add(jPanel13, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 550, 250, 60));

        parentPanel.add(panel_menu);
        panel_menu.setBounds(0, 0, 250, 700);

        panel_display.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jPanel15.setBackground(new java.awt.Color(102, 153, 255));
        jPanel15.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(255, 255, 255), 2));
        jPanel15.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jLabel9.setFont(new java.awt.Font("Verdana", 0, 20)); // NOI18N
        jLabel9.setForeground(new java.awt.Color(255, 255, 255));
        jLabel9.setText("Enter User Id");
        jPanel15.add(jLabel9, new org.netbeans.lib.awtextra.AbsoluteConstraints(80, 180, 180, -1));

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
        jPanel15.add(txt_userId, new org.netbeans.lib.awtextra.AbsoluteConstraints(80, 210, 260, 40));

        jLabel5.setFont(new java.awt.Font("Verdana", 0, 20)); // NOI18N
        jLabel5.setForeground(new java.awt.Color(255, 255, 255));
        jLabel5.setIcon(new javax.swing.ImageIcon(getClass().getResource("/res/AddNewBookIcons/icons8_Contact_26px.png"))); // NOI18N
        jPanel15.add(jLabel5, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 200, 60, 50));

        jLabel6.setFont(new java.awt.Font("Verdana", 0, 20)); // NOI18N
        jLabel6.setForeground(new java.awt.Color(255, 255, 255));
        jLabel6.setIcon(new javax.swing.ImageIcon(getClass().getResource("/res/AddNewBookIcons/icons8_Collaborator_Male_26px.png"))); // NOI18N
        jPanel15.add(jLabel6, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 310, 60, 50));

        jLabel10.setFont(new java.awt.Font("Verdana", 0, 20)); // NOI18N
        jLabel10.setForeground(new java.awt.Color(255, 255, 255));
        jLabel10.setText("Enter User Name");
        jPanel15.add(jLabel10, new org.netbeans.lib.awtextra.AbsoluteConstraints(80, 290, 180, -1));

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
        jPanel15.add(txt_userName, new org.netbeans.lib.awtextra.AbsoluteConstraints(80, 320, 260, 40));

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
        jPanel15.add(jLabel13, new org.netbeans.lib.awtextra.AbsoluteConstraints(80, 400, 180, -1));

        txt_email.setBackground(new java.awt.Color(102, 153, 255));
        txt_email.setBorder(javax.swing.BorderFactory.createMatteBorder(0, 0, 2, 0, new java.awt.Color(255, 255, 255)));
        txt_email.setFont(new java.awt.Font("Tahoma", 0, 17)); // NOI18N
        txt_email.setPlaceholder("Please Enter Email ....");
        txt_email.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txt_emailActionPerformed(evt);
            }
        });
        jPanel15.add(txt_email, new org.netbeans.lib.awtextra.AbsoluteConstraints(80, 430, 270, 40));

        jLabel16.setFont(new java.awt.Font("Verdana", 0, 20)); // NOI18N
        jLabel16.setForeground(new java.awt.Color(255, 255, 255));
        jLabel16.setIcon(new javax.swing.ImageIcon(getClass().getResource("/res/icons/icons8_Secured_Letter_50px.png"))); // NOI18N
        jPanel15.add(jLabel16, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 420, 60, -1));

        jLabel15.setFont(new java.awt.Font("Verdana", 0, 20)); // NOI18N
        jLabel15.setForeground(new java.awt.Color(255, 255, 255));
        jLabel15.setText("Contact");
        jPanel15.add(jLabel15, new org.netbeans.lib.awtextra.AbsoluteConstraints(80, 520, 180, -1));

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
        jPanel15.add(txt_contact, new org.netbeans.lib.awtextra.AbsoluteConstraints(80, 550, 270, 40));

        jLabel14.setFont(new java.awt.Font("Verdana", 0, 20)); // NOI18N
        jLabel14.setForeground(new java.awt.Color(255, 255, 255));
        jLabel14.setIcon(new javax.swing.ImageIcon(getClass().getResource("/res/icons/icons8_Google_Mobile_50px.png"))); // NOI18N
        jPanel15.add(jLabel14, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 540, 60, 50));

        jLabel3.setFont(new java.awt.Font("Tahoma", 0, 30)); // NOI18N
        jLabel3.setForeground(new java.awt.Color(255, 255, 255));
        jLabel3.setIcon(new javax.swing.ImageIcon(getClass().getResource("/res/AddNewBookIcons/icons8_Student_Registration_100px_2.png"))); // NOI18N
        jLabel3.setText("  Manage Users'");
        jPanel15.add(jLabel3, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 20, 320, -1));

        jPanel16.setBackground(new java.awt.Color(255, 255, 255));
        jPanel16.setForeground(new java.awt.Color(255, 255, 255));

        javax.swing.GroupLayout jPanel16Layout = new javax.swing.GroupLayout(jPanel16);
        jPanel16.setLayout(jPanel16Layout);
        jPanel16Layout.setHorizontalGroup(
            jPanel16Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 190, Short.MAX_VALUE)
        );
        jPanel16Layout.setVerticalGroup(
            jPanel16Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 0, Short.MAX_VALUE)
        );

        jPanel15.add(jPanel16, new org.netbeans.lib.awtextra.AbsoluteConstraints(140, 90, 190, 4));

        panel_display.add(jPanel15, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 370, 766));

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

        getContentPane().add(parentPanel, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 70, 1370, 700));

        setSize(new java.awt.Dimension(1366, 766));
        setLocationRelativeTo(null);
    }// </editor-fold>//GEN-END:initComponents

    public void init() {
        Time.setTime(txtTime, txtDate);  // Calling the setTime method from the Time class

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

    private void lbl_viewIssuedGoodMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_lbl_viewIssuedGoodMouseClicked
        ViewIssuedGoodsDetails viewIssued = new ViewIssuedGoodsDetails();
        viewIssued.setVisible(true);
        dispose();
    }//GEN-LAST:event_lbl_viewIssuedGoodMouseClicked

    private void lbl_viewIssuedGoodMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_lbl_viewIssuedGoodMouseEntered
        jPanel7.setBackground(mouseEnterColor);
    }//GEN-LAST:event_lbl_viewIssuedGoodMouseEntered

    private void lbl_viewIssuedGoodMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_lbl_viewIssuedGoodMouseExited
        jPanel7.setBackground(mouseExitColor);
    }//GEN-LAST:event_lbl_viewIssuedGoodMouseExited

    private void lbl_manageInventoryMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_lbl_manageInventoryMouseClicked

        ManageInventory inventory = new ManageInventory();
        inventory.setVisible(true);
        dispose();

//        Displaying only JPanels
//        JPanel panel_manageInventory = manageInventory.getPanel_manageInventory();
//
//        if (panel_menu.isVisible()) {
//
//            // Ensure panel_manageBooks is added to parentPanel
//            parentPanel.add(panel_manageInventory);
//            // Set panel_display to invisible
//            panel_display.setVisible(false);
//            // Set panel_manageBooks to visible
//            panel_manageInventory.setVisible(true);
//            // Set the bounds of panel_manageBooks to fill the entire parentPanel
//            panel_manageInventory.setBounds(250, 0, parentPanel.getWidth(), parentPanel.getHeight());
//
//        } else {
//            panel_menu.setVisible(true);
//            // Adjust panel_display when panel_menu reappears
//            panel_manageInventory.setBounds(panel_menu.getWidth(), 0, parentPanel.getWidth() - panel_menu.getWidth(), 700);
//
//        }
//
//        // Force panel_display to re-layout its components
//        parentPanel.repaint();

    }//GEN-LAST:event_lbl_manageInventoryMouseClicked

    private void lbl_manageInventoryMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_lbl_manageInventoryMouseEntered
        jPanel8.setBackground(mouseEnterColor);
    }//GEN-LAST:event_lbl_manageInventoryMouseEntered

    private void lbl_manageInventoryMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_lbl_manageInventoryMouseExited
        jPanel8.setBackground(mouseExitColor);
    }//GEN-LAST:event_lbl_manageInventoryMouseExited

    private void lbl_issueGoodMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_lbl_issueGoodMouseClicked
        IssueGoods issue = new IssueGoods();
        issue.setVisible(true);
        dispose();
    }//GEN-LAST:event_lbl_issueGoodMouseClicked

    private void lbl_issueGoodMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_lbl_issueGoodMouseEntered
        jPanel10.setBackground(mouseEnterColor);
    }//GEN-LAST:event_lbl_issueGoodMouseEntered

    private void lbl_issueGoodMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_lbl_issueGoodMouseExited
        jPanel10.setBackground(mouseExitColor);
    }//GEN-LAST:event_lbl_issueGoodMouseExited

    private void lbl_returnGoodMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_lbl_returnGoodMouseClicked
        ReturnGood ret = new ReturnGood();
        ret.setVisible(true);
        dispose();
    }//GEN-LAST:event_lbl_returnGoodMouseClicked

    private void lbl_returnGoodMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_lbl_returnGoodMouseEntered
        jPanel11.setBackground(mouseEnterColor);
    }//GEN-LAST:event_lbl_returnGoodMouseEntered

    private void lbl_returnGoodMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_lbl_returnGoodMouseExited
        jPanel11.setBackground(mouseExitColor);
    }//GEN-LAST:event_lbl_returnGoodMouseExited

    private void lbl_viewRecordsMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_lbl_viewRecordsMouseClicked
        ViewAllRecords view = new ViewAllRecords();
        view.setVisible(true);
        dispose();
    }//GEN-LAST:event_lbl_viewRecordsMouseClicked

    private void lbl_viewRecordsMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_lbl_viewRecordsMouseEntered
        jPanel12.setBackground(mouseEnterColor);
    }//GEN-LAST:event_lbl_viewRecordsMouseEntered

    private void lbl_viewRecordsMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_lbl_viewRecordsMouseExited
        jPanel12.setBackground(mouseExitColor);
    }//GEN-LAST:event_lbl_viewRecordsMouseExited

    private void lbl_logoutMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_lbl_logoutMouseClicked
        LoginPage login = new LoginPage();
        login.setVisible(true);
        dispose();
    }//GEN-LAST:event_lbl_logoutMouseClicked

    private void lbl_defaulterListMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_lbl_defaulterListMouseClicked
        DefaulterList dl = new DefaulterList();
        dl.setVisible(true);
        dispose();
    }//GEN-LAST:event_lbl_defaulterListMouseClicked

    private void lbl_defaulterListMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_lbl_defaulterListMouseEntered
        jPanel13.setBackground(mouseEnterColor);
    }//GEN-LAST:event_lbl_defaulterListMouseEntered

    private void lbl_defaulterListMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_lbl_defaulterListMouseExited
        jPanel13.setBackground(mouseExitColor);
    }//GEN-LAST:event_lbl_defaulterListMouseExited

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

    private void lbl_dashboardMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_lbl_dashboardMouseEntered
        jPanel6.setBackground(mouseEnterColor);
    }//GEN-LAST:event_lbl_dashboardMouseEntered

    private void lbl_dashboardMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_lbl_dashboardMouseExited
        jPanel6.setBackground(mouseExitColor);
    }//GEN-LAST:event_lbl_dashboardMouseExited

    private void lbl_homePageMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_lbl_homePageMouseEntered
        jPanel5.setBackground(mouseEnterColor);
    }//GEN-LAST:event_lbl_homePageMouseEntered

    private void lbl_homePageMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_lbl_homePageMouseExited
        jPanel5.setBackground(mouseExitColor);
    }//GEN-LAST:event_lbl_homePageMouseExited

    private void lbl_homePageMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_lbl_homePageMouseClicked
        Home_page home = new Home_page();
        home.setVisible(true);
        dispose();
    }//GEN-LAST:event_lbl_homePageMouseClicked

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

    private void lbl_dashboardMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_lbl_dashboardMouseClicked
        // TODO add your handling code here:
        Dashboard dash = new Dashboard();
        dash.setVisible(true);
        dispose();
    }//GEN-LAST:event_lbl_dashboardMouseClicked

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
            java.util.logging.Logger.getLogger(ManageUsers.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(ManageUsers.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(ManageUsers.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(ManageUsers.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new ManageUsers().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private rojerusan.RSMaterialButtonCircle btn_add;
    private rojerusan.RSMaterialButtonCircle btn_delete;
    private rojerusan.RSMaterialButtonCircle btn_update;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel13;
    private javax.swing.JLabel jLabel14;
    private javax.swing.JLabel jLabel15;
    private javax.swing.JLabel jLabel16;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel10;
    private javax.swing.JPanel jPanel11;
    private javax.swing.JPanel jPanel12;
    private javax.swing.JPanel jPanel13;
    private javax.swing.JPanel jPanel14;
    private javax.swing.JPanel jPanel15;
    private javax.swing.JPanel jPanel16;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JPanel jPanel5;
    private javax.swing.JPanel jPanel6;
    private javax.swing.JPanel jPanel7;
    private javax.swing.JPanel jPanel8;
    private javax.swing.JPanel jPanel9;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JLabel lbl_close;
    private javax.swing.JLabel lbl_dashboard;
    private javax.swing.JLabel lbl_defaulterList;
    private javax.swing.JLabel lbl_homePage;
    private javax.swing.JLabel lbl_issueGood;
    private javax.swing.JLabel lbl_logout;
    private javax.swing.JLabel lbl_manageInventory;
    private javax.swing.JLabel lbl_manageUsers;
    private javax.swing.JLabel lbl_menu;
    private javax.swing.JLabel lbl_returnGood;
    private javax.swing.JLabel lbl_viewIssuedGood;
    private javax.swing.JLabel lbl_viewRecords;
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
