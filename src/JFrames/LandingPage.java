package JFrames;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.io.FileOutputStream;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JViewport;
import javax.swing.SwingConstants;
import javax.swing.Timer;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import timetable_app.*;

/**
 *
 * @author Lenovo-x130
 */
public class LandingPage extends javax.swing.JFrame {

    // 2D Array (For Grid Structure)...
    private TimetableCell[][] timetableCells; // [row][column]

    // Gets the window's screen position
    int xx, xy;// For window dragging

    //Global variable for Hover Effect
    Color mouseEnterColor = new Color(255, 153, 0);
    Color mouseExitColor = new Color(51, 51, 51);

    DefaultTableModel model; // Table model for data
    JTable table; // Declare JTable as a global variable

//    ... LOGIC FOR RANDOM ALLOCATION OF LEARNONG AREAS ...
    // ==== SCHEDULING LOGIC ====
    private boolean isSubjectOnDay(int row, String subject) {
        if (subject == null) {
            return false;
        }
        for (int col = 0; col < 12; col++) {
            if (col != 2 && col != 5 && col != 8) {
                String cellValue = timetableCells[row][col].getLearningArea();
                if (subject.equals(cellValue)) {
                    return true;
                }
            }
        }
        return false;
    }

    // Helper method 1: Clear timetable
    //    clearing the TimeTable cells
    private void clearTimetableCells() {
        for (int row = 0; row < 5; row++) {
            for (int col = 0; col < 12; col++) {
                if (col != 2 && col != 5 && col != 8) { // Skip BREAK/LUNCH
                    timetableCells[row][col].setLearningArea(null);
                    String placeholder = String.valueOf((char) ('A' + row)) + (col + 1);
                    model.setValueAt(placeholder, row, col);
                }
            }
        }
    }

// Helper method 2: Process rules from rules table
    private void processRuleBasedAllocation(List<String> learningAreas) {
        DefaultTableModel rulesModel = (DefaultTableModel) tbl_rules.getModel();

        for (int i = 0; i < rulesModel.getRowCount(); i++) {
            String subject = rulesModel.getValueAt(i, 0).toString();
            int lessons = Integer.parseInt(rulesModel.getValueAt(i, 1).toString());
            String type = rulesModel.getValueAt(i, 2).toString();
            String time = rulesModel.getValueAt(i, 3).toString();

            // Remove from general pool if exists
            learningAreas.remove(subject);

            // Allocate according to rules
            allocateSubjectWithRules(subject, lessons, type, time);
        }
    }

// Helper method 3: Allocate subject with rules
    private void allocateSubjectWithRules(String subject, int lessons, String type, String timeOfDay) {
        List<Point> availableSlots = getAvailableCellsForTime(timeOfDay);
        Collections.shuffle(availableSlots);

        int requiredSlots = type.equals("Double") ? lessons / 2 : lessons;
        int allocated = 0;

        boolean[] dayUsed = new boolean[5]; // Track which days have been used for this subject

        for (Point slot : availableSlots) {
            int row = slot.x;
            int col = slot.y;

            // Skip if we've already used this day for the subject
            if (dayUsed[row] || isSubjectOnDay(row, subject)) {
                continue;
            }

            if (type.equals("Double")) {
                if (isCellAvailable(row, col) && isCellAvailable(row, col + 1)) {
                    setCellValue(row, col, subject, true); // Using the 4-arg version for double lessons
                    allocated++;
                    dayUsed[row] = true; // Mark day as used

                    // Early exit if we've met requirements
                    if (allocated >= requiredSlots) {
                        break;
                    }
                }
            } else { // Single lesson
                if (isCellAvailable(row, col)) {
                    setCellValue(row, col, subject); // Using the 3-arg version for single lessons
                    allocated++;
                    dayUsed[row] = true; // Mark day as used

                    // Early exit if we've met requirements
                    if (allocated >= requiredSlots) {
                        break;
                    }
                }
            }
        }

        if (allocated < requiredSlots) {
            showAllocationWarning(subject, type.equals("Double") ? allocated * 2 : allocated,
                    type.equals("Double") ? requiredSlots * 2 : requiredSlots);
        }
    }

// Helper method 4: Fill remaining slots randomly
    private void fillRemainingSlotsRandomly(List<String> subjects) {
        Collections.shuffle(subjects);
        int index = 0;

        for (int row = 0; row < 5; row++) {
            for (int col = 0; col < 12; col++) {
                if (isCellAvailable(row, col) && index < subjects.size()) {
                    String subject = subjects.get(index);

                    // Ensure subject isn't already on this day
                    if (!isSubjectOnDay(row, subject)) {
                        setCellValue(row, col, subject);
                        index++;
                    }
                }
            }
        }
    }

    // Helper method 5: to get available cells based on time of day
    private List<Point> getAvailableCellsForTime(String timeOfDay) {
        List<Point> cells = new ArrayList<>();

        switch (timeOfDay) {
            case "Morning":
                // Morning slots: columns 0,1,3,4 (skip BREAK at column 2)
                for (int row = 0; row < 5; row++) {
                    // Column 0
                    if (isCellAvailable(row, 0)) {
                        cells.add(new Point(row, 0));
                    }
                    // Column 1
                    if (isCellAvailable(row, 1)) {
                        cells.add(new Point(row, 1));
                    }
                    // Column 3 (after morning break)
                    if (isCellAvailable(row, 3)) {
                        cells.add(new Point(row, 3));
                    }
                    // Column 4 (last morning slot)
                    if (isCellAvailable(row, 4)) {
                        cells.add(new Point(row, 4));
                    }
                }
                break;

            case "Mid-Morning":
                // Mid-Morning slots: columns 6,7 (after mid-morning break at column 5)
                for (int row = 0; row < 5; row++) {
                    // Column 6
                    if (isCellAvailable(row, 6)) {
                        cells.add(new Point(row, 6));
                    }
                    // Column 7
                    if (isCellAvailable(row, 7)) {
                        cells.add(new Point(row, 7));
                    }
                }
                break;

            case "Evening":
                // Evening slots: columns 9,10,11 (after LUNCH at column 8)
                // Exclude row 4 (Friday) for evening slots
                for (int row = 0; row < 4; row++) { // Only rows 0-3 (Monday-Thursday)
                    // Column 9
                    if (isCellAvailable(row, 9)) {
                        cells.add(new Point(row, 9));
                    }
                    // Column 10
                    if (isCellAvailable(row, 10)) {
                        cells.add(new Point(row, 10));
                    }
                    // Column 11 (only for non-Friday)
                    if (isCellAvailable(row, 11)) {
                        cells.add(new Point(row, 11));
                    }
                }
                break;

            case "Last Lesson":
                // Last lesson slot: column 11 in last row (Friday)
                int lastRow = 4; // Friday
                if (isCellAvailable(lastRow, 11)) {
                    cells.add(new Point(lastRow, 11));
                }
                break;

            default:
                throw new IllegalArgumentException("Unknown time of day: " + timeOfDay);
        }

        return cells;
    }

    // Helper method 6: Check cell availability
    private boolean isCellAvailable(int row, int col) {
        return (col != 2 && col != 5 && col != 8)
                && // Not BREAK/LUNCH
                timetableCells[row][col].getLearningArea() == null
                && // Empty
                model.getValueAt(row, col).toString().matches("[A-E]\\d+"); // Has placeholder
    }

// Helper method 7: Set cell value
    // For single lessons (original)
    private void setCellValue(int row, int col, String value) {
        timetableCells[row][col].setLearningArea(value);
        model.setValueAt(value, row, col);
    }

    // For double lessons (new)
    private boolean setCellValue(int row, int col, String value, boolean isDoubleLesson) {
        // Check if cell is available
        if (!isCellAvailable(row, col)) {
            return false;
        }

        // For double lessons, check next cell
        if (isDoubleLesson && (!isCellAvailable(row, col + 1))) {
            return false;
        }

        // Update cells
        timetableCells[row][col].setLearningArea(value);
        model.setValueAt(value, row, col);

        if (isDoubleLesson) {
            timetableCells[row][col + 1].setLearningArea(value);
            model.setValueAt(value, row, col + 1);
        }

        return true;
    }

// Helper method 8: Show warning
    private void showAllocationWarning(String subject, int allocated, int required) {
        JOptionPane.showMessageDialog(this,
                "Only allocated " + allocated + "/" + required + " slots for " + subject,
                "Allocation Warning", JOptionPane.WARNING_MESSAGE);
    }

// Helper method 9: Refresh view
    private void refreshTimetableView() {
        jPanel1.revalidate();
        jPanel1.repaint();
        printTimetableData();
    }

// ... LOGIC ENDS HERE ...
//    ... LOGIC OF SAVING THE GENERATED TIMETABLE TO THE DATABASE ...
    private void saveTimetableToDatabase() {
        if (table == null || cbo_grade.getSelectedItem() == null) {
            JOptionPane.showMessageDialog(this, "Please generate a timetable first and select a grade",
                    "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        String grade = cbo_grade.getSelectedItem().toString();
        String dateCreated = txtDate.getText();

        try (Connection con = DBConnection.getConnection()) {
            // Clear existing entries for this grade
            String clearSql = "DELETE FROM timetable_tbl WHERE grade = ?";
            try (PreparedStatement clearStmt = con.prepareStatement(clearSql)) {
                clearStmt.setString(1, grade);
                clearStmt.executeUpdate();
            }

            // Insert new entries
            String insertSql = "INSERT INTO timetable_tbl (grade, day, lesson1, lesson2, lesson3, "
                    + "lesson4, lesson5, lesson6, lesson7, lesson8, lesson9, date_created) "
                    + "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

            try (PreparedStatement pstmt = con.prepareStatement(insertSql)) {
                // Get row header table (days)
//                JTable rowHeaderTable = (JTable) ((JScrollPane) jPanel1.getComponent(0)).getRowHeader().getView();

                for (int row = 0; row < 5; row++) {
                    String day = getDayForRow(row); // USING OUR HELPER METHOD HERE
//                    String day = rowHeaderTable.getValueAt(row, 0).toString();

                    pstmt.setString(1, grade);
                    pstmt.setString(2, day);

                    // Map timetable columns to lesson columns (skipping BREAK/LUNCH columns 2,5,8)
                    int[] timetableCols = {0, 1, 3, 4, 6, 7, 9, 10, 11}; // These map to lesson1-lesson9
                    for (int i = 0; i < timetableCols.length; i++) {
                        Object value = table.getValueAt(row, timetableCols[i]);
                        pstmt.setString(3 + i, value != null ? value.toString() : null);
                    }

                    pstmt.setString(12, dateCreated);
                    pstmt.addBatch();
                }

                pstmt.executeBatch();
                JOptionPane.showMessageDialog(this, "Timetable saved successfully for " + grade,
                        "Success", JOptionPane.INFORMATION_MESSAGE);
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Error saving timetable: " + e.getMessage(),
                    "Database Error", JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
        }
    }

//    helper method to get the row headers:
    private String getDayForRow(int row) {
        try {
            JScrollPane scrollPane = (JScrollPane) jPanel1.getComponent(0);
            JViewport rowHeader = scrollPane.getRowHeader();
            JTable rowHeaderTable = (JTable) rowHeader.getView();
            return rowHeaderTable.getValueAt(row, 0).toString();
        } catch (Exception e) {
            // Fallback to default day names if row header fails
            String[] days = {"MONDAY", "TUESDAY", "WEDNESDAY", "THURSDAY", "FRIDAY"};
            return row < days.length ? days[row] : "DAY " + (row + 1);
        }
    }
//  ... LOGIC OF SAVING TO DATABASE ENDS HERE ...

//    ... LOGIC TO CONVERT TO EXCEL/ SPREADSHEET ...
    // Generate Spreadsheet Report
    private void ConvertExcel() {

        try {
            Connection con = DBConnection.getConnection();
            String sql = "select * from issue_book_details";

            PreparedStatement pst = con.prepareStatement(sql);
            ResultSet rs = pst.executeQuery();

            // Create a new Spreadsheet
            Workbook workbook = new XSSFWorkbook();
            Sheet sheet = workbook.createSheet("Issued_Books_List");

            // Create header row
            Row headerRow = sheet.createRow(0);
            headerRow.createCell(0).setCellValue("Sno");
            headerRow.createCell(1).setCellValue("Book ID");
            headerRow.createCell(2).setCellValue("Book Name");
            headerRow.createCell(3).setCellValue("Student ID");
            headerRow.createCell(4).setCellValue("Student Name");
            headerRow.createCell(5).setCellValue("Date Issued");
            headerRow.createCell(6).setCellValue("Due Date");
            headerRow.createCell(7).setCellValue("Status");

            // Populate data rows
            int rowIndex = 1;
            while (rs.next()) {
                Row dataRow = sheet.createRow(rowIndex++);
                dataRow.createCell(0).setCellValue(rs.getString("id"));
                dataRow.createCell(1).setCellValue(rs.getString("book_id"));
                dataRow.createCell(2).setCellValue(rs.getString("book_name"));
                dataRow.createCell(3).setCellValue(rs.getString("student_id"));
                dataRow.createCell(4).setCellValue(rs.getString("student_name"));
                dataRow.createCell(5).setCellValue(rs.getString("issue_date"));
                dataRow.createCell(6).setCellValue(rs.getString("due_date"));
                dataRow.createCell(7).setCellValue(rs.getString("status"));
            }

            // Auto-size columns
            for (int columnIndex = 0; columnIndex < 9; columnIndex++) {
                sheet.autoSizeColumn(columnIndex);
            }

            JOptionPane.showMessageDialog(null, "Converting in process...");
            // Save the workbook to a file
            String filePath = "C:/LIBRARY_APP/Issued_books_report.xlsx";
            FileOutputStream fileOut = new FileOutputStream(filePath);
            workbook.write(fileOut);
            fileOut.close();

            pst.close();
            con.close();

            JOptionPane.showMessageDialog(null, "Report generated successfully. And saved in Drive C:/, in directory LIBRARY_APP ");

        } catch (SQLException | IOException ex) {
//            ex.printStackTrace();
            Logger.getLogger(LandingPage.class.getName()).log(Level.SEVERE, null, ex);
        }

    }
//    .... LOGIC OF CONVERTING TO SPREADSHEET ENDS HERE ...

    /**
     * Creates new form landing_page
     */
    public LandingPage() {
        initComponents();
        init();
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        buttonGroup_schoolLevel = new javax.swing.ButtonGroup();
        buttonGroup_session = new javax.swing.ButtonGroup();
        buttonGroup_lesson = new javax.swing.ButtonGroup();
        jPanel2 = new javax.swing.JPanel();
        lbl_menu = new javax.swing.JLabel();
        jLabel1 = new javax.swing.JLabel();
        lbl_close = new javax.swing.JLabel();
        txtTime = new javax.swing.JLabel();
        txtDate = new javax.swing.JLabel();
        jPanel3 = new javax.swing.JPanel();
        jLabel2 = new javax.swing.JLabel();
        jPanel1 = new javax.swing.JPanel();
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
        jPanel4 = new javax.swing.JPanel();
        jPanel5 = new javax.swing.JPanel();
        radioButton_lower = new javax.swing.JRadioButton();
        radioButton_upper = new javax.swing.JRadioButton();
        cbo_grade = new rojerusan.RSComboMetro();
        jLabel19 = new javax.swing.JLabel();
        jLabel20 = new javax.swing.JLabel();
        panelRules = new javax.swing.JPanel();
        cbo_learning_area = new rojerusan.RSComboMetro();
        jLabel3 = new javax.swing.JLabel();
        jPanel10 = new javax.swing.JPanel();
        radioButton_doubleLesson = new javax.swing.JRadioButton();
        radioButton_singleLesson = new javax.swing.JRadioButton();
        jPanel9 = new javax.swing.JPanel();
        radioButton_morning = new javax.swing.JRadioButton();
        radioButton_evening = new javax.swing.JRadioButton();
        radioButton_lastLesson = new javax.swing.JRadioButton();
        radioButton_midMorning = new javax.swing.JRadioButton();
        jPanel13 = new javax.swing.JPanel();
        txt_noOfLessons = new app.bolivia.swing.JCTextField();
        jPanel12 = new javax.swing.JPanel();
        btn_Add = new rojerusan.RSMaterialButtonCircle();
        jPanel16 = new javax.swing.JPanel();
        btn_Delete = new rojerusan.RSMaterialButtonCircle();
        jPanel17 = new javax.swing.JPanel();
        btn_populate = new rojerusan.RSMaterialButtonCircle();
        btn_refresh = new rojerusan.RSMaterialButtonCircle();
        panel_rulesTbl = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        tbl_rules = new rojerusan.RSTableMetro();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setMinimumSize(new java.awt.Dimension(1370, 770));
        setUndecorated(true);
        setResizable(false);
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

        jPanel1.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jPanel1MouseClicked(evt);
            }
        });
        jPanel1.setLayout(new java.awt.BorderLayout());
        getContentPane().add(jPanel1, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 380, 1370, 390));

        parentPanel.setLayout(null);

        panel_menu.setBackground(new java.awt.Color(51, 51, 51));
        panel_menu.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jPanel6.setBackground(new java.awt.Color(51, 51, 51));
        jPanel6.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jLabel4.setFont(new java.awt.Font("Yu Gothic UI Semilight", 1, 18)); // NOI18N
        jLabel4.setForeground(new java.awt.Color(255, 255, 255));
        jLabel4.setText("Features");
        jPanel6.add(jLabel4, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 10, 120, 30));

        panel_menu.add(jPanel6, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 240, 60));

        jPanel7.setBackground(new java.awt.Color(51, 51, 51));
        jPanel7.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        lbl_manageSubjects.setFont(new java.awt.Font("Yu Gothic UI Semilight", 1, 18)); // NOI18N
        lbl_manageSubjects.setForeground(new java.awt.Color(153, 153, 153));
        lbl_manageSubjects.setIcon(new javax.swing.ImageIcon(getClass().getResource("/res/icons/icons8_Unit_26px.png"))); // NOI18N
        lbl_manageSubjects.setText("  Manage Learning Areas");
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
        jPanel7.add(lbl_manageSubjects, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 16, 250, 30));

        panel_menu.add(jPanel7, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 120, 250, 60));

        jPanel8.setBackground(new java.awt.Color(255, 153, 0));
        jPanel8.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        lbl_dashboard.setFont(new java.awt.Font("Yu Gothic UI Semilight", 1, 18)); // NOI18N
        lbl_dashboard.setForeground(new java.awt.Color(153, 153, 153));
        lbl_dashboard.setIcon(new javax.swing.ImageIcon(getClass().getResource("/res/icons/icons8_Library_32px.png"))); // NOI18N
        lbl_dashboard.setText(" Dashboard");
        lbl_dashboard.addMouseListener(new java.awt.event.MouseAdapter() {
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
        lbl_manageTutors.setText("  Manage Facilitators");
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
        jPanel11.add(lbl_manageTutors, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 10, 250, 40));

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
        jPanel15.add(lbl_logout, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 10, 240, 40));

        panel_menu.add(jPanel15, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 240, 250, 60));

        parentPanel.add(panel_menu);
        panel_menu.setBounds(0, 0, 250, 310);

        panel_display.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jPanel4.setBackground(new java.awt.Color(102, 153, 255));
        jPanel4.setDoubleBuffered(false);
        jPanel4.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jPanel5.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Choose the School Level", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.ABOVE_TOP, new java.awt.Font("Times New Roman", 1, 18), new java.awt.Color(255, 154, 0))); // NOI18N
        jPanel5.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        buttonGroup_schoolLevel.add(radioButton_lower);
        radioButton_lower.setFont(new java.awt.Font("Times New Roman", 1, 14)); // NOI18N
        radioButton_lower.setText("Lower Primary");
        radioButton_lower.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                radioButton_lowerActionPerformed(evt);
            }
        });
        jPanel5.add(radioButton_lower, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 30, -1, -1));

        buttonGroup_schoolLevel.add(radioButton_upper);
        radioButton_upper.setFont(new java.awt.Font("Times New Roman", 1, 14)); // NOI18N
        radioButton_upper.setText("Junior Secondary");
        radioButton_upper.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                radioButton_upperActionPerformed(evt);
            }
        });
        jPanel5.add(radioButton_upper, new org.netbeans.lib.awtextra.AbsoluteConstraints(130, 30, -1, -1));

        jPanel4.add(jPanel5, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 270, 70));

        cbo_grade.setForeground(new java.awt.Color(0, 0, 0));
        cbo_grade.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Select" }));
        cbo_grade.setColorBorde(new java.awt.Color(102, 153, 255));
        cbo_grade.setColorFondo(new java.awt.Color(255, 153, 0));
        cbo_grade.setFont(new java.awt.Font("Times New Roman", 1, 18)); // NOI18N
        cbo_grade.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                cbo_gradeItemStateChanged(evt);
            }
        });
        cbo_grade.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cbo_gradeActionPerformed(evt);
            }
        });
        jPanel4.add(cbo_grade, new org.netbeans.lib.awtextra.AbsoluteConstraints(300, 30, 140, 30));

        jLabel19.setFont(new java.awt.Font("Verdana", 0, 20)); // NOI18N
        jLabel19.setForeground(new java.awt.Color(255, 255, 255));
        jLabel19.setText("Grade");
        jPanel4.add(jLabel19, new org.netbeans.lib.awtextra.AbsoluteConstraints(300, 0, 80, -1));

        jLabel20.setFont(new java.awt.Font("Verdana", 0, 20)); // NOI18N
        jLabel20.setForeground(new java.awt.Color(255, 255, 255));
        jLabel20.setIcon(new javax.swing.ImageIcon(getClass().getResource("/res/icons/icons8_Library_32px.png"))); // NOI18N
        jPanel4.add(jLabel20, new org.netbeans.lib.awtextra.AbsoluteConstraints(270, 30, 30, 30));

        panel_display.add(jPanel4, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 450, 70));

        panelRules.setBackground(new java.awt.Color(204, 153, 0));
        panelRules.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "RULES", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Century Gothic", 1, 16))); // NOI18N
        panelRules.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        cbo_learning_area.setForeground(new java.awt.Color(0, 0, 0));
        cbo_learning_area.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Select" }));
        cbo_learning_area.setColorBorde(new java.awt.Color(102, 153, 255));
        cbo_learning_area.setColorFondo(new java.awt.Color(255, 153, 0));
        cbo_learning_area.setFont(new java.awt.Font("Times New Roman", 1, 18)); // NOI18N
        panelRules.add(cbo_learning_area, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 50, 200, 30));

        jLabel3.setFont(new java.awt.Font("Times New Roman", 1, 12)); // NOI18N
        jLabel3.setText("LEARNING AREA");
        panelRules.add(jLabel3, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 30, -1, -1));

        jPanel10.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Double/ Single Lesson", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Century Gothic", 0, 12))); // NOI18N
        jPanel10.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        buttonGroup_lesson.add(radioButton_doubleLesson);
        radioButton_doubleLesson.setFont(new java.awt.Font("Times New Roman", 1, 14)); // NOI18N
        radioButton_doubleLesson.setText("Double lesson");
        jPanel10.add(radioButton_doubleLesson, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 20, -1, -1));

        buttonGroup_lesson.add(radioButton_singleLesson);
        radioButton_singleLesson.setFont(new java.awt.Font("Times New Roman", 1, 14)); // NOI18N
        radioButton_singleLesson.setText("Single");
        jPanel10.add(radioButton_singleLesson, new org.netbeans.lib.awtextra.AbsoluteConstraints(120, 20, -1, -1));

        panelRules.add(jPanel10, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 180, 200, 50));

        jPanel9.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Session Hours", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Century Gothic", 0, 12))); // NOI18N
        jPanel9.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        buttonGroup_session.add(radioButton_morning);
        radioButton_morning.setFont(new java.awt.Font("Times New Roman", 1, 14)); // NOI18N
        radioButton_morning.setText("Morning");
        jPanel9.add(radioButton_morning, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 20, -1, -1));

        buttonGroup_session.add(radioButton_evening);
        radioButton_evening.setFont(new java.awt.Font("Times New Roman", 1, 14)); // NOI18N
        radioButton_evening.setText("Evening");
        jPanel9.add(radioButton_evening, new org.netbeans.lib.awtextra.AbsoluteConstraints(120, 20, -1, -1));

        buttonGroup_session.add(radioButton_lastLesson);
        radioButton_lastLesson.setFont(new java.awt.Font("Times New Roman", 1, 14)); // NOI18N
        radioButton_lastLesson.setText("Last");
        jPanel9.add(radioButton_lastLesson, new org.netbeans.lib.awtextra.AbsoluteConstraints(120, 50, -1, -1));

        buttonGroup_session.add(radioButton_midMorning);
        radioButton_midMorning.setFont(new java.awt.Font("Times New Roman", 1, 14)); // NOI18N
        radioButton_midMorning.setText("Mid-Morning");
        jPanel9.add(radioButton_midMorning, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 50, -1, -1));

        panelRules.add(jPanel9, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 90, 200, 80));

        jPanel13.setBackground(new java.awt.Color(204, 153, 0));
        jPanel13.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Lessons/ week", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Times New Roman", 0, 14))); // NOI18N
        jPanel13.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        txt_noOfLessons.setBackground(new java.awt.Color(102, 153, 255));
        txt_noOfLessons.setBorder(javax.swing.BorderFactory.createMatteBorder(0, 0, 2, 0, new java.awt.Color(255, 255, 255)));
        txt_noOfLessons.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        txt_noOfLessons.setFont(new java.awt.Font("Times New Roman", 1, 16)); // NOI18N
        txt_noOfLessons.setPlaceholder("Integer Values (1-10) ....");
        txt_noOfLessons.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                txt_noOfLessonsKeyTyped(evt);
            }
        });
        jPanel13.add(txt_noOfLessons, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 20, 170, -1));

        panelRules.add(jPanel13, new org.netbeans.lib.awtextra.AbsoluteConstraints(210, 20, 200, 60));

        jPanel12.setBackground(new java.awt.Color(204, 153, 0));
        jPanel12.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Add the rules", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Times New Roman", 0, 14))); // NOI18N
        jPanel12.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        btn_Add.setText("CLICK TOADD");
        btn_Add.setFont(new java.awt.Font("Century Gothic", 1, 11)); // NOI18N
        btn_Add.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_AddActionPerformed(evt);
            }
        });
        jPanel12.add(btn_Add, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 20, 100, 40));

        panelRules.add(jPanel12, new org.netbeans.lib.awtextra.AbsoluteConstraints(210, 80, 100, 70));

        jPanel16.setBackground(new java.awt.Color(204, 153, 0));
        jPanel16.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Delete the rules", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Times New Roman", 0, 14))); // NOI18N
        jPanel16.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        btn_Delete.setBackground(new java.awt.Color(204, 51, 0));
        btn_Delete.setText("CLICK TO DELETE");
        btn_Delete.setFont(new java.awt.Font("Century Gothic", 1, 11)); // NOI18N
        jPanel16.add(btn_Delete, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 20, 100, 40));

        panelRules.add(jPanel16, new org.netbeans.lib.awtextra.AbsoluteConstraints(310, 80, 120, 70));

        jPanel17.setBackground(new java.awt.Color(204, 153, 0));
        jPanel17.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Click to Populate Timetable", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Times New Roman", 0, 14))); // NOI18N
        jPanel17.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        btn_populate.setIcon(new javax.swing.ImageIcon(getClass().getResource("/res/icons/refresh.png"))); // NOI18N
        btn_populate.setText("POPULATE");
        btn_populate.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_populateActionPerformed(evt);
            }
        });
        jPanel17.add(btn_populate, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 20, 110, 50));

        btn_refresh.setIcon(new javax.swing.ImageIcon(getClass().getResource("/res/icons/refresh.png"))); // NOI18N
        btn_refresh.setText("Refresh");
        btn_refresh.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_refreshActionPerformed(evt);
            }
        });
        jPanel17.add(btn_refresh, new org.netbeans.lib.awtextra.AbsoluteConstraints(120, 20, 90, 50));

        panelRules.add(jPanel17, new org.netbeans.lib.awtextra.AbsoluteConstraints(210, 150, 220, 80));

        panel_display.add(panelRules, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 70, 450, 240));

        parentPanel.add(panel_display);
        panel_display.setBounds(250, 0, 450, 310);

        panel_rulesTbl.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        tbl_rules.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Learning Area", "No. of Lessons", "Double/Single", "Time of Day"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        jScrollPane1.setViewportView(tbl_rules);

        panel_rulesTbl.add(jScrollPane1, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 670, 310));

        parentPanel.add(panel_rulesTbl);
        panel_rulesTbl.setBounds(700, 0, 670, 310);

        getContentPane().add(parentPanel, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 70, 1370, 310));

        pack();
    }// </editor-fold>//GEN-END:initComponents

    public void init() {
        Time.setTime(txtTime, txtDate);  // Calling the setTime method from the Time class

    }

    public void clearComponents() {
        // Refresh the panel to display the table
        jPanel1.revalidate();
        jPanel1.removeAll();
        cbo_grade.setSelectedItem(null);
    }

    // ==== UI INITIALIZATION ====
    // to populate Lower primary timetable format
    public void lowerPrimaryJtable() {

// Create a table model with 6 rows and 13 columns
        model = new DefaultTableModel(5, 12);

        // Set column headers
        String[] columns = {"8:00-8:35", "8:35-9:10", "BREAK", "9:30-10:05", "10:05-10:40", "BREAK", "11:30-12:05", "12:05-12:40", "LUNCH", "2:00-2:35", "2:35-3:10", "3:10-3:45"};
        model.setColumnIdentifiers(columns);

        // Initializing the timetableCells 
        timetableCells = new TimetableCell[5][12];
        for (int row = 0; row < 5; row++) {
            for (int col = 0; col < 12; col++) {
                String placeholder = String.valueOf((char) ('A' + row)) + (col + 1);
                timetableCells[row][col] = new TimetableCell(placeholder);
                model.setValueAt(placeholder, row, col); // Set placeholder in table
            }
        }

        // Set row headers
        String[] rows = {"MONDAY", "TUESDAY", "WEDNESDAY", "THURSDAY", "FRIDAY"};

        // Create the table with the model
        table = new JTable(model);

        // Adding mouse click listener for saving timetable
        table.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                saveTimetableToDatabase();
            }
        });

        // ===== CRITICAL INITIALIZATION POINT =====
        table.setDefaultRenderer(Object.class, new CustomPlaceholderRenderer());  // Must be set BEFORE other table configurations

        // Configuring table appearance
        table.setRowHeight(70);// Optimal height for readability
        table.setGridColor(Color.BLACK);
// Make the table non-editable
        table.setDefaultEditor(Object.class, null);

        // Set column widths
        table.getColumnModel().getColumn(0).setMinWidth(100);  // First column wider
        for (int i = 1; i < table.getColumnCount(); i++) {
            table.getColumnModel().getColumn(i).setMinWidth(80);  // Other columns
        }

        // Customize column header
// Set font style and color for column headers
        table.getTableHeader().setFont(new Font("Times New Roman", Font.BOLD, 18));
        table.getTableHeader().setForeground(new Color(0, 0, 139)); // Dark blue color

        // Create row header table
        DefaultTableModel rowHeaderModel = new DefaultTableModel();
        rowHeaderModel.setColumnIdentifiers(new String[]{"DAYS"});
        for (String row : rows) {
            rowHeaderModel.addRow(new Object[]{row});
        }

        // This creates a new JTable called rowHeaderTable, which will serve as the row headers
        JTable rowHeaderTable = new JTable(rowHeaderModel); //Creating the Row Header Table
        //Ensures that the row heights of rowHeaderTable (row headers) match those of table (main timetable)
        rowHeaderTable.setRowHeight(table.getRowHeight()); // Synchronizing Row Heights
        //This makes the rowHeaderTable non-editable, so users cannot modify the row headers.
        rowHeaderTable.setEnabled(false); // Disable editing on the Row Header Table

        // Set font style and color for row headers
        rowHeaderTable.setFont(new Font("Times New Roman", Font.BOLD, 16));
        rowHeaderTable.setForeground(Color.ORANGE.darker());

//        table.getColumnModel().getColumn(0).setPreferredWidth(100); // Adjust width of first column
//Setting the Preferred Size for the Row Header Table
        rowHeaderTable.setPreferredScrollableViewportSize(new Dimension(100, table.getPreferredSize().height));

        // Disable column reordering
        table.getTableHeader().setReorderingAllowed(false);

// Synchronize row heights
        for (int i = 0; i < table.getRowCount(); i++) {
            rowHeaderTable.setRowHeight(i, table.getRowHeight(i));
        }

        // Add the table to a scroll pane
        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.setRowHeaderView(rowHeaderTable);

        // Add the scroll pane to jPanel1
        jPanel1.add(scrollPane, BorderLayout.CENTER);

        // Refresh the panel to display the table
        jPanel1.revalidate();
        jPanel1.repaint();
    }

    // to populate JSS/ Upper primary timetable format
    public void jssPrimaryJtable() {
        // Create a table model with 6 rows and 13 columns

        model = new DefaultTableModel(5, 12);

        // Set column headers
        String[] columns = {"8:00-8:40", "8:40-9:20", "BREAK", "9:30-10:10", "10:10-10:50", "BREAK", "11:30-12:10", "12:10-12:50", "LUNCH", "2:00-2:40", "2:40-3:20", "3:20-4:00"};
        model.setColumnIdentifiers(columns);

        // Initializing the timetableCells
        timetableCells = new TimetableCell[5][12];
        for (int row = 0; row < 5; row++) {
            for (int col = 0; col < 12; col++) {
                String placeholder = String.valueOf((char) ('A' + row)) + (col + 1);
                timetableCells[row][col] = new TimetableCell(placeholder);
                model.setValueAt(placeholder, row, col);
            }
        }

        // Set row headers
        String[] rows = {"MONDAY", "TUESDAY", "WEDNESDAY", "THURSDAY", "FRIDAY"};

        // Create the table with the model
        table = new JTable(model);

        // Adding mouse click listener for saving timetable
        table.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                saveTimetableToDatabase();
            }
        });

        // ===== CRITICAL INITIALIZATION POINT =====
        table.setDefaultRenderer(Object.class, new CustomPlaceholderRenderer());  // Must be set BEFORE other table configurations

        // Configuring table appearance
        table.setRowHeight(70);// Optimal height for readability
        table.setGridColor(Color.BLACK);
// Make the table non-editable
        table.setDefaultEditor(Object.class, null);

        // Set column widths
        table.getColumnModel().getColumn(0).setMinWidth(100);  // First column wider
        for (int i = 1; i < table.getColumnCount(); i++) {
            table.getColumnModel().getColumn(i).setMinWidth(80);  // Other columns
        }

        // Customize column header
// Set font style and color for column headers
        table.getTableHeader().setFont(new Font("Times New Roman", Font.BOLD, 18));
        table.getTableHeader().setForeground(new Color(0, 0, 139)); // Dark blue color

        // Create row header table
        DefaultTableModel rowHeaderModel = new DefaultTableModel();
        rowHeaderModel.setColumnIdentifiers(new String[]{"DAYS"});
        for (String row : rows) {
            rowHeaderModel.addRow(new Object[]{row});
        }

        // This creates a new JTable called rowHeaderTable, which will serve as the row headers
        JTable rowHeaderTable = new JTable(rowHeaderModel); //Creating the Row Header Table
        //Ensures that the row heights of rowHeaderTable (row headers) match those of table (main timetable)
        rowHeaderTable.setRowHeight(table.getRowHeight()); // Synchronizing Row Heights
        //This makes the rowHeaderTable non-editable, so users cannot modify the row headers.
        rowHeaderTable.setEnabled(false); // Disable editing on the Row Header Table

        // Set font style and color for row headers
        rowHeaderTable.setFont(new Font("Times New Roman", Font.BOLD, 16));
        rowHeaderTable.setForeground(Color.ORANGE.darker());

//        table.getColumnModel().getColumn(0).setPreferredWidth(100); // Adjust width of first column
        //Setting the Preferred Size for the Row Header Table
        rowHeaderTable.setPreferredScrollableViewportSize(new Dimension(100, table.getPreferredSize().height));

        // Disable column reordering
        table.getTableHeader().setReorderingAllowed(false);

// Synchronize row heights
        for (int i = 0; i < table.getRowCount(); i++) {
            rowHeaderTable.setRowHeight(i, table.getRowHeight(i));
        }

        // Add the table to a scroll pane
        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.setRowHeaderView(rowHeaderTable);

        // Add the scroll pane to jPanel1
        jPanel1.add(scrollPane, BorderLayout.CENTER);

        // Refresh the panel to display the table
        jPanel1.revalidate();
        jPanel1.repaint();
    }

    // INSERT THE VERTICAL "BREAK and LUNCH) IN COLUMN NUMBER 3, 6 AND 9
    public void insertVerticalWords() {

//ALTERNATIVE using loops
// Words to insert and their respective column indices
        String[] words = {"BREAK", "BREAK", "LUNCH"};
        int[] columns = {2, 5, 8}; // Corresponding columns

        // Create a custom cell renderer
        DefaultTableCellRenderer verticalRenderer = new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable table, Object value,
                    boolean isSelected, boolean hasFocus, int row, int column) {
                super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);

                // Selection styling
                if (isSelected) {
                    setBackground(table.getSelectionBackground());
                    setForeground(table.getSelectionForeground());
                } else {
                    setBackground(table.getBackground());
                }

                // Special styling for vertical words
                setFont(new Font("Times New Roman", Font.BOLD, 16));
                setForeground(Color.BLUE);
                // Keep vertical text alignment
                setHorizontalAlignment(SwingConstants.CENTER);
                setVerticalAlignment(SwingConstants.CENTER);

                return this;
            }
        };

        table = (JTable) ((JScrollPane) jPanel1.getComponent(0)).getViewport().getView();
        for (int i = 0; i < words.length; i++) {
            for (int j = 0; j < words[i].length(); j++) {
                model.setValueAt(String.valueOf(words[i].charAt(j)), j, columns[i]);
            }
            table.getColumnModel().getColumn(columns[i]).setCellRenderer(verticalRenderer);
        }

    }

    // Insert/Mark the blank cells with spreadsheet like placeholders.
    public void markBlankCellsWithPlaceholders() {
        // Columns to skip (BREAK and LUNCH columns)
        int[] skipColumns = {2, 5, 8};

        // Iterate through the table and mark blank cells with placeholders
        for (int row = 0; row < model.getRowCount(); row++) {
            for (int col = 0; col < model.getColumnCount(); col++) {
                // Skip the specified columns
                boolean skip = false;
                for (int skipCol : skipColumns) {
                    if (col == skipCol) {
                        skip = true;
                        break;
                    }
                }
                if (skip) {
                    continue; // Skip this column
                }
                // Check if the cell is blank
                if (model.getValueAt(row, col) == null || model.getValueAt(row, col).toString().isEmpty()) {
                    // Generate placeholder in spreadsheet format (A1, A2, ..., B1, B2, ...)
                    String placeholder = String.valueOf((char) ('A' + row)) + (col + 1);
                    model.setValueAt(placeholder, row, col);
                }
            }
        }
        // Apply custom renderer for placeholder styling
        table.setDefaultRenderer(Object.class, new CustomPlaceholderRenderer());

    }

    // POPULATE LEARNING AREAS TO THE TIMETABLE
    public void populateLearningAreas() {
//         // 1. Clear existing allocations
        clearTimetableCells();

        // 2. Get the selected grade
        String selectedGrade = cbo_grade.getSelectedItem().toString();

        // 3. Fetch learning areas from database
        List<String> learningAreas = fetchLearningAreasFromDatabase(selectedGrade);

        // 3. Process rules-based allocation first
        processRuleBasedAllocation(learningAreas);

        // 4. Fill remaining slots with other subjects
        fillRemainingSlotsRandomly(learningAreas);
        // 4. First apply rule-based allocation
//        applySchedulingRules(timetableCells, model, learningAreas);

        // 5. Then fill remaining slots with other subjects
//        fillRemainingSlots(timetableCells, model, learningAreas);
        // 6. Refresh display
        refreshTimetableView();
    }

    private List<String> fetchLearningAreasFromDatabase(String grade) {
        List<String> learningAreas = new ArrayList<>();

        // Database connection details
        try (Connection con = DBConnection.getConnection()) {

            // SQL query to fetch learning areas for the selected grade
            String query = "SELECT learning_area FROM learning_area_tbl WHERE grade = ?";

            try (PreparedStatement pstmt = con.prepareStatement(query)) {
                pstmt.setString(1, grade);
                ResultSet rs = pstmt.executeQuery();

                // Add learning areas to the list
                while (rs.next()) {
                    learningAreas.add(rs.getString("learning_area"));
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error fetching learning areas from the database.", "Error", JOptionPane.ERROR_MESSAGE);
        }

        return learningAreas;
    }

//    helper method to access or populate the data:
    public void printTimetableData() {
        for (int row = 0; row < 5; row++) {
            for (int col = 0; col < 12; col++) {
                TimetableCell cell = timetableCells[row][col];
                System.out.printf("Cell %s: %s\n",
                        cell.getPlaceholder(),
                        cell.getLearningArea() != null ? cell.getLearningArea() : "Empty"
                );
            }
        }
    }

    private void populateLearningAreastoCombobox(String grade) {
        //Clear existing items
        cbo_learning_area.removeAllItems();

        // Add default item
        cbo_learning_area.addItem("-- Select Learning Area --");

        try {
            // Database connection
            Connection con = DBConnection.getConnection();

            // SQL query to fetch learning areas for the selected grade
            String sql = "SELECT DISTINCT learning_area FROM facilitator_tbl WHERE grade = ?";
            PreparedStatement pstmt = con.prepareStatement(sql);
            pstmt.setString(1, grade);

            ResultSet rs = pstmt.executeQuery();

            // Populate the combo box
            while (rs.next()) {
                cbo_learning_area.addItem(rs.getString("learning_area"));
            }

            // Close resources
            rs.close();
            pstmt.close();
            con.close();

        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Error fetching learning areas: " + e.getMessage(),
                    "Database Error", JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
        }
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

    // method to update the Grades combo box
    private void updateComboBoxLowerGrades() {
        // Define the grades to be displayed in the combo box
        String[] grades = {"Grade 1", "Grade 2", "Grade 3", "Grade 4", "Grade 5", "Grade 6"};

        // Set the combo box model with the grades
        cbo_grade.setModel(new javax.swing.DefaultComboBoxModel<>(grades));
    }

    private void updateComboBoxUpperGrades() {
        // Define the grades to be displayed in the combo box
        String[] grades = {"Grade 7", "Grade 8", "Grade 9"};

        // Set the combo box model with the grades
        cbo_grade.setModel(new javax.swing.DefaultComboBoxModel<>(grades));
    }

    private void setRules() {
        // Get values from components
        String learningArea = cbo_learning_area.getSelectedItem().toString();
        String noOfLessons = txt_noOfLessons.getText().trim();

        // Validate inputs
        if (learningArea.isEmpty() || learningArea.equals("--Select Learning Area--")) {
            JOptionPane.showMessageDialog(this, "Please select a learning area", "Error", JOptionPane.ERROR_MESSAGE);
            cbo_learning_area.requestFocus();
            return;
        }

        if (noOfLessons.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please enter number of lessons", "Input Error", JOptionPane.ERROR_MESSAGE);
            txt_noOfLessons.requestFocus();
            return;
        }

        try {
            int lessons = Integer.parseInt(noOfLessons);
            if (lessons <= 0 || lessons > 10) { //reasonable max number of 10 lessons
                JOptionPane.showMessageDialog(this, "Number of lessons must be between 1 and 10", "Input Error", JOptionPane.ERROR_MESSAGE);
                txt_noOfLessons.selectAll();
                txt_noOfLessons.requestFocus();
                return;
            }
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Please enter a valid number for lessons", "Input Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        // Determine Double/Single Lesson value
        String lessonType = "";
        if (radioButton_doubleLesson.isSelected()) {
            lessonType = "Double";
        } else if (radioButton_singleLesson.isSelected()) {
            lessonType = "Single";
        } else {
            JOptionPane.showMessageDialog(this, "Please select lesson type (Single/Double)", "Selection Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        // Determine Time of Day value
        String timeOfDay = "";
        if (radioButton_morning.isSelected()) {
            timeOfDay = "Morning";
        } else if (radioButton_midMorning.isSelected()) {
            timeOfDay = "Mid-Morning";
        } else if (radioButton_evening.isSelected()) {
            timeOfDay = "Evening";
        } else if (radioButton_lastLesson.isSelected()) {
            timeOfDay = "Last Lesson";
        } else {
            JOptionPane.showMessageDialog(this, "Please select time of day", "Selection Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        // Create a new row for the table
        Object[] rowData = new Object[]{
            learningArea,
            noOfLessons,
            lessonType,
            timeOfDay
        };

        // Get the table model and add the row
        DefaultTableModel model = (DefaultTableModel) tbl_rules.getModel();
        model.addRow(rowData);

        // Clear inputs if needed (optional)
        // cbo_learning_area.setSelectedIndex(0);
        // txt_noOfLessons.setText("");
        // Clear radio button selections
        // buttonGroup1.clearSelection(); //e.t.c
    }

    private void lbl_closeMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_lbl_closeMouseClicked
        int a = JOptionPane.showConfirmDialog(null, "Do you really want to Close Application?", "Select", JOptionPane.YES_NO_OPTION);
        if (a == 0) {
            System.exit(0);
//            dispose();
        }
    }//GEN-LAST:event_lbl_closeMouseClicked

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

    private void jPanel2MouseDragged(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jPanel2MouseDragged

        int x = evt.getXOnScreen();
        int y = evt.getYOnScreen();
        this.setLocation(x - xx, y - xy);
    }//GEN-LAST:event_jPanel2MouseDragged

    private void jPanel2MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jPanel2MouseClicked

        xx = evt.getX();
        xy = evt.getY();
    }//GEN-LAST:event_jPanel2MouseClicked

    private void radioButton_lowerActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_radioButton_lowerActionPerformed
        // Refresh the panel to display the table
        clearComponents();

        lowerPrimaryJtable();

        // Call the method to update the combo box^
        updateComboBoxLowerGrades();

        insertVerticalWords();

        markBlankCellsWithPlaceholders();

    }//GEN-LAST:event_radioButton_lowerActionPerformed

    private void radioButton_upperActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_radioButton_upperActionPerformed
        // Refresh the panel to display the table
        clearComponents();

        jssPrimaryJtable();

        // Call the method to update the combo box
        updateComboBoxUpperGrades();

        insertVerticalWords();

        markBlankCellsWithPlaceholders();
    }//GEN-LAST:event_radioButton_upperActionPerformed

    private void btn_refreshActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_refreshActionPerformed
        clearComponents();
    }//GEN-LAST:event_btn_refreshActionPerformed

    private void lbl_manageSubjectsMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_lbl_manageSubjectsMouseClicked
        ManageLearningArea home = new ManageLearningArea();
        home.setVisible(true);

        delayBeforeClosingPreviousJframe();

    }//GEN-LAST:event_lbl_manageSubjectsMouseClicked

    private void lbl_manageSubjectsMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_lbl_manageSubjectsMouseEntered
        jPanel7.setBackground(mouseEnterColor);
    }//GEN-LAST:event_lbl_manageSubjectsMouseEntered

    private void lbl_manageSubjectsMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_lbl_manageSubjectsMouseExited
        jPanel7.setBackground(mouseExitColor);
    }//GEN-LAST:event_lbl_manageSubjectsMouseExited

    private void lbl_dashboardMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_lbl_dashboardMouseEntered
        jPanel8.setBackground(mouseEnterColor);
    }//GEN-LAST:event_lbl_dashboardMouseEntered

    private void lbl_dashboardMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_lbl_dashboardMouseExited
//        jPanel6.setBackground(mouseExitColor);
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

    private void cbo_gradeItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_cbo_gradeItemStateChanged

    }//GEN-LAST:event_cbo_gradeItemStateChanged

    private void btn_populateActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_populateActionPerformed

        populateLearningAreas();
//        printTimetableData();
    }//GEN-LAST:event_btn_populateActionPerformed

    private void cbo_gradeActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cbo_gradeActionPerformed
        // Get the selected grade
        String selectedGrade = (String) cbo_grade.getSelectedItem();

        if (selectedGrade != null && !selectedGrade.isEmpty()) {
            // Fetch and populate learning areas
            populateLearningAreastoCombobox(selectedGrade);
        }
    }//GEN-LAST:event_cbo_gradeActionPerformed

    private void btn_AddActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_AddActionPerformed
        setRules();
    }//GEN-LAST:event_btn_AddActionPerformed

    private void txt_noOfLessonsKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txt_noOfLessonsKeyTyped
        if (!Character.isDigit(evt.getKeyChar())) {
            evt.consume();
        }
    }//GEN-LAST:event_txt_noOfLessonsKeyTyped

    private void jPanel1MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jPanel1MouseClicked
//        saveTimetableToDatabase();
    }//GEN-LAST:event_jPanel1MouseClicked

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
            java.util.logging.Logger.getLogger(LandingPage.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(LandingPage.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(LandingPage.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(LandingPage.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new LandingPage().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private rojerusan.RSMaterialButtonCircle btn_Add;
    private rojerusan.RSMaterialButtonCircle btn_Delete;
    private rojerusan.RSMaterialButtonCircle btn_populate;
    private rojerusan.RSMaterialButtonCircle btn_refresh;
    private javax.swing.ButtonGroup buttonGroup_lesson;
    private javax.swing.ButtonGroup buttonGroup_schoolLevel;
    private javax.swing.ButtonGroup buttonGroup_session;
    private rojerusan.RSComboMetro cbo_grade;
    private rojerusan.RSComboMetro cbo_learning_area;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel19;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel20;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel10;
    private javax.swing.JPanel jPanel11;
    private javax.swing.JPanel jPanel12;
    private javax.swing.JPanel jPanel13;
    private javax.swing.JPanel jPanel15;
    private javax.swing.JPanel jPanel16;
    private javax.swing.JPanel jPanel17;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JPanel jPanel5;
    private javax.swing.JPanel jPanel6;
    private javax.swing.JPanel jPanel7;
    private javax.swing.JPanel jPanel8;
    private javax.swing.JPanel jPanel9;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JLabel lbl_close;
    private javax.swing.JLabel lbl_dashboard;
    private javax.swing.JLabel lbl_logout;
    private javax.swing.JLabel lbl_manageSubjects;
    private javax.swing.JLabel lbl_manageTutors;
    private javax.swing.JLabel lbl_menu;
    private javax.swing.JPanel panelRules;
    private javax.swing.JPanel panel_display;
    private javax.swing.JPanel panel_menu;
    private javax.swing.JPanel panel_rulesTbl;
    private javax.swing.JPanel parentPanel;
    private javax.swing.JRadioButton radioButton_doubleLesson;
    private javax.swing.JRadioButton radioButton_evening;
    private javax.swing.JRadioButton radioButton_lastLesson;
    private javax.swing.JRadioButton radioButton_lower;
    private javax.swing.JRadioButton radioButton_midMorning;
    private javax.swing.JRadioButton radioButton_morning;
    private javax.swing.JRadioButton radioButton_singleLesson;
    private javax.swing.JRadioButton radioButton_upper;
    private rojerusan.RSTableMetro tbl_rules;
    private javax.swing.JLabel txtDate;
    private javax.swing.JLabel txtTime;
    private app.bolivia.swing.JCTextField txt_noOfLessons;
    // End of variables declaration//GEN-END:variables
}
