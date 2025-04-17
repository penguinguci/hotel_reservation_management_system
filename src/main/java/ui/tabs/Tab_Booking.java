/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JPanel.java to edit this template
 */
package ui.tabs;

import dao.CustomerDAOImpl;
import dao.ReservationDAOImpl;
import dao.RoomDAOImpl;
import dao.RoomTypesImpl;
import entities.*;
import interfaces.CustomerDAO;
import interfaces.ReservationDAO;
import interfaces.RoomDAO;
import interfaces.RoomTypesDAO;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityTransaction;
import lombok.SneakyThrows;
import ui.components.button.ButtonRenderer;
import ui.components.popup.PopupSearch;
import ui.components.table.CustomTable;
import ui.components.table.CustomTableButton;
import ui.dialogs.Dialog_AddCustomer;
import ui.dialogs.Dialog_AddService;
import ui.dialogs.Dialog_ViewRoomDetails;
import ultilities.GenerateString;
import utils.AppUtil;

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
    private List<ReservationDetails> reservationDetailsList = new ArrayList<>();
    private Map<String, List<ReservationDetails>> listMapReservationDetails = new LinkedHashMap<>();

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
                CustomTableButton.ColumnEditorType.DEFAULT,     // Dịch vụ
                CustomTableButton.ColumnEditorType.DEFAULT     // Thành tiền
        };

        table_Cart.setColumnEditorTypes(editorTypes);

        table_Cart.getTable().getColumnModel().getColumn(3).setCellRenderer(new CustomTableButton.SpinnerRenderer());
        table_Cart.getTable().getColumnModel().getColumn(3).setCellEditor(new CustomTableButton.SpinnerEditor());

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

        spinner_Capacity.setUI(new javax.swing.plaf.basic.BasicSpinnerUI() {
            @Override
            protected Component createNextButton() {
                JButton button = new JButton("▲");
                button.setFocusable(false);
                button.setForeground(new Color(109, 104, 230));
                button.setFont(new Font("Arial", Font.BOLD, 10));
                button.setPreferredSize(new Dimension(40, 40));
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

                button.addActionListener(e -> spinner_Capacity.setValue(spinner_Capacity.getNextValue()));
                return button;
            }

            @Override
            protected Component createPreviousButton() {
                JButton button = new JButton("▼");
                button.setFocusable(false);
                button.setFont(new Font("Arial", Font.BOLD, 10));
                button.setForeground(new Color(109, 104, 230));
                button.setPreferredSize(new Dimension(40, 40));
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

                button.addActionListener(e -> spinner_Capacity.setValue(spinner_Capacity.getPreviousValue()));
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
                // Chỉ ẩn popup khi mất focus, không cố focus lại
                popupSearchCustomer.hidePopup();
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
        table_Cart = new ui.components.table.CustomTableButton();
        pnl_BottomService = new javax.swing.JPanel();
        btn_AddService = new ui.components.button.ButtonCustom();
        btn_UpdateService = new ui.components.button.ButtonCustom();
        pnl_TopCart = new javax.swing.JPanel();
        lbl_CartTittle = new javax.swing.JLabel();
        btn_DeleteOne = new ui.components.button.ButtonCancelCustom();
        btn_DeleteAll = new ui.components.button.ButtonCancelCustom();
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
        btn_SeeDetails.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_SeeDetailsActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout pnl_EmptyRoomLayout = new javax.swing.GroupLayout(pnl_EmptyRoom);
        pnl_EmptyRoom.setLayout(pnl_EmptyRoomLayout);
        pnl_EmptyRoomLayout.setHorizontalGroup(
            pnl_EmptyRoomLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
            .addComponent(pnl_InforBookingRoom, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(table_EntityRoom, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addGroup(javax.swing.GroupLayout.Alignment.LEADING, pnl_EmptyRoomLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(btn_AddRoom, javax.swing.GroupLayout.PREFERRED_SIZE, 133, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(btn_SeeDetails, javax.swing.GroupLayout.PREFERRED_SIZE, 130, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, Short.MAX_VALUE))
        );
        pnl_EmptyRoomLayout.setVerticalGroup(
            pnl_EmptyRoomLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, pnl_EmptyRoomLayout.createSequentialGroup()
                .addComponent(pnl_InforBookingRoom, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(table_EntityRoom, javax.swing.GroupLayout.PREFERRED_SIZE, 197, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(pnl_EmptyRoomLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(btn_AddRoom, javax.swing.GroupLayout.DEFAULT_SIZE, 39, Short.MAX_VALUE)
                    .addComponent(btn_SeeDetails, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
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
        btn_Booking.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                try {
                    btn_BookingActionPerformed(evt);
                } catch (RemoteException e) {
                    throw new RuntimeException(e);
                }
            }
        });

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
        pnl_Cart.setLayout(new java.awt.BorderLayout());

        table_Cart.setColumnNames(new String[] {"STT", "Số phòng", "Giá/đêm", "Số đêm", "Dịch vụ", "Thành tiền"});
        table_Cart.setHeaderBackgroundColor(new java.awt.Color(153, 153, 255));
        pnl_Cart.add(table_Cart, java.awt.BorderLayout.CENTER);

        pnl_BottomService.setBackground(new java.awt.Color(255, 255, 255));
        pnl_BottomService.setPreferredSize(new java.awt.Dimension(818, 50));

        btn_AddService.setText("Thêm dịch vụ");
        btn_AddService.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_AddServiceActionPerformed(evt);
            }
        });

        btn_UpdateService.setText("Sửa dịch vụ");
        btn_UpdateService.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_UpdateServiceActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout pnl_BottomServiceLayout = new javax.swing.GroupLayout(pnl_BottomService);
        pnl_BottomService.setLayout(pnl_BottomServiceLayout);
        pnl_BottomServiceLayout.setHorizontalGroup(
            pnl_BottomServiceLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnl_BottomServiceLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(btn_AddService, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(btn_UpdateService, javax.swing.GroupLayout.PREFERRED_SIZE, 130, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(534, Short.MAX_VALUE))
        );
        pnl_BottomServiceLayout.setVerticalGroup(
            pnl_BottomServiceLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, pnl_BottomServiceLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(pnl_BottomServiceLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(btn_AddService, javax.swing.GroupLayout.DEFAULT_SIZE, 38, Short.MAX_VALUE)
                    .addComponent(btn_UpdateService, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );

        pnl_Cart.add(pnl_BottomService, java.awt.BorderLayout.PAGE_END);

        pnl_TopCart.setBackground(new java.awt.Color(255, 255, 255));
        pnl_TopCart.setPreferredSize(new java.awt.Dimension(818, 32));

        lbl_CartTittle.setBackground(new java.awt.Color(255, 255, 255));
        lbl_CartTittle.setFont(new java.awt.Font("Segoe UI", 1, 16)); // NOI18N
        lbl_CartTittle.setForeground(new java.awt.Color(153, 153, 255));
        lbl_CartTittle.setText("Hàng chờ");
        lbl_CartTittle.setPreferredSize(new java.awt.Dimension(73, 25));

        btn_DeleteOne.setText("Xóa");
        btn_DeleteOne.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_DeleteOneActionPerformed(evt);
            }
        });

        btn_DeleteAll.setText("Xóa tất cả");
        btn_DeleteAll.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_DeleteAllActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout pnl_TopCartLayout = new javax.swing.GroupLayout(pnl_TopCart);
        pnl_TopCart.setLayout(pnl_TopCartLayout);
        pnl_TopCartLayout.setHorizontalGroup(
            pnl_TopCartLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, pnl_TopCartLayout.createSequentialGroup()
                .addContainerGap(624, Short.MAX_VALUE)
                .addComponent(btn_DeleteOne, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(btn_DeleteAll, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
            .addGroup(pnl_TopCartLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(pnl_TopCartLayout.createSequentialGroup()
                    .addComponent(lbl_CartTittle, javax.swing.GroupLayout.PREFERRED_SIZE, 411, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGap(0, 409, Short.MAX_VALUE)))
        );
        pnl_TopCartLayout.setVerticalGroup(
            pnl_TopCartLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnl_TopCartLayout.createSequentialGroup()
                .addGroup(pnl_TopCartLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btn_DeleteOne, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btn_DeleteAll, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(0, 6, Short.MAX_VALUE))
            .addGroup(pnl_TopCartLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(pnl_TopCartLayout.createSequentialGroup()
                    .addComponent(lbl_CartTittle, javax.swing.GroupLayout.PREFERRED_SIZE, 31, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGap(0, 0, Short.MAX_VALUE)))
        );

        pnl_Cart.add(pnl_TopCart, java.awt.BorderLayout.PAGE_START);

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
        lbl_LastTotalPrice_Value.setForeground(new java.awt.Color(255, 0, 51));
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
        cancel();
    }//GEN-LAST:event_btn_CancelActionPerformed

    private void btn_SearchRoomActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_SearchRoomActionPerformed
        searchEntityRoom();
    }//GEN-LAST:event_btn_SearchRoomActionPerformed

    private void btn_AddRoomActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_AddRoomActionPerformed
        addEntityRoomToCart();
    }//GEN-LAST:event_btn_AddRoomActionPerformed

    private void btn_AddCustomerActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_AddCustomerActionPerformed
        showFormAddCustomer();
    }//GEN-LAST:event_btn_AddCustomerActionPerformed

    private void txt_SearchCustomerMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_txt_SearchCustomerMouseClicked
        // TODO add your handling code here:
    }//GEN-LAST:event_txt_SearchCustomerMouseClicked

    private void btn_ClearActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_ClearActionPerformed
        clear();
    }//GEN-LAST:event_btn_ClearActionPerformed

    private void btn_AddServiceActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_AddServiceActionPerformed
        int selectedRow = table_Cart.getTable().getSelectedRow();
        if (selectedRow != -1) {
            showServicesDialog(selectedRow);
        } else {
            JOptionPane.showMessageDialog(this, "Vui lòng chọn phòng để thêm dịch vụ", "Thông báo", JOptionPane.WARNING_MESSAGE);
        }
    }//GEN-LAST:event_btn_AddServiceActionPerformed

    private void btn_UpdateServiceActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_UpdateServiceActionPerformed
        int selectedRow = table_Cart.getTable().getSelectedRow();
        if (selectedRow != -1 && reservationDetailsList.size() > 0) {
            showServicesDialogAfterClickUpdate(selectedRow, reservationDetailsList);
        } else if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Vui lòng chọn phòng để sửa dịch vụ", "Thông báo", JOptionPane.WARNING_MESSAGE);
        } else {
            JOptionPane.showMessageDialog(this, "Chưa có dịch vụ nào được thêm vào phòng này", "Thông báo", JOptionPane.WARNING_MESSAGE);
        }
    }//GEN-LAST:event_btn_UpdateServiceActionPerformed

    private void btn_DeleteOneActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_DeleteOneActionPerformed
        int selectedRow = table_Cart.getTable().getSelectedRow();
        if (selectedRow != -1) {
            int modelRow = table_Cart.getTable().convertRowIndexToModel(selectedRow);
            CustomTableButton.CustomTableModel model = table_Cart.getTableModel();
            model.removeRow(modelRow);
            updateSummaryTotals();
        } else {
            JOptionPane.showMessageDialog(this, "Vui lòng phòng trong hàng chờ để xóa", "Thông báo", JOptionPane.WARNING_MESSAGE);
        }
    }//GEN-LAST:event_btn_DeleteOneActionPerformed

    private void btn_DeleteAllActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_DeleteAllActionPerformed
        int confirm = JOptionPane.showConfirmDialog(this, "Bạn có chắc chắn muốn xóa tất cả phòng trong hàng chờ không?", "Xác nhận", JOptionPane.YES_NO_OPTION);
        if (confirm == JOptionPane.YES_OPTION) {
            CustomTableButton.CustomTableModel model = table_Cart.getTableModel();
            model.clearData();
            updateSummaryTotals();
        }
    }//GEN-LAST:event_btn_DeleteAllActionPerformed

    private void btn_BookingActionPerformed(java.awt.event.ActionEvent evt) throws RemoteException {//GEN-FIRST:event_btn_BookingActionPerformed
        bookingRoom();
    }//GEN-LAST:event_btn_BookingActionPerformed

    private void btn_SeeDetailsActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_SeeDetailsActionPerformed
        int seletedRow = table_EntityRoom.getTable().getSelectedRow();
        if (seletedRow != -1) {
            String roomID = table_EntityRoom.getTable().getValueAt(seletedRow, 1).toString();
            showViewDetailsRoom(roomID);
        } else {
            JOptionPane.showMessageDialog(this, "Vui lòng chọn phòng để xem chi tiết", "Thông báo", JOptionPane.WARNING_MESSAGE);
        }
    }//GEN-LAST:event_btn_SeeDetailsActionPerformed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private ui.components.button.ButtonCustom btn_AddCustomer;
    private ui.components.button.ButtonCustom btn_AddRoom;
    private ui.components.button.ButtonCustom btn_AddService;
    private ui.components.button.ButtonCustom btn_Booking;
    private ui.components.button.ButtonCancelCustom btn_Cancel;
    private ui.components.button.ButtonCancelCustom btn_Clear;
    private ui.components.button.ButtonCancelCustom btn_DeleteAll;
    private ui.components.button.ButtonCancelCustom btn_DeleteOne;
    private ui.components.button.ButtonCustom btn_KeyboardNumber;
    private ui.components.button.ButtonCustom btn_SearchRoom;
    private ui.components.button.ButtonCustom btn_SeeDetails;
    private ui.components.button.ButtonCustom btn_UpdateService;
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
    private javax.swing.JPanel pnl_BottomService;
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
    private javax.swing.JPanel pnl_TopCart;
    private javax.swing.JSpinner spinner_Capacity;
    private ui.components.table.CustomTableButton table_Cart;
    private ui.components.table.CustomTableButton table_EntityRoom;
    private ui.components.textfield.SearchTextField txt_SearchCustomer;
    // End of variables declaration//GEN-END:variables

    /**
     * Tìm kiếm phòng trống dựa trên các tiêu chí đã chọn.
     */
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


    /**
     * Tạo danh sách khoảng giá thông minh dựa trên danh sách phòng đã cho.
     */
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

    /**
     * Hiển thị danh sách phòng trong bảng.
     */
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

    /**
     * Phân tích chuỗi khoảng giá và trả về mảng chứa giá tối thiểu và tối đa.
     * @param priceRange Chuỗi khoảng giá.
     * @return Mảng chứa giá tối thiểu và tối đa.
     */
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

    /**
     * Lọc danh sách phòng theo loại phòng đã chọn.
     * @param roomType Loại phòng đã chọn.
     */
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

    /**
     * Tính toán số đêm dựa trên ngày check-in và check-out đã chọn.
     * @return Số đêm đã chọn.
     */
    private int calculateNumberOfNights() {
        Date checkInDate = calendar_Checkin.getSelectedDate();
        Date checkOutDate = calendar_Checout.getSelectedDate();
        if (checkInDate != null && checkOutDate != null) {
            long diffInMillis = checkOutDate.getTime() - checkInDate.getTime();
            return (int) Math.max(1, diffInMillis / (1000 * 60 * 60 * 24));
        }
        return 1; // Mặc định 1 đêm nếu chưa chọn ngày
    }

    /**
     * Kiểm tra xem phòng đã có trong giỏ hàng chưa.
     * @param roomId ID của phòng cần kiểm tra.
     * @return true nếu phòng đã có trong giỏ hàng, false nếu không.
     */
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

    /**
     * Thêm phòng vào giỏ hàng.
     * @param roomId ID của phòng cần thêm.
     * @param price Giá của phòng.
     * @param numberOfNights Số đêm đã chọn.
     */
    private void addToCart(String roomId, double price, int numberOfNights) {
        CustomTableButton.CustomTableModel cartModel = table_Cart.getTableModel();

        Object[] cartRow = {
                cartModel.getRowCount() + 1,
                roomId,
                String.format("%,.0f VND", price),
                numberOfNights,
                "",
                String.format("%,.0f VND", numberOfNights * price)
        };
        cartModel.addRow(cartRow, null);
    }

    /**
     * Thêm phòng vào giỏ hàng.
     */
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

    /**
     * Cập nhật tiền cho hàng trong giỏ hàng.
     */
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

    /**
     * Cập nhật tổng tiền trong giỏ hàng.
     */
    private void updateSummaryTotals() {
        try {
            double totalPrice = 0;
            CustomTableButton.CustomTableModel cartModel = table_Cart.getTableModel();

            for (int i = 0; i < cartModel.getRowCount(); i++) {
                Object[] rowData = cartModel.getRowData(i);
                double rowTotal = Double.parseDouble(((String) rowData[5]).replaceAll("[^\\d.]", ""));
                totalPrice += rowTotal;
            }

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

            // Update UI
            lbl_LastTotalPrice_Value.setText(String.format("%,.0f VND", totalPrice));
            lbl_Deposit_Value.setText(String.format("%,.0f VND", depositAmount));
            lbl_RemainingAmount_Value.setText(String.format("%,.0f VND", remainingAmount));

        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Đã xảy ra lỗi khi tính toán tổng giá: " + e.getMessage(),
                    "Lỗi", JOptionPane.ERROR_MESSAGE);
        }
    }

    /**
     * Tính toán số tiền đặt cọc dựa trên hình thức đặt phòng.
     * @param totalPrice Tổng tiền.
     * @param bookingMethod Hình thức đặt phòng.
     * @return Số tiền đặt cọc.
     */
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


    /**
     * Xóa tất cả thông tin trong form.
     */
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


    /**
     * Hiển thị hộp thoại chọn dịch vụ.
     * @param row Hàng cần cập nhật dịch vụ.
     */
    private void showServicesDialog(int row) {
        JDialog dialog = new JDialog();
        dialog.setTitle("Chọn dịch vụ");
        dialog.setSize(900, 620);
        dialog.setModal(true);
        dialog.setLayout(new BorderLayout());

        Object[] rowData = table_Cart.getTableModel().getRowData(row);
        if (rowData == null) {
            return;
        }

        String roomID = (String) rowData[1];

        Dialog_AddService addServiceDialog = new Dialog_AddService(roomID);
        dialog.add(addServiceDialog, BorderLayout.CENTER);

        dialog.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosed(WindowEvent e) {
                List<ReservationDetails> reservationDetails = addServiceDialog.getSelectedService();
                if (reservationDetails != null && !reservationDetails.isEmpty()) {
                    updateServicesInCart(row, reservationDetails);
                    listMapReservationDetails.put(roomID, reservationDetails);
                }
            }
        });

        dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
        dialog.setLocationRelativeTo(this);
        dialog.setVisible(true);
    }

    /**
     * Hiển thị hộp thoại chọn dịch vụ sau khi nhấn nút cập nhật.
     * @param row Hàng cần cập nhật dịch vụ.
     * @param listReservationDetails Danh sách dịch vụ đã chọn.
     */
    private void showServicesDialogAfterClickUpdate(int row, List<ReservationDetails> listReservationDetails) {
        JDialog dialog = new JDialog();
        dialog.setTitle("Cập nhật dịch vụ đã chọn");
        dialog.setSize(900, 620);
        dialog.setModal(true);
        dialog.setLayout(new BorderLayout());

        Object[] rowData = table_Cart.getTableModel().getRowData(row);
        if (rowData == null) {
            return;
        }

        String roomID = (String) rowData[1];

        Dialog_AddService addServiceDialog = new Dialog_AddService(roomID);
        dialog.add(addServiceDialog, BorderLayout.CENTER);
        addServiceDialog.setSelectedService(listReservationDetails);
        addServiceDialog.updateTableCartAfterClickUpdate(listReservationDetails);


        dialog.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosed(WindowEvent e) {
                List<ReservationDetails> reservationDetails = addServiceDialog.getSelectedService();
                if (reservationDetails != null && !reservationDetails.isEmpty()) {
                    reservationDetailsList.clear();
                    reservationDetailsList.addAll(reservationDetails);
                    listMapReservationDetails.put(roomID, reservationDetails);
                    updateServicesInCart(row, reservationDetails);
                }
            }
        });

        dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
        dialog.setLocationRelativeTo(this);
        dialog.setVisible(true);
    }


    /**
     * Cập nhật dịch vụ trong giỏ hàng.
     * @param row Hàng cần cập nhật.
     * @param reservationDetails Danh sách dịch vụ đã chọn.
     */
    private void updateServicesInCart(int row, List<ReservationDetails> reservationDetails) {
        CustomTableButton.CustomTableModel model = table_Cart.getTableModel();
        Object[] rowData = model.getRowData(row);

        StringBuilder servicesText = new StringBuilder();
        double servicesTotal = 0;

        for (ReservationDetails detail : reservationDetails) {
            if (servicesText.length() > 0) {
                servicesText.append(", ");
            }
            servicesText.append(detail.getService().getName())
                    .append(" (x")
                    .append(detail.getQuantity())
                    .append(")");
            servicesTotal += detail.getService().getPrice() * detail.getQuantity();

            reservationDetailsList.add(detail);
        }

        rowData[4] = servicesText.toString();

        rowData[5] = String.format("%,.0f VND",
                (Double.parseDouble(((String) rowData[2]).replaceAll("[^\\d.]", "")) * (int) rowData[3]) + servicesTotal);

        model.fireTableRowsUpdated(row, row);
        updateSummaryTotals();
    }

    /**
     * Đặt phòng.
     * @throws RemoteException
     * @throws IllegalStateException
     *
     */
    private void bookingRoom() throws RemoteException {
        // Validate input
        int numOfCartRows = table_Cart.getTableModel().getRowCount();
        if (numOfCartRows == 0) {
            JOptionPane.showMessageDialog(this, "Vui lòng thêm phòng vào giỏ hàng trước khi đặt phòng",
                    "Thông báo", JOptionPane.WARNING_MESSAGE);
            return;
        }

        String bookingMethod = (String) cbx_BookingMethod.getSelectedItem();
        if (bookingMethod == null || bookingMethod.equals("Chọn hình thức đặt phòng")) {
            JOptionPane.showMessageDialog(this, "Vui lòng chọn hình thức đặt phòng",
                    "Thông báo", JOptionPane.WARNING_MESSAGE);
            return;
        }

        String customerName = lbl_CustomerName_Value.getText();
        String customerPhone = lbl_CustomerPhone_Value.getText();
        if (customerName == null || customerName.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Vui lòng chọn khách hàng",
                    "Thông báo", JOptionPane.WARNING_MESSAGE);
            return;
        }

        Customer customer = customerDAO.getCustomerByPhone(customerPhone);
        if (customer == null) {
            JOptionPane.showMessageDialog(this, "Không tìm thấy thông tin khách hàng",
                    "Thông báo", JOptionPane.ERROR_MESSAGE);
            return;
        }

        Date checkInDate = calendar_Checkin.getSelectedDate();
        Date checkOutDate = calendar_Checout.getSelectedDate();
        if (checkInDate == null || checkOutDate == null) {
            JOptionPane.showMessageDialog(this, "Vui lòng chọn ngày check-in và check-out",
                    "Thông báo", JOptionPane.WARNING_MESSAGE);
            return;
        }

        // Start transaction
        EntityManager em = AppUtil.getEntityManager();
        EntityTransaction transaction = em.getTransaction();

        try {
            transaction.begin();

            CustomTableButton.CustomTableModel model = table_Cart.getTableModel();
            int countBooking = 0;

            for (int row = 0; row < numOfCartRows; row++) {
                Object[] rowData = model.getRowData(row);
                String roomID = (String) rowData[1];
                int numberOfNights = (int) rowData[3];

                String reservationId = GenerateString.generateReservationId();
                if (reservationId == null || reservationId.isEmpty()) {
                    throw new IllegalStateException("Không thể tạo ID đặt phòng");
                }

                Reservation reservation = new Reservation();
                reservation.setReservationId(reservationId);

                Room room = em.find(Room.class, roomID);
                if (room == null) {
                    throw new IllegalStateException("Không tìm thấy phòng với ID: " + roomID);
                }
                reservation.setRoom(room);

                reservation.setCustomer(customer);
                reservation.setNumberOfNights(numberOfNights);
                reservation.setBookingDate(new Date());
                reservation.setCheckInDate(checkInDate);
                reservation.setCheckOutDate(checkOutDate);

                reservation.setBookingMethod(bookingMethod.equals("Tại quầy") ?
                        BookingMethod.AT_THE_COUNTER : BookingMethod.CONTACT);

                reservation.setStatus(false);

                if (listMapReservationDetails.containsKey(roomID)) {
                    List<ReservationDetails> details = listMapReservationDetails.get(roomID);
                    for (ReservationDetails detail : details) {
                        detail.setReservation(reservation);
                    }
                    reservation.setReservationDetails(details);
                }

                reservation.calculateTotalPrice();
                reservation.calculateDepositAmount();
                reservation.calculateRemainingAmount();
                em.persist(reservation);
                countBooking++;
            }

            transaction.commit();

            if (countBooking == numOfCartRows) {
                JOptionPane.showMessageDialog(this, "Đặt phòng thành công",
                        "Thông báo", JOptionPane.INFORMATION_MESSAGE);
                clear();
            } else {
                JOptionPane.showMessageDialog(this, "Có lỗi xảy ra khi đặt phòng",
                        "Thông báo", JOptionPane.ERROR_MESSAGE);
            }
        } catch (Exception e) {
            if (transaction != null && transaction.isActive()) {
                transaction.rollback();
            }
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Đã xảy ra lỗi khi đặt phòng: " + e.getMessage(),
                    "Lỗi", JOptionPane.ERROR_MESSAGE);
        } finally {
            if (em != null && em.isOpen()) {
                em.close();
            }
        }
    }

    private void showFormAddCustomer() {
        JDialog dialog = new JDialog();
        dialog.setTitle("Thêm khách hàng");
        dialog.setSize(750, 400);
        dialog.setModal(true);
        dialog.setLayout(new BorderLayout());

        Dialog_AddCustomer addCustomerDialog = new Dialog_AddCustomer();
        dialog.add(addCustomerDialog, BorderLayout.CENTER);

        dialog.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosed(WindowEvent e) {
                Customer customer = addCustomerDialog.getCustomer();
                if (customer != null) {
                    showInforCustomerAfterAdd(customer);
                }
            }
        });

        dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
        dialog.setLocationRelativeTo(this);
        dialog.setVisible(true);
    }

    private void showInforCustomerAfterAdd(Customer customer) {
        if (customer != null) {
            lbl_CustomerName_Value.setText(customer.getFirstName() + " " + customer.getLastName());
            lbl_CustomerPhone_Value.setText(customer.getPhoneNumber());
            lbl_CustomerCCCD_Value.setText(customer.getCCCD());
            lbl_CustomerGender_Value.setText(customer.isGender() == true ? "Nam" : "Nữ");
        } else {
            JOptionPane.showMessageDialog(this, "Không tìm thấy thông tin khách hàng",
                    "Thông báo", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void showViewDetailsRoom(String roomID) {
        JDialog dialog = new JDialog();
        dialog.setTitle("Chi tiết phòng");
        dialog.setSize(800, 500);
        dialog.setModal(true);
        dialog.setLayout(new BorderLayout());

        Dialog_ViewRoomDetails viewDetailsRoomDialog = new Dialog_ViewRoomDetails(roomID);
        dialog.add(viewDetailsRoomDialog, BorderLayout.CENTER);

        dialog.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosed(WindowEvent e) {
                Room room = viewDetailsRoomDialog.getRoom();
                if (room != null) {
                    int numberOfNights = calculateNumberOfNights();
                    String roomID = room.getRoomId();
                    double price = room.getPrice();
                    if (isRoomAlreadyInCart(roomID)) {
                        JOptionPane.showMessageDialog(dialog, "Phòng này đã có trong giỏ hàng",
                                "Thông báo", JOptionPane.WARNING_MESSAGE);
                        return;
                    }
                    addToCart(roomID, price, numberOfNights);
                    updateSummaryTotals();
                }
            }
        });

        dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
        dialog.setLocationRelativeTo(this);
        dialog.setVisible(true);
    }

    private void cancel() {
        int confirm = JOptionPane.showConfirmDialog(this, "Bạn có chắc chắn muốn hủy không?", "Xác nhận", JOptionPane.YES_NO_OPTION);
        if (confirm == JOptionPane.YES_OPTION) {
            table_Cart.getTableModel().clearData();
            table_EntityRoom.getTableModel().clearData();
            lbl_CustomerName_Value.setText("");
            lbl_CustomerPhone_Value.setText("");
            lbl_CustomerCCCD_Value.setText("");
            lbl_CustomerGender_Value.setText("");
            clear();
        }
    }
}
