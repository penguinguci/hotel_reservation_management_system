/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JPanel.java to edit this template
 */
package ui.tabs;

import dao.CustomerDAOImpl;
import entities.Customer;
import interfaces.CustomerDAO;
import ui.components.popup.PopupSearch;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import java.awt.*;
import java.awt.event.*;
import java.rmi.RemoteException;
import java.util.List;
import java.util.stream.Collectors;

/**
 *
 * @author TRAN LONG VU
 */
public class Tab_Booking extends javax.swing.JPanel {
    private Timer debounceTimer;
    private static final int DEBOUNCE_DELAY = 300;

    /**
     * Creates new form Tab_Booking
     */
    public Tab_Booking() {
        customerDAO = new CustomerDAOImpl();
        initComponents();
        initializeSearchCustomer();

        setFocusTraversalPolicy(new FocusTraversalPolicy() {
            @Override
            public Component getComponentAfter(Container aContainer, Component aComponent) {
                return txt_SearchCustomer.getTextField();
            }

            @Override
            public Component getComponentBefore(Container aContainer, Component aComponent) {
                return txt_SearchCustomer.getTextField();
            }

            @Override
            public Component getFirstComponent(Container aContainer) {
                return txt_SearchCustomer.getTextField();
            }

            @Override
            public Component getLastComponent(Container aContainer) {
                return txt_SearchCustomer.getTextField();
            }

            @Override
            public Component getDefaultComponent(Container aContainer) {
                return txt_SearchCustomer.getTextField();
            }
        });
        setFocusCycleRoot(true);
    }

    private void initializeSearchCustomer() {
        popupSearchCustomer = new PopupSearch();
        popupSearchCustomer.setSuggestionBackground(new Color(240, 240, 240));
        popupSearchCustomer.setSuggestionForeground(Color.BLACK);

        txt_SearchCustomer.getTextField().addFocusListener(new FocusAdapter() {
            @Override
            public void focusLost(FocusEvent e) {
                if (e.getOppositeComponent() != null && !e.getOppositeComponent().equals(txt_SearchCustomer.getTextField())) {
                    ensureFocus();
                }
            }
        });

        // Thêm DocumentListener với debounce
        txt_SearchCustomer.getTextField().getDocument().addDocumentListener(new DocumentListener() {
            @Override
            public void insertUpdate(DocumentEvent e) {
                scheduleUpdateSuggestions();
            }

            @Override
            public void removeUpdate(DocumentEvent e) {
                scheduleUpdateSuggestions();
            }

            @Override
            public void changedUpdate(DocumentEvent e) {
                scheduleUpdateSuggestions();
            }

            private void scheduleUpdateSuggestions() {
                if (debounceTimer != null && debounceTimer.isRunning()) {
                    debounceTimer.stop();
                }

                debounceTimer = new Timer(DEBOUNCE_DELAY, e -> updateSuggestions());
                debounceTimer.setRepeats(false);
                debounceTimer.start();
            }

            private void updateSuggestions() {
                String input = txt_SearchCustomer.getText().trim();
                if (!input.isEmpty()) {
                    try {
                        List<Customer> matchingCustomers = customerDAO.searchCustomersByPhoneOrCCCD(input);
                        List<String> suggestions = matchingCustomers.stream()
                                .map(c -> String.format("%s %s - %s - %s",
                                        c.getFirstName(), c.getLastName(),
                                        c.getPhoneNumber(), c.getCCCD() != null ? c.getCCCD() : "N/A"))
                                .collect(Collectors.toList());
                        popupSearchCustomer.setSuggestions(suggestions);
                        if (!suggestions.isEmpty()) {
                            popupSearchCustomer.showPopup(txt_SearchCustomer);
                        } else {
                            popupSearchCustomer.hidePopup();
                            ensureFocus();
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                        popupSearchCustomer.hidePopup();
                        ensureFocus();
                    }
                } else {
                    popupSearchCustomer.hidePopup();
                    ensureFocus();
                }
            }
        });

        txt_SearchCustomer.getTextField().addKeyListener(new KeyAdapter() {
            private int selectedIndex = -1;

            @Override
            public void keyPressed(KeyEvent e) {
                List<String> suggestions = popupSearchCustomer.getSuggestions();
                if (suggestions.isEmpty()) {
                    selectedIndex = -1;
                    popupSearchCustomer.setSelectedIndex(-1);
                    return;
                }

                switch (e.getKeyCode()) {
                    case KeyEvent.VK_DOWN:
                        if (selectedIndex < suggestions.size() - 1) {
                            selectedIndex++;
                            popupSearchCustomer.setSelectedIndex(selectedIndex);
                            System.out.println("Selected suggestion index: " + selectedIndex);
                        }
                        e.consume();
                        break;
                    case KeyEvent.VK_UP:
                        if (selectedIndex > 0) {
                            selectedIndex--;
                            popupSearchCustomer.setSelectedIndex(selectedIndex);
                            System.out.println("Selected suggestion index: " + selectedIndex);
                        }
                        e.consume();
                        break;
                    case KeyEvent.VK_ENTER:
                        if (selectedIndex >= 0 && selectedIndex < suggestions.size()) {
                            popupSearchCustomer.selectSuggestion(suggestions.get(selectedIndex));
                        }
                        e.consume();
                        break;
                    case KeyEvent.VK_ESCAPE:
                        popupSearchCustomer.hidePopup();
                        selectedIndex = -1;
                        popupSearchCustomer.setSelectedIndex(-1);
                        ensureFocus();
                        e.consume();
                        break;
                }
            }
        });

        popupSearchCustomer.setSuggestionListener(suggestion -> {
            System.out.println("Selected suggestion: " + suggestion);
            List<Customer> customers = customerDAO.searchCustomersByPhoneOrCCCD(txt_SearchCustomer.getText().trim());
            Customer selectedCustomer = customers.stream()
                    .filter(c -> {
                        String suggestionText = String.format("%s %s - %s - %s",
                                c.getFirstName(), c.getLastName(),
                                c.getPhoneNumber(), c.getCCCD() != null ? c.getCCCD() : "N/A");
                        return suggestionText.equals(suggestion);
                    })
                    .findFirst()
                    .orElse(null);

            if (selectedCustomer != null) {
                lbl_CustomerName_Value.setText(selectedCustomer.getFirstName() + " " + selectedCustomer.getLastName());
                lbl_CustomerGender_Value.setText(selectedCustomer.isGender() ? "Nam" : "Nữ");
                lbl_CustomerPhone_Value.setText(selectedCustomer.getPhoneNumber());
                lbl_CustomerCCCD_Value.setText(selectedCustomer.getCCCD() != null ? selectedCustomer.getCCCD() : "N/A");
                txt_SearchCustomer.setText("");
                popupSearchCustomer.hidePopup();
                ensureFocus();
            }
        });

        txt_SearchCustomer.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent evt) {
                if (!txt_SearchCustomer.getText().isEmpty()) {
                    txt_SearchCustomer.setText("");
                    lbl_CustomerName_Value.setText("");
                    lbl_CustomerGender_Value.setText("");
                    lbl_CustomerPhone_Value.setText("");
                    lbl_CustomerCCCD_Value.setText("");
                    popupSearchCustomer.hidePopup();
                    ensureFocus();
                }
            }
        });
    }

    private void ensureFocus() {
        JTextField textField = txt_SearchCustomer.getTextField();
        if (!textField.hasFocus()) {
            textField.requestFocusInWindow();

            Timer focusTimer = new Timer(50, new ActionListener() {
                int retries = 0;
                final int maxRetries = 5;

                @Override
                public void actionPerformed(ActionEvent e) {
                    if (textField.hasFocus() || retries >= maxRetries) {
                        ((Timer) e.getSource()).stop();
                        return;
                    }
                    textField.requestFocusInWindow();
                    retries++;
                }
            });
            focusTimer.setRepeats(true);
            focusTimer.start();
        }
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        pnl_EmptyRoom = new javax.swing.JPanel();
        pnl_InforBookingRoom = new javax.swing.JPanel();
        btn_SearchRoom = new ui.components.button.ButtonCustom();
        pnl_Left_InforBookRoom = new javax.swing.JPanel();
        pnl_Label_Left_InforBookRoom = new javax.swing.JPanel();
        lbl_Checkin = new javax.swing.JLabel();
        lbl_Checkout = new javax.swing.JLabel();
        lbl_RangePrice = new javax.swing.JLabel();
        pnl_Input_Left_InforBookRoom = new javax.swing.JPanel();
        calendar_Checkin = new ui.components.calendar.CustomCalendar();
        calendar_Checout = new ui.components.calendar.CustomCalendar();
        cbx_RangePrice = new ui.components.combobox.StyledComboBox();
        pnl_Right_InforBookRoom = new javax.swing.JPanel();
        pnl_Label_Right_InforBookRoom = new javax.swing.JPanel();
        lbl_Capacity = new javax.swing.JLabel();
        lbl_TypeRoom = new javax.swing.JLabel();
        pnl_Input_Right_InforBookRoom = new javax.swing.JPanel();
        spinner_Capacity = new ui.components.spinner.CustomSpinner();
        cbx_TypeRoom = new ui.components.combobox.StyledComboBox();
        btn_Clear = new ui.components.button.ButtonCancelCustom();
        table_EntityRoom = new ui.components.table.CustomTableButton();
        btn_AddRoom = new ui.components.button.ButtonCustom();
        btn_SeeDetails = new ui.components.button.ButtonCustom();
        pnl_InforCustomer = new javax.swing.JPanel();
        txt_SearchCustomer = new ui.components.textfield.SearchTextField();
        pnl_CustomerLeft = new javax.swing.JPanel();
        lbl_CustomerName = new javax.swing.JLabel();
        lbl_CustomerGender = new javax.swing.JLabel();
        lbl_CustomerPhone = new javax.swing.JLabel();
        lbl_CustomerCCCD = new javax.swing.JLabel();
        pnl_CustomerRight = new javax.swing.JPanel();
        lbl_CustomerName_Value = new javax.swing.JLabel();
        lbl_CustomerGender_Value = new javax.swing.JLabel();
        lbl_CustomerPhone_Value = new javax.swing.JLabel();
        lbl_CustomerCCCD_Value = new javax.swing.JLabel();
        btn_AddCustomer = new ui.components.button.ButtonCustom();
        pnl_PriceNumber = new javax.swing.JPanel();
        pnl_PriceNumber_Left = new javax.swing.JPanel();
        lbl_TotalPrice = new javax.swing.JLabel();
        lbl_Discount = new javax.swing.JLabel();
        lbl_Tax = new javax.swing.JLabel();
        lbl_LastTotalPrice = new javax.swing.JLabel();
        pnl_PriceNumber_Right = new javax.swing.JPanel();
        lbl_TotolPrice_Value = new javax.swing.JLabel();
        lbl_Discount_Value = new javax.swing.JLabel();
        lbl_Tax_Value = new javax.swing.JLabel();
        lbl_LastTotalPrice_Value = new javax.swing.JLabel();
        pnl_ButtonActions = new javax.swing.JPanel();
        btn_Booking = new ui.components.button.ButtonCustom();
        btn_Cancel = new ui.components.button.ButtonCancelCustom();
        btn_KeyboardNumber = new ui.components.button.ButtonCustom();
        pnl_Cart = new javax.swing.JPanel();
        lbl_CartTittle = new javax.swing.JLabel();
        table_Cart = new ui.components.table.CustomTableButton();
        popupSearchCustomer = new ui.components.popup.PopupSearch();

        setBackground(new java.awt.Color(255, 255, 255));
        setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        pnl_EmptyRoom.setBackground(new java.awt.Color(255, 255, 255));

        pnl_InforBookingRoom.setBackground(new java.awt.Color(255, 255, 255));
        pnl_InforBookingRoom.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Thông tin đặt phòng", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Segoe UI", 1, 15), new java.awt.Color(136, 130, 246))); // NOI18N

        btn_SearchRoom.setText("Tìm kiếm");
        btn_SearchRoom.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_SearchRoomActionPerformed(evt);
            }
        });

        pnl_Left_InforBookRoom.setBackground(new java.awt.Color(255, 255, 255));

        pnl_Label_Left_InforBookRoom.setBackground(new java.awt.Color(255, 255, 255));
        pnl_Label_Left_InforBookRoom.setLayout(new java.awt.GridLayout(3, 1));

        lbl_Checkin.setFont(new java.awt.Font("Segoe UI", 0, 15)); // NOI18N
        lbl_Checkin.setText("Ngày checkin:");
        pnl_Label_Left_InforBookRoom.add(lbl_Checkin);

        lbl_Checkout.setFont(new java.awt.Font("Segoe UI", 0, 15)); // NOI18N
        lbl_Checkout.setText("Ngày checkout:");
        pnl_Label_Left_InforBookRoom.add(lbl_Checkout);

        lbl_RangePrice.setFont(new java.awt.Font("Segoe UI", 0, 15)); // NOI18N
        lbl_RangePrice.setText("Khoảng giá:");
        pnl_Label_Left_InforBookRoom.add(lbl_RangePrice);

        pnl_Input_Left_InforBookRoom.setBackground(new java.awt.Color(255, 255, 255));
        pnl_Input_Left_InforBookRoom.setLayout(new java.awt.GridLayout(3, 1, 0, 10));
        pnl_Input_Left_InforBookRoom.add(calendar_Checkin);
        pnl_Input_Left_InforBookRoom.add(calendar_Checout);
        pnl_Input_Left_InforBookRoom.add(cbx_RangePrice);

        javax.swing.GroupLayout pnl_Left_InforBookRoomLayout = new javax.swing.GroupLayout(pnl_Left_InforBookRoom);
        pnl_Left_InforBookRoom.setLayout(pnl_Left_InforBookRoomLayout);
        pnl_Left_InforBookRoomLayout.setHorizontalGroup(
            pnl_Left_InforBookRoomLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnl_Left_InforBookRoomLayout.createSequentialGroup()
                .addComponent(pnl_Label_Left_InforBookRoom, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(pnl_Input_Left_InforBookRoom, javax.swing.GroupLayout.DEFAULT_SIZE, 227, Short.MAX_VALUE))
        );
        pnl_Left_InforBookRoomLayout.setVerticalGroup(
            pnl_Left_InforBookRoomLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(pnl_Label_Left_InforBookRoom, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(pnl_Input_Left_InforBookRoom, javax.swing.GroupLayout.DEFAULT_SIZE, 142, Short.MAX_VALUE)
        );

        pnl_Right_InforBookRoom.setBackground(new java.awt.Color(255, 255, 255));

        pnl_Label_Right_InforBookRoom.setBackground(new java.awt.Color(255, 255, 255));
        pnl_Label_Right_InforBookRoom.setLayout(new java.awt.GridLayout(3, 1));

        lbl_Capacity.setFont(new java.awt.Font("Segoe UI", 0, 15)); // NOI18N
        lbl_Capacity.setText("Sức chứa:");
        pnl_Label_Right_InforBookRoom.add(lbl_Capacity);

        lbl_TypeRoom.setFont(new java.awt.Font("Segoe UI", 0, 15)); // NOI18N
        lbl_TypeRoom.setText("Loại phòng:");
        pnl_Label_Right_InforBookRoom.add(lbl_TypeRoom);

        pnl_Input_Right_InforBookRoom.setBackground(new java.awt.Color(255, 255, 255));
        pnl_Input_Right_InforBookRoom.setLayout(new java.awt.GridLayout(3, 1, 0, 10));
        pnl_Input_Right_InforBookRoom.add(spinner_Capacity);
        pnl_Input_Right_InforBookRoom.add(cbx_TypeRoom);

        javax.swing.GroupLayout pnl_Right_InforBookRoomLayout = new javax.swing.GroupLayout(pnl_Right_InforBookRoom);
        pnl_Right_InforBookRoom.setLayout(pnl_Right_InforBookRoomLayout);
        pnl_Right_InforBookRoomLayout.setHorizontalGroup(
            pnl_Right_InforBookRoomLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 312, Short.MAX_VALUE)
            .addGroup(pnl_Right_InforBookRoomLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(pnl_Right_InforBookRoomLayout.createSequentialGroup()
                    .addComponent(pnl_Label_Right_InforBookRoom, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGap(0, 233, Short.MAX_VALUE)))
            .addGroup(pnl_Right_InforBookRoomLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, pnl_Right_InforBookRoomLayout.createSequentialGroup()
                    .addContainerGap(103, Short.MAX_VALUE)
                    .addComponent(pnl_Input_Right_InforBookRoom, javax.swing.GroupLayout.PREFERRED_SIZE, 203, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addContainerGap()))
        );
        pnl_Right_InforBookRoomLayout.setVerticalGroup(
            pnl_Right_InforBookRoomLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 142, Short.MAX_VALUE)
            .addGroup(pnl_Right_InforBookRoomLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addComponent(pnl_Label_Right_InforBookRoom, javax.swing.GroupLayout.DEFAULT_SIZE, 142, Short.MAX_VALUE))
            .addGroup(pnl_Right_InforBookRoomLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(pnl_Right_InforBookRoomLayout.createSequentialGroup()
                    .addComponent(pnl_Input_Right_InforBookRoom, javax.swing.GroupLayout.PREFERRED_SIZE, 142, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGap(0, 0, Short.MAX_VALUE)))
        );

        btn_Clear.setText("Xóa trắng");

        javax.swing.GroupLayout pnl_InforBookingRoomLayout = new javax.swing.GroupLayout(pnl_InforBookingRoom);
        pnl_InforBookingRoom.setLayout(pnl_InforBookingRoomLayout);
        pnl_InforBookingRoomLayout.setHorizontalGroup(
            pnl_InforBookingRoomLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, pnl_InforBookingRoomLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(pnl_Left_InforBookRoom, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(pnl_Right_InforBookRoom, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addGroup(pnl_InforBookingRoomLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(btn_SearchRoom, javax.swing.GroupLayout.DEFAULT_SIZE, 114, Short.MAX_VALUE)
                    .addComponent(btn_Clear, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );
        pnl_InforBookingRoomLayout.setVerticalGroup(
            pnl_InforBookingRoomLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(pnl_Right_InforBookRoom, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
            .addGroup(pnl_InforBookingRoomLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                .addGroup(pnl_InforBookingRoomLayout.createSequentialGroup()
                    .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(btn_SearchRoom, javax.swing.GroupLayout.PREFERRED_SIZE, 39, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                    .addComponent(btn_Clear, javax.swing.GroupLayout.PREFERRED_SIZE, 41, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGroup(pnl_InforBookingRoomLayout.createSequentialGroup()
                    .addGap(0, 0, Short.MAX_VALUE)
                    .addComponent(pnl_Left_InforBookRoom, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
        );

        table_EntityRoom.setColumnNames(new String[] {"STT", "Số phòng", "Loại phòng", "Giá/đêm", "Tiện nghi", "Trạng thái"});
        table_EntityRoom.setHeaderBackgroundColor(new java.awt.Color(153, 153, 255));

        btn_AddRoom.setText("Thêm");
        btn_AddRoom.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_AddRoomActionPerformed(evt);
            }
        });

        btn_SeeDetails.setText("Xem chi tiết");

        javax.swing.GroupLayout pnl_EmptyRoomLayout = new javax.swing.GroupLayout(pnl_EmptyRoom);
        pnl_EmptyRoom.setLayout(pnl_EmptyRoomLayout);
        pnl_EmptyRoomLayout.setHorizontalGroup(
            pnl_EmptyRoomLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
            .addComponent(pnl_InforBookingRoom, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(table_EntityRoom, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addGroup(javax.swing.GroupLayout.Alignment.LEADING, pnl_EmptyRoomLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(btn_AddRoom, javax.swing.GroupLayout.PREFERRED_SIZE, 117, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(btn_SeeDetails, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 566, Short.MAX_VALUE))
        );
        pnl_EmptyRoomLayout.setVerticalGroup(
            pnl_EmptyRoomLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, pnl_EmptyRoomLayout.createSequentialGroup()
                .addComponent(pnl_InforBookingRoom, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(table_EntityRoom, javax.swing.GroupLayout.PREFERRED_SIZE, 197, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(pnl_EmptyRoomLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(btn_SeeDetails, javax.swing.GroupLayout.DEFAULT_SIZE, 39, Short.MAX_VALUE)
                    .addComponent(btn_AddRoom, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap(22, Short.MAX_VALUE))
        );

        add(pnl_EmptyRoom, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 820, 440));

        pnl_InforCustomer.setBackground(new java.awt.Color(255, 255, 255));
        pnl_InforCustomer.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Thông tin khách hàng", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Segoe UI", 1, 15), new java.awt.Color(136, 130, 246))); // NOI18N
        pnl_InforCustomer.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        txt_SearchCustomer.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        txt_SearchCustomer.setPlaceholder("Nhập SĐT hoặc CCCD tìm kiếm...");
        txt_SearchCustomer.setPlaceholderColor(new java.awt.Color(102, 102, 102));
        pnl_InforCustomer.add(txt_SearchCustomer, new org.netbeans.lib.awtextra.AbsoluteConstraints(12, 25, 460, 40));

        pnl_CustomerLeft.setBackground(new java.awt.Color(255, 255, 255));
        pnl_CustomerLeft.setLayout(new java.awt.GridLayout(4, 0, 0, 5));

        lbl_CustomerName.setFont(new java.awt.Font("Segoe UI", 0, 15)); // NOI18N
        lbl_CustomerName.setText("Tên khách hàng:");
        pnl_CustomerLeft.add(lbl_CustomerName);

        lbl_CustomerGender.setFont(new java.awt.Font("Segoe UI", 0, 15)); // NOI18N
        lbl_CustomerGender.setText("Giới tính:");
        lbl_CustomerGender.setPreferredSize(new java.awt.Dimension(50, 16));
        pnl_CustomerLeft.add(lbl_CustomerGender);

        lbl_CustomerPhone.setFont(new java.awt.Font("Segoe UI", 0, 15)); // NOI18N
        lbl_CustomerPhone.setText("Số điện thoại:");
        pnl_CustomerLeft.add(lbl_CustomerPhone);

        lbl_CustomerCCCD.setFont(new java.awt.Font("Segoe UI", 0, 15)); // NOI18N
        lbl_CustomerCCCD.setText("CCCD:");
        pnl_CustomerLeft.add(lbl_CustomerCCCD);

        pnl_InforCustomer.add(pnl_CustomerLeft, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 70, 110, 170));

        pnl_CustomerRight.setBackground(new java.awt.Color(255, 255, 255));
        pnl_CustomerRight.setLayout(new java.awt.GridLayout(4, 0, 0, 5));

        lbl_CustomerName_Value.setFont(new java.awt.Font("Segoe UI", 0, 15)); // NOI18N
        lbl_CustomerName_Value.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(204, 204, 204)));
        pnl_CustomerRight.add(lbl_CustomerName_Value);

        lbl_CustomerGender_Value.setFont(new java.awt.Font("Segoe UI", 0, 15)); // NOI18N
        lbl_CustomerGender_Value.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(204, 204, 204)));
        lbl_CustomerGender_Value.setPreferredSize(new java.awt.Dimension(50, 16));
        pnl_CustomerRight.add(lbl_CustomerGender_Value);

        lbl_CustomerPhone_Value.setFont(new java.awt.Font("Segoe UI", 0, 15)); // NOI18N
        lbl_CustomerPhone_Value.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(204, 204, 204)));
        pnl_CustomerRight.add(lbl_CustomerPhone_Value);

        lbl_CustomerCCCD_Value.setFont(new java.awt.Font("Segoe UI", 0, 15)); // NOI18N
        lbl_CustomerCCCD_Value.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(204, 204, 204)));
        pnl_CustomerRight.add(lbl_CustomerCCCD_Value);

        pnl_InforCustomer.add(pnl_CustomerRight, new org.netbeans.lib.awtextra.AbsoluteConstraints(140, 70, 320, 170));

        btn_AddCustomer.setText("Thêm khách hàng");
        btn_AddCustomer.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_AddCustomerActionPerformed(evt);
            }
        });
        pnl_InforCustomer.add(btn_AddCustomer, new org.netbeans.lib.awtextra.AbsoluteConstraints(300, 250, -1, 40));

        add(pnl_InforCustomer, new org.netbeans.lib.awtextra.AbsoluteConstraints(820, 0, 480, 300));

        pnl_PriceNumber.setBackground(new java.awt.Color(255, 255, 255));
        pnl_PriceNumber.setPreferredSize(new java.awt.Dimension(815, 230));

        pnl_PriceNumber_Left.setBackground(new java.awt.Color(255, 255, 255));
        pnl_PriceNumber_Left.setLayout(new java.awt.GridLayout(4, 1));

        lbl_TotalPrice.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        lbl_TotalPrice.setText("Tổng tiền:");
        pnl_PriceNumber_Left.add(lbl_TotalPrice);

        lbl_Discount.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        lbl_Discount.setText("Chiết khấu:");
        pnl_PriceNumber_Left.add(lbl_Discount);

        lbl_Tax.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        lbl_Tax.setText("Thuế:");
        pnl_PriceNumber_Left.add(lbl_Tax);

        lbl_LastTotalPrice.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        lbl_LastTotalPrice.setText("Thành tiền:");
        pnl_PriceNumber_Left.add(lbl_LastTotalPrice);

        pnl_PriceNumber_Right.setBackground(new java.awt.Color(255, 255, 255));
        pnl_PriceNumber_Right.setLayout(new java.awt.GridLayout(4, 1, 0, 10));

        lbl_TotolPrice_Value.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        lbl_TotolPrice_Value.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(204, 204, 204)));
        pnl_PriceNumber_Right.add(lbl_TotolPrice_Value);

        lbl_Discount_Value.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        lbl_Discount_Value.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(204, 204, 204)));
        pnl_PriceNumber_Right.add(lbl_Discount_Value);

        lbl_Tax_Value.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        lbl_Tax_Value.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(204, 204, 204)));
        pnl_PriceNumber_Right.add(lbl_Tax_Value);

        lbl_LastTotalPrice_Value.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        lbl_LastTotalPrice_Value.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(204, 204, 204)));
        pnl_PriceNumber_Right.add(lbl_LastTotalPrice_Value);

        javax.swing.GroupLayout pnl_PriceNumberLayout = new javax.swing.GroupLayout(pnl_PriceNumber);
        pnl_PriceNumber.setLayout(pnl_PriceNumberLayout);
        pnl_PriceNumberLayout.setHorizontalGroup(
            pnl_PriceNumberLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnl_PriceNumberLayout.createSequentialGroup()
                .addContainerGap(16, Short.MAX_VALUE)
                .addComponent(pnl_PriceNumber_Left, javax.swing.GroupLayout.PREFERRED_SIZE, 112, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(pnl_PriceNumber_Right, javax.swing.GroupLayout.PREFERRED_SIZE, 340, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );
        pnl_PriceNumberLayout.setVerticalGroup(
            pnl_PriceNumberLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, pnl_PriceNumberLayout.createSequentialGroup()
                .addContainerGap(16, Short.MAX_VALUE)
                .addGroup(pnl_PriceNumberLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                    .addComponent(pnl_PriceNumber_Left, javax.swing.GroupLayout.DEFAULT_SIZE, 224, Short.MAX_VALUE)
                    .addComponent(pnl_PriceNumber_Right, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addGap(30, 30, 30))
        );

        add(pnl_PriceNumber, new org.netbeans.lib.awtextra.AbsoluteConstraints(820, 300, 480, 270));

        pnl_ButtonActions.setBackground(new java.awt.Color(255, 255, 255));

        btn_Booking.setText("Đặt phòng");

        btn_Cancel.setText("Hủy");
        btn_Cancel.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_CancelActionPerformed(evt);
            }
        });

        btn_KeyboardNumber.setText("Bàn phím số");

        javax.swing.GroupLayout pnl_ButtonActionsLayout = new javax.swing.GroupLayout(pnl_ButtonActions);
        pnl_ButtonActions.setLayout(pnl_ButtonActionsLayout);
        pnl_ButtonActionsLayout.setHorizontalGroup(
            pnl_ButtonActionsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnl_ButtonActionsLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(pnl_ButtonActionsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(btn_Booking, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(pnl_ButtonActionsLayout.createSequentialGroup()
                        .addComponent(btn_Cancel, javax.swing.GroupLayout.PREFERRED_SIZE, 227, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(btn_KeyboardNumber, javax.swing.GroupLayout.DEFAULT_SIZE, 235, Short.MAX_VALUE))))
        );
        pnl_ButtonActionsLayout.setVerticalGroup(
            pnl_ButtonActionsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnl_ButtonActionsLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(btn_Booking, javax.swing.GroupLayout.PREFERRED_SIZE, 49, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(pnl_ButtonActionsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btn_Cancel, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btn_KeyboardNumber, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(0, 13, Short.MAX_VALUE))
        );

        add(pnl_ButtonActions, new org.netbeans.lib.awtextra.AbsoluteConstraints(820, 570, 480, 130));

        pnl_Cart.setBackground(new java.awt.Color(255, 255, 255));
        pnl_Cart.setLayout(new java.awt.BorderLayout());

        lbl_CartTittle.setBackground(new java.awt.Color(255, 255, 255));
        lbl_CartTittle.setFont(new java.awt.Font("Segoe UI", 1, 16)); // NOI18N
        lbl_CartTittle.setForeground(new java.awt.Color(153, 153, 255));
        lbl_CartTittle.setText("Hàng chờ");
        pnl_Cart.add(lbl_CartTittle, java.awt.BorderLayout.PAGE_START);

        table_Cart.setColumnNames(new String[] {"STT", "Số phòng", "Giá/đêm", "Số đêm", "Dịch vụ", "Thành tiền"});
        table_Cart.setHeaderBackgroundColor(new java.awt.Color(153, 153, 255));
        pnl_Cart.add(table_Cart, java.awt.BorderLayout.CENTER);

        add(pnl_Cart, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 440, 820, 260));
    }// </editor-fold>//GEN-END:initComponents

    private void btn_CancelActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_CancelActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_btn_CancelActionPerformed

    private void btn_SearchRoomActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_SearchRoomActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_btn_SearchRoomActionPerformed

    private void btn_AddRoomActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_AddRoomActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_btn_AddRoomActionPerformed

    private void btn_AddCustomerActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_AddCustomerActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_btn_AddCustomerActionPerformed

    private void txt_SearchCustomerMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_txt_SearchCustomerMouseClicked
        // TODO add your handling code here:
    }//GEN-LAST:event_txt_SearchCustomerMouseClicked


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private ui.components.button.ButtonCustom btn_AddCustomer;
    private ui.components.button.ButtonCustom btn_AddRoom;
    private ui.components.button.ButtonCustom btn_Booking;
    private ui.components.button.ButtonCancelCustom btn_Cancel;
    private ui.components.button.ButtonCancelCustom btn_Clear;
    private ui.components.button.ButtonCustom btn_KeyboardNumber;
    private ui.components.button.ButtonCustom btn_SearchRoom;
    private ui.components.button.ButtonCustom btn_SeeDetails;
    private ui.components.calendar.CustomCalendar calendar_Checkin;
    private ui.components.calendar.CustomCalendar calendar_Checout;
    private ui.components.combobox.StyledComboBox cbx_RangePrice;
    private ui.components.combobox.StyledComboBox cbx_TypeRoom;
    private javax.swing.JLabel lbl_Capacity;
    private javax.swing.JLabel lbl_CartTittle;
    private javax.swing.JLabel lbl_Checkin;
    private javax.swing.JLabel lbl_Checkout;
    private javax.swing.JLabel lbl_CustomerCCCD;
    private javax.swing.JLabel lbl_CustomerCCCD_Value;
    private javax.swing.JLabel lbl_CustomerGender;
    private javax.swing.JLabel lbl_CustomerGender_Value;
    private javax.swing.JLabel lbl_CustomerName;
    private javax.swing.JLabel lbl_CustomerName_Value;
    private javax.swing.JLabel lbl_CustomerPhone;
    private javax.swing.JLabel lbl_CustomerPhone_Value;
    private javax.swing.JLabel lbl_Discount;
    private javax.swing.JLabel lbl_Discount_Value;
    private javax.swing.JLabel lbl_LastTotalPrice;
    private javax.swing.JLabel lbl_LastTotalPrice_Value;
    private javax.swing.JLabel lbl_RangePrice;
    private javax.swing.JLabel lbl_Tax;
    private javax.swing.JLabel lbl_Tax_Value;
    private javax.swing.JLabel lbl_TotalPrice;
    private javax.swing.JLabel lbl_TotolPrice_Value;
    private javax.swing.JLabel lbl_TypeRoom;
    private javax.swing.JPanel pnl_ButtonActions;
    private javax.swing.JPanel pnl_Cart;
    private javax.swing.JPanel pnl_CustomerLeft;
    private javax.swing.JPanel pnl_CustomerRight;
    private javax.swing.JPanel pnl_EmptyRoom;
    private javax.swing.JPanel pnl_InforBookingRoom;
    private javax.swing.JPanel pnl_InforCustomer;
    private javax.swing.JPanel pnl_Input_Left_InforBookRoom;
    private javax.swing.JPanel pnl_Input_Right_InforBookRoom;
    private javax.swing.JPanel pnl_Label_Left_InforBookRoom;
    private javax.swing.JPanel pnl_Label_Right_InforBookRoom;
    private javax.swing.JPanel pnl_Left_InforBookRoom;
    private javax.swing.JPanel pnl_PriceNumber;
    private javax.swing.JPanel pnl_PriceNumber_Left;
    private javax.swing.JPanel pnl_PriceNumber_Right;
    private javax.swing.JPanel pnl_Right_InforBookRoom;
    private ui.components.spinner.CustomSpinner spinner_Capacity;
    private ui.components.table.CustomTableButton table_Cart;
    private ui.components.table.CustomTableButton table_EntityRoom;
    private ui.components.textfield.SearchTextField txt_SearchCustomer;
    private ui.components.popup.PopupSearch popupSearchCustomer;
    private CustomerDAO customerDAO;
    // End of variables declaration//GEN-END:variables


}
