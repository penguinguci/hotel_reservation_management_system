package ui.components.spinner;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.event.*;
import javax.swing.plaf.basic.BasicSpinnerUI;
import java.text.DecimalFormat;

/**
 * CustomSpinner - A modern, elegant JSpinner with enhanced visual appearance
 */
public class CustomSpinner extends JSpinner {

    // Flat modern color scheme
    private Color backgroundColor = new Color(250, 250, 250);
    private Color foregroundColor = new Color(60, 60, 60);
    private Color buttonColor = new Color(240, 240, 240);
    private Color buttonHoverColor = new Color(230, 230, 230);
    private Color buttonPressedColor = new Color(220, 220, 220);
    private Color borderColor = new Color(119, 118, 118);
    private Color textColor = new Color(43, 43, 43);
    private int arcSize = 6;

    public CustomSpinner() {
        initialize();
    }

    public CustomSpinner(SpinnerModel model) {
        super(model);
        initialize();
    }

    private void initialize() {
        setUI(new CustomSpinnerUI());
        setBorder(BorderFactory.createEmptyBorder(2, 5, 2, 2));
        setFont(new Font("SansSerif", Font.PLAIN, 14));

        // Set up number format for numeric spinners
        if (getModel() instanceof SpinnerNumberModel) {
            JSpinner.NumberEditor editor = new JSpinner.NumberEditor(
                    this, "#,##0.##");
            setEditor(editor);

            // Customize editor field
            JFormattedTextField textField = editor.getTextField();
            textField.setForeground(textColor);
            textField.setBorder(BorderFactory.createEmptyBorder(0, 4, 0, 4));
            textField.setFont(new Font("SansSerif", Font.PLAIN, 14));
        }
    }

    // Getter and setter methods for customization
    public Color getBackgroundColor() {
        return backgroundColor;
    }

    public void setBackgroundColor(Color backgroundColor) {
        this.backgroundColor = backgroundColor;
        repaint();
    }

    public Color getForegroundColor() {
        return foregroundColor;
    }

    public void setForegroundColor(Color foregroundColor) {
        this.foregroundColor = foregroundColor;
        repaint();
    }

    public Color getButtonColor() {
        return buttonColor;
    }

    public void setButtonColor(Color buttonColor) {
        this.buttonColor = buttonColor;
        repaint();
    }

    public Color getBorderColor() {
        return borderColor;
    }

    public void setBorderColor(Color borderColor) {
        this.borderColor = borderColor;
        repaint();
    }

    public Color getTextColor() {
        return textColor;
    }

    public void setTextColor(Color textColor) {
        this.textColor = textColor;

        // Update editor text color if applicable
        JComponent editor = getEditor();
        if (editor instanceof JSpinner.DefaultEditor) {
            JFormattedTextField textField = ((JSpinner.DefaultEditor) editor).getTextField();
            textField.setForeground(textColor);
        }

        repaint();
    }

    public int getArcSize() {
        return arcSize;
    }

    public void setArcSize(int arcSize) {
        this.arcSize = arcSize;
        repaint();
    }

    // Custom UI implementation
    class CustomSpinnerUI extends BasicSpinnerUI {
        private boolean upButtonHover = false;
        private boolean downButtonHover = false;
        private boolean upButtonPressed = false;
        private boolean downButtonPressed = false;

        // Explicitly declare these protected fields from BasicSpinnerUI
        protected JComponent editor;
        protected JButton nextButton;
        protected JButton previousButton;

        @Override
        public void installUI(JComponent c) {
            super.installUI(c);
            c.setOpaque(false);
        }

        @Override
        protected JComponent createEditor() {
            // Get the editor from super class implementation
            editor = super.createEditor();

            // Apply custom styling
            if (editor instanceof JSpinner.DefaultEditor) {
                JFormattedTextField textField = ((JSpinner.DefaultEditor) editor).getTextField();
                textField.setForeground(textColor);
                textField.setBorder(BorderFactory.createEmptyBorder(0, 4, 0, 4));
                textField.setFont(new Font("SansSerif", Font.PLAIN, 14));
            }

            return editor;
        }

        @Override
        protected Component createPreviousButton() {
            previousButton = new JButton() {
                @Override
                protected void paintComponent(Graphics g) {
                    Graphics2D g2 = (Graphics2D) g.create();
                    g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

                    // Paint button background - subtle and flat
                    Color btnColor = buttonColor;
                    if (downButtonPressed) {
                        btnColor = buttonPressedColor;
                    } else if (downButtonHover) {
                        btnColor = buttonHoverColor;
                    }

                    g2.setColor(btnColor);
                    g2.fillRect(0, 0, getWidth(), getHeight());

                    // Draw minimalist arrow
                    g2.setColor(foregroundColor);
                    int arrowWidth = 7;
                    int arrowHeight = 3;
                    int x = (getWidth() - arrowWidth) / 2;
                    int y = (getHeight() - arrowHeight) / 2 + 1;

                    for (int i = 0; i < arrowHeight; i++) {
                        g2.drawLine(x + i, y + i, x + arrowWidth - 1 - i, y + i);
                    }

                    g2.dispose();
                }

                @Override
                public Dimension getPreferredSize() {
                    return new Dimension(20, 12);
                }
            };

            previousButton.setFocusPainted(false);
            previousButton.setBorderPainted(false);
            previousButton.setContentAreaFilled(false);

            previousButton.addMouseListener(new MouseAdapter() {
                @Override
                public void mouseEntered(MouseEvent e) {
                    downButtonHover = true;
                    previousButton.repaint();
                }

                @Override
                public void mouseExited(MouseEvent e) {
                    downButtonHover = false;
                    downButtonPressed = false;
                    previousButton.repaint();
                }

                @Override
                public void mousePressed(MouseEvent e) {
                    downButtonPressed = true;
                    previousButton.repaint();
                }

                @Override
                public void mouseReleased(MouseEvent e) {
                    downButtonPressed = false;
                    previousButton.repaint();
                }
            });

            return previousButton;
        }

        @Override
        protected Component createNextButton() {
            nextButton = new JButton() {
                @Override
                protected void paintComponent(Graphics g) {
                    Graphics2D g2 = (Graphics2D) g.create();
                    g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

                    // Paint button background - subtle and flat
                    Color btnColor = buttonColor;
                    if (upButtonPressed) {
                        btnColor = buttonPressedColor;
                    } else if (upButtonHover) {
                        btnColor = buttonHoverColor;
                    }

                    g2.setColor(btnColor);
                    g2.fillRect(0, 0, getWidth(), getHeight());

                    // Draw minimalist arrow
                    g2.setColor(foregroundColor);
                    int arrowWidth = 7;
                    int arrowHeight = 3;
                    int x = (getWidth() - arrowWidth) / 2;
                    int y = (getHeight() - arrowHeight) / 2 - 1;

                    for (int i = 0; i < arrowHeight; i++) {
                        g2.drawLine(x + i, y + (arrowHeight - 1 - i), x + arrowWidth - 1 - i, y + (arrowHeight - 1 - i));
                    }

                    g2.dispose();
                }

                @Override
                public Dimension getPreferredSize() {
                    return new Dimension(20, 12);
                }
            };

            nextButton.setFocusPainted(false);
            nextButton.setBorderPainted(false);
            nextButton.setContentAreaFilled(false);

            nextButton.addMouseListener(new MouseAdapter() {
                @Override
                public void mouseEntered(MouseEvent e) {
                    upButtonHover = true;
                    nextButton.repaint();
                }

                @Override
                public void mouseExited(MouseEvent e) {
                    upButtonHover = false;
                    upButtonPressed = false;
                    nextButton.repaint();
                }

                @Override
                public void mousePressed(MouseEvent e) {
                    upButtonPressed = true;
                    nextButton.repaint();
                }

                @Override
                public void mouseReleased(MouseEvent e) {
                    upButtonPressed = false;
                    nextButton.repaint();
                }
            });

            return nextButton;
        }

        @Override
        public void paint(Graphics g, JComponent c) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

            int width = c.getWidth();
            int height = c.getHeight();

            // Draw clean background with subtle rounded corners
            g2.setColor(backgroundColor);
            g2.fillRoundRect(0, 0, width, height, arcSize, arcSize);

            // Draw thin, subtle border
            g2.setColor(borderColor);
            g2.setStroke(new BasicStroke(1.0f));
            g2.drawRoundRect(0, 0, width - 1, height - 1, arcSize, arcSize);

            // Draw subtle separator between buttons
            int buttonDividerY = height / 2;
            g2.setColor(new Color(borderColor.getRed(), borderColor.getGreen(), borderColor.getBlue(), 150));
            g2.drawLine(width - 20, buttonDividerY, width - 1, buttonDividerY);

            // Draw subtle vertical separator between text and buttons
            g2.drawLine(width - 21, 1, width - 21, height - 2);

            g2.dispose();

            super.paint(g, c);
        }

        @Override
        protected LayoutManager createLayout() {
            return new SpinnerLayout();
        }

        // Improved layout for better proportions
        private class SpinnerLayout implements LayoutManager {
            public void addLayoutComponent(String name, Component c) {}
            public void removeLayoutComponent(Component c) {}

            public Dimension preferredLayoutSize(Container parent) {
                Dimension editorSize = editor.getPreferredSize();
                Dimension nextButtonSize = nextButton.getPreferredSize();
                Dimension previousButtonSize = previousButton.getPreferredSize();

                return new Dimension(
                        editorSize.width + nextButtonSize.width,
                        Math.max(editorSize.height, nextButtonSize.height + previousButtonSize.height)
                );
            }

            public Dimension minimumLayoutSize(Container parent) {
                return preferredLayoutSize(parent);
            }

            public void layoutContainer(Container parent) {
                int width = parent.getWidth();
                int height = parent.getHeight();

                // Layout with fixed button width
                int buttonWidth = 20;

                // Position editor
                editor.setBounds(0, 0, width - buttonWidth, height);

                // Position buttons
                nextButton.setBounds(width - buttonWidth, 0, buttonWidth, height / 2);
                previousButton.setBounds(width - buttonWidth, height / 2, buttonWidth, height / 2);
            }
        }
    }

    // Demo method with multiple theme options
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            try {
                UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
            } catch (Exception e) {
                e.printStackTrace();
            }

            JFrame frame = new JFrame("Custom Spinner Demo");
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.getContentPane().setBackground(new Color(245, 245, 245));
            frame.setLayout(new GridLayout(6, 2, 15, 15));
            ((GridLayout)frame.getLayout()).setHgap(20);
            ((GridLayout)frame.getLayout()).setVgap(20);
            frame.getRootPane().setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

            // Default style
            CustomSpinner defaultSpinner = new CustomSpinner(
                    new SpinnerNumberModel(42, 0, 100, 1));

            // Light blue theme
            CustomSpinner blueSpinner = new CustomSpinner(
                    new SpinnerNumberModel(75, 0, 100, 5));
            blueSpinner.setBackgroundColor(new Color(240, 248, 255));
            blueSpinner.setBorderColor(new Color(176, 196, 222));
            blueSpinner.setButtonColor(new Color(230, 240, 250));
            blueSpinner.setForegroundColor(new Color(70, 130, 180));

            // Light green theme
            CustomSpinner greenSpinner = new CustomSpinner(
                    new SpinnerNumberModel(50, 0, 100, 5));
            greenSpinner.setBackgroundColor(new Color(240, 255, 240));
            greenSpinner.setBorderColor(new Color(144, 238, 144));
            greenSpinner.setButtonColor(new Color(230, 250, 230));
            greenSpinner.setForegroundColor(new Color(60, 179, 113));

            // Date spinner
            CustomSpinner dateSpinner = new CustomSpinner(new SpinnerDateModel());
            JSpinner.DateEditor dateEditor = new JSpinner.DateEditor(
                    dateSpinner, "dd/MM/yyyy");
            dateSpinner.setEditor(dateEditor);
            dateSpinner.setBackgroundColor(new Color(253, 245, 230));
            dateSpinner.setBorderColor(new Color(210, 180, 140));
            dateSpinner.setButtonColor(new Color(250, 240, 230));
            dateSpinner.setForegroundColor(new Color(160, 82, 45));

            // List spinner
            String[] items = {"Apple", "Banana", "Cherry", "Durian", "Elderberry"};
            CustomSpinner listSpinner = new CustomSpinner(
                    new SpinnerListModel(items));
            listSpinner.setBackgroundColor(new Color(245, 245, 255));
            listSpinner.setBorderColor(new Color(230, 230, 250));
            listSpinner.setButtonColor(new Color(240, 240, 245));
            listSpinner.setForegroundColor(new Color(106, 90, 205));

            // Month spinner
            String[] months = {"January", "February", "March", "April", "May", "June",
                    "July", "August", "September", "October", "November", "December"};
            CustomSpinner monthSpinner = new CustomSpinner(
                    new SpinnerListModel(months));
            monthSpinner.setBackgroundColor(new Color(255, 245, 250));
            monthSpinner.setBorderColor(new Color(219, 112, 147, 100));
            monthSpinner.setButtonColor(new Color(255, 240, 245));
            monthSpinner.setForegroundColor(new Color(199, 21, 133));

            // Add components with labels
            JLabel defaultLabel = new JLabel("Default Style:");
            defaultLabel.setFont(new Font("SansSerif", Font.PLAIN, 14));

            JLabel blueLabel = new JLabel("Blue Theme:");
            blueLabel.setFont(new Font("SansSerif", Font.PLAIN, 14));

            JLabel greenLabel = new JLabel("Green Theme:");
            greenLabel.setFont(new Font("SansSerif", Font.PLAIN, 14));

            JLabel dateLabel = new JLabel("Date Spinner:");
            dateLabel.setFont(new Font("SansSerif", Font.PLAIN, 14));

            JLabel listLabel = new JLabel("List Spinner:");
            listLabel.setFont(new Font("SansSerif", Font.PLAIN, 14));

            JLabel monthLabel = new JLabel("Month Spinner:");
            monthLabel.setFont(new Font("SansSerif", Font.PLAIN, 14));

            frame.add(defaultLabel);
            frame.add(defaultSpinner);
            frame.add(blueLabel);
            frame.add(blueSpinner);
            frame.add(greenLabel);
            frame.add(greenSpinner);
            frame.add(dateLabel);
            frame.add(dateSpinner);
            frame.add(listLabel);
            frame.add(listSpinner);
            frame.add(monthLabel);
            frame.add(monthSpinner);

            frame.setSize(450, 350);
            frame.setLocationRelativeTo(null);
            frame.setVisible(true);
        });
    }
}