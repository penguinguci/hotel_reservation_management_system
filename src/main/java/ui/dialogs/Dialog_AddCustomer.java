/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JPanel.java to edit this template
 */
package ui.dialogs;

import dao.CustomerDAOImpl;
import entities.Customer;
import interfaces.CustomerDAO;
import jakarta.persistence.EntityTransaction;
import ultilities.GenerateString;
import ultilities.RegexPattern;
import utils.AppUtil;

import javax.swing.*;
import java.awt.*;

/**
 *
 * @author TRAN LONG VU
 */
public class Dialog_AddCustomer extends javax.swing.JPanel {
    private Customer customer;

    public void setCustomer(Customer customer) {
        this.customer = customer;
    }

    public Customer getCustomer() {
        return customer;
    }

    /**
     * Creates new form Dialog_AddCustomer
     */
    public Dialog_AddCustomer() {
        initComponents();
        initComboboxGender();
    }

    private void initComboboxGender() {
        String[] genders = {"Nam", "Nữ"};
        DefaultComboBoxModel<String> model = new DefaultComboBoxModel<>();
        model.addElement("Chọn giới tính");
        for (String gender : genders) {
            model.addElement(gender);
        }
        cbx_Gender.setModel(model);
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        pnl_Left = new javax.swing.JPanel();
        pnl_Left_Label = new javax.swing.JPanel();
        lbl_FisrtName = new javax.swing.JLabel();
        lbl_Gender = new javax.swing.JLabel();
        lbl_Phone = new javax.swing.JLabel();
        lbl_CCCD = new javax.swing.JLabel();
        pnl_Left_Input = new javax.swing.JPanel();
        txt_FirstName = new javax.swing.JTextField();
        cbx_Gender = new ui.components.combobox.StyledComboBox();
        txt_Phone = new javax.swing.JTextField();
        txt_CCCD = new javax.swing.JTextField();
        pnl_Right = new javax.swing.JPanel();
        pnl_Right_Label = new javax.swing.JPanel();
        lbl_LastName = new javax.swing.JLabel();
        lbl_BirthDate = new javax.swing.JLabel();
        lbl_Email = new javax.swing.JLabel();
        lbl_Address = new javax.swing.JLabel();
        pnl_Right_Input = new javax.swing.JPanel();
        txt_LastName = new javax.swing.JTextField();
        calendar_BirthDate = new ui.components.calendar.CustomCalendarDialog();
        txt_Email = new javax.swing.JTextField();
        txt_Address = new javax.swing.JTextField();
        pnl_GroupButton = new javax.swing.JPanel();
        btn_Cancel = new ui.components.button.ButtonCancelCustom();
        btn_Add = new ui.components.button.ButtonCustom();
        btn_Clear = new ui.components.button.ButtonCancelCustom();
        pnl_Title = new javax.swing.JPanel();
        lbl_Title = new javax.swing.JLabel();

        setBackground(new java.awt.Color(255, 255, 255));

        pnl_Left.setBackground(new java.awt.Color(255, 255, 255));

        pnl_Left_Label.setBackground(new java.awt.Color(255, 255, 255));
        pnl_Left_Label.setLayout(new java.awt.GridLayout(4, 1, 0, 20));

        lbl_FisrtName.setFont(new java.awt.Font("Segoe UI", 0, 15)); // NOI18N
        lbl_FisrtName.setText("Họ:");
        pnl_Left_Label.add(lbl_FisrtName);

        lbl_Gender.setFont(new java.awt.Font("Segoe UI", 0, 15)); // NOI18N
        lbl_Gender.setText("Giới tính:");
        pnl_Left_Label.add(lbl_Gender);

        lbl_Phone.setFont(new java.awt.Font("Segoe UI", 0, 15)); // NOI18N
        lbl_Phone.setText("Số điện thoại:");
        pnl_Left_Label.add(lbl_Phone);

        lbl_CCCD.setFont(new java.awt.Font("Segoe UI", 0, 15)); // NOI18N
        lbl_CCCD.setText("CCCD:");
        pnl_Left_Label.add(lbl_CCCD);

        pnl_Left_Input.setBackground(new java.awt.Color(255, 255, 255));
        pnl_Left_Input.setLayout(new java.awt.GridLayout(4, 1, 0, 25));

        txt_FirstName.setFont(new java.awt.Font("Segoe UI", 0, 15)); // NOI18N
        txt_FirstName.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(204, 204, 204)));
        txt_FirstName.setMargin(new java.awt.Insets(2, 20, 2, 6));
        pnl_Left_Input.add(txt_FirstName);
        pnl_Left_Input.add(cbx_Gender);

        txt_Phone.setFont(new java.awt.Font("Segoe UI", 0, 15)); // NOI18N
        txt_Phone.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(204, 204, 204)));
        pnl_Left_Input.add(txt_Phone);

        txt_CCCD.setFont(new java.awt.Font("Segoe UI", 0, 15)); // NOI18N
        txt_CCCD.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(204, 204, 204)));
        pnl_Left_Input.add(txt_CCCD);

        javax.swing.GroupLayout pnl_LeftLayout = new javax.swing.GroupLayout(pnl_Left);
        pnl_Left.setLayout(pnl_LeftLayout);
        pnl_LeftLayout.setHorizontalGroup(
            pnl_LeftLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, pnl_LeftLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(pnl_Left_Label, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(pnl_Left_Input, javax.swing.GroupLayout.PREFERRED_SIZE, 217, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(7, Short.MAX_VALUE))
        );
        pnl_LeftLayout.setVerticalGroup(
            pnl_LeftLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnl_LeftLayout.createSequentialGroup()
                .addGroup(pnl_LeftLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                    .addComponent(pnl_Left_Label, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(pnl_Left_Input, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        pnl_Right.setBackground(new java.awt.Color(255, 255, 255));

        pnl_Right_Label.setBackground(new java.awt.Color(255, 255, 255));
        pnl_Right_Label.setLayout(new java.awt.GridLayout(4, 1, 0, 20));

        lbl_LastName.setFont(new java.awt.Font("Segoe UI", 0, 15)); // NOI18N
        lbl_LastName.setText("Tên:");
        pnl_Right_Label.add(lbl_LastName);

        lbl_BirthDate.setFont(new java.awt.Font("Segoe UI", 0, 15)); // NOI18N
        lbl_BirthDate.setText("Ngày sinh:");
        pnl_Right_Label.add(lbl_BirthDate);

        lbl_Email.setFont(new java.awt.Font("Segoe UI", 0, 15)); // NOI18N
        lbl_Email.setText("Email:");
        pnl_Right_Label.add(lbl_Email);

        lbl_Address.setFont(new java.awt.Font("Segoe UI", 0, 15)); // NOI18N
        lbl_Address.setText("Địa chỉ:");
        pnl_Right_Label.add(lbl_Address);

        pnl_Right_Input.setBackground(new java.awt.Color(255, 255, 255));
        pnl_Right_Input.setLayout(new java.awt.GridLayout(4, 1, 0, 25));

        txt_LastName.setFont(new java.awt.Font("Segoe UI", 0, 15)); // NOI18N
        txt_LastName.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(204, 204, 204)));
        pnl_Right_Input.add(txt_LastName);

        calendar_BirthDate.setFont(new java.awt.Font("Segoe UI", 0, 15)); // NOI18N
        pnl_Right_Input.add(calendar_BirthDate);

        txt_Email.setFont(new java.awt.Font("Segoe UI", 0, 15)); // NOI18N
        txt_Email.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(204, 204, 204)));
        pnl_Right_Input.add(txt_Email);

        txt_Address.setFont(new java.awt.Font("Segoe UI", 0, 15)); // NOI18N
        txt_Address.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(204, 204, 204)));
        pnl_Right_Input.add(txt_Address);

        javax.swing.GroupLayout pnl_RightLayout = new javax.swing.GroupLayout(pnl_Right);
        pnl_Right.setLayout(pnl_RightLayout);
        pnl_RightLayout.setHorizontalGroup(
            pnl_RightLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 415, Short.MAX_VALUE)
            .addGroup(pnl_RightLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(pnl_RightLayout.createSequentialGroup()
                    .addGap(44, 44, 44)
                    .addComponent(pnl_Right_Label, javax.swing.GroupLayout.PREFERRED_SIZE, 88, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addContainerGap(283, Short.MAX_VALUE)))
            .addGroup(pnl_RightLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, pnl_RightLayout.createSequentialGroup()
                    .addContainerGap(141, Short.MAX_VALUE)
                    .addComponent(pnl_Right_Input, javax.swing.GroupLayout.PREFERRED_SIZE, 230, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGap(44, 44, 44)))
        );
        pnl_RightLayout.setVerticalGroup(
            pnl_RightLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 0, Short.MAX_VALUE)
            .addGroup(pnl_RightLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(pnl_RightLayout.createSequentialGroup()
                    .addComponent(pnl_Right_Label, javax.swing.GroupLayout.PREFERRED_SIZE, 214, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGap(0, 7, Short.MAX_VALUE)))
            .addGroup(pnl_RightLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(pnl_RightLayout.createSequentialGroup()
                    .addComponent(pnl_Right_Input, javax.swing.GroupLayout.DEFAULT_SIZE, 215, Short.MAX_VALUE)
                    .addContainerGap()))
        );

        pnl_GroupButton.setBackground(new java.awt.Color(255, 255, 255));
        pnl_GroupButton.setLayout(new java.awt.GridLayout(1, 0, 15, 0));

        btn_Cancel.setText("Hủy");
        btn_Cancel.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_CancelActionPerformed(evt);
            }
        });
        pnl_GroupButton.add(btn_Cancel);

        btn_Add.setText("Thêm");
        btn_Add.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_AddActionPerformed(evt);
            }
        });
        pnl_GroupButton.add(btn_Add);

        btn_Clear.setText("Xóa rỗng");
        btn_Clear.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_ClearActionPerformed(evt);
            }
        });
        pnl_GroupButton.add(btn_Clear);

        pnl_Title.setBackground(new java.awt.Color(255, 255, 255));
        pnl_Title.setLayout(new java.awt.BorderLayout());

        lbl_Title.setFont(new java.awt.Font("Segoe UI", 1, 16)); // NOI18N
        lbl_Title.setForeground(new java.awt.Color(153, 153, 255));
        lbl_Title.setText("Thêm khách hàng mới");
        pnl_Title.add(lbl_Title, java.awt.BorderLayout.CENTER);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(pnl_Title, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(pnl_Left, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(pnl_Right, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, Short.MAX_VALUE))
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(pnl_GroupButton, javax.swing.GroupLayout.PREFERRED_SIZE, 422, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(36, 36, 36))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(pnl_Title, javax.swing.GroupLayout.PREFERRED_SIZE, 46, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(pnl_Right, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(pnl_Left, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 50, Short.MAX_VALUE)
                .addComponent(pnl_GroupButton, javax.swing.GroupLayout.PREFERRED_SIZE, 43, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(16, 16, 16))
        );
    }// </editor-fold>//GEN-END:initComponents

    private void btn_CancelActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_CancelActionPerformed
        cancel();
    }//GEN-LAST:event_btn_CancelActionPerformed

    private void btn_AddActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_AddActionPerformed
        add();
    }//GEN-LAST:event_btn_AddActionPerformed

    private void btn_ClearActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_ClearActionPerformed
        clear();
    }//GEN-LAST:event_btn_ClearActionPerformed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private ui.components.button.ButtonCustom btn_Add;
    private ui.components.button.ButtonCancelCustom btn_Cancel;
    private ui.components.button.ButtonCancelCustom btn_Clear;
    private ui.components.calendar.CustomCalendarDialog calendar_BirthDate;
    private ui.components.combobox.StyledComboBox cbx_Gender;
    private javax.swing.JLabel lbl_Address;
    private javax.swing.JLabel lbl_BirthDate;
    private javax.swing.JLabel lbl_CCCD;
    private javax.swing.JLabel lbl_Email;
    private javax.swing.JLabel lbl_FisrtName;
    private javax.swing.JLabel lbl_Gender;
    private javax.swing.JLabel lbl_LastName;
    private javax.swing.JLabel lbl_Phone;
    private javax.swing.JLabel lbl_Title;
    private javax.swing.JPanel pnl_GroupButton;
    private javax.swing.JPanel pnl_Left;
    private javax.swing.JPanel pnl_Left_Input;
    private javax.swing.JPanel pnl_Left_Label;
    private javax.swing.JPanel pnl_Right;
    private javax.swing.JPanel pnl_Right_Input;
    private javax.swing.JPanel pnl_Right_Label;
    private javax.swing.JPanel pnl_Title;
    private javax.swing.JTextField txt_Address;
    private javax.swing.JTextField txt_CCCD;
    private javax.swing.JTextField txt_Email;
    private javax.swing.JTextField txt_FirstName;
    private javax.swing.JTextField txt_LastName;
    private javax.swing.JTextField txt_Phone;
    // End of variables declaration//GEN-END:variables


    /**
     * Kiểm tra tính hợp lệ của các trường nhập liệu
     * @return true nếu tất cả các trường hợp lệ, false nếu có trường không hợp lệ
     */
    private boolean validateInput() {
        if (txt_FirstName.getText().trim().isEmpty() || txt_LastName.getText().trim().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Họ và tên không được để trống!", "Lỗi", JOptionPane.ERROR_MESSAGE);
            txt_FirstName.requestFocus();
            return false;
        }

        String phone = txt_Phone.getText().trim();
        if (!phone.matches(RegexPattern.PHONE_VN)) {
            JOptionPane.showMessageDialog(this, "Số điện thoại không hợp lệ!", "Lỗi", JOptionPane.ERROR_MESSAGE);
            txt_Phone.requestFocus();
            return false;
        } else if (phone.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Số điện thoại không được để trống!", "Lỗi", JOptionPane.ERROR_MESSAGE);
            txt_Phone.requestFocus();
            return false;
        }

        String email = txt_Email.getText().trim();
        if (!email.isEmpty() && !email.matches(RegexPattern.EMAIL)) {
            JOptionPane.showMessageDialog(this, "Email không hợp lệ!", "Lỗi", JOptionPane.ERROR_MESSAGE);
            txt_Email.requestFocus();
            return false;
        }

        String gender = cbx_Gender.getSelectedItem().toString();
        if (gender.equals("Chọn giới tính")) {
            JOptionPane.showMessageDialog(this, "Vui lòng chọn giới tính", "Lỗi", JOptionPane.ERROR_MESSAGE);
            cbx_Gender.requestFocus();
            return false;
        }


        if (calendar_BirthDate.getSelectedDate() == null) {
            JOptionPane.showMessageDialog(this, "Vui lòng chọn ngày sinh!", "Lỗi", JOptionPane.ERROR_MESSAGE);
            calendar_BirthDate.requestFocus();
            return false;
        }

        if (txt_CCCD.getText().trim().isEmpty()) {
            JOptionPane.showMessageDialog(this, "CCCD không được để trống!", "Lỗi", JOptionPane.ERROR_MESSAGE);
            txt_CCCD.requestFocus();
            return false;
        } else if (!txt_CCCD.getText().matches(RegexPattern.CCCD)) {
            JOptionPane.showMessageDialog(this, "CCCD không hợp lệ!", "Lỗi", JOptionPane.ERROR_MESSAGE);
            txt_CCCD.requestFocus();
            return false;
        }

        return true;
    }

    /**
     * Thêm khách hàng mới vào cơ sở dữ liệu
     * @throws Exception nếu có lỗi xảy ra trong quá trình thêm
     *
     */
    private void add() {
        if (!validateInput()) {
            return;
        }

        EntityTransaction transaction = null;
        try {
            CustomerDAO customerDAO = new CustomerDAOImpl();

            String phone = txt_Phone.getText().trim();
            if (customerDAO.isPhoneExists(phone)) {
                JOptionPane.showMessageDialog(this, "Số điện thoại đã tồn tại!", "Lỗi", JOptionPane.ERROR_MESSAGE);
                return;
            }

            String email = txt_Email.getText().trim();
            if (customerDAO.isEmailExists(email)) {
                JOptionPane.showMessageDialog(this, "Email đã tồn tại!", "Lỗi", JOptionPane.ERROR_MESSAGE);
                return;
            }

            Customer newCustomer = new Customer();
            newCustomer.setCustomerId(GenerateString.generateCustomerID());
            newCustomer.setFirstName(txt_FirstName.getText().trim());
            newCustomer.setLastName(txt_LastName.getText().trim());

            String gender = cbx_Gender.getSelectedItem().toString();
            newCustomer.setGender(gender.equals("Nam"));
            newCustomer.setDateOfBirth(calendar_BirthDate.getSelectedDate());
            newCustomer.setPhoneNumber(phone);
            newCustomer.setEmail(email);
            newCustomer.setCCCD(txt_CCCD.getText().trim());
            newCustomer.setAddress(txt_Address.getText().trim());
            newCustomer.setBonusPoint(0);

            // Xử lý transaction
            transaction = AppUtil.getEntityManager().getTransaction();
            transaction.begin();
            if (customerDAO.create(newCustomer)) {
                transaction.commit();
                JOptionPane.showMessageDialog(this, "Thêm khách hàng thành công!", "Thành công", JOptionPane.INFORMATION_MESSAGE);
                setCustomer(newCustomer);
                clear();
                Window window = SwingUtilities.getWindowAncestor(this);
                window.dispose();
            } else {
                transaction.rollback();
                JOptionPane.showMessageDialog(this, "Thêm khách hàng thất bại!", "Lỗi", JOptionPane.ERROR_MESSAGE);
            }
        } catch (Exception e) {
            if (transaction != null && transaction.isActive()) {
                transaction.rollback();
            }
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Lỗi khi thêm khách hàng!", "Lỗi", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void clear() {
        txt_FirstName.setText("");
        txt_LastName.setText("");
        cbx_Gender.setSelectedIndex(0);
        calendar_BirthDate.setSelectedDate(null);
        txt_Phone.setText("");
        txt_CCCD.setText("");
        txt_Email.setText("");
        txt_Address.setText("");
    }

    private void cancel() {
        Window window = SwingUtilities.getWindowAncestor(this);
        window.dispose();
    }
}
