/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JPanel.java to edit this template
 */
package ui.tabs;

import dao.CustomerDAOImpl;
import dao.RoomDAOImpl;
import dao.RoomTypesImpl;
import entities.*;
import interfaces.CustomerDAO;
import interfaces.RoomDAO;
import interfaces.RoomTypesDAO;
import ui.components.button.ButtonRenderer;
import ui.components.popup.PopupSearch;
import ui.components.table.CustomTable;
import ui.components.table.CustomTableButton;
import ui.dialogs.Dialog_AddService;

import javax.swing.*;
import javax.swing.Timer;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellRenderer;
import java.awt.*;
import java.awt.event.*;
import java.rmi.RemoteException;
import java.util.*;
import java.util.List;
import java.util.stream.Collectors;

/**
 *
 * @author TRAN LONG VU
 */
public class Tab_Booking extends javax.swing.JPanel {
    private Timer debounceTimer;
    private static final int DEBOUNCE_DELAY = 300;
    private CustomerDAO customerDAO;
    private RoomDAO roomDAO;
    private PopupSearch popupSearchCustomer;

    /**
     * Creates new form Tab_Booking
     */
    public Tab_Booking() {
        customerDAO = new CustomerDAOImpl();
        roomDAO = new RoomDAOImpl();
        initComponents();
        initializeSearchCustomer();
        initializePriceRangeComboBox();
        initializeRoomTypeComboBox();
        initComboboxBookingMethod();
        initializeCartTable();

        initSpinner();

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

    private void initializeCartTable() {
        CustomTableButton.ColumnEditorType[] editorTypes = {
                CustomTableButton.ColumnEditorType.DEFAULT,    // STT
                CustomTableButton.ColumnEditorType.DEFAULT,    // Số phòng
                CustomTableButton.ColumnEditorType.DEFAULT,    // Giá/đêm
                CustomTableButton.ColumnEditorType.SPINNER,    // Số đêm
                CustomTableButton.ColumnEditorType.BUTTON,     // Dịch vụ
                CustomTableButton.ColumnEditorType.DEFAULT     // Thành tiền
        };

        table_Cart.setColumnEditorTypes(editorTypes);

        table_Cart.getTable().getColumnModel().getColumn(3).setCellRenderer(new SpinnerRenderer());
        table_Cart.getTable().getColumnModel().getColumn(3).setCellEditor(new CustomTableButton.SpinnerEditor());

        table_Cart.setButtonClickListener((buttonType, row, column) -> {
            if (column == 4) {
                showServicesDialog(row);
            }
        });

        table_Cart.getTable().getModel().addTableModelListener(e -> {
            if (e.getColumn() == 3) {
                updateRowTotal(e.getFirstRow());
                updateSummaryTotals();
            }
        });
    }

    private void initComboboxBookingMethod() {
        DefaultComboBoxModel<String> model = new DefaultComboBoxModel<>();
        model.addElement("Chọn hình thức đặt phòng");
        model.addElement("Tại quầy");
        model.addElement("Trực tuyến");
        cbx_BookingMethod.setModel(model);

        cbx_BookingMethod.addActionListener(e -> {
            if (table_Cart.getTableModel().getRowCount() > 0) {
                updateSummaryTotals();
            }
        });
    }

    private void initSpinner() {
        SpinnerNumberModel spinnerModel = new SpinnerNumberModel(1, 1, 10, 1);
        spinner_Capacity.setModel(spinnerModel);

        JSpinner.NumberEditor editor = new JSpinner.NumberEditor(spinner_Capacity, "#");
        spinner_Capacity.setEditor(editor);

        spinner_Capacity.setPreferredSize(new Dimension(100, 30));
        spinner_Capacity.setFocusable(true);

        // Sử dụng BasicSpinnerUI với các nút được tùy chỉnh
        spinner_Capacity.setUI(new javax.swing.plaf.basic.BasicSpinnerUI() {
            @Override
            protected Component createNextButton() {
                JButton button = new JButton("▲"); // Mũi tên lên
                button.setFocusable(false);
                button.setPreferredSize(new Dimension(20, 10));
                button.setBackground(new Color(255, 255, 255));

                button.addMouseListener(new MouseAdapter() {
                    @Override
                    public void mouseEntered(MouseEvent e) {
                        button.setBackground(new Color(220, 220, 220));
                    }

                    @Override
                    public void mouseExited(MouseEvent e) {
                        button.setBackground(new Color(255, 255, 255));
                    }
                });

                button.addActionListener(e -> {
                    spinner_Capacity.setValue(spinner_Capacity.getNextValue());
                });

                return button;
            }

            @Override
            protected Component createPreviousButton() {
                JButton button = new JButton("▼");
                button.setFocusable(false);
                button.setPreferredSize(new Dimension(20, 10));
                button.setBackground(new Color(255, 255, 255));

                // Hiệu ứng hover
                button.addMouseListener(new MouseAdapter() {
                    @Override
                    public void mouseEntered(MouseEvent e) {
                        button.setBackground(new Color(220, 220, 220));
                    }

                    @Override
                    public void mouseExited(MouseEvent e) {
                        button.setBackground(new Color(255, 255, 255));
                    }
                });

                // Xử lý sự kiện click
                button.addActionListener(e -> {
                    spinner_Capacity.setValue(spinner_Capacity.getPreviousValue());
                });

                return button;
            }
        });

        spinner_Capacity.addChangeListener(e -> {
            System.out.println("Spinner value changed: " + spinner_Capacity.getValue());
        });
    }

    private void initializeRoomTypeComboBox() {
        RoomTypesDAO roomTypesDAO = new RoomTypesImpl();
        List<RoomType> roomTypes = roomTypesDAO.getAllRoomTypes();

        DefaultComboBoxModel<String> model = new DefaultComboBoxModel<>();
        model.addElement("Tất cả loại phòng");

        for (RoomType roomType : roomTypes) {
            model.addElement(roomType.getTypeName());
        }

        cbx_TypeRoom.setModel(model);

        cbx_TypeRoom.addItemListener(new ItemListener() {
            @Override
            public void itemStateChanged(ItemEvent e) {
                if (e.getStateChange() == ItemEvent.SELECTED) {
                    String selectedType = (String) cbx_TypeRoom.getSelectedItem();
                    filterRoomsByType(selectedType);
                }
            }
        });
    }

    private void initializePriceRangeComboBox() {
        RoomDAO roomDAO = new RoomDAOImpl();
        List<Room> allRooms = roomDAO.findAll();
        List<String> priceRanges = generateSmartPriceRanges(allRooms);
        DefaultComboBoxModel<String> model = new DefaultComboBoxModel<>();
        model.addElement("Tất cả giá"); // Option đầu tiên
        for (String range : priceRanges) {
            model.addElement(range);
        }

        cbx_RangePrice.setModel(model);

        displayRoomsInTable(allRooms);
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
        spinner_Capacity = new javax.swing.JSpinner();
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
        pnl_ButtonActions = new javax.swing.JPanel();
        btn_Booking = new ui.components.button.ButtonCustom();
        btn_Cancel = new ui.components.button.ButtonCancelCustom();
        btn_KeyboardNumber = new ui.components.button.ButtonCustom();
        pnl_Cart = new javax.swing.JPanel();
        lbl_CartTittle = new javax.swing.JLabel();
        table_Cart = new ui.components.table.CustomTableButton();
        pnl_InforBooking = new javax.swing.JPanel();
        pnl_Left_InforBooking = new javax.swing.JPanel();
        lbl_LastTotalPrice = new javax.swing.JLabel();
        lbl_BookingMethod = new javax.swing.JLabel();
        lbl_Deposit = new javax.swing.JLabel();
        lbl_RemainingAmount = new javax.swing.JLabel();
        pnl_Right_InforBooking = new javax.swing.JPanel();
        lbl_LastTotalPrice_Value = new javax.swing.JLabel();
        cbx_BookingMethod = new ui.components.combobox.StyledComboBox();
        lbl_Deposit_Value = new javax.swing.JLabel();
        lbl_RemainingAmount_Value = new javax.swing.JLabel();

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

        calendar_Checkin.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        pnl_Input_Left_InforBookRoom.add(calendar_Checkin);

        calendar_Checout.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        pnl_Input_Left_InforBookRoom.add(calendar_Checout);

        cbx_RangePrice.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
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
        pnl_Input_Right_InforBookRoom.setPreferredSize(new java.awt.Dimension(84, 100));
        pnl_Input_Right_InforBookRoom.setLayout(new java.awt.GridLayout(3, 1, 0, 12));

        spinner_Capacity.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        spinner_Capacity.setFocusable(false);
        spinner_Capacity.setVerifyInputWhenFocusTarget(false);
        pnl_Input_Right_InforBookRoom.add(spinner_Capacity);

        cbx_TypeRoom.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
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
                    .addComponent(pnl_Input_Right_InforBookRoom, javax.swing.GroupLayout.DEFAULT_SIZE, 136, Short.MAX_VALUE)
                    .addContainerGap()))
        );

        btn_Clear.setText("Xóa trắng");
        btn_Clear.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_ClearActionPerformed(evt);
            }
        });

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
        txt_SearchCustomer.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                txt_SearchCustomerMouseClicked(evt);
            }
        });
        pnl_InforCustomer.add(txt_SearchCustomer, new org.netbeans.lib.awtextra.AbsoluteConstraints(12, 25, 460, -1));

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
                        .addComponent(btn_KeyboardNumber, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))))
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
                .addGap(0, 0, Short.MAX_VALUE))
        );

        add(pnl_ButtonActions, new org.netbeans.lib.awtextra.AbsoluteConstraints(820, 570, 480, 130));

        pnl_Cart.setBackground(new java.awt.Color(255, 255, 255));
        pnl_Cart.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(204, 204, 204)));
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

        pnl_InforBooking.setBackground(new java.awt.Color(255, 255, 255));
        pnl_InforBooking.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Thông tin đặt phòng", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Segoe UI", 1, 16), new java.awt.Color(153, 153, 255))); // NOI18N

        pnl_Left_InforBooking.setBackground(new java.awt.Color(255, 255, 255));
        pnl_Left_InforBooking.setLayout(new java.awt.GridLayout(4, 1));

        lbl_LastTotalPrice.setFont(new java.awt.Font("Segoe UI", 0, 16)); // NOI18N
        lbl_LastTotalPrice.setText("Tổng tiền:");
        pnl_Left_InforBooking.add(lbl_LastTotalPrice);

        lbl_BookingMethod.setFont(new java.awt.Font("Segoe UI", 0, 16)); // NOI18N
        lbl_BookingMethod.setText("Hình thức đặt phòng:");
        pnl_Left_InforBooking.add(lbl_BookingMethod);

        lbl_Deposit.setFont(new java.awt.Font("Segoe UI", 0, 16)); // NOI18N
        lbl_Deposit.setText("Số tiền cọc:");
        pnl_Left_InforBooking.add(lbl_Deposit);

        lbl_RemainingAmount.setFont(new java.awt.Font("Segoe UI", 0, 16)); // NOI18N
        lbl_RemainingAmount.setText("Số tiền còn lại:");
        pnl_Left_InforBooking.add(lbl_RemainingAmount);

        pnl_Right_InforBooking.setBackground(new java.awt.Color(255, 255, 255));
        pnl_Right_InforBooking.setLayout(new java.awt.GridLayout(4, 1, 0, 10));

        lbl_LastTotalPrice_Value.setFont(new java.awt.Font("Segoe UI", 0, 16)); // NOI18N
        lbl_LastTotalPrice_Value.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(204, 204, 204)));
        pnl_Right_InforBooking.add(lbl_LastTotalPrice_Value);

        cbx_BookingMethod.setFont(new java.awt.Font("Segoe UI", 0, 16)); // NOI18N
        pnl_Right_InforBooking.add(cbx_BookingMethod);

        lbl_Deposit_Value.setFont(new java.awt.Font("Segoe UI", 0, 16)); // NOI18N
        lbl_Deposit_Value.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(204, 204, 204)));
        pnl_Right_InforBooking.add(lbl_Deposit_Value);

        lbl_RemainingAmount_Value.setFont(new java.awt.Font("Segoe UI", 0, 16)); // NOI18N
        lbl_RemainingAmount_Value.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(204, 204, 204)));
        pnl_Right_InforBooking.add(lbl_RemainingAmount_Value);

        javax.swing.GroupLayout pnl_InforBookingLayout = new javax.swing.GroupLayout(pnl_InforBooking);
        pnl_InforBooking.setLayout(pnl_InforBookingLayout);
        pnl_InforBookingLayout.setHorizontalGroup(
            pnl_InforBookingLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnl_InforBookingLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(pnl_Left_InforBooking, javax.swing.GroupLayout.PREFERRED_SIZE, 159, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(pnl_Right_InforBooking, javax.swing.GroupLayout.PREFERRED_SIZE, 285, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(14, Short.MAX_VALUE))
        );
        pnl_InforBookingLayout.setVerticalGroup(
            pnl_InforBookingLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnl_InforBookingLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(pnl_InforBookingLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(pnl_Left_InforBooking, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(pnl_Right_InforBooking, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
        );

        add(pnl_InforBooking, new org.netbeans.lib.awtextra.AbsoluteConstraints(820, 310, 480, 260));
    }// </editor-fold>//GEN-END:initComponents

    private void btn_CancelActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_CancelActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_btn_CancelActionPerformed

    private void btn_SearchRoomActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_SearchRoomActionPerformed
        searchEntityRoom();
    }//GEN-LAST:event_btn_SearchRoomActionPerformed

    private void btn_AddRoomActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_AddRoomActionPerformed
        addEntityRoomToCart();
    }//GEN-LAST:event_btn_AddRoomActionPerformed

    private void btn_AddCustomerActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_AddCustomerActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_btn_AddCustomerActionPerformed

    private void txt_SearchCustomerMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_txt_SearchCustomerMouseClicked
        // TODO add your handling code here:
    }//GEN-LAST:event_txt_SearchCustomerMouseClicked

    private void btn_ClearActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_ClearActionPerformed
        clear();
    }//GEN-LAST:event_btn_ClearActionPerformed


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
    private ui.components.combobox.StyledComboBox cbx_BookingMethod;
    private ui.components.combobox.StyledComboBox cbx_RangePrice;
    private ui.components.combobox.StyledComboBox cbx_TypeRoom;
    private javax.swing.JLabel lbl_BookingMethod;
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
    private javax.swing.JLabel lbl_Deposit;
    private javax.swing.JLabel lbl_Deposit_Value;
    private javax.swing.JLabel lbl_LastTotalPrice;
    private javax.swing.JLabel lbl_LastTotalPrice_Value;
    private javax.swing.JLabel lbl_RangePrice;
    private javax.swing.JLabel lbl_RemainingAmount;
    private javax.swing.JLabel lbl_RemainingAmount_Value;
    private javax.swing.JLabel lbl_TypeRoom;
    private javax.swing.JPanel pnl_ButtonActions;
    private javax.swing.JPanel pnl_Cart;
    private javax.swing.JPanel pnl_CustomerLeft;
    private javax.swing.JPanel pnl_CustomerRight;
    private javax.swing.JPanel pnl_EmptyRoom;
    private javax.swing.JPanel pnl_InforBooking;
    private javax.swing.JPanel pnl_InforBookingRoom;
    private javax.swing.JPanel pnl_InforCustomer;
    private javax.swing.JPanel pnl_Input_Left_InforBookRoom;
    private javax.swing.JPanel pnl_Input_Right_InforBookRoom;
    private javax.swing.JPanel pnl_Label_Left_InforBookRoom;
    private javax.swing.JPanel pnl_Label_Right_InforBookRoom;
    private javax.swing.JPanel pnl_Left_InforBookRoom;
    private javax.swing.JPanel pnl_Left_InforBooking;
    private javax.swing.JPanel pnl_Right_InforBookRoom;
    private javax.swing.JPanel pnl_Right_InforBooking;
    private javax.swing.JSpinner spinner_Capacity;
    private ui.components.table.CustomTableButton table_Cart;
    private ui.components.table.CustomTableButton table_EntityRoom;
    private ui.components.textfield.SearchTextField txt_SearchCustomer;
    // End of variables declaration//GEN-END:variables

    private void searchEntityRoom() {
        try {
            // Lấy các tham số tìm kiếm (cho phép null)
            Date checkinDate = calendar_Checkin.getSelectedDate();
            Date checkoutDate = calendar_Checout.getSelectedDate();

            // Validate ngày nếu có chọn
            if (checkinDate != null && checkoutDate != null && checkinDate.after(checkoutDate)) {
                JOptionPane.showMessageDialog(this, "Ngày check-out phải sau ngày check-in", "Lỗi", JOptionPane.ERROR_MESSAGE);
                return;
            }

            // Lấy các tham số khác (cho phép null)
            Integer capacity = null;
            try {
                capacity = (spinner_Capacity.getValue() != null) ? (int) spinner_Capacity.getValue() : null;
            } catch (Exception e) {
                // Bỏ qua nếu có lỗi khi lấy giá trị spinner
            }

            String typeRoom = cbx_TypeRoom.getSelectedItem() != null ?
                    cbx_TypeRoom.getSelectedItem().toString() : null;
            typeRoom = (typeRoom != null && "Tất cả loại phòng".equals(typeRoom)) ? null : typeRoom;

            // Xử lý khoảng giá
            Double minPrice = null;
            Double maxPrice = null;
            if (cbx_RangePrice.getSelectedItem() != null) {
                String priceRange = cbx_RangePrice.getSelectedItem().toString();
                if (!"Tất cả giá".equals(priceRange)) {
                    if (priceRange.startsWith("Trên")) {
                        minPrice = Double.parseDouble(priceRange.replaceAll("[^\\d.]", ""));
                        maxPrice = Double.MAX_VALUE;
                    } else {
                        String[] priceParts = priceRange.split(" - ");
                        if (priceParts.length == 2) {
                            minPrice = Double.parseDouble(priceParts[0].replaceAll("[^\\d.]", ""));
                            maxPrice = Double.parseDouble(priceParts[1].replaceAll("[^\\d.]", ""));
                        }
                    }
                }
            }

            // Tìm kiếm phòng (có thể không có điều kiện nào)
            List<Room> availableRooms = roomDAO.findAvailableRooms(
                    checkinDate,
                    checkoutDate,
                    capacity,
                    typeRoom,
                    minPrice,
                    maxPrice
            );

            // Hiển thị kết quả
            if (availableRooms == null || availableRooms.isEmpty()) {
                table_EntityRoom.getTableModel().clearData();
                JOptionPane.showMessageDialog(this, "Không tìm thấy phòng phù hợp", "Thông báo", JOptionPane.INFORMATION_MESSAGE);
            } else {
                displayRoomsInTable(availableRooms);
            }

        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Đã xảy ra lỗi khi tìm kiếm phòng: " + e.getMessage(),
                    "Lỗi", JOptionPane.ERROR_MESSAGE);
        }
    }

    public List<String> generateSmartPriceRanges(List<Room> rooms) {
        List<String> priceRanges = new ArrayList<>();

        if (rooms == null || rooms.isEmpty()) {
            return Arrays.asList(
                    "0 - 1,000,000 VND",
                    "1,000,000 - 2,000,000 VND",
                    "2,000,000 - 3,000,000 VND",
                    "3,000,000 - 5,000,000 VND",
                    "Trên 5,000,000 VND"
            );
        }

        double minPrice = rooms.stream().mapToDouble(Room::getPrice).min().orElse(0);
        double maxPrice = rooms.stream().mapToDouble(Room::getPrice).max().orElse(5000000);

        List<Double> prices = rooms.stream().map(Room::getPrice).sorted().collect(Collectors.toList());

        int size = prices.size();
        double q1 = prices.get(size / 4);
        double median = prices.get(size / 2);
        double q3 = prices.get(3 * size / 4);

        minPrice = Math.floor(minPrice / 100000) * 100000;
        q1 = Math.round(q1 / 100000) * 100000;
        median = Math.round(median / 100000) * 100000;
        q3 = Math.round(q3 / 100000) * 100000;
        maxPrice = Math.ceil(maxPrice / 100000) * 100000;

        priceRanges.add(String.format("%,.0f - %,.0f VND", minPrice, q1));
        priceRanges.add(String.format("%,.0f - %,.0f VND", q1, median));
        priceRanges.add(String.format("%,.0f - %,.0f VND", median, q3));
        priceRanges.add(String.format("%,.0f - %,.0f VND", q3, maxPrice));
        priceRanges.add(String.format("Trên %,.0f VND", maxPrice));

        return priceRanges;
    }

    private void displayRoomsInTable(List<Room> rooms) {
        CustomTableButton.CustomTableModel model = table_EntityRoom.getTableModel();
        model.clearData();

        for (Room room : rooms) {
            // Xử lý amenities an toàn
            String amenitiesStr = "N/A";
            try {
                amenitiesStr = room.getAmenities() != null ?
                        String.join(", ", room.getAmenities()) : "N/A";
            } catch (Exception e) {
                amenitiesStr = "Lỗi khi tải tiện nghi";
            }

            // Xử lý loại phòng an toàn
            String roomTypeStr = "N/A";
            try {
                roomTypeStr = room.getRoomType() != null ?
                        room.getRoomType().getTypeName() : "N/A";
            } catch (Exception e) {
                roomTypeStr = "Lỗi khi tải loại phòng";
            }

            Object[] rowData = {
                    model.getRowCount() + 1,
                    room.getRoomId(),
                    roomTypeStr,
                    String.format("%,.0f VND", room.getPrice()),
                    amenitiesStr,
                    room.getStatus() == Room.STATUS_AVAILABLE ? "Trống" : "Đã thuê"
            };
            model.addRow(rowData, null);
        }
    }

    private String getStatusText(int status) {
        switch (status) {
            case Room.STATUS_AVAILABLE: return "Trống";
            case Room.STATUS_OCCUPIED: return "Đã thuê";
            case Room.STATUS_MAINTENANCE: return "Bảo trì";
            case Room.STATUS_RESERVED: return "Đã đặt";
            default: return "Không xác định";
        }
    }

    public double[] parsePriceRange(String priceRange) {
        double[] result = new double[2];

        if (priceRange == null || priceRange.isEmpty() || priceRange.equals("Tất cả giá")) {
            result[0] = 0;
            result[1] = Double.MAX_VALUE;
            return result;
        }

        if (priceRange.startsWith("Trên")) {
            String numberStr = priceRange.replaceAll("[^\\d.]", "");
            result[0] = Double.parseDouble(numberStr);
            result[1] = Double.MAX_VALUE;
            return result;
        }

        // Xử lý chuỗi dạng "1,000,000 - 2,000,000 VND"
        String[] parts = priceRange.split(" - ");
        if (parts.length == 2) {
            String minStr = parts[0].replaceAll("[^\\d.]", "");
            String maxStr = parts[1].replaceAll("[^\\d.]", "");

            result[0] = Double.parseDouble(minStr);
            result[1] = Double.parseDouble(maxStr);
        }

        return result;
    }

    private void filterRoomsByType(String roomType) {
        Date checkIn = calendar_Checkin.getSelectedDate();
        Date checkOut = calendar_Checout.getSelectedDate();
        int capacity = (int) spinner_Capacity.getValue();

        String priceRange = (String) cbx_RangePrice.getSelectedItem();
        double[] prices = parsePriceRange(priceRange);

        RoomDAO roomDAO = new RoomDAOImpl();
        List<Room> filteredRooms = roomDAO.findAvailableRooms(
                checkIn,
                checkOut,
                capacity,
                roomType.equals("Tất cả loại") ? null : roomType,
                prices[0],
                prices[1]
        );

        displayRoomsInTable(filteredRooms);
    }

    private void updateTotalPrices() {
        try {
            CustomTableButton.CustomTableModel cartModel = table_Cart.getTableModel();
            for (int i = 0; i < cartModel.getRowCount(); i++) {
                Object[] rowData = cartModel.getRowData(i);

                Reservation reservation = new Reservation();
                String roomId = (String) rowData[1];
                Room room = roomDAO.findById(roomId);
                reservation.setRoom(room);

                // Thiết lập hình thức đặt phòng
                String bookingMethodStr = (String) cbx_BookingMethod.getSelectedItem();
                BookingMethod bookingMethod = "Tại quầy".equals(bookingMethodStr)
                        ? BookingMethod.AT_THE_COUNTER
                        : BookingMethod.CONTACT;
                reservation.setBookingMethod(bookingMethod);

                // Số đêm
                int numberOfNights = (int) rowData[3];
                reservation.setNumberOfNights(numberOfNights);


//                Set<ReservationDetails> details = new HashSet<>();
//
//                ReservationDetails detail = new ReservationDetails();
//                detail.setReservation(reservation);
//
//                // Dịch vụ (nếu có)
//                // Giả sử rowData[4] chứa danh sách dịch vụ đã chọn
//                String servicesStr = (String) rowData[4];
//                if (!servicesStr.isEmpty()) {
//                    // Xử lý thêm dịch vụ vào detail nếu cần
//                }
//
//                details.add(detail);
//
//                // Thiết lập chi tiết đặt phòng
//                reservation.setReservationDetails(details);

                // Tính toán các giá trị
                reservation.calculateTotalPrice();
                reservation.calculateDepositAmount();
                reservation.calculateRemainingAmount();

                // Cập nhật giao diện
                lbl_LastTotalPrice_Value.setText(String.format("%,.0f VND", reservation.getTotalPrice()));
                if (!cbx_BookingMethod.getSelectedItem().equals("Chọn hình thức đặt phòng")) {
                    lbl_Deposit_Value.setText(String.format("%,.0f VND", reservation.getDepositAmount()));
                }
                lbl_RemainingAmount_Value.setText(String.format("%,.0f VND", reservation.getRemainingAmount()));
            }
        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Đã xảy ra lỗi khi tính toán giá: " + e.getMessage(),
                    "Lỗi", JOptionPane.ERROR_MESSAGE);
        }
    }

    private int calculateNumberOfNights() {
        Date checkInDate = calendar_Checkin.getSelectedDate();
        Date checkOutDate = calendar_Checout.getSelectedDate();
        if (checkInDate != null && checkOutDate != null) {
            long diffInMillis = checkOutDate.getTime() - checkInDate.getTime();
            return (int) Math.max(1, diffInMillis / (1000 * 60 * 60 * 24));
        }
        return 1; // Mặc định 1 đêm nếu chưa chọn ngày
    }


    private boolean isRoomAlreadyInCart(String roomId) {
        CustomTableButton.CustomTableModel cartModel = table_Cart.getTableModel();
        for (int i = 0; i < cartModel.getRowCount(); i++) {
            Object[] cartRow = cartModel.getRowData(i);
            if (roomId.equals(cartRow[1])) {
                return true;
            }
        }
        return false;
    }

    private void addToCart(String roomId, double price, int numberOfNights) {
        CustomTableButton.CustomTableModel cartModel = table_Cart.getTableModel();

        Object[] cartRow = {
                cartModel.getRowCount() + 1,
                roomId,
                String.format("%,.0f VND", price),
                numberOfNights,
                "Thêm dịch vụ",
                String.format("%,.0f VND", numberOfNights * price)
        };

        CustomTableButton.ButtonType[] buttonTypes = new CustomTableButton.ButtonType[cartModel.getColumnCount()];
        buttonTypes[4] = CustomTableButton.ButtonType.SERVICE;

        cartModel.addRow(cartRow, buttonTypes);
    }

    private void addEntityRoomToCart() {
        try {
            int selectedRow = table_EntityRoom.getTable().getSelectedRow();
            if (selectedRow == -1) {
                JOptionPane.showMessageDialog(this, "Vui lòng chọn phòng cần thêm",
                        "Thông báo", JOptionPane.WARNING_MESSAGE);
                return;
            }

            int modelRow = table_EntityRoom.getTable().convertRowIndexToModel(selectedRow);
            Object[] rowData = table_EntityRoom.getTableModel().getRowData(modelRow);

            // Kiểm tra trạng thái phòng
            String status = (String) rowData[5];
            if (!"Trống".equals(status)) {
                JOptionPane.showMessageDialog(this, "Chỉ có thể thêm phòng có trạng thái Trống",
                        "Thông báo", JOptionPane.WARNING_MESSAGE);
                return;
            }

            String roomId = (String) rowData[1];
            double price = Double.parseDouble(((String) rowData[3]).replaceAll("[^\\d.]", ""));

            // Tính số đêm
            int numberOfNights = calculateNumberOfNights();

            // Kiểm tra phòng đã có trong giỏ chưa
            if (isRoomAlreadyInCart(roomId)) {
                JOptionPane.showMessageDialog(this, "Phòng này đã có trong giỏ hàng",
                        "Thông báo", JOptionPane.WARNING_MESSAGE);
                return;
            }

            // Thêm vào giỏ hàng
            addToCart(roomId, price, numberOfNights);
            updateSummaryTotals();

        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Đã xảy ra lỗi khi thêm phòng: " + e.getMessage(),
                    "Lỗi", JOptionPane.ERROR_MESSAGE);
        }
    }

    // Cập nhật tổng tiền khi số đêm thay đổi
    private void updateRowTotal(int row) {
        try {
            CustomTableButton.CustomTableModel model = table_Cart.getTableModel();
            Object[] rowData = model.getRowData(row);

            // Lấy thông tin từ hàng
            String roomId = (String) rowData[1];
            int numberOfNights = (int) rowData[3];
            double pricePerNight = Double.parseDouble(((String) rowData[2]).replaceAll("[^\\d.]", ""));

            // Tính toán tổng tiền cho hàng
            double rowTotal = pricePerNight * numberOfNights;

            // Cập nhật dữ liệu hàng
            rowData[5] = String.format("%,.0f VND", rowTotal);
            model.fireTableRowsUpdated(row, row);
        } catch (Exception ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this, "Lỗi cập nhật giá trị hàng: " + ex.getMessage(),
                    "Lỗi", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void updateSummaryTotals() {
        try {
            double totalPrice = 0;
            CustomTableButton.CustomTableModel cartModel = table_Cart.getTableModel();

            // Tính tổng giá trị từ tất cả các hàng
            for (int i = 0; i < cartModel.getRowCount(); i++) {
                Object[] rowData = cartModel.getRowData(i);
                double rowTotal = Double.parseDouble(((String) rowData[5]).replaceAll("[^\\d.]", ""));
                totalPrice += rowTotal;
            }

            // Tính toán các giá trị tổng hợp
            double depositAmount = 0;
            double remainingAmount = 0;
            String bookingMethodStr = (String) cbx_BookingMethod.getSelectedItem();
            if (!"Chọn hình thức đặt phòng".equals(bookingMethodStr)) {
                BookingMethod bookingMethod = "Tại quầy".equals(bookingMethodStr)
                        ? BookingMethod.AT_THE_COUNTER
                        : BookingMethod.CONTACT;

                depositAmount = calculateDeposit(totalPrice, bookingMethod);
                remainingAmount = totalPrice - depositAmount;

            }
            lbl_Deposit_Value.setText(String.format("%,.0f VND", depositAmount));
            lbl_RemainingAmount_Value.setText(String.format("%,.0f VND", remainingAmount));
            lbl_LastTotalPrice_Value.setText(String.format("%,.0f VND", totalPrice));


        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Đã xảy ra lỗi khi tính toán tổng giá: " + e.getMessage(),
                    "Lỗi", JOptionPane.ERROR_MESSAGE);
        }
    }

    private double calculateDeposit(double totalPrice, BookingMethod bookingMethod) {
        // Logic tính tiền đặt cọc dựa trên hình thức đặt phòng
        switch (bookingMethod) {
            case AT_THE_COUNTER:
                return Math.min(1000000, totalPrice * 0.3); // Tối đa 1 triệu hoặc 30%
            case CONTACT:
                return totalPrice * 0.5; // 50% cho đặt qua liên hệ
            default:
                return 0;
        }
    }


    private void clear() {
        calendar_Checkin.setSelectedDate(null);
        calendar_Checout.setSelectedDate(null);
        spinner_Capacity.setValue(1);
        cbx_TypeRoom.setSelectedIndex(0);
        cbx_RangePrice.setSelectedIndex(0);

        table_EntityRoom.getTableModel().clearData();
        table_Cart.getTableModel().clearData();

        lbl_LastTotalPrice_Value.setText("");
        lbl_Deposit_Value.setText("");
        lbl_RemainingAmount_Value.setText("");
        cbx_BookingMethod.setSelectedIndex(0);

        List<Room> allRooms = roomDAO.findAll();
        displayRoomsInTable(allRooms);
    }

    // Renderer cho Spinner
    private class SpinnerRenderer implements TableCellRenderer {
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

    // Hiển thị dialog chọn dịch vụ
    private void showServicesDialog(int row) {
        System.out.println("Opening services dialog for row: " + row);

        try {
            JDialog dialog = new JDialog();
            dialog.setTitle("Chọn dịch vụ");
            dialog.setSize(900, 600);
            dialog.setModal(true);
            dialog.setLayout(new BorderLayout());

            Object[] rowData = table_Cart.getTableModel().getRowData(row);
            if (rowData == null) {
                System.out.println("Row data is null for row: " + row);
                return;
            }

            String roomID = (String) rowData[1];
            System.out.println("Room ID: " + roomID);

            Dialog_AddService addServiceDialog = new Dialog_AddService(roomID);
            dialog.add(addServiceDialog, BorderLayout.CENTER);

            dialog.setLocationRelativeTo(this);
            dialog.setVisible(true);
        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Lỗi khi mở dialog: " + e.getMessage(), "Lỗi", JOptionPane.ERROR_MESSAGE);
        }
    }

    // Cập nhật dịch vụ vào giỏ hàng
    private void updateServicesInCart(int row, List<Service> services) {
        CustomTableButton.CustomTableModel model = table_Cart.getTableModel();
        Object[] rowData = model.getRowData(row);

        // Cập nhật cột dịch vụ
        String servicesText = services.stream()
                .map(Service::getName)
                .collect(Collectors.joining(", "));
        rowData[4] = servicesText;

        // Tính giá dịch vụ
        double servicesTotal = services.stream()
                .mapToDouble(Service::getPrice)
                .sum();

        // Cập nhật thành tiền (giá phòng + dịch vụ)
        double pricePerNight = Double.parseDouble(((String) rowData[2]).replaceAll("[^\\d.]", ""));
        int numberOfNights = (int) rowData[3];
        double roomTotal = pricePerNight * numberOfNights;
        double total = roomTotal + servicesTotal;

        rowData[5] = String.format("%,.0f VND", total);

        // Cập nhật model
        model.fireTableRowsUpdated(row, row);
        updateTotalPrices();
    }


}
