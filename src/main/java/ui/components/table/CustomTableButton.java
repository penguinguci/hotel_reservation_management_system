package ui.components.table;

import javax.swing.*;
import javax.swing.plaf.basic.BasicScrollBarUI;
import javax.swing.table.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.beans.PropertyChangeListener;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class CustomTableButton extends JPanel implements Serializable {

    private static final long serialVersionUID = 1L;

    private JTable table;
    private CustomTableModel tableModel;
    private JScrollPane scrollPane;
    private Color headerBackgroundColor = new Color(51, 102, 153);
    private Color headerForegroundColor = Color.WHITE;
    private int headerHeight = 35;
    private String[] columnNames = {"Column 1", "Column 2", "Column 3"};
    private ColumnEditorType[] columnEditorTypes;

    public CustomTableButton() {
        initialize(columnNames);
    }

    public CustomTableButton(String[] columnNames) {
        initialize(columnNames);
    }

    private void initialize(String[] columns) {
        setLayout(new BorderLayout());

        tableModel = new CustomTableModel(columns);
        table = new JTable(tableModel) {
            @Override
            public Component prepareRenderer(TableCellRenderer renderer, int row, int column) {
                Component comp = super.prepareRenderer(renderer, row, column);
                if (!isRowSelected(row)) {
                    Color bg = (row % 2 == 0) ? new Color(250, 250, 250) : new Color(230, 230, 230);
                    comp.setBackground(bg);
                } else {
                    comp.setBackground(new Color(156, 151, 246));
                    comp.setFont(new Font("SansSerif", Font.PLAIN, 12));
                }
                return comp;
            }
        };
        table.setRowHeight(40);
        table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        table.setAutoCreateRowSorter(true);

        applyHeaderProperties();

        scrollPane = new JScrollPane(table);
        scrollPane.setBorder(BorderFactory.createEmptyBorder());
        scrollPane.getViewport().setBackground(Color.WHITE);

        scrollPane.getVerticalScrollBar().setUI(new CustomScrollBarUI());
        scrollPane.getHorizontalScrollBar().setUI(new CustomScrollBarUI());

        add(scrollPane, BorderLayout.CENTER);
        setPreferredSize(new Dimension(400, 300));

        addPropertyChangeListener("headerBackgroundColor", evt -> applyHeaderProperties());
        addPropertyChangeListener("headerForegroundColor", evt -> applyHeaderProperties());
        addPropertyChangeListener("headerHeight", evt -> applyHeaderProperties());

        updateTableRenderers();
    }

    private void applyHeaderProperties() {
        if (table != null && table.getTableHeader() != null) {
            JTableHeader header = table.getTableHeader();
            header.setOpaque(true);
            header.setBackground(headerBackgroundColor);
            header.setForeground(headerForegroundColor);
            header.setFont(new Font("SansSerif", Font.BOLD, 12));
            header.setPreferredSize(new Dimension(header.getWidth(), headerHeight));
            header.setDefaultRenderer(new DefaultTableCellRenderer() {
                @Override
                public Component getTableCellRendererComponent(JTable table, Object value,
                                                               boolean isSelected, boolean hasFocus, int row, int column) {
                    JLabel label = (JLabel) super.getTableCellRendererComponent(
                            table, value, isSelected, hasFocus, row, column);
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
        }
    }

    public void setColumnNames(String[] columnNames) {
        this.columnNames = columnNames;
        tableModel = new CustomTableModel(columnNames);
        table.setModel(tableModel);
        updateTableRenderers();
        applyHeaderProperties();
    }

    public String[] getColumnNames() {
        return columnNames;
    }

    public void setHeaderBackgroundColor(Color color) {
        Color oldColor = this.headerBackgroundColor;
        this.headerBackgroundColor = color;
        firePropertyChange("headerBackgroundColor", oldColor, color);
        applyHeaderProperties();
    }

    public Color getHeaderBackgroundColor() {
        return headerBackgroundColor;
    }

    public void setHeaderForegroundColor(Color color) {
        Color oldColor = this.headerForegroundColor;
        this.headerForegroundColor = color;
        firePropertyChange("headerForegroundColor", oldColor, color);
        applyHeaderProperties();
    }

    public Color getHeaderForegroundColor() {
        return headerForegroundColor;
    }

    public void setHeaderHeight(int height) {
        int oldHeight = this.headerHeight;
        this.headerHeight = height;
        firePropertyChange("headerHeight", oldHeight, height);
        applyHeaderProperties();
    }

    public int getHeaderHeight() {
        return headerHeight;
    }

    public void setRowHeight(int height) {
        table.setRowHeight(height);
    }

    public int getRowHeight() {
        return table.getRowHeight();
    }

    public void addRow(Object[] rowData, ButtonType[] buttonTypes) {
        tableModel.addRow(rowData, buttonTypes);
    }

    public void clearTable() {
        tableModel.clearData();
    }

    public void setScrollPaneSize(int width, int height) {
        scrollPane.setPreferredSize(new Dimension(width, height));
    }

    public JTable getTable() {
        return table;
    }

    public JScrollPane getScrollPane() {
        return scrollPane;
    }

    public CustomTableModel getTableModel() {
        return tableModel;
    }

    private void updateTableRenderers() {
        for (int i = 0; i < table.getColumnCount(); i++) {
            if (columnEditorTypes != null && i < columnEditorTypes.length) {
                switch (columnEditorTypes[i]) {
                    case SPINNER:
                        table.getColumnModel().getColumn(i).setCellRenderer(new SpinnerRenderer());
                        table.getColumnModel().getColumn(i).setCellEditor(new SpinnerEditor());
                        break;
                    case BUTTON:
                        table.getColumnModel().getColumn(i).setCellRenderer(new ButtonRenderer());
                        table.getColumnModel().getColumn(i).setCellEditor(new ButtonEditor(null));
                        break;
                    default:
                        table.getColumnModel().getColumn(i).setCellRenderer(new ButtonRenderer());
                        break;
                }
            } else {
                table.getColumnModel().getColumn(i).setCellRenderer(new ButtonRenderer());
            }
        }
    }

    public enum ButtonType {
        EDIT("Sửa", new Color(66, 139, 202)),
        DELETE("Xóa", new Color(217, 83, 79)),
        VIEW("Xem", new Color(91, 192, 222)),
        ADD("Thêm", new Color(92, 184, 92)),
        CUSTOM("Tùy chỉnh", new Color(240, 173, 78)),
        SERVICE("Thêm dịch vụ", new Color(153, 102, 255));

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

    public enum ColumnEditorType {
        DEFAULT, SPINNER, BUTTON
    }

    public void setColumnEditorTypes(ColumnEditorType[] editorTypes) {
        this.columnEditorTypes = editorTypes;
        setupCellEditors();
    }

    private void setupCellEditors() {
        if (columnEditorTypes == null || columnEditorTypes.length != table.getColumnCount()) {
            return;
        }

        for (int i = 0; i < columnEditorTypes.length; i++) {
            switch (columnEditorTypes[i]) {
                case SPINNER:
                    table.getColumnModel().getColumn(i).setCellEditor(new SpinnerEditor());
                    break;
                case BUTTON:
                    table.getColumnModel().getColumn(i).setCellEditor(new ButtonEditor(null));
                    break;
                default:
                    // Use default editor
                    break;
            }
        }
    }

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
            if (columnEditorTypes != null && columnIndex < columnEditorTypes.length) {
                return columnEditorTypes[columnIndex] != ColumnEditorType.DEFAULT;
            }
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

        public void setValueAt(Object value, int row, int col) {
            data.get(row)[col] = value;
            fireTableCellUpdated(row, col);
        }
    }

    public static class ButtonRenderer extends JButton implements TableCellRenderer {
        public ButtonRenderer() {
            setOpaque(true);
            setBorderPainted(false);
            setFocusPainted(false);
            setContentAreaFilled(true);
            setPreferredSize(new Dimension(70, 20));
            setMargin(new Insets(2, 5, 2, 5));
            setHorizontalAlignment(SwingConstants.CENTER);
        }

        @Override
        public Component getTableCellRendererComponent(JTable table, Object value,
                                                       boolean isSelected, boolean hasFocus, int row, int column) {
            ButtonType buttonType = ((CustomTableModel) table.getModel()).getButtonTypeAt(row, column);
            if (buttonType != null) {
                setText(buttonType.getText());
                setBackground(new Color(6, 157, 213));
                setOpaque(true);
                setForeground(Color.WHITE);
            } else {
                setText(value != null ? value.toString() : "");
                setBackground(null);
                setForeground(isSelected ? table.getSelectionForeground() : table.getForeground());
                setPreferredSize(null);
            }
            return this;
        }
    }

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
            button.setPreferredSize(new Dimension(70, 20));
            button.setMargin(new Insets(2, 5, 2, 5));
            button.setHorizontalAlignment(SwingConstants.CENTER);
            button.addActionListener(e -> {
                System.out.println("Button in table clicked!");
                fireEditingStopped();
                if (listener != null) {
                    ButtonType buttonType = ((CustomTableModel) table.getModel())
                            .getButtonTypeAt(clickedRow, clickedColumn);
                    System.out.println("Button type: " + buttonType);
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
                button.setPreferredSize(null);
            }
            return button;
        }

        @Override
        public Object getCellEditorValue() {
            return button.getText();
        }
    }

    public interface ButtonClickListener {
        void onButtonClick(ButtonType buttonType, int row, int column);
    }

    public void setButtonClickListener(ButtonClickListener listener) {
        for (int i = 0; i < table.getColumnCount(); i++) {
            if (columnEditorTypes != null && i < columnEditorTypes.length &&
                    columnEditorTypes[i] == ColumnEditorType.BUTTON) {
                table.getColumnModel().getColumn(i).setCellRenderer(new ButtonRenderer());
                table.getColumnModel().getColumn(i).setCellEditor(new ButtonEditor(listener));
            }
        }
    }

    public static class SpinnerRenderer extends DefaultTableCellRenderer {
        private JSpinner spinner;

        public SpinnerRenderer() {
            spinner = new JSpinner(new SpinnerNumberModel(1, 1, 30, 1));
        }

        @Override
        public Component getTableCellRendererComponent(JTable table, Object value,
                                                       boolean isSelected, boolean hasFocus,
                                                       int row, int column) {
            spinner.setValue(value != null ? value : 1);
            return spinner;
        }
    }

    public static class SpinnerEditor extends DefaultCellEditor {
        private JSpinner spinner;

        public SpinnerEditor() {
            super(new JTextField());
            spinner = new JSpinner(new SpinnerNumberModel(1, 1, 30, 1));
            spinner.addChangeListener(e -> fireEditingStopped());
            editorComponent = spinner;
        }

        @Override
        public Object getCellEditorValue() {
            return spinner.getValue();
        }

        @Override
        public Component getTableCellEditorComponent(JTable table, Object value,
                                                     boolean isSelected, int row, int column) {
            spinner.setValue(value != null ? value : 1);
            return spinner;
        }
    }

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