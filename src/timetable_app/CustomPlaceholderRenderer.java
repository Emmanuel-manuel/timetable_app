
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
        JLabel label = new JLabel(value != null ? value.toString() : "", SwingConstants.LEFT);
        label.setFont(new Font("Courier New", Font.PLAIN, 8)); // Font type & size
        label.setVerticalAlignment(SwingConstants.TOP); // Align text to the top
        return label;
    }
}
