package ui.forms;

import javax.swing.*;
import java.awt.*;

public class Form_Booking extends JPanel {

    public Form_Booking() {
        initComponents();
    }

    @SuppressWarnings("unchecked")
    private void initComponents() {
        JLabel lblTitle = new JLabel("Booking Form");
        lblTitle.setFont(new Font("Segoe UI", Font.BOLD, 24));
        lblTitle.setHorizontalAlignment(SwingConstants.CENTER);

        JLabel lblName = new JLabel("Guest Name:");
        JTextField txtName = new JTextField(20);

        JLabel lblCheckInDate = new JLabel("Check-in Date:");
        JTextField txtCheckInDate = new JTextField(20);

        JLabel lblCheckOutDate = new JLabel("Check-out Date:");
        JTextField txtCheckOutDate = new JTextField(20);

        JLabel lblRoomType = new JLabel("Room Type:");
        String[] roomTypes = {"Single", "Double", "Suite"};
        JComboBox<String> cbRoomType = new JComboBox<>(roomTypes);

        JButton btnBook = new JButton("Book Now");
        btnBook.setBackground(new Color(149, 145, 239));
        btnBook.setForeground(Color.WHITE);
        btnBook.addActionListener(e -> {
            // Xử lý đặt phòng ở đây
            JOptionPane.showMessageDialog(this, "Booking Successful!");
        });

        // Tạo layout
        setLayout(new GridLayout(6, 2, 10, 10));
        add(lblTitle);
        add(new JLabel()); // Placeholder for alignment
        add(lblName);
        add(txtName);
        add(lblCheckInDate);
        add(txtCheckInDate);
        add(lblCheckOutDate);
        add(txtCheckOutDate);
        add(lblRoomType);
        add(cbRoomType);
        add(new JLabel()); // Placeholder for alignment
        add(btnBook);

        // Thiết lập kích thước và màu nền
        setPreferredSize(new Dimension(800, 525));
        setBackground(Color.WHITE);
    }
}