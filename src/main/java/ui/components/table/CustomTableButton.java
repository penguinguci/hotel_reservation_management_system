package ui.components.table;

import javax.swing.*;
import javax.swing.plaf.basic.BasicScrollBarUI;
import javax.swing.table.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * CustomTableButton - bảng tùy chỉnh với các tính năng:
 * - Tùy chỉnh màu header
 * - Tùy chỉnh JScrollPane
 * - Hỗ trợ thêm nút trong các hàng
 * - JavaBean hỗ trợ kéo thả trong NetBeans
 */
public class CustomTableButton extends JPanel implements Serializable {

    private static final long serialVersionUID = 1L;

    private JTable table;
    private CustomTableModel tableModel;
    private JScrollPane scrollPane;
    private Color headerBackgroundColor = new Color(51, 102, 153);
    private Color headerForegroundColor = Color.WHITE;
    private int headerHeight = 35;
    private String[] columnNames = {"Column 1", "Column 2", "Column 3"};

    /**
     * Constructor mặc định không tham số (cần thiết cho JavaBean)
     */
    public CustomTableButton() {
        initialize(columnNames);
    }

    /**
     * Khởi tạo CustomTable với các cột
     * @param columnNames Tên các cột
     */
    public CustomTableButton(String[] columnNames) {
        initialize(columnNames);
    }

    /**
     * Khởi tạo component
     */
    private void initialize(String[] columns) {
        setLayout(new BorderLayout());

        // Tạo Table Model
        tableModel = new CustomTableModel(columns);

        // Tạo JTable với model
        table = new JTable(tableModel);
        table.setRowHeight(40); // Đặt chiều cao hàng để chứa nút
        table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        table.setAutoCreateRowSorter(true);

        // Tùy chỉnh header của bảng
        applyHeaderProperties();

        // Tạo scroll pane với JTable
        scrollPane = new JScrollPane(table);
        scrollPane.setBorder(BorderFactory.createEmptyBorder());
        scrollPane.getViewport().setBackground(Color.WHITE);

        // Tùy chỉnh thanh cuộn
        scrollPane.getVerticalScrollBar().setUI(new CustomScrollBarUI());
        scrollPane.getHorizontalScrollBar().setUI(new CustomScrollBarUI());

        // Đặt chiều rộng cột cho cột có nút
        for (int i = 0; i < columns.length; i++) {
            if (i >= 5) { // Cột "Thao Tác" và "Dịch Vụ" (index 5, 6)
                table.getColumnModel().getColumn(i).setPreferredWidth(100);
            }
        }

        add(scrollPane, BorderLayout.CENTER);

        // Thiết lập kích thước mặc định
        setPreferredSize(new Dimension(400, 300));

        // Lắng nghe thay đổi thuộc tính để cập nhật giao diện
        addPropertyChangeListener("headerBackgroundColor", evt -> applyHeaderProperties());
        addPropertyChangeListener("headerForegroundColor", evt -> applyHeaderProperties());
        addPropertyChangeListener("headerHeight", evt -> applyHeaderProperties());

        // Áp dụng renderer mặc định cho các cột
        updateTableRenderers();
    }

    /**
     * Áp dụng các thuộc tính cho header
     */
    private void applyHeaderProperties() {
        if (table != null && table.getTableHeader() != null) {
            JTableHeader header = table.getTableHeader();
            header.setOpaque(true);
            header.setBackground(headerBackgroundColor);
            header.setForeground(headerForegroundColor);
            header.setFont(new Font("SansSerif", Font.BOLD, 12));
            header.setPreferredSize(new Dimension(header.getWidth(), headerHeight));

            // Sử dụng renderer tùy chỉnh để căn giữa văn bản và đảm bảo màu hiển thị đúng
            header.setDefaultRenderer(new DefaultTableCellRenderer() {
                @Override
                public Component getTableCellRendererComponent(JTable table, Object value,
                                                               boolean isSelected, boolean hasFocus, int row, int column) {
                    JLabel label = (JLabel) super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
                    label.setBackground(headerBackgroundColor);
                    label.setForeground(headerForegroundColor);
                    label.setFont(new Font("SansSerif", Font.BOLD, 12));
                    label.setOpaque(true);
                    label.setHorizontalAlignment(JLabel.CENTER);
                    label.setBorder(BorderFactory.createMatteBorder(0, 0, 1, 1, Color.GRAY));
                    return label;
                }
            });

            header.revalidate();
            header.repaint();
            table.revalidate();
            table.repaint();
        }
    }

    /**
     * Thiết lập tên các cột
     * @param columnNames Mảng tên các cột
     */
    public void setColumnNames(String[] columnNames) {
        this.columnNames = columnNames;
        tableModel = new CustomTableModel(columnNames);
        table.setModel(tableModel);
        updateTableRenderers();
        applyHeaderProperties();
        // Cập nhật chiều rộng cột khi thay đổi tên cột
        for (int i = 0; i < columnNames.length; i++) {
            if (i >= 5) {
                table.getColumnModel().getColumn(i).setPreferredWidth(100);
            }
        }
    }

    /**
     * Lấy tên các cột
     * @return Mảng tên các cột
     */
    public String[] getColumnNames() {
        return columnNames;
    }

    /**
     * Đặt màu nền cho header
     * @param color Màu nền
     */
    public void setHeaderBackgroundColor(Color color) {
        Color oldColor = this.headerBackgroundColor;
        this.headerBackgroundColor = color;
        firePropertyChange("headerBackgroundColor", oldColor, color);
        applyHeaderProperties();
    }

    /**
     * Lấy màu nền cho header
     * @return Màu nền header
     */
    public Color getHeaderBackgroundColor() {
        return headerBackgroundColor;
    }

    /**
     * Đặt màu chữ cho header
     * @param color Màu chữ
     */
    public void setHeaderForegroundColor(Color color) {
        Color oldColor = this.headerForegroundColor;
        this.headerForegroundColor = color;
        firePropertyChange("headerForegroundColor", oldColor, color);
        applyHeaderProperties();
    }

    /**
     * Lấy màu chữ của header
     * @return Màu chữ header
     */
    public Color getHeaderForegroundColor() {
        return headerForegroundColor;
    }

    /**
     * Đặt chiều cao cho header
     * @param height Chiều cao
     */
    public void setHeaderHeight(int height) {
        int oldHeight = this.headerHeight;
        this.headerHeight = height;
        firePropertyChange("headerHeight", oldHeight, height);
        applyHeaderProperties();
    }

    /**
     * Lấy chiều cao của header
     * @return Chiều cao header
     */
    public int getHeaderHeight() {
        return headerHeight;
    }

    /**
     * Đặt chiều cao cho hàng
     * @param height Chiều cao
     */
    public void setRowHeight(int height) {
        table.setRowHeight(height);
    }

    /**
     * Lấy chiều cao của hàng
     * @return Chiều cao hàng
     */
    public int getRowHeight() {
        return table.getRowHeight();
    }

    /**
     * Thêm dữ liệu vào bảng
     * @param rowData Dữ liệu hàng
     * @param buttonTypes Loại nút cho mỗi hàng (có thể null)
     */
    public void addRow(Object[] rowData, ButtonType[] buttonTypes) {
        tableModel.addRow(rowData, buttonTypes);
    }

    /**
     * Xóa tất cả dữ liệu trong bảng
     */
    public void clearTable() {
        tableModel.clearData();
    }

    /**
     * Đặt kích thước cho JScrollPane
     * @param width Chiều rộng
     * @param height Chiều cao
     */
    public void setScrollPaneSize(int width, int height) {
        scrollPane.setPreferredSize(new Dimension(width, height));
    }

    /**
     * Lấy JTable đang sử dụng
     * @return JTable
     */
    public JTable getTable() {
        return table;
    }

    /**
     * Lấy JScrollPane đang sử dụng
     * @return JScrollPane
     */
    public JScrollPane getScrollPane() {
        return scrollPane;
    }

    /**
     * Lấy TableModel đang sử dụng
     * @return CustomTableModel
     */
    public CustomTableModel getTableModel() {
        return tableModel;
    }

    /**
     * Cập nhật renderers cho bảng
     */
    private void updateTableRenderers() {
        for (int i = 0; i < table.getColumnCount(); i++) {
            table.getColumnModel().getColumn(i).setCellRenderer(new ButtonRenderer());
        }
    }

    /**
     * Định nghĩa các loại nút có thể thêm vào các hàng
     */
    public enum ButtonType {
        EDIT("Sửa", new Color(66, 139, 202)),
        DELETE("Xóa", new Color(217, 83, 79)),
        VIEW("Xem", new Color(91, 192, 222)),
        ADD("Thêm", new Color(92, 184, 92)),
        CUSTOM("Tùy chỉnh", new Color(240, 173, 78));

        private final String text;
        private final Color color;

        ButtonType(String text, Color color) {
            this.text = text;
            this.color = color;
        }

        public String getText() {
            return text;
        }

        public Color getColor() {
            return color;
        }
    }

    /**
     * Tùy chỉnh model cho JTable
     */
    public class CustomTableModel extends AbstractTableModel {
        private String[] columnNames;
        private List<Object[]> data;
        private List<ButtonType[]> buttonTypes;

        public CustomTableModel(String[] columnNames) {
            this.columnNames = columnNames;
            this.data = new ArrayList<>();
            this.buttonTypes = new ArrayList<>();
        }

        @Override
        public int getRowCount() {
            return data.size();
        }

        @Override
        public int getColumnCount() {
            return columnNames.length;
        }

        @Override
        public String getColumnName(int column) {
            return columnNames[column];
        }

        @Override
        public Object getValueAt(int rowIndex, int columnIndex) {
            if (rowIndex >= data.size() || columnIndex >= columnNames.length) {
                return null;
            }
            ButtonType buttonType = getButtonTypeAt(rowIndex, columnIndex);
            if (buttonType != null) {
                return buttonType.getText();
            }
            return data.get(rowIndex)[columnIndex];
        }

        @Override
        public boolean isCellEditable(int rowIndex, int columnIndex) {
            return buttonTypes.size() > rowIndex &&
                    buttonTypes.get(rowIndex) != null &&
                    buttonTypes.get(rowIndex).length > columnIndex &&
                    buttonTypes.get(rowIndex)[columnIndex] != null;
        }

        public ButtonType getButtonTypeAt(int rowIndex, int columnIndex) {
            if (buttonTypes.size() <= rowIndex || buttonTypes.get(rowIndex) == null ||
                    buttonTypes.get(rowIndex).length <= columnIndex) {
                return null;
            }
            return buttonTypes.get(rowIndex)[columnIndex];
        }

        public void addRow(Object[] rowData, ButtonType[] rowButtonTypes) {
            data.add(rowData);
            buttonTypes.add(rowButtonTypes);
            fireTableRowsInserted(data.size() - 1, data.size() - 1);
        }

        public void removeRow(int rowIndex) {
            if (rowIndex >= 0 && rowIndex < data.size()) {
                data.remove(rowIndex);
                buttonTypes.remove(rowIndex);
                fireTableRowsDeleted(rowIndex, rowIndex);
            }
        }

        public void clearData() {
            int size = data.size();
            data.clear();
            buttonTypes.clear();
            if (size > 0) {
                fireTableRowsDeleted(0, size - 1);
            }
        }

        public Object[] getRowData(int rowIndex) {
            if (rowIndex >= 0 && rowIndex < data.size()) {
                return data.get(rowIndex);
            }
            return null;
        }
    }

    /**
     * Cell Renderer tùy chỉnh để hỗ trợ hiển thị nút trong bảng
     */
    public class ButtonRenderer extends JButton implements TableCellRenderer {
        public ButtonRenderer() {
            setOpaque(true);
            setBorderPainted(false);
            setFocusPainted(false);
            setContentAreaFilled(true);
            // Tùy chỉnh kích thước nút ButtonType
            setPreferredSize(new Dimension(70, 20)); // Chiều rộng 80px, chiều cao 30px
            setMargin(new Insets(2, 5, 2, 5)); // Lề để văn bản gọn gàng
            setHorizontalAlignment(SwingConstants.CENTER); // Căn giữa văn bản trong nút
        }

        @Override
        public Component getTableCellRendererComponent(JTable table, Object value,
                                                       boolean isSelected, boolean hasFocus, int row, int column) {
            ButtonType buttonType = ((CustomTableModel) table.getModel()).getButtonTypeAt(row, column);
            if (buttonType != null) {
                setText(buttonType.getText());
                setBackground(buttonType.getColor());
                setForeground(Color.WHITE);
            } else {
                setText(value != null ? value.toString() : "");
                setBackground(isSelected ? table.getSelectionBackground() : table.getBackground());
                setForeground(isSelected ? table.getSelectionForeground() : table.getForeground());
                setPreferredSize(null); // Không áp dụng kích thước cố định cho ô không phải nút
            }
            return this;
        }
    }

    /**
     * Cell Editor tùy chỉnh để hỗ trợ sự kiện click nút trong bảng
     */
    public class ButtonEditor extends DefaultCellEditor {
        private JButton button;
        private int clickedRow;
        private int clickedColumn;
        private ButtonClickListener listener;

        public ButtonEditor(ButtonClickListener listener) {
            super(new JTextField());
            this.listener = listener;

            button = new JButton();
            button.setOpaque(true);
            button.setBorderPainted(false);
            button.setFocusPainted(false);
            button.setContentAreaFilled(true);
            // Tùy chỉnh kích thước nút ButtonType
            button.setPreferredSize(new Dimension(70, 20)); // Đồng bộ với renderer
            button.setMargin(new Insets(2, 5, 2, 5)); // Lề để văn bản gọn gàng
            button.setHorizontalAlignment(SwingConstants.CENTER); // Căn giữa văn bản

            button.addActionListener(e -> {
                fireEditingStopped();
                if (listener != null) {
                    ButtonType buttonType = ((CustomTableModel) table.getModel())
                            .getButtonTypeAt(clickedRow, clickedColumn);
                    if (buttonType != null) {
                        listener.onButtonClick(buttonType, clickedRow, clickedColumn);
                    }
                }
            });
        }

        @Override
        public Component getTableCellEditorComponent(JTable table, Object value,
                                                     boolean isSelected, int row, int column) {
            this.clickedRow = row;
            this.clickedColumn = column;

            ButtonType buttonType = ((CustomTableModel) table.getModel()).getButtonTypeAt(row, column);
            if (buttonType != null) {
                button.setText(buttonType.getText());
                button.setBackground(buttonType.getColor());
                button.setForeground(Color.WHITE);
            } else {
                button.setText(value != null ? value.toString() : "");
                button.setBackground(table.getBackground());
                button.setForeground(table.getForeground());
                button.setPreferredSize(null); // Không áp dụng kích thước cố định cho ô không phải nút
            }

            return button;
        }

        @Override
        public Object getCellEditorValue() {
            return button.getText();
        }
    }

    /**
     * Interface để lắng nghe sự kiện click nút
     */
    public interface ButtonClickListener {
        void onButtonClick(ButtonType buttonType, int row, int column);
    }

    /**
     * Thiết lập listener cho sự kiện click nút
     * @param listener ButtonClickListener
     */
    public void setButtonClickListener(ButtonClickListener listener) {
        for (int i = 0; i < table.getColumnCount(); i++) {
            table.getColumnModel().getColumn(i).setCellRenderer(new ButtonRenderer());
            table.getColumnModel().getColumn(i).setCellEditor(new ButtonEditor(listener));
        }
    }

    /**
     * UI tùy chỉnh cho thanh cuộn
     */
    private class CustomScrollBarUI extends BasicScrollBarUI {
        @Override
        protected void configureScrollBarColors() {
            this.thumbColor = new Color(180, 180, 180);
            this.thumbDarkShadowColor = new Color(180, 180, 180);
            this.thumbHighlightColor = new Color(180, 180, 180);
            this.thumbLightShadowColor = new Color(180, 180, 180);
            this.trackColor = Color.WHITE;
            this.trackHighlightColor = Color.WHITE;
        }

        @Override
        protected JButton createDecreaseButton(int orientation) {
            return createZeroButton();
        }

        @Override
        protected JButton createIncreaseButton(int orientation) {
            return createZeroButton();
        }

        private JButton createZeroButton() {
            JButton button = new JButton();
            button.setPreferredSize(new Dimension(0, 0));
            button.setMinimumSize(new Dimension(0, 0));
            button.setMaximumSize(new Dimension(0, 0));
            return button;
        }

        @Override
        protected void paintThumb(Graphics g, JComponent c, Rectangle thumbBounds) {
            if (thumbBounds.isEmpty() || !scrollbar.isEnabled()) {
                return;
            }

            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2.setColor(thumbColor);

            if (scrollbar.getOrientation() == JScrollBar.VERTICAL) {
                g2.fillRoundRect(thumbBounds.x + 2, thumbBounds.y, thumbBounds.width - 4,
                        thumbBounds.height, 10, 10);
            } else {
                g2.fillRoundRect(thumbBounds.x, thumbBounds.y + 2, thumbBounds.width,
                        thumbBounds.height - 4, 10, 10);
            }

            g2.dispose();
        }

        @Override
        protected void paintTrack(Graphics g, JComponent c, Rectangle trackBounds) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setColor(trackColor);
            g2.fillRect(trackBounds.x, trackBounds.y, trackBounds.width, trackBounds.height);
            g2.dispose();
        }
    }
}