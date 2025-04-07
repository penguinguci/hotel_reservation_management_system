package ui.components.table;

import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.JTableHeader;
import java.awt.*;

public class CustomTable extends JTable {
    private Color headerBackground = new Color(70, 130, 180); // Màu mặc định cho header
    private Color headerForeground = Color.WHITE;
    private Font headerFont = new Font("Segoe UI", Font.BOLD, 14);

    public CustomTable() {
        setShowGrid(false);
        setIntercellSpacing(new Dimension(0, 0));
        setRowMargin(0);
        setFillsViewportHeight(true);

        // Custom header
        JTableHeader header = getTableHeader();
        header.setReorderingAllowed(false);
        header.setResizingAllowed(true);
        header.setDefaultRenderer(new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable table, Object value,
                                                           boolean isSelected, boolean hasFocus, int row, int column) {
                super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
                setBackground(headerBackground);
                setForeground(headerForeground);
                setFont(headerFont);
                setHorizontalAlignment(SwingConstants.CENTER);
                setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
                return this;
            }
        });

        // Custom cell renderer (không có nút)
        setDefaultRenderer(Object.class, new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable table, Object value,
                                                           boolean isSelected, boolean hasFocus, int row, int column) {
                super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
                setBorder(noFocusBorder);
                if (isSelected) {
                    setBackground(new Color(173, 216, 230)); // Màu khi chọn hàng
                } else {
                    setBackground(row % 2 == 0 ? new Color(250, 250, 250) : Color.WHITE); // Màu xen kẽ
                }
                setHorizontalAlignment(SwingConstants.LEFT); // Canh trái cho tất cả cột
                return this;
            }
        });
    }

    public void setHeaderBackground(Color color) {
        this.headerBackground = color;
        getTableHeader().repaint();
    }

    public void setHeaderForeground(Color color) {
        this.headerForeground = color;
        getTableHeader().repaint();
    }

    public void setHeaderFont(Font font) {
        this.headerFont = font;
        getTableHeader().setFont(font);
    }
}