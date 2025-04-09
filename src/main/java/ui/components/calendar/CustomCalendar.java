package ui.components.calendar;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.geom.RoundRectangle2D;
import java.util.Calendar;
import java.util.Date;

/**
 * CustomCalendar component with purple theme and calendar icon
 * @author TRAN LONG VU
 */
public class CustomCalendar extends JPanel {
    private RoundedButton calendarButton;
    private JPanel calendarPanel;
    private JLabel monthLabel;
    private RoundedButton[] dayButtons;
    private RoundedButton prevButton, nextButton;
    private JDialog calendarDialog;
    private JLabel titleLabel;
    private RoundedButton closeButton;

    private Calendar currentCalendar;
    private boolean isCalendarVisible;
    private Color purpleTheme = new Color(128, 0, 128);
    private Color lightPurple = new Color(230, 220, 255);
    private ImageIcon calendarIcon;

    // Variables for dialog dragging
    private Point initialClick;
    private boolean isDragging = false;

    /**
     * Constructor for CustomCalendar
     */
    public CustomCalendar() {
        setLayout(new BorderLayout());
        currentCalendar = Calendar.getInstance();
        isCalendarVisible = false;

        // Make the panel transparent
        setOpaque(false);

        initializeComponents();
        setupLayout();
        addEventListeners();
    }

    /**
     * Initialize all components
     */
    private void initializeComponents() {
        // Create calendar button with icon
        calendarButton = new RoundedButton("");
        calendarButton.setPreferredSize(new Dimension(150, 30)); // Set a larger size to fit icon and text
        calendarButton.setToolTipText("Select a date"); // Add a tooltip for better usability
        loadCalendarIcon(); // Load icon first
        updateButtonText(); // Then update text (preserves icon)

        // Style the button
        calendarButton.setBackground(lightPurple);
        calendarButton.setForeground(purpleTheme);
        calendarButton.setBorderColor(purpleTheme);
        calendarButton.setArcSize(15);
        calendarButton.setFocusPainted(false);
    }

    /**
     * Initialize the calendar dialog (called when needed)
     */
    private void initializeCalendarDialog() {
        // Get the parent frame
        Frame parentFrame = (Frame) SwingUtilities.getWindowAncestor(this);
        if (parentFrame == null) {
            System.err.println("Parent frame not found. Ensure CustomCalendar is added to a window hierarchy.");
            return;
        }

        // Create undecorated calendar dialog with rounded corners
        calendarDialog = new JDialog(parentFrame, "Select Date", true);
        calendarDialog.setUndecorated(true); // Remove default decorations
        calendarDialog.setSize(300, 280); // Additional height for title bar
        calendarDialog.setResizable(false);
        calendarDialog.setLayout(new BorderLayout());

        // Try to set rounded corners on the dialog (not supported on all platforms)
        try {
            calendarDialog.setShape(new RoundRectangle2D.Double(0, 0, 300, 280, 15, 15));
        } catch (UnsupportedOperationException ex) {
            // Silently ignore if platform doesn't support it
        }

        // Create custom title bar
        JPanel titleBar = new RoundedPanel(15, purpleTheme, null);
        titleBar.setLayout(new BorderLayout());
        titleBar.setBorder(BorderFactory.createEmptyBorder(5, 10, 5, 10));
        titleBar.setPreferredSize(new Dimension(300, 30));

        // Add title and icon to title bar
        titleLabel = new JLabel("Calendar");
        titleLabel.setForeground(Color.WHITE);
        titleLabel.setFont(new Font("SansSerif", Font.BOLD, 14));

        // Add close button to title bar
        closeButton = new RoundedButton("×");
        closeButton.setPreferredSize(new Dimension(20, 20));
        closeButton.setBackground(purpleTheme);
        closeButton.setForeground(Color.WHITE);
        closeButton.setBorderColor(Color.WHITE);
        closeButton.setArcSize(10);
        closeButton.setFocusPainted(false);

        titleBar.add(titleLabel, BorderLayout.WEST);
        titleBar.add(closeButton, BorderLayout.EAST);

        // Create calendar panel with rounded corners
        calendarPanel = new RoundedPanel(0, Color.WHITE, null);
        calendarPanel.setLayout(new BorderLayout());
        calendarPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        // Create month navigation panel
        JPanel navigationPanel = new JPanel(new BorderLayout(10, 0));
        navigationPanel.setOpaque(false);

        prevButton = new RoundedButton("◀");
        prevButton.setBackground(lightPurple);
        prevButton.setForeground(purpleTheme);
        prevButton.setBorderColor(null); // No border
        prevButton.setArcSize(10);
        prevButton.setFocusPainted(false);

        nextButton = new RoundedButton("▶");
        nextButton.setBackground(lightPurple);
        nextButton.setForeground(purpleTheme);
        nextButton.setBorderColor(null); // No border
        nextButton.setArcSize(10);
        nextButton.setFocusPainted(false);

        monthLabel = new JLabel("", JLabel.CENTER);
        monthLabel.setFont(new Font("SansSerif", Font.BOLD, 14));
        monthLabel.setForeground(purpleTheme);

        navigationPanel.add(prevButton, BorderLayout.WEST);
        navigationPanel.add(monthLabel, BorderLayout.CENTER);
        navigationPanel.add(nextButton, BorderLayout.EAST);

        // Create days panel
        JPanel daysPanel = new JPanel(new GridLayout(7, 7, 4, 4));
        daysPanel.setOpaque(false);

        // Add day of week headers
        String[] daysOfWeek = {"Su", "Mo", "Tu", "We", "Th", "Fr", "Sa"};
        for (String day : daysOfWeek) {
            JLabel dayLabel = new JLabel(day, JLabel.CENTER);
            dayLabel.setFont(new Font("SansSerif", Font.BOLD, 12));
            dayLabel.setForeground(purpleTheme);
            daysPanel.add(dayLabel);
        }

        // Initialize day buttons
        dayButtons = new RoundedButton[42]; // Maximum 6 weeks * 7 days
        for (int i = 0; i < dayButtons.length; i++) {
            dayButtons[i] = new RoundedButton();
            dayButtons[i].setFocusPainted(false);
            dayButtons[i].setBackground(Color.WHITE);
            dayButtons[i].setForeground(purpleTheme); // Set text color
            dayButtons[i].setBorderColor(null);
            dayButtons[i].setArcSize(10);
            dayButtons[i].setFont(new Font("SansSerif", Font.PLAIN, 12)); // Set font
            daysPanel.add(dayButtons[i]);
        }

        // Add components to calendar panel
        calendarPanel.add(navigationPanel, BorderLayout.NORTH);
        calendarPanel.add(daysPanel, BorderLayout.CENTER);

        // Create main container for dialog with rounded corners
        RoundedPanel mainContainer = new RoundedPanel(15, lightPurple, purpleTheme);
        mainContainer.setLayout(new BorderLayout());
        mainContainer.add(titleBar, BorderLayout.NORTH);
        mainContainer.add(calendarPanel, BorderLayout.CENTER);

        // Add the main container to dialog
        calendarDialog.add(mainContainer);

        // Update calendar display
        updateCalendarDisplay();

        // Add event listeners for dialog components
        addDialogEventListeners();
    }

    /**
     * Load the calendar icon once
     */
    private void loadCalendarIcon() {
        try {
            // Load icon from the specified path
            String iconPath = "src/main/java/ui/components/calendar/calendar.png";
            java.io.File file = new java.io.File(iconPath);
            if (!file.exists()) {
                System.err.println("Calendar icon not found at: " + iconPath);
                calendarIcon = null;
                return;
            }
            ImageIcon icon = new ImageIcon(file.getAbsolutePath());

            // If the icon is valid, resize it
            if (icon.getIconWidth() != -1) {
                calendarIcon = new ImageIcon(icon.getImage().getScaledInstance(16, 16, Image.SCALE_SMOOTH));
            } else {
                System.err.println("Invalid calendar icon at: " + iconPath);
                calendarIcon = null;
            }
        } catch (Exception e) {
            System.err.println("Error loading calendar icon: " + e.getMessage());
            calendarIcon = null; // Fallback if loading fails
        }
    }

    /**
     * Update the button to show the date and icon
     */
    private void updateButtonText() {
        // Clear text and icon before updating
        calendarButton.setText("");
        calendarButton.setIcon(null);

        // Set the text
        String formattedDate = formatDate(currentCalendar.getTime());
        calendarButton.setText(formattedDate);

        // If there is an icon, set the icon and adjust positioning
        if (calendarIcon != null) {
            calendarButton.setIcon(calendarIcon);
            calendarButton.setHorizontalTextPosition(SwingConstants.RIGHT); // Text to the right of the icon
            calendarButton.setVerticalTextPosition(SwingConstants.CENTER);  // Vertically center the text
            calendarButton.setHorizontalAlignment(SwingConstants.LEFT);    // Align content to the left
            calendarButton.setIconTextGap(20); // Increase the gap to avoid overlap
        }

        // Adjust font and padding
        calendarButton.setFont(new Font("Arial", Font.PLAIN, 12));
        calendarButton.setBorder(BorderFactory.createEmptyBorder(5, 15, 5, 15)); // Increase padding for better spacing
    }

    /**
     * Set up the layout of the component
     */
    private void setupLayout() {
        add(calendarButton, BorderLayout.CENTER);
    }

    /**
     * Add event listeners to components
     */
    private void addEventListeners() {
        // Calendar button click event
        calendarButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Initialize the dialog if not already created
                if (calendarDialog == null) {
                    initializeCalendarDialog();
                }
                if (calendarDialog == null) {
                    return; // Failed to initialize dialog
                }

                // Position the dialog relative to the button
                Point buttonLocation = calendarButton.getLocationOnScreen();
                calendarDialog.setLocation(buttonLocation.x, buttonLocation.y + calendarButton.getHeight());

                // Show calendar dialog
                calendarDialog.setVisible(true);
            }
        });
    }

    /**
     * Add event listeners to dialog components
     */
    private void addDialogEventListeners() {
        // Close button action
        closeButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                calendarDialog.setVisible(false);
            }
        });

        // Previous month button action
        prevButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                currentCalendar.add(Calendar.MONTH, -1);
                updateCalendarDisplay();
            }
        });

        // Next month button action
        nextButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                currentCalendar.add(Calendar.MONTH, 1);
                updateCalendarDisplay();
            }
        });

        // Add mouse listeners for dragging the dialog
        titleLabel.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                initialClick = e.getPoint();
                initialClick.y += titleLabel.getY(); // Add the component's y position
                isDragging = true;
            }

            @Override
            public void mouseReleased(MouseEvent e) {
                isDragging = false;
            }
        });

        titleLabel.addMouseMotionListener(new MouseMotionAdapter() {
            @Override
            public void mouseDragged(MouseEvent e) {
                if (isDragging) {
                    // Get current location of the dialog
                    Point currentLocation = calendarDialog.getLocation();

                    // Calculate how much the mouse moved
                    int dx = e.getX() - initialClick.x;
                    int dy = e.getY() - initialClick.y;

                    // Move the dialog
                    calendarDialog.setLocation(currentLocation.x + dx, currentLocation.y + dy);
                }
            }
        });
    }

    /**
     * Update the calendar display based on the current month
     */
    private void updateCalendarDisplay() {
        // Update month label
        String[] months = {"January", "February", "March", "April", "May", "June",
                "July", "August", "September", "October", "November", "December"};
        int month = currentCalendar.get(Calendar.MONTH);
        int year = currentCalendar.get(Calendar.YEAR);
        monthLabel.setText(months[month] + " " + year);

        // Clear all days
        for (RoundedButton dayButton : dayButtons) {
            dayButton.setText("");
            dayButton.setEnabled(false);
            dayButton.setBackground(Color.WHITE);
            dayButton.setForeground(purpleTheme);
            // Remove all action listeners
            for (ActionListener al : dayButton.getActionListeners()) {
                dayButton.removeActionListener(al);
            }
        }

        // Get current date info
        Calendar calendar = (Calendar) currentCalendar.clone();
        calendar.set(Calendar.DAY_OF_MONTH, 1); // Set to first day of month

        // Calculate first day of week (Sunday = 1, Saturday = 7)
        int firstDayOfWeek = calendar.get(Calendar.DAY_OF_WEEK);
        // Convert to 0-based index (0 = Sunday, 6 = Saturday)
        int startDay = firstDayOfWeek - 1;

        // Get days in current month
        int daysInMonth = calendar.getActualMaximum(Calendar.DAY_OF_MONTH);

        // Get days in the previous month
        calendar.add(Calendar.MONTH, -1);
        int daysInPrevMonth = calendar.getActualMaximum(Calendar.DAY_OF_MONTH);
        calendar.add(Calendar.MONTH, 1); // Reset to current month

        // Fill in days from the previous month
        for (int i = startDay - 1; i >= 0; i--) {
            int day = daysInPrevMonth - (startDay - 1 - i);
            RoundedButton button = dayButtons[i];
            button.setText(String.valueOf(day));
            button.setEnabled(false); // Disable buttons for previous month
            button.setForeground(Color.GRAY); // Use a lighter color for previous month
        }

        // Fill in days for the current month
        for (int i = 0; i < daysInMonth; i++) {
            final int day = i + 1;
            int position = startDay + i;
            RoundedButton button = dayButtons[position];
            button.setText(String.valueOf(day));
            button.setEnabled(true);
            button.setForeground(purpleTheme);

            // Highlight current day
            Calendar todayCal = Calendar.getInstance();
            if (day == todayCal.get(Calendar.DAY_OF_MONTH) &&
                    month == todayCal.get(Calendar.MONTH) &&
                    year == todayCal.get(Calendar.YEAR)) {
                button.setBackground(new Color(200, 180, 255));
            } else {
                button.setBackground(Color.WHITE);
            }

            // Add action listener to day button
            button.addActionListener(e -> {
                currentCalendar.set(Calendar.DAY_OF_MONTH, day);
                updateButtonText();
                calendarDialog.setVisible(false);
            });

            // Hover effect
            button.addMouseListener(new MouseAdapter() {
                @Override
                public void mouseEntered(MouseEvent e) {
                    button.setBackground(lightPurple);
                }

                @Override
                public void mouseExited(MouseEvent e) {
                    if (day == todayCal.get(Calendar.DAY_OF_MONTH) &&
                            month == todayCal.get(Calendar.MONTH) &&
                            year == todayCal.get(Calendar.YEAR)) {
                        button.setBackground(new Color(200, 180, 255));
                    } else {
                        button.setBackground(Color.WHITE);
                    }
                }
            });
        }

        // Fill in days from the next month
        int remainingDays = 42 - (startDay + daysInMonth); // Total grid slots - filled slots
        for (int i = 0; i < remainingDays; i++) {
            int day = i + 1;
            int position = startDay + daysInMonth + i;
            RoundedButton button = dayButtons[position];
            button.setText(String.valueOf(day));
            button.setEnabled(false); // Disable buttons for next month
            button.setForeground(Color.GRAY); // Use a lighter color for next month
        }
    }

    /**
     * Format date to display on calendar button
     * @param date Date to format
     * @return Formatted date string
     */
    private String formatDate(Date date) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        return String.format("%02d/%02d/%04d",
                cal.get(Calendar.DAY_OF_MONTH),
                cal.get(Calendar.MONTH) + 1,
                cal.get(Calendar.YEAR));
    }

    /**
     * Get the currently selected date
     * @return Selected date
     */
    public Date getSelectedDate() {
        return currentCalendar.getTime();
    }

    /**
     * Set the selected date
     * @param date Date to set
     */
    public void setSelectedDate(Date date) {
        currentCalendar.setTime(date);
        updateButtonText(); // Update text while preserving icon
        updateCalendarDisplay();
    }



    /**
     * Custom JButton with rounded corners
     */
    private class RoundedButton extends JButton {
        private Color borderColor;
        private int arcSize = 10;

        public RoundedButton() {
            this("");
        }

        public RoundedButton(String text) {
            super(text);
            setContentAreaFilled(false);
            setBorderColor(purpleTheme);
            setOpaque(false);
            setFocusPainted(false);
            setCursor(new Cursor(Cursor.HAND_CURSOR));
        }

        public void setBorderColor(Color color) {
            this.borderColor = color;
            repaint();
        }

        public void setArcSize(int arcSize) {
            this.arcSize = arcSize;
            repaint();
        }

        @Override
        protected void paintComponent(Graphics g) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

            // Draw the background based on button state
            if (getModel().isPressed()) {
                g2.setColor(getBackground().darker());
            } else if (getModel().isRollover()) {
                g2.setColor(getBackground().brighter());
            } else {
                g2.setColor(getBackground());
            }

            g2.fillRoundRect(0, 0, getWidth() - 1, getHeight() - 1, arcSize, arcSize);

            // Draw the border if specified
            if (borderColor != null) {
                g2.setColor(borderColor);
                g2.drawRoundRect(0, 0, getWidth() - 1, getHeight() - 1, arcSize, arcSize);
            }

            g2.dispose();

            // Let the default JButton rendering handle the text and icon
            super.paintComponent(g);
        }
    }

    /**
     * Custom JPanel with rounded corners
     */
    private class RoundedPanel extends JPanel {
        private int arcSize;
        private Color backgroundColor;
        private Color borderColor;

        public RoundedPanel(int arcSize, Color backgroundColor, Color borderColor) {
            this.arcSize = arcSize;
            this.backgroundColor = backgroundColor;
            this.borderColor = borderColor;
            setOpaque(false);
        }

        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

            // Fill panel
            g2.setColor(backgroundColor);
            g2.fillRoundRect(0, 0, getWidth() - 1, getHeight() - 1, arcSize, arcSize);

            // Draw border
            if (borderColor != null) {
                g2.setColor(borderColor);
                g2.drawRoundRect(0, 0, getWidth() - 1, getHeight() - 1, arcSize, arcSize);
            }

            g2.dispose();
        }
    }

    /**
     * Test the component
     */
    public static void main(String[] args) {
        // Set the look and feel to system default
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
            e.printStackTrace();
        }

        // Test the calendar component
        JFrame frame = new JFrame("Custom Calendar Test");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(400, 100);
        frame.setLayout(new FlowLayout());

        CustomCalendar calendar = new CustomCalendar();
        frame.add(calendar);

        frame.setVisible(true);
    }
}