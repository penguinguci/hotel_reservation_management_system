/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */
package ui.gui;


import dao.AccountDAO;
import dao.AccountDAOImpl;
import entities.Account;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;

import javax.swing.*;
import java.awt.*;
import java.rmi.RemoteException;

public class GUI_Login extends javax.swing.JFrame {

    public GUI_Login() {
        initComponents();
        setLocationRelativeTo(null);
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
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

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("Login to Melody System");

        jPanel1.setBackground(new java.awt.Color(255, 255, 255));
        jPanel1.setPreferredSize(new java.awt.Dimension(800, 500));
        jPanel1.setLayout(null);

        Left.setBackground(new java.awt.Color(149, 145, 239));
        Left.setPreferredSize(new java.awt.Dimension(400, 500));

        lb_Welcome.setFont(new java.awt.Font("Segoe UI", 3, 24)); // NOI18N
        lb_Welcome.setForeground(new java.awt.Color(255, 255, 255));
        lb_Welcome.setText("Welcome to Melody Hotel");


        String imagePath = "D:\\Distributed Programming\\workspace\\hotel_reservation_management_system\\src\\main\\java\\images\\logo.png";
        ImageIcon originalIcon = new ImageIcon(imagePath);

        lb_Logo.setBounds(0, 0, 350, 230);

        int labelWidth = lb_Logo.getWidth();
        int labelHeight = lb_Logo.getHeight();

        Image resizedImage = originalIcon.getImage().getScaledInstance(labelWidth, labelHeight, Image.SCALE_SMOOTH);
        ImageIcon resizedIcon = new ImageIcon(resizedImage);

        lb_Logo.setIcon(resizedIcon);
        lb_Logo.setText("");


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
        lb_Title.setText("LOGIN");

        lb_Username.setBackground(new java.awt.Color(102, 102, 102));
        lb_Username.setFont(new java.awt.Font("Segoe UI", 0, 16)); // NOI18N
        lb_Username.setText("Username:");

        lb_Password.setBackground(new java.awt.Color(102, 102, 102));
        lb_Password.setFont(new java.awt.Font("Segoe UI", 0, 16)); // NOI18N
        lb_Password.setText("Password:");

        btn_Login.setBackground(new java.awt.Color(149, 145, 239));
        btn_Login.setText("Login");
        btn_Login.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                try {
                    btn_LoginActionPerformed(evt);
                } catch (RemoteException e) {
                    throw new RuntimeException(e);
                }
            }
        });

        btn_Exit.setText("Exit");
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

        javax.swing.GroupLayout RightLayout = new javax.swing.GroupLayout(Right);
        Right.setLayout(RightLayout);
        RightLayout.setHorizontalGroup(
            RightLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, RightLayout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(lb_Title)
                .addGap(160, 160, 160))
            .addGroup(RightLayout.createSequentialGroup()
                .addGap(39, 39, 39)
                .addGroup(RightLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, RightLayout.createSequentialGroup()
                        .addComponent(btn_Login, javax.swing.GroupLayout.PREFERRED_SIZE, 145, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(27, 27, 27)
                        .addComponent(btn_Exit, javax.swing.GroupLayout.PREFERRED_SIZE, 145, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(lb_Username)
                    .addComponent(lb_Password)
                    .addComponent(roundedTextField1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(roundedPasswordField1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap(44, Short.MAX_VALUE))
        );
        RightLayout.setVerticalGroup(
            RightLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(RightLayout.createSequentialGroup()
                .addGap(55, 55, 55)
                .addComponent(lb_Title)
                .addGap(44, 44, 44)
                .addComponent(lb_Username)
                .addGap(18, 18, 18)
                .addComponent(roundedTextField1, javax.swing.GroupLayout.PREFERRED_SIZE, 45, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(lb_Password)
                .addGap(18, 18, 18)
                .addComponent(roundedPasswordField1, javax.swing.GroupLayout.PREFERRED_SIZE, 45, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(26, 26, 26)
                .addGroup(RightLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btn_Login, javax.swing.GroupLayout.PREFERRED_SIZE, 47, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btn_Exit, javax.swing.GroupLayout.PREFERRED_SIZE, 47, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(108, Short.MAX_VALUE))
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
    }// </editor-fold>//GEN-END:initComponents

    private void btn_LoginActionPerformed(java.awt.event.ActionEvent evt) throws RemoteException {//GEN-FIRST:event_btn_LoginActionPerformed
        // TODO add your handling code here:
        String username = roundedTextField1.getText();
        String password = new String(roundedPasswordField1.getPassword());

        AccountDAO accountDAO = new AccountDAOImpl();
        Account account = accountDAO.getAccount(username);
        if(account != null && account.getPassword().equals(password)){
            JOptionPane.showMessageDialog(this  , "Login Successful", "Notification", JOptionPane.INFORMATION_MESSAGE);
            this.dispose();
            GUI_Main guiMain = new GUI_Main();
            guiMain.setVisible(true);
        }
        else{
            JOptionPane.showMessageDialog(this, "Login Fail", "Warning", JOptionPane.INFORMATION_MESSAGE);

        }
    }//GEN-LAST:event_btn_LoginActionPerformed

    private void btn_ExitActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_ExitActionPerformed
        System.exit(0);
    }//GEN-LAST:event_btn_ExitActionPerformed

    private void roundedTextField1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_roundedTextField1ActionPerformed
        // TODO add your handling code here:
        if (evt.getSource() == roundedTextField1) {
            roundedPasswordField1.requestFocus();
        }
    }//GEN-LAST:event_roundedTextField1ActionPerformed
    private void roundedPasswordField1ActionPerformed(java.awt.event.ActionEvent evt) throws RemoteException {
        if (evt.getSource() == roundedPasswordField1) {
            btn_LoginActionPerformed(evt);
        }
    }

    /**
     * @param args the command line arguments
     */ 
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(GUI_Login.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(GUI_Login.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(GUI_Login.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(GUI_Login.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new GUI_Login().setVisible(true);
                
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JPanel Left;
    private javax.swing.JPanel Right;
    private ui.components.button.ButtonCustom btn_Exit;
    private ui.components.button.ButtonCustom btn_Login;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JLabel lb_Logo;
    private javax.swing.JLabel lb_Password;
    private javax.swing.JLabel lb_Title;
    private javax.swing.JLabel lb_Username;
    private javax.swing.JLabel lb_Welcome;
    private ui.components.textfield.RoundedPasswordField roundedPasswordField1;
    private ui.components.textfield.RoundedTextField roundedTextField1;
    // End of variables declaration//GEN-END:variables
}
