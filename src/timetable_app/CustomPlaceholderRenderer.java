package timetable_app;

import java.awt.*;
import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;

/**
 *
 * @author Lenovo-x130
 */
public class CustomPlaceholderRenderer extends DefaultTableCellRenderer {

    @Override
    public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
//        JLabel label = new JLabel(value != null ? value.toString() : "", SwingConstants.LEFT);
//        label.setFont(new Font("Courier New", Font.PLAIN, 8)); // Font type & size
//        label.setVerticalAlignment(SwingConstants.TOP); // Align text to the top
//        return label;
        super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);

        // Selection styling
        if (isSelected) {
            setBackground(table.getSelectionBackground());
            setForeground(table.getSelectionForeground());
        } else {
            setBackground(table.getBackground());
        }

        // Center alignment for all content
        setHorizontalAlignment(SwingConstants.CENTER);

        // Different styling for placeholders vs learning areas
        if (value != null) {
            String cellValue = value.toString();

            if (cellValue.matches("[A-Z]\\d+")) {
                // Placeholder cells (A1, B2, etc.)
                setFont(new Font("Courier New", Font.PLAIN, 8));
                setForeground(Color.GRAY);
                setVerticalAlignment(SwingConstants.TOP); // Align placeholders to top
            } else if (!cellValue.isEmpty()) {
                // Learning area cells
                setFont(new Font("Times New Roman", Font.PLAIN, 18));
                setForeground(Color.BLACK);
                setVerticalAlignment(SwingConstants.CENTER); // Center learning areas
            }
        }

        return this;
    }
}
