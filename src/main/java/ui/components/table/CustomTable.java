package ui.components.table;

import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.JTableHeader;
import java.awt.*;

public class CustomTable extends JTable {
    private Color headerBackground = new Color(136, 130, 246); // SteelBlue - màu header mới
    private Color headerForeground = Color.WHITE;
    private Font headerFont = new Font("Segoe UI", Font.BOLD, 14);
    private Color evenRowColor = new Color(240, 248, 255); // AliceBlue - màu hàng chẵn
    private Color oddRowColor = Color.WHITE;
    private Color selectionColor = new Color(166, 161, 246); // CornflowerBlue - màu khi chọn
    private Color gridColor = new Color(220, 220, 220); // Màu đường kẻ mờ
    private int rowHeight = 30; // Chiều cao hàng

    public CustomTable() {
        setShowHorizontalLines(true);
        setShowVerticalLines(false);
        setGridColor(gridColor);
        setIntercellSpacing(new Dimension(0, 0));
        setRowMargin(0);
        setFillsViewportHeight(true);
        setRowHeight(rowHeight);
        setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        // Đặt font mặc định cho nội dung bảng
        setFont(new Font("Segoe UI", Font.PLAIN, 13));

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
                setHorizontalAlignment(SwingConstants.LEFT);
                setBorder(BorderFactory.createCompoundBorder(
                        BorderFactory.createMatteBorder(0, 0, 1, 0, new Color(50, 90, 140)), // Viền dưới header
                        BorderFactory.createEmptyBorder(0, 10, 0, 0) // Padding trái
                ));
                return this;
            }
        });

        // Custom cell renderer
        setDefaultRenderer(Object.class, new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable table, Object value,
                                                           boolean isSelected, boolean hasFocus, int row, int column) {
                super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);

                // Đặt màu nền
                if (isSelected) {
                    setBackground(selectionColor);
                    setForeground(Color.WHITE);
                } else {
                    setBackground(row % 2 == 0 ? evenRowColor : oddRowColor);
                    setForeground(Color.BLACK);
                }

                // Đặt border và padding
                setBorder(BorderFactory.createEmptyBorder(0, 10, 0, 0)); // Padding trái
                setHorizontalAlignment(SwingConstants.LEFT);

                return this;
            }
        });
    }

    // Các phương thức setter để tùy chỉnh
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

    public void setEvenRowColor(Color color) {
        this.evenRowColor = color;
        repaint();
    }

    public void setOddRowColor(Color color) {
        this.oddRowColor = color;
        repaint();
    }

    public void setSelectionColor(Color color) {
        this.selectionColor = color;
        repaint();
    }

    public void setGridColor(Color color) {
        this.gridColor = color;
        super.setGridColor(color); // Sử dụng super để gọi phương thức của lớp cha
    }

    public void setTableRowHeight(int height) {
        this.rowHeight = height;
        setRowHeight(height);
    }
}