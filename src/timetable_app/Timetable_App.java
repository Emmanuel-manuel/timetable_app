package timetable_app;

import java.awt.*;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;

/**
 *
 * @author Lenovo-x130
 */
public class Timetable_App extends JFrame {

    public Timetable_App() {
        setTitle("Class Timetable");
        setSize(800, 400);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        // Create a table model with 6 rows and 13 columns
        DefaultTableModel model = new DefaultTableModel(6, 13);

        // Set column headers
        String[] columns = {"TIME", "MON", "TUE", "WED", "THU", "FRI", "SAT", "SUN", "BREAK", "LUNCH", "EXTRA", "EXTRA2", "EXTRA3"};
        model.setColumnIdentifiers(columns);

        // Create the table with the model
        JTable table = new JTable(model);
        table.setRowHeight(30);
        table.setGridColor(Color.BLACK);

        // Add the table to a scroll pane
        JScrollPane scrollPane = new JScrollPane(table);
        add(scrollPane, BorderLayout.CENTER);

        // Display the window
        setVisible(true);
    }
    /**
     * @param args the command line arguments
     */
//    public static void main(String[] args) {
//        // TODO code application logic here
//        SwingUtilities.invokeLater(() -> new Timetable_App());
//    }
    
}
