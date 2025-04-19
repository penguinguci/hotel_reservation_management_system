/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */
package ui.gui;

import dao.AccountDAOImpl;
import entities.Account;
import interfaces.AccountDAO;
import org.mindrot.jbcrypt.BCrypt;
import utils.CurrentAccount;

import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import javax.swing.*;
import java.awt.*;
import java.awt.geom.RoundRectangle2D;
import java.rmi.RemoteException;
import java.util.Properties;
import java.util.Random;

public class GUI_Login extends javax.swing.JFrame {

    private String verificationCode; // Lưu mã xác thực
    private String verifiedEmail; // Lưu email đã được xác thực

    public GUI_Login() {
        setUndecorated(true); // Ẩn các nút điều khiển
        setShape(new RoundRectangle2D.Double(0, 0, 800, 500, 30, 30));
        initComponents();
        setLocationRelativeTo(null);
    }

    @Override
    public void paint(Graphics g) {
        super.paint(g);
        Graphics2D g2 = (Graphics2D) g;
        g2.setColor(Color.black); // Màu đường viền
        g2.setStroke(new BasicStroke(4)); // Độ dày của đường viền
        g2.draw(new RoundRectangle2D.Double(0, 0, getWidth(), getHeight(), 30, 30)); // Vẽ đường viền
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">
    private void initComponents() {
        jPanel1 = new javax.swing.JPanel();
        Left = new javax.swing.JPanel();
        lb_Welcome = new javax.swing.JLabel();
        lb_Logo = new javax.swing.JLabel();
        Right = new javax.swing.JPanel();
        lb_Title = new javax.swing.JLabel();
        lb_Username = new javax.swing.JLabel();
        lb_Password = new javax.swing.JLabel();
        btn_Login = new ui.components.button.ButtonCustom();
        btn_Exit = new ui.components.button.ButtonCustom();
        roundedTextField1 = new ui.components.textfield.RoundedTextField();
        roundedPasswordField1 = new ui.components.textfield.RoundedPasswordField();
        btn_ForgotPassword = new ui.components.button.ButtonCustom();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("Login to Melody System");

        jPanel1.setBackground(new java.awt.Color(255, 255, 255));
        jPanel1.setPreferredSize(new java.awt.Dimension(800, 500));
        jPanel1.setLayout(null);

        Left.setBackground(new java.awt.Color(149, 145, 239));
        Left.setPreferredSize(new java.awt.Dimension(400, 500));

        lb_Welcome.setFont(new java.awt.Font("Segoe UI", 3, 24)); // NOI18N
        lb_Welcome.setForeground(new java.awt.Color(255, 255, 255));
        lb_Welcome.setText("Melody Hotel xin chào!!!");

        lb_Logo.setIcon(new javax.swing.ImageIcon(getClass().getResource("/logo.png"))); // NOI18N

        javax.swing.GroupLayout LeftLayout = new javax.swing.GroupLayout(Left);
        Left.setLayout(LeftLayout);
        LeftLayout.setHorizontalGroup(
                LeftLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(LeftLayout.createSequentialGroup()
                                .addGroup(LeftLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                        .addGroup(LeftLayout.createSequentialGroup()
                                                .addGap(55, 55, 55)
                                                .addComponent(lb_Welcome))
                                        .addGroup(LeftLayout.createSequentialGroup()
                                                .addGap(19, 19, 19)
                                                .addComponent(lb_Logo, javax.swing.GroupLayout.PREFERRED_SIZE, 347, javax.swing.GroupLayout.PREFERRED_SIZE)))
                                .addContainerGap(34, Short.MAX_VALUE))
        );
        LeftLayout.setVerticalGroup(
                LeftLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(LeftLayout.createSequentialGroup()
                                .addGap(55, 55, 55)
                                .addComponent(lb_Welcome)
                                .addGap(44, 44, 44)
                                .addComponent(lb_Logo)
                                .addContainerGap(130, Short.MAX_VALUE))
        );

        jPanel1.add(Left);
        Left.setBounds(0, 0, 400, 500);

        Right.setBackground(new java.awt.Color(255, 255, 255));
        Right.setMinimumSize(new java.awt.Dimension(400, 500));

        lb_Title.setFont(new java.awt.Font("Segoe UI", 1, 24)); // NOI18N
        lb_Title.setForeground(new java.awt.Color(149, 145, 239));
        lb_Title.setText("Đăng nhập");

        lb_Username.setBackground(new java.awt.Color(102, 102, 102));
        lb_Username.setFont(new java.awt.Font("Segoe UI", 0, 16)); // NOI18N
        lb_Username.setText("Tài khoản:");

        lb_Password.setBackground(new java.awt.Color(102, 102, 102));
        lb_Password.setFont(new java.awt.Font("Segoe UI", 0, 16)); // NOI18N
        lb_Password.setText("Mật khẩu:");

        btn_Login.setBackground(new java.awt.Color(149, 145, 239));
        btn_Login.setText("Đăng nhập");
        btn_Login.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                try {
                    btn_LoginActionPerformed(evt);
                } catch (RemoteException e) {
                    throw new RuntimeException(e);
                }
            }
        });

        btn_Exit.setText("Thoát");
        btn_Exit.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_ExitActionPerformed(evt);
            }
        });

        roundedTextField1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                roundedTextField1ActionPerformed(evt);
            }
        });
        roundedPasswordField1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                try {
                    roundedPasswordField1ActionPerformed(evt);
                } catch (RemoteException e) {
                    throw new RuntimeException(e);
                }
            }
        });
        btn_ForgotPassword.setBackground(new java.awt.Color(149, 145, 239));
        btn_ForgotPassword.setText("Quên mật khẩu");
        btn_ForgotPassword.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                try {
                    btn_ForgotPasswordActionPerformed(evt);
                } catch (RemoteException e) {
                    throw new RuntimeException(e);
                }
            }
        });

        javax.swing.GroupLayout RightLayout = new javax.swing.GroupLayout(Right);
        Right.setLayout(RightLayout);
        RightLayout.setHorizontalGroup(
                RightLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, RightLayout.createSequentialGroup()
                                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(lb_Title)
                                .addGap(131, 131, 131))
                        .addGroup(RightLayout.createSequentialGroup()
                                .addGroup(RightLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                        .addGroup(RightLayout.createSequentialGroup()
                                                .addContainerGap()
                                                .addComponent(btn_Exit, javax.swing.GroupLayout.PREFERRED_SIZE, 317, javax.swing.GroupLayout.PREFERRED_SIZE))
                                        .addGroup(javax.swing.GroupLayout.Alignment.LEADING, RightLayout.createSequentialGroup()
                                                .addGap(39, 39, 39)
                                                .addGroup(RightLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                                        .addComponent(roundedTextField1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                                        .addComponent(roundedPasswordField1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                                        .addGroup(RightLayout.createSequentialGroup()
                                                                .addGroup(RightLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                                                        .addComponent(lb_Username)
                                                                        .addComponent(lb_Password))
                                                                .addGap(247, 247, 247))
                                                        .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, RightLayout.createSequentialGroup()
                                                                .addComponent(btn_ForgotPassword, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                                .addGap(27, 27, 27)
                                                                .addComponent(btn_Login, javax.swing.GroupLayout.PREFERRED_SIZE, 145, javax.swing.GroupLayout.PREFERRED_SIZE)))))
                                .addContainerGap(44, Short.MAX_VALUE))
        );
        RightLayout.setVerticalGroup(
                RightLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(RightLayout.createSequentialGroup()
                                .addGap(61, 61, 61)
                                .addComponent(lb_Title)
                                .addGap(38, 38, 38)
                                .addComponent(lb_Username)
                                .addGap(18, 18, 18)
                                .addComponent(roundedTextField1, javax.swing.GroupLayout.PREFERRED_SIZE, 45, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(18, 18, 18)
                                .addComponent(lb_Password)
                                .addGap(18, 18, 18)
                                .addComponent(roundedPasswordField1, javax.swing.GroupLayout.PREFERRED_SIZE, 45, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(34, 34, 34)
                                .addGroup(RightLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                        .addComponent(btn_Login, javax.swing.GroupLayout.PREFERRED_SIZE, 47, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addComponent(btn_ForgotPassword, javax.swing.GroupLayout.PREFERRED_SIZE, 47, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addGap(18, 18, 18)
                                .addComponent(btn_Exit, javax.swing.GroupLayout.PREFERRED_SIZE, 47, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addContainerGap(35, Short.MAX_VALUE))
        );

        jPanel1.add(Right);
        Right.setBounds(400, 0, 400, 500);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
                layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(layout.createSequentialGroup()
                                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(0, 0, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
                layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
        );

        pack();
    }

    private void btn_LoginActionPerformed(java.awt.event.ActionEvent evt) throws RemoteException {
        String username = roundedTextField1.getText();
        String password = new String(roundedPasswordField1.getPassword());

        AccountDAO accountDAO = new AccountDAOImpl();
        Account account = accountDAO.getAccount(username);
        if (account != null && BCrypt.checkpw(password, account.getPassword())) {
            CurrentAccount.setCurrentAccount(account);
            //JOptionPane.showMessageDialog(this, "Login Successful", "Notification", JOptionPane.INFORMATION_MESSAGE);
            this.dispose();
            GUI_Main guiMain = new GUI_Main();
            guiMain.setVisible(true);
        } else {
            JOptionPane.showMessageDialog(this, "Đăng nhập thất bại", "Warning", JOptionPane.INFORMATION_MESSAGE);
        }
    }

    private void btn_ExitActionPerformed(java.awt.event.ActionEvent evt) {
        System.exit(0);
    }

    private void roundedTextField1ActionPerformed(java.awt.event.ActionEvent evt) {
        if (evt.getSource() == roundedTextField1) {
            roundedPasswordField1.requestFocus();
        }
    }

    private void btn_ForgotPasswordActionPerformed(java.awt.event.ActionEvent evt) throws RemoteException {
        // Hiển thị dialog nhập email
        JTextField emailField = new JTextField();
        Object[] message = {
                "Nhập email của bạn:", emailField
        };
        int option = JOptionPane.showConfirmDialog(this, message, "Quên mật khẩu", JOptionPane.OK_CANCEL_OPTION);
        if (option == JOptionPane.OK_OPTION) {
            String email = emailField.getText().trim();
            if (email.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Vui lòng nhập email.", "Lỗi", JOptionPane.ERROR_MESSAGE);
                return;
            }

            AccountDAO accountDAO = new AccountDAOImpl();
            Account account = accountDAO.getAccountByEmail(email);

            if (account != null) {
                // Tạo mã xác thực ngẫu nhiên
                verificationCode = generateVerificationCode();
                verifiedEmail = email;

                // Gửi mã xác thực qua email
                boolean sent = sendVerificationEmail(email, verificationCode);
                if (sent) {
                    // Hiển thị dialog nhập mã xác thực và mật khẩu mới
                    showVerificationDialog(account);
                } else {
                    JOptionPane.showMessageDialog(this, "Lỗi khi gửi email. Vui lòng thử lại.", "Lỗi", JOptionPane.ERROR_MESSAGE);
                }
            } else {
                JOptionPane.showMessageDialog(this, "Email không tồn tại trong hệ thống.", "Lỗi", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void roundedPasswordField1ActionPerformed(java.awt.event.ActionEvent evt) throws RemoteException {
        if (evt.getSource() == roundedPasswordField1) {
            btn_LoginActionPerformed(evt);
        }
    }

    // Tạo mã xác thực ngẫu nhiên
    private String generateVerificationCode() {
        Random random = new Random();
        int code = 100000 + random.nextInt(900000); // Mã 6 chữ số
        return String.valueOf(code);
    }

    // Gửi email chứa mã xác thực
    private boolean sendVerificationEmail(String toEmail, String code) {
        final String fromEmail = "vinhthai.2612@gmail.com"; // Thay bằng email của bạn
        final String password = "spkl cubi udxk zjmz"; // Thay bằng mật khẩu ứng dụng

        Properties props = new Properties();
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");
        props.put("mail.smtp.host", "smtp.gmail.com");
        props.put("mail.smtp.port", "587");

        Session session = Session.getInstance(props, new Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(fromEmail, password);
            }
        });

        try {
            Message message = new MimeMessage(session);
            message.setFrom(new InternetAddress(fromEmail));
            message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(toEmail));
            message.setSubject("Mã xác thực đặt lại mật khẩu");
            message.setText("Mã xác thực của bạn là: " + code + "\nVui lòng sử dụng mã này để đặt lại mật khẩu.");
            Transport.send(message);
            return true;
        } catch (MessagingException e) {
            e.printStackTrace();
            return false;
        }
    }

    // Hiển thị dialog để nhập mã xác thực và mật khẩu mới
    private void showVerificationDialog(Account account) throws RemoteException {
        JTextField codeField = new JTextField();
        JPasswordField newPasswordField = new JPasswordField();
        JPasswordField confirmPasswordField = new JPasswordField();
        Object[] message = {
                "Nhập mã xác thực:", codeField,
                "Nhập mật khẩu mới:", newPasswordField,
                "Xác nhận mật khẩu mới:", confirmPasswordField
        };
        int option = JOptionPane.showConfirmDialog(this, message, "Xác thực và đặt lại mật khẩu", JOptionPane.OK_CANCEL_OPTION);
        if (option == JOptionPane.OK_OPTION) {
            String enteredCode = codeField.getText().trim();
            String newPassword = new String(newPasswordField.getPassword());
            String confirmPassword = new String(confirmPasswordField.getPassword());

            if (!newPassword.equals(confirmPassword)) {
                JOptionPane.showMessageDialog(this, "Mật khẩu xác nhận không khớp.", "Lỗi", JOptionPane.ERROR_MESSAGE);
                return;
            }

            if (enteredCode.equals(verificationCode)) {
                AccountDAO accountDAO = new AccountDAOImpl();
                String hashedPassword = BCrypt.hashpw(newPassword, BCrypt.gensalt());
                account.setPassword(hashedPassword);
                accountDAO.updateAccount(account);
                JOptionPane.showMessageDialog(this, "Đặt lại mật khẩu thành công!", "Thông báo", JOptionPane.INFORMATION_MESSAGE);
            }
        }
    }

    public static void main(String args[]) {
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException | InstantiationException | IllegalAccessException | javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(GUI_Login.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }

        java.awt.EventQueue.invokeLater(() -> new GUI_Login().setVisible(true));
    }

    // Variables declaration
    private javax.swing.JPanel Left;
    private javax.swing.JPanel Right;
    private ui.components.button.ButtonCustom btn_Exit;
    private ui.components.button.ButtonCustom btn_ForgotPassword;
    private ui.components.button.ButtonCustom btn_Login;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JLabel lb_Logo;
    private javax.swing.JLabel lb_Password;
    private javax.swing.JLabel lb_Title;
    private javax.swing.JLabel lb_Username;
    private javax.swing.JLabel lb_Welcome;
    private ui.components.textfield.RoundedPasswordField roundedPasswordField1;
    private ui.components.textfield.RoundedTextField roundedTextField1;
}