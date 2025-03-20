/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package JFrames;

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

/**
 *
 * @author Lenovo-x130
 */
public class ManageLearningArea extends javax.swing.JFrame {

    // Gets the window's screen position
    int xx, xy;

    //Global variable for Hover Effect
    Color mouseEnterColor = new Color(255, 153, 0);
    Color mouseExitColor = new Color(51, 51, 51);

    // Global Variables
    String learningArea, grade, learningAreaId;
//    int learningAreaId;
    DefaultTableModel model;

    //    for seamless JFrame migration, this method causes a 2-seconds delay before disposing the previous JFrame
    public void delayBeforeClosingPreviousJframe() {
        // Create a Timer with a 4000ms (4 seconds) delay
        Timer timer = new Timer(2000, (ActionEvent e) -> {
            dispose(); // Dispose the LandingPage JFrame after 4 seconds
        });

        timer.setRepeats(false); // Ensure the timer only fires once
        timer.start(); // Start the timer
    }

    /**
     * Creates new form ManageSubjects
     */
    public ManageLearningArea() {
        initComponents();
        init();
        autoIncrementLearningAreaId(); // Call the method to auto-increment the ID
    }

    //to pull the learningArea' details from the db to the table
    public void setLearningAreaDetailsToTable() {

        try {
            Connection con = DBConnection.getConnection();

            Statement st = con.createStatement();
            ResultSet rs = st.executeQuery("select * from learning_area_tbl");

            while (rs.next()) {
                learningAreaId = rs.getString("learning_area_id");
                learningArea = rs.getString("learning_area");
                grade = rs.getString("grade");

                Object[] obj = {learningAreaId, learningArea, grade};
                model = (DefaultTableModel) tbl_learningAreaDetails.getModel();
                //adds a row array
                model.addRow(obj);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // Method to auto-increment the Learning Area ID and set it in the text field
    public void autoIncrementLearningAreaId() {
        try {
            // Establish a database connection
            Connection con = DBConnection.getConnection();

            // Query to get the maximum value of learning_area_id
            String sql = "SELECT MAX(learning_area_id) AS max_id FROM learning_area_tbl";
            PreparedStatement pst = con.prepareStatement(sql);

            // Execute the query
            ResultSet rs = pst.executeQuery();

            int maxId = 0; // Default value if no records exist

            // Get the maximum learning_area_id from the result set
            if (rs.next()) {
                maxId = rs.getInt("max_id");
            }

            // Increment the maximum ID by 1
            int newId = maxId + 1;

            // Set the new ID in the text field
            txt_leaningArearId.setText(String.valueOf(newId));

            // Close the database resources
            rs.close();
            pst.close();
            con.close();

        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error while auto-incrementing Learning Area ID: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    //to add Learning Area to the database in 'learning_area' table
    public boolean addLearningArea() {

        boolean isAdded = false;

        learningAreaId = txt_leaningArearId.getText();
        learningArea = txt_learningAreaName.getText();
        grade = (String) cbo_grade.getSelectedItem();

        try {
            Connection con = DBConnection.getConnection();

            // Check if a record with the same learning_area and grade already exists
            String checkSql = "SELECT COUNT(*) AS count FROM learning_area_tbl WHERE learning_area = ? AND grade = ?";
            PreparedStatement checkPst = con.prepareStatement(checkSql);
            checkPst.setString(1, learningArea);
            checkPst.setString(2, grade);

            ResultSet rs = checkPst.executeQuery();

            if (rs.next()) {
                int count = rs.getInt("count");

                // If no duplicate record exists, proceed with the insertion
                if (count == 0) {

                    String sql = "insert into learning_area_tbl (learning_area_id, learning_area, grade) values(?, ?, ?)";
                    PreparedStatement pst = con.prepareStatement(sql);

                    //sets the values from the textfield to the colums in the db
                    pst.setString(1, learningAreaId);
                    pst.setString(2, learningArea);
                    pst.setString(3, grade);

                    //If a database row is added to output a success message
                    int rowCount = pst.executeUpdate();

                    // If the insertion is successful, set isAdded to true
                    if (rowCount > 0) {
                        isAdded = true;
                    }

                    // Close the insert PreparedStatement
                    pst.close();

                } else {
                    // If a duplicate record exists, show a message to the user
                    JOptionPane.showMessageDialog(this, "A record with the same Learning Area and Grade already exists!", "Duplicate Entry", JOptionPane.WARNING_MESSAGE);
                }
            }

            // Close the check PreparedStatement and ResultSet
            rs.close();
            checkPst.close();
            con.close();

        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error while adding Learning Area: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }

        // Return the status of the operation
        return isAdded;
    }

    //method to Update the learning area details
    public boolean updateLearningArea() {

        boolean isUpdated = false;

        // Get values from the input fields
        learningAreaId = txt_leaningArearId.getText();
        learningArea = txt_learningAreaName.getText();
        grade = (String) cbo_grade.getSelectedItem();

        try {
            Connection con = DBConnection.getConnection();

            // Check if another record with the same learning_area and grade already exists (excluding the current record)
            String checkSql = "SELECT COUNT(*) AS count FROM learning_area_tbl WHERE learning_area = ? AND grade = ? AND learning_area_id != ?";
            PreparedStatement checkPst = con.prepareStatement(checkSql);
            checkPst.setString(1, learningArea);
            checkPst.setString(2, grade);
            checkPst.setString(3, learningAreaId);

            ResultSet rs = checkPst.executeQuery();

            if (rs.next()) {
                int count = rs.getInt("count");

                // If no duplicate record exists, proceed with the update
                if (count == 0) {
                    // SQL query to update the learning area
                    String updateSql = "update learning_area_tbl set learning_area = ?, grade = ? where learning_area_id = ?";
                    PreparedStatement pst = con.prepareStatement(updateSql);

                    //sets the values from the textfield to the colums in the db
                    pst.setString(1, learningArea);
                    pst.setString(2, grade);
                    pst.setString(3, learningAreaId);

                    //If a database row is added to output a success message
                    int rowCount = pst.executeUpdate();

                    if (rowCount > 0) {
                        isUpdated = true;
                    }
                    // Close the update PreparedStatement
                    pst.close();
                } else {
                    // If a duplicate record exists, show a message to the user
                    JOptionPane.showMessageDialog(this, "A record with the same Learning Area and Grade already exists!", "Duplicate Entry", JOptionPane.WARNING_MESSAGE);
                }
            }

            // Close the check PreparedStatement and ResultSet
            rs.close();
            checkPst.close();
            con.close();

        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error while updating Learning Area: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }

        // Return the status of the operation
        return isUpdated;
    }

    //method to delete learningArea detail
    public boolean deleteLearningArea() {

        boolean isDeleted = false;

        learningAreaId = txt_leaningArearId.getText();

        try {
            Connection con = DBConnection.getConnection();
            String sql = "delete from learning_area_tbl where learning_area_id = ?";
            PreparedStatement pst = con.prepareStatement(sql);

            //sets the values from the textfield to the colums in the db
            pst.setString(1, learningAreaId);

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
        DefaultTableModel model = (DefaultTableModel) tbl_learningAreaDetails.getModel();
        model.setRowCount(0);
    }

    private void clearComponents() {
        txt_leaningArearId.setText("");
        txt_learningAreaName.setText("");
        cbo_grade.setSelectedIndex(0);
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel2 = new javax.swing.JPanel();
        lbl_menu = new javax.swing.JLabel();
        jLabel1 = new javax.swing.JLabel();
        lbl_close = new javax.swing.JLabel();
        txtTime = new javax.swing.JLabel();
        txtDate = new javax.swing.JLabel();
        jPanel3 = new javax.swing.JPanel();
        jLabel2 = new javax.swing.JLabel();
        parentPanel = new javax.swing.JPanel();
        panel_menu = new javax.swing.JPanel();
        jPanel6 = new javax.swing.JPanel();
        jLabel4 = new javax.swing.JLabel();
        jPanel7 = new javax.swing.JPanel();
        lbl_manageSubjects = new javax.swing.JLabel();
        jPanel8 = new javax.swing.JPanel();
        lbl_dashboard = new javax.swing.JLabel();
        jPanel11 = new javax.swing.JPanel();
        lbl_manageTutors = new javax.swing.JLabel();
        jPanel15 = new javax.swing.JPanel();
        lbl_logout = new javax.swing.JLabel();
        panel_display = new javax.swing.JPanel();
        panelSearch = new javax.swing.JPanel();
        txt_searchLearningArea = new app.bolivia.swing.JCTextField();
        jLabel7 = new javax.swing.JLabel();
        jPanel17 = new javax.swing.JPanel();
        jLabel9 = new javax.swing.JLabel();
        txt_leaningArearId = new app.bolivia.swing.JCTextField();
        jLabel5 = new javax.swing.JLabel();
        jLabel6 = new javax.swing.JLabel();
        jLabel10 = new javax.swing.JLabel();
        txt_learningAreaName = new app.bolivia.swing.JCTextField();
        btn_delete = new rojerusan.RSMaterialButtonCircle();
        btn_add = new rojerusan.RSMaterialButtonCircle();
        btn_update = new rojerusan.RSMaterialButtonCircle();
        jLabel19 = new javax.swing.JLabel();
        jLabel20 = new javax.swing.JLabel();
        cbo_grade = new rojerusan.RSComboMetro();
        jPanel4 = new javax.swing.JPanel();
        jLabel3 = new javax.swing.JLabel();
        jPanel16 = new javax.swing.JPanel();
        jScrollPane2 = new javax.swing.JScrollPane();
        tbl_learningAreaDetails = new rojeru_san.complementos.RSTableMetro();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setMinimumSize(new java.awt.Dimension(1370, 765));
        setUndecorated(true);
        setPreferredSize(new java.awt.Dimension(1370, 765));
        getContentPane().setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

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

        lbl_menu.setIcon(new javax.swing.ImageIcon(getClass().getResource("/res/icons/icons8_menu_48px_1.png"))); // NOI18N
        lbl_menu.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                lbl_menuMouseClicked(evt);
            }
        });
        jPanel2.add(lbl_menu, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 10, 70, 50));

        jLabel1.setFont(new java.awt.Font("Times New Roman", 1, 36)); // NOI18N
        jLabel1.setForeground(new java.awt.Color(255, 255, 255));
        jLabel1.setText("TIME-TABLE APPLICATION");
        jPanel2.add(jLabel1, new org.netbeans.lib.awtextra.AbsoluteConstraints(470, 10, 510, -1));

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
        jPanel2.add(txtTime, new org.netbeans.lib.awtextra.AbsoluteConstraints(230, 10, 200, 30));

        txtDate.setFont(new java.awt.Font("Times New Roman", 0, 18)); // NOI18N
        txtDate.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jPanel2.add(txtDate, new org.netbeans.lib.awtextra.AbsoluteConstraints(230, 40, 210, 30));

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

        jLabel2.setFont(new java.awt.Font("Verdana", 0, 20)); // NOI18N
        jLabel2.setForeground(new java.awt.Color(255, 255, 255));
        jLabel2.setIcon(new javax.swing.ImageIcon(getClass().getResource("/res/icons/male_user_50px.png"))); // NOI18N
        jLabel2.setText("Welcome, Admin");
        jPanel2.add(jLabel2, new org.netbeans.lib.awtextra.AbsoluteConstraints(1070, 10, -1, -1));

        getContentPane().add(jPanel2, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 1370, 70));

        parentPanel.setLayout(null);

        panel_menu.setBackground(new java.awt.Color(51, 51, 51));
        panel_menu.setMinimumSize(new java.awt.Dimension(250, 300));
        panel_menu.setPreferredSize(new java.awt.Dimension(250, 300));
        panel_menu.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jPanel6.setBackground(new java.awt.Color(51, 51, 51));
        jPanel6.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jLabel4.setFont(new java.awt.Font("Yu Gothic UI Semilight", 1, 18)); // NOI18N
        jLabel4.setForeground(new java.awt.Color(255, 255, 255));
        jLabel4.setText("Features");
        jPanel6.add(jLabel4, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 10, 120, 30));

        panel_menu.add(jPanel6, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 240, 60));

        jPanel7.setBackground(new java.awt.Color(255, 153, 0));
        jPanel7.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        lbl_manageSubjects.setFont(new java.awt.Font("Yu Gothic UI Semilight", 1, 18)); // NOI18N
        lbl_manageSubjects.setForeground(new java.awt.Color(153, 153, 153));
        lbl_manageSubjects.setIcon(new javax.swing.ImageIcon(getClass().getResource("/res/icons/icons8_Unit_26px.png"))); // NOI18N
        lbl_manageSubjects.setText(" Manage Learning Areas");
        lbl_manageSubjects.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                lbl_manageSubjectsMouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                lbl_manageSubjectsMouseExited(evt);
            }
        });
        jPanel7.add(lbl_manageSubjects, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 16, 250, 30));

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

        jPanel11.setBackground(new java.awt.Color(51, 51, 51));
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

        jPanel15.setBackground(new java.awt.Color(153, 51, 0));
        jPanel15.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        lbl_logout.setFont(new java.awt.Font("Yu Gothic UI Semilight", 1, 18)); // NOI18N
        lbl_logout.setForeground(new java.awt.Color(255, 255, 255));
        lbl_logout.setText("  Log Out");
        lbl_logout.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                lbl_logoutMouseClicked(evt);
            }
        });
        jPanel15.add(lbl_logout, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 20, 160, 30));

        panel_menu.add(jPanel15, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 240, 250, 60));

        parentPanel.add(panel_menu);
        panel_menu.setBounds(0, 0, 250, 310);

        panel_display.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        panelSearch.setBackground(new java.awt.Color(255, 153, 0));
        panelSearch.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        txt_searchLearningArea.setBackground(new java.awt.Color(255, 153, 0));
        txt_searchLearningArea.setBorder(javax.swing.BorderFactory.createMatteBorder(0, 0, 2, 0, new java.awt.Color(255, 255, 255)));
        txt_searchLearningArea.setFont(new java.awt.Font("Century Gothic", 1, 21)); // NOI18N
        txt_searchLearningArea.setOpaque(false);
        txt_searchLearningArea.setPlaceholder("Type to Search using the Name of the Learning Area ....");
        txt_searchLearningArea.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                txt_searchLearningAreaFocusLost(evt);
            }
        });
        txt_searchLearningArea.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txt_searchLearningAreaActionPerformed(evt);
            }
        });
        txt_searchLearningArea.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                txt_searchLearningAreaKeyReleased(evt);
            }
        });
        panelSearch.add(txt_searchLearningArea, new org.netbeans.lib.awtextra.AbsoluteConstraints(130, 0, 570, 50));

        jLabel7.setIcon(new javax.swing.ImageIcon(getClass().getResource("/res/icons/search.png"))); // NOI18N
        panelSearch.add(jLabel7, new org.netbeans.lib.awtextra.AbsoluteConstraints(70, 0, 50, 40));

        panel_display.add(panelSearch, new org.netbeans.lib.awtextra.AbsoluteConstraints(420, 0, 700, 50));

        jPanel17.setBackground(new java.awt.Color(102, 153, 255));
        jPanel17.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(255, 255, 255), 2));
        jPanel17.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jLabel9.setFont(new java.awt.Font("Verdana", 0, 20)); // NOI18N
        jLabel9.setForeground(new java.awt.Color(255, 255, 255));
        jLabel9.setText("Learning Area Id");
        jPanel17.add(jLabel9, new org.netbeans.lib.awtextra.AbsoluteConstraints(80, 70, 180, -1));

        txt_leaningArearId.setEditable(false);
        txt_leaningArearId.setBackground(new java.awt.Color(102, 153, 255));
        txt_leaningArearId.setBorder(javax.swing.BorderFactory.createMatteBorder(0, 0, 2, 0, new java.awt.Color(255, 255, 255)));
        txt_leaningArearId.setFont(new java.awt.Font("Tahoma", 0, 17)); // NOI18N
        txt_leaningArearId.setPlaceholder("Learning Area Id ....");
        txt_leaningArearId.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                txt_leaningArearIdFocusLost(evt);
            }
        });
        txt_leaningArearId.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txt_leaningArearIdActionPerformed(evt);
            }
        });
        txt_leaningArearId.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                txt_leaningArearIdKeyTyped(evt);
            }
        });
        jPanel17.add(txt_leaningArearId, new org.netbeans.lib.awtextra.AbsoluteConstraints(80, 90, 310, 40));

        jLabel5.setFont(new java.awt.Font("Verdana", 0, 20)); // NOI18N
        jLabel5.setForeground(new java.awt.Color(255, 255, 255));
        jLabel5.setIcon(new javax.swing.ImageIcon(getClass().getResource("/res/icons/icons8_View_Details_26px.png"))); // NOI18N
        jPanel17.add(jLabel5, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 80, 50, 50));

        jLabel6.setFont(new java.awt.Font("Verdana", 0, 20)); // NOI18N
        jLabel6.setForeground(new java.awt.Color(255, 255, 255));
        jLabel6.setIcon(new javax.swing.ImageIcon(getClass().getResource("/res/icons/icons8_Books_26px.png"))); // NOI18N
        jPanel17.add(jLabel6, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 160, 50, 50));

        jLabel10.setFont(new java.awt.Font("Verdana", 0, 20)); // NOI18N
        jLabel10.setForeground(new java.awt.Color(255, 255, 255));
        jLabel10.setText("Learning Area");
        jPanel17.add(jLabel10, new org.netbeans.lib.awtextra.AbsoluteConstraints(80, 150, 180, -1));

        txt_learningAreaName.setBackground(new java.awt.Color(102, 153, 255));
        txt_learningAreaName.setBorder(javax.swing.BorderFactory.createMatteBorder(0, 0, 2, 0, new java.awt.Color(255, 255, 255)));
        txt_learningAreaName.setFont(new java.awt.Font("Tahoma", 0, 17)); // NOI18N
        txt_learningAreaName.setPlaceholder("Name of the Learning Area ....");
        txt_learningAreaName.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                txt_learningAreaNameFocusLost(evt);
            }
        });
        txt_learningAreaName.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txt_learningAreaNameActionPerformed(evt);
            }
        });
        jPanel17.add(txt_learningAreaName, new org.netbeans.lib.awtextra.AbsoluteConstraints(80, 170, 310, 40));

        btn_delete.setBackground(new java.awt.Color(255, 102, 51));
        btn_delete.setText("DELETE");
        btn_delete.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_deleteActionPerformed(evt);
            }
        });
        jPanel17.add(btn_delete, new org.netbeans.lib.awtextra.AbsoluteConstraints(260, 610, 110, 60));

        btn_add.setBackground(new java.awt.Color(255, 102, 51));
        btn_add.setText("ADD");
        btn_add.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_addActionPerformed(evt);
            }
        });
        jPanel17.add(btn_add, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 610, 110, 60));

        btn_update.setBackground(new java.awt.Color(255, 102, 51));
        btn_update.setText("UPDATE");
        btn_update.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_updateActionPerformed(evt);
            }
        });
        jPanel17.add(btn_update, new org.netbeans.lib.awtextra.AbsoluteConstraints(130, 610, 110, 60));

        jLabel19.setFont(new java.awt.Font("Verdana", 0, 20)); // NOI18N
        jLabel19.setForeground(new java.awt.Color(255, 255, 255));
        jLabel19.setText("Grade");
        jPanel17.add(jLabel19, new org.netbeans.lib.awtextra.AbsoluteConstraints(80, 230, 80, -1));

        jLabel20.setFont(new java.awt.Font("Verdana", 0, 20)); // NOI18N
        jLabel20.setForeground(new java.awt.Color(255, 255, 255));
        jLabel20.setIcon(new javax.swing.ImageIcon(getClass().getResource("/res/icons/icons8_Library_32px.png"))); // NOI18N
        jPanel17.add(jLabel20, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 240, 50, 50));

        cbo_grade.setForeground(new java.awt.Color(0, 0, 0));
        cbo_grade.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Grade 1", "Grade 2 ", "Grade 3", "Grade 4", "Grade 5", "Grade 6", "Grade 7", "Grade 8", "Grade 9" }));
        cbo_grade.setColorBorde(new java.awt.Color(102, 153, 255));
        cbo_grade.setColorFondo(new java.awt.Color(255, 153, 0));
        cbo_grade.setFont(new java.awt.Font("Times New Roman", 1, 18)); // NOI18N
        jPanel17.add(cbo_grade, new org.netbeans.lib.awtextra.AbsoluteConstraints(80, 260, 190, 30));

        jPanel4.setBackground(new java.awt.Color(102, 153, 255));
        jPanel4.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jLabel3.setFont(new java.awt.Font("Tahoma", 0, 30)); // NOI18N
        jLabel3.setForeground(new java.awt.Color(255, 255, 255));
        jLabel3.setText("  Manage Learning Area");
        jPanel4.add(jLabel3, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 330, -1));

        jPanel16.setBackground(new java.awt.Color(255, 255, 255));
        jPanel16.setForeground(new java.awt.Color(255, 255, 255));
        jPanel16.setPreferredSize(new java.awt.Dimension(300, 2));

        javax.swing.GroupLayout jPanel16Layout = new javax.swing.GroupLayout(jPanel16);
        jPanel16.setLayout(jPanel16Layout);
        jPanel16Layout.setHorizontalGroup(
            jPanel16Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 300, Short.MAX_VALUE)
        );
        jPanel16Layout.setVerticalGroup(
            jPanel16Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 2, Short.MAX_VALUE)
        );

        jPanel4.add(jPanel16, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 40, 300, 2));

        jPanel17.add(jPanel4, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 10, 340, 50));

        panel_display.add(jPanel17, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 420, 690));

        tbl_learningAreaDetails.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Learning Area Id", "Learning Area", "Grade"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        tbl_learningAreaDetails.setColorBackgoundHead(new java.awt.Color(102, 153, 255));
        tbl_learningAreaDetails.setColorBordeFilas(new java.awt.Color(102, 153, 255));
        tbl_learningAreaDetails.setColorFilasBackgound2(new java.awt.Color(255, 255, 255));
        tbl_learningAreaDetails.setColorSelBackgound(new java.awt.Color(255, 153, 0));
        tbl_learningAreaDetails.setFont(new java.awt.Font("Yu Gothic UI Light", 0, 25)); // NOI18N
        tbl_learningAreaDetails.setFuenteFilas(new java.awt.Font("Yu Gothic UI Semibold", 0, 18)); // NOI18N
        tbl_learningAreaDetails.setFuenteFilasSelect(new java.awt.Font("Yu Gothic UI", 1, 20)); // NOI18N
        tbl_learningAreaDetails.setFuenteHead(new java.awt.Font("Yu Gothic UI Semibold", 1, 20)); // NOI18N
        tbl_learningAreaDetails.setIntercellSpacing(new java.awt.Dimension(0, 0));
        tbl_learningAreaDetails.setRowHeight(26);
        tbl_learningAreaDetails.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tbl_learningAreaDetailsMouseClicked(evt);
            }
        });
        jScrollPane2.setViewportView(tbl_learningAreaDetails);

        panel_display.add(jScrollPane2, new org.netbeans.lib.awtextra.AbsoluteConstraints(420, 50, 700, 630));

        parentPanel.add(panel_display);
        panel_display.setBounds(250, 0, 1120, 700);

        getContentPane().add(parentPanel, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 70, 1370, 700));

        pack();
    }// </editor-fold>//GEN-END:initComponents

    public void init() {
        Time.setTime(txtTime, txtDate);  // Calling the setTime method from the Time class

        setLearningAreaDetailsToTable();
    }

    private void lbl_menuMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_lbl_menuMouseClicked

        if (panel_menu.isVisible()) {
            panel_menu.setVisible(false);
            // Adjust panel_display when panel_menu disappears
            parentPanel.setBounds(0, 0, parentPanel.getWidth(), parentPanel.getHeight());

        } else {
            panel_menu.setVisible(true);
            // Adjust panel_display when panel_menu reappears
            parentPanel.setBounds(panel_menu.getWidth(), 0, parentPanel.getWidth() - panel_menu.getWidth(), 700);

        }
        // Force panel_display to re-layout its components
        parentPanel.repaint();
    }//GEN-LAST:event_lbl_menuMouseClicked

    private void lbl_closeMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_lbl_closeMouseClicked
        int a = JOptionPane.showConfirmDialog(null, "Do you really want to Close Application?", "Select", JOptionPane.YES_NO_OPTION);
        if (a == 0) {
            System.exit(0);
            //            dispose();
        }
    }//GEN-LAST:event_lbl_closeMouseClicked

    private void jPanel2MouseDragged(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jPanel2MouseDragged

        int x = evt.getXOnScreen();
        int y = evt.getYOnScreen();
        this.setLocation(x - xx, y - xy);
    }//GEN-LAST:event_jPanel2MouseDragged

    private void jPanel2MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jPanel2MouseClicked

        xx = evt.getX();
        xy = evt.getY();
    }//GEN-LAST:event_jPanel2MouseClicked

    private void lbl_manageSubjectsMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_lbl_manageSubjectsMouseEntered
        jPanel7.setBackground(mouseEnterColor);
    }//GEN-LAST:event_lbl_manageSubjectsMouseEntered

    private void lbl_manageSubjectsMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_lbl_manageSubjectsMouseExited
//        jPanel7.setBackground(mouseExitColor);
    }//GEN-LAST:event_lbl_manageSubjectsMouseExited

    private void lbl_dashboardMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_lbl_dashboardMouseEntered
        jPanel8.setBackground(mouseEnterColor);
    }//GEN-LAST:event_lbl_dashboardMouseEntered

    private void lbl_dashboardMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_lbl_dashboardMouseExited
        jPanel8.setBackground(mouseExitColor);
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
        jPanel11.setBackground(mouseExitColor);
    }//GEN-LAST:event_lbl_manageTutorsMouseExited

    private void lbl_logoutMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_lbl_logoutMouseClicked
        LoginPage login = new LoginPage();
        login.setVisible(true);

        delayBeforeClosingPreviousJframe();
    }//GEN-LAST:event_lbl_logoutMouseClicked

    private void lbl_dashboardMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_lbl_dashboardMouseClicked
        LandingPage land = new LandingPage();
        land.setVisible(true);

        delayBeforeClosingPreviousJframe();
    }//GEN-LAST:event_lbl_dashboardMouseClicked

    private void txt_leaningArearIdFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txt_leaningArearIdFocusLost

    }//GEN-LAST:event_txt_leaningArearIdFocusLost

    private void txt_leaningArearIdActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txt_leaningArearIdActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txt_leaningArearIdActionPerformed

    private void txt_leaningArearIdKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txt_leaningArearIdKeyTyped
        if (!Character.isDigit(evt.getKeyChar())) {
            evt.consume();
        }
    }//GEN-LAST:event_txt_leaningArearIdKeyTyped

    private void txt_learningAreaNameFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txt_learningAreaNameFocusLost
        // TODO add your handling code here:
    }//GEN-LAST:event_txt_learningAreaNameFocusLost

    private void txt_learningAreaNameActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txt_learningAreaNameActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txt_learningAreaNameActionPerformed

    private void btn_deleteActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_deleteActionPerformed
        if (deleteLearningArea() == true) {
            JOptionPane.showMessageDialog(this, "Learning Area Deleted Successfully...");
            clearTable();
            setLearningAreaDetailsToTable();
            clearComponents();
            autoIncrementLearningAreaId();
        } else {
            JOptionPane.showMessageDialog(this, "User Deletion failed, Please check your Database Connection...");
        }
    }//GEN-LAST:event_btn_deleteActionPerformed

    private void btn_addActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_addActionPerformed
        if (addLearningArea()) {
            JOptionPane.showMessageDialog(this, "Learning Area added successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);

            clearTable();
            setLearningAreaDetailsToTable();
            clearComponents();
            autoIncrementLearningAreaId();
        } else {
            JOptionPane.showMessageDialog(this, "Failed to add Learning Area.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }//GEN-LAST:event_btn_addActionPerformed

    private void btn_updateActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_updateActionPerformed

        if (updateLearningArea()) {
            JOptionPane.showMessageDialog(this, "Learning Area Updated Successfully...");
            clearTable();
            setLearningAreaDetailsToTable();
            clearComponents();
            autoIncrementLearningAreaId();
        } else {
            JOptionPane.showMessageDialog(this, "User Updation failed, Please check your Database Connection or Duplicate Entry...");
        }

    }//GEN-LAST:event_btn_updateActionPerformed

    private void tbl_learningAreaDetailsMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tbl_learningAreaDetailsMouseClicked

        int rowNo = tbl_learningAreaDetails.getSelectedRow();
        TableModel model = tbl_learningAreaDetails.getModel();

        txt_leaningArearId.setText(model.getValueAt(rowNo, 0).toString());
        txt_learningAreaName.setText(model.getValueAt(rowNo, 1).toString());
        cbo_grade.setSelectedItem(model.getValueAt(rowNo, 2).toString());
    }//GEN-LAST:event_tbl_learningAreaDetailsMouseClicked

    private void txt_searchLearningAreaFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txt_searchLearningAreaFocusLost
        clearTable();
        setLearningAreaDetailsToTable();
        clearComponents();
    }//GEN-LAST:event_txt_searchLearningAreaFocusLost

    private void txt_searchLearningAreaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txt_searchLearningAreaActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txt_searchLearningAreaActionPerformed

    private void txt_searchLearningAreaKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txt_searchLearningAreaKeyReleased

        // Get the search text from the text field
        String searchText = txt_searchLearningArea.getText().trim();

        try {
            // Establish a database connection
            Connection con = DBConnection.getConnection();

            // Prepare the SQL query to filter by learning_area
            String sql = "SELECT * FROM learning_area_tbl WHERE learning_area LIKE ?";
            PreparedStatement pst = con.prepareStatement(sql);

            // Use % wildcard to match any part of the learning_area name
            pst.setString(1, "%" + searchText + "%");

            // Execute the query
            ResultSet rs = pst.executeQuery();

            // Get the table model from the JTable
            model = (DefaultTableModel) tbl_learningAreaDetails.getModel();

            // Clear the existing table data
            model.setRowCount(0);

            // Populate the table with the filtered results
            while (rs.next()) {
                // Add each row to the table model
                model.addRow(new Object[]{
                    //                    rs.getInt("init"), // Assuming 'init' is the first column
                    rs.getString("learning_area_id"), // Assuming 'learning_area_id' is the second column
                    rs.getString("learning_area"), // Assuming 'learning_area' is the third column
                    rs.getString("grade") // Assuming 'grade' is the fourth column
                });
            }

            // Close the database resources
            rs.close();
            pst.close();
            con.close();

        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error while searching: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }//GEN-LAST:event_txt_searchLearningAreaKeyReleased

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
            java.util.logging.Logger.getLogger(ManageLearningArea.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(ManageLearningArea.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(ManageLearningArea.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(ManageLearningArea.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new ManageLearningArea().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private rojerusan.RSMaterialButtonCircle btn_add;
    private rojerusan.RSMaterialButtonCircle btn_delete;
    private rojerusan.RSMaterialButtonCircle btn_update;
    private rojerusan.RSComboMetro cbo_grade;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel19;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel20;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JPanel jPanel11;
    private javax.swing.JPanel jPanel15;
    private javax.swing.JPanel jPanel16;
    private javax.swing.JPanel jPanel17;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
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
    private javax.swing.JPanel panelSearch;
    private javax.swing.JPanel panel_display;
    private javax.swing.JPanel panel_menu;
    private javax.swing.JPanel parentPanel;
    private rojeru_san.complementos.RSTableMetro tbl_learningAreaDetails;
    private javax.swing.JLabel txtDate;
    private javax.swing.JLabel txtTime;
    private app.bolivia.swing.JCTextField txt_leaningArearId;
    private app.bolivia.swing.JCTextField txt_learningAreaName;
    private app.bolivia.swing.JCTextField txt_searchLearningArea;
    // End of variables declaration//GEN-END:variables

}
