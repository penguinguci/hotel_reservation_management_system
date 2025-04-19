package ui.tabs;

import dao.RoomTypeDAOImpl;
import dao.ServicesDAOImpl;
import entities.RoomType;
import entities.Service;
import interfaces.RoomTypesDAO;
import interfaces.ServicesDAO;
import jakarta.xml.bind.annotation.XmlType;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.geom.RoundRectangle2D;
import java.util.List;

/**
 *
 * @author Lenovo
 */
public class Tab_ServicesManagement extends javax.swing.JFrame {

    /**
     * Creates new form Tab_ServicesManagement
     */
    private ServicesDAO serviceDAO;
    private Service selectedService;
    private DefaultTableModel tableModel;

    public Tab_ServicesManagement() {
        serviceDAO = new ServicesDAOImpl();
        initComponents();
        // Đặt DefaultTableModel cho customTableButton1
        String[] columnNames = {"Mã dịch vụ", "Tên dịch vụ", "Mô tả", "Giá", "Đang hoạt động"};
        tableModel = new DefaultTableModel(columnNames, 0);
        customTableButton1.getTable().setModel(tableModel);
        // Thêm các giá trị cho ComboBox
        styledComboBox1.addItem("Có");
        styledComboBox1.addItem("Không");
        setBackground(new Color(255, 255, 255));
        setLocationRelativeTo(null);
        loadServices();
        setupListeners();
    }

    private void loadServices() {
        List<Service> services = serviceDAO.getAllServices();
        tableModel.setRowCount(0); // Xóa dữ liệu cũ
        for (Service service : services) {
            tableModel.addRow(new Object[]{
                    service.getServiceId(),
                    service.getName(),
                    service.getDescription(),
                    service.getPrice(),
                    service.isAvailability() ? "Có" : "Không"
            });
        }
        tableModel.fireTableDataChanged();
        customTableButton1.repaint();
        customTableButton1.revalidate();
    }

    private void setupListeners() {
        customTableButton1.getTable().getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                int selectedRow = customTableButton1.getTable().getSelectedRow();
                if (selectedRow >= 0) {
                    int serviceId = (int) customTableButton1.getTable().getValueAt(selectedRow, 0);
                    selectedService = serviceDAO.findServiceByID(serviceId);
                    if (selectedService != null) {
                        customRoundedTextField1.setText(String.valueOf(selectedService.getServiceId()));
                        customRoundedTextField2.setText(selectedService.getName());
                        customRoundedTextField3.setText(String.valueOf(selectedService.getPrice()));
                        jTextArea1.setText(selectedService.getDescription());
                        styledComboBox1.setSelectedItem(selectedService.isAvailability() ? "Có" : "Không");
                    }
                } else {
                    clearFields();
                }
            }
        });
    }

    private void clearFields() {
        customRoundedTextField1.setText("");
        customRoundedTextField2.setText("");
        customRoundedTextField3.setText("");
        jTextArea1.setText("");
        styledComboBox1.setSelectedIndex(0);
        selectedService = null;
        customTableButton1.getTable().clearSelection();
    }

    private int generateNewServiceId() {
        List<Service> services = serviceDAO.getAllServices();
        int maxId = 0;

        // Tìm mã dịch vụ lớn nhất hiện có
        for (Service service : services) {
            int serviceId = service.getServiceId();
            maxId = Math.max(maxId, serviceId);
        }

        // Tăng mã lên 1 để có mã mới
        return maxId + 1;
    }

    // Tìm kiếm dịch vụ theo tên
    private void searchServices() {
        String keyword = customRoundedTextFieldSearch.getText().trim();
        if (keyword.isEmpty()) {
            loadServices(); // Nếu không có từ khóa, hiển thị toàn bộ dịch vụ
            return;
        }

        List<Service> services = serviceDAO.searchServicesByName(keyword);
        tableModel.setRowCount(0); // Xóa dữ liệu cũ
        if (services.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Không tìm thấy dịch vụ nào với từ khóa: " + keyword, "Thông báo", JOptionPane.INFORMATION_MESSAGE);
        } else {
            for (Service service : services) {
                tableModel.addRow(new Object[]{
                        service.getServiceId(),
                        service.getName(),
                        service.getDescription(),
                        service.getPrice(),
                        service.isAvailability() ? "Có" : "Không"
                });
            }
        }
        tableModel.fireTableDataChanged();
        customTableButton1.repaint();
        customTableButton1.revalidate();
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">
    private void initComponents() {

        customTableButtonBeanInfo1 = new ui.components.table.CustomTableButtonBeanInfo();
        jPanel6 = new javax.swing.JPanel();
        jPanel2 = new javax.swing.JPanel();
        jPanel3 = new javax.swing.JPanel();
        customRoundedTextField1 = new ui.components.textfield.CustomRoundedTextField();
        customRoundedTextField1.setEnabled(false);
        jLabel1 = new javax.swing.JLabel();
        jPanel4 = new javax.swing.JPanel();
        customRoundedTextField2 = new ui.components.textfield.CustomRoundedTextField();
        jLabel2 = new javax.swing.JLabel();
        jPanel5 = new javax.swing.JPanel();
        jLabel3 = new javax.swing.JLabel();
        jScrollPane2 = new javax.swing.JScrollPane();
        jTextArea1 = new javax.swing.JTextArea();
        jLabel6 = new javax.swing.JLabel();
        styledComboBox1 = new ui.components.combobox.StyledComboBox();
        btnAddType = new ui.components.button.ButtonCustom();
        btnUpdateType = new ui.components.button.ButtonCustom();
        btnResetType = new ui.components.button.ButtonCustom();
        btnExit = new ui.components.button.ButtonCancelCustom();
        jLabel5 = new javax.swing.JLabel();
        customRoundedTextField3 = new ui.components.textfield.CustomRoundedTextField();
        customTableButton1 = new ui.components.table.CustomTableButton();
        jLabel4 = new javax.swing.JLabel();
        jPanelSearch = new javax.swing.JPanel();
        jLabelSearch = new javax.swing.JLabel();
        customRoundedTextFieldSearch = new ui.components.textfield.CustomRoundedTextField();
        btnSearch = new ui.components.button.ButtonCustom();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("Quản lý dịch vụ");
        setBackground(new java.awt.Color(255, 255, 255));
        setBounds(new java.awt.Rectangle(0, 0, 850, 510));
        setForeground(new java.awt.Color(0, 0, 0));
        setUndecorated(true);
        setResizable(false);
        getContentPane().setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jPanel6.setBackground(new java.awt.Color(255, 255, 255));
        jPanel6.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(149, 145, 239), 2));

        jPanel2.setBackground(new java.awt.Color(255, 255, 255));
        jPanel2.setBorder(javax.swing.BorderFactory.createMatteBorder(1, 1, 1, 1, new java.awt.Color(149, 145, 239)));

        jPanel3.setBackground(new java.awt.Color(255, 255, 255));

        customRoundedTextField1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                customRoundedTextField1ActionPerformed(evt);
            }
        });

        jLabel1.setText("Mã dịch vụ:");

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
                jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel3Layout.createSequentialGroup()
                                .addGap(40, 40, 40)
                                .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 64, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(customRoundedTextField1, javax.swing.GroupLayout.PREFERRED_SIZE, 234, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel3Layout.setVerticalGroup(
                jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(jPanel3Layout.createSequentialGroup()
                                .addContainerGap()
                                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                        .addComponent(customRoundedTextField1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addComponent(jLabel1))
                                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jPanel4.setBackground(new java.awt.Color(255, 255, 255));

        customRoundedTextField2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                customRoundedTextField2ActionPerformed(evt);
            }
        });

        jLabel2.setText("Tên dịch vụ:");

        javax.swing.GroupLayout jPanel4Layout = new javax.swing.GroupLayout(jPanel4);
        jPanel4.setLayout(jPanel4Layout);
        jPanel4Layout.setHorizontalGroup(
                jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel4Layout.createSequentialGroup()
                                .addGap(40, 40, 40)
                                .addComponent(jLabel2, javax.swing.GroupLayout.PREFERRED_SIZE, 64, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(customRoundedTextField2, javax.swing.GroupLayout.PREFERRED_SIZE, 234, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel4Layout.setVerticalGroup(
                jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(jPanel4Layout.createSequentialGroup()
                                .addContainerGap()
                                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                        .addComponent(customRoundedTextField2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addComponent(jLabel2))
                                .addContainerGap(22, Short.MAX_VALUE))
        );

        jPanel5.setBackground(new java.awt.Color(255, 255, 255));

        jLabel3.setText("Mô tả");

        jTextArea1.setColumns(20);
        jTextArea1.setRows(5);
        jScrollPane2.setViewportView(jTextArea1);

        jLabel6.setText("Khả dụng:");

        javax.swing.GroupLayout jPanel5Layout = new javax.swing.GroupLayout(jPanel5);
        jPanel5.setLayout(jPanel5Layout);
        jPanel5Layout.setHorizontalGroup(
                jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel5Layout.createSequentialGroup()
                                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                        .addComponent(jLabel3, javax.swing.GroupLayout.PREFERRED_SIZE, 64, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addComponent(jLabel6))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                        .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 232, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addComponent(styledComboBox1, javax.swing.GroupLayout.PREFERRED_SIZE, 232, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addContainerGap())
        );
        jPanel5Layout.setVerticalGroup(
                jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel5Layout.createSequentialGroup()
                                .addGap(21, 21, 21)
                                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                        .addComponent(styledComboBox1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addComponent(jLabel6, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 26, Short.MAX_VALUE)
                                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                        .addComponent(jLabel3)
                                        .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 55, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addGap(22, 22, 22))
        );

        btnAddType.setIcon(new javax.swing.ImageIcon(getClass().getResource("/plus.png")));
        btnAddType.setText("Thêm");
        btnAddType.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnAddTypeActionPerformed(evt);
            }
        });

        btnUpdateType.setIcon(new javax.swing.ImageIcon(getClass().getResource("/update.png")));
        btnUpdateType.setText("Cập nhật");
        btnUpdateType.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnUpdateTypeActionPerformed(evt);
            }
        });

        btnResetType.setIcon(new javax.swing.ImageIcon(getClass().getResource("/update.png")));
        btnResetType.setText("Làm mới");
        btnResetType.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnResetTypeActionPerformed(evt);
            }
        });

        btnExit.setText("Thoát");
        btnExit.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnExitActionPerformed(evt);
            }
        });

        jLabel5.setText("Giá:");

        customRoundedTextField3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                customRoundedTextField3ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
                jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(jPanel2Layout.createSequentialGroup()
                                .addContainerGap()
                                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                        .addComponent(jPanel3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                        .addComponent(jPanel4, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                        .addComponent(jPanel5, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                        .addGroup(jPanel2Layout.createSequentialGroup()
                                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                        .addGroup(jPanel2Layout.createSequentialGroup()
                                                .addGap(23, 23, 23)
                                                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                                        .addComponent(btnAddType, javax.swing.GroupLayout.PREFERRED_SIZE, 140, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                        .addComponent(btnResetType, javax.swing.GroupLayout.PREFERRED_SIZE, 140, javax.swing.GroupLayout.PREFERRED_SIZE))
                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                                        .addComponent(btnExit, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 140, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                        .addComponent(btnUpdateType, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 140, javax.swing.GroupLayout.PREFERRED_SIZE)))
                                        .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createSequentialGroup()
                                                .addGap(0, 0, Short.MAX_VALUE)
                                                .addComponent(jLabel5, javax.swing.GroupLayout.PREFERRED_SIZE, 64, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                                .addComponent(customRoundedTextField3, javax.swing.GroupLayout.PREFERRED_SIZE, 234, javax.swing.GroupLayout.PREFERRED_SIZE)))
                                .addContainerGap())
        );
        jPanel2Layout.setVerticalGroup(
                jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(jPanel2Layout.createSequentialGroup()
                                .addGap(16, 16, 16)
                                .addComponent(jPanel3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(16, 16, 16)
                                .addComponent(jPanel4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                        .addComponent(customRoundedTextField3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addComponent(jLabel5))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jPanel5, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                        .addComponent(btnAddType, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addComponent(btnUpdateType, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                        .addComponent(btnResetType, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                        .addComponent(btnExit, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addGap(40, 40, 40))
        );

        customTableButton1.setColumnNames(new String[] {"Mã dịch vụ", "Tên dịch vụ", "Mô tả", "Giá", "Đang hoạt động"});
        customTableButton1.setHeaderBackgroundColor(new java.awt.Color(149, 145, 239));

        jLabel4.setBackground(new java.awt.Color(149, 145, 239));
        jLabel4.setFont(new java.awt.Font("Segoe UI", 1, 14));
        jLabel4.setForeground(new java.awt.Color(149, 145, 239));
        jLabel4.setText("QUẢN LÝ DỊCH VỤ");

        // Thêm panel tìm kiếm
        jPanelSearch.setBackground(new java.awt.Color(255, 255, 255));

        jLabelSearch.setText("Tìm kiếm:");

        customRoundedTextFieldSearch.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                customRoundedTextFieldSearchActionPerformed(evt);
            }
        });

        btnSearch.setIcon(new javax.swing.ImageIcon(getClass().getResource("/search.png")));
        btnSearch.setText("Tìm kiếm");
        btnSearch.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnSearchActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanelSearchLayout = new javax.swing.GroupLayout(jPanelSearch);
        jPanelSearch.setLayout(jPanelSearchLayout);
        jPanelSearchLayout.setHorizontalGroup(
                jPanelSearchLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(jPanelSearchLayout.createSequentialGroup()
                                .addGap(40, 40, 40)
                                .addComponent(jLabelSearch, javax.swing.GroupLayout.PREFERRED_SIZE, 64, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(customRoundedTextFieldSearch, javax.swing.GroupLayout.PREFERRED_SIZE, 234, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(btnSearch, javax.swing.GroupLayout.PREFERRED_SIZE, 120, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanelSearchLayout.setVerticalGroup(
                jPanelSearchLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(jPanelSearchLayout.createSequentialGroup()
                                .addContainerGap()
                                .addGroup(jPanelSearchLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                        .addComponent(jLabelSearch)
                                        .addComponent(customRoundedTextFieldSearch, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addComponent(btnSearch, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout jPanel6Layout = new javax.swing.GroupLayout(jPanel6);
        jPanel6.setLayout(jPanel6Layout);
        jPanel6Layout.setHorizontalGroup(
                jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(jPanel6Layout.createSequentialGroup()
                                .addContainerGap()
                                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                        .addComponent(customTableButton1, javax.swing.GroupLayout.DEFAULT_SIZE, 475, Short.MAX_VALUE)
                                        .addComponent(jPanelSearch, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                        .addGroup(jPanel6Layout.createSequentialGroup()
                                                .addGap(3, 3, 3)
                                                .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                                        .addGroup(jPanel6Layout.createSequentialGroup()
                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                                .addComponent(jLabel4)
                                                .addGap(117, 117, 117))))
        );
        jPanel6Layout.setVerticalGroup(
                jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(jPanel6Layout.createSequentialGroup()
                                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                        .addGroup(jPanel6Layout.createSequentialGroup()
                                                .addGap(9, 9, 9)
                                                .addComponent(jLabel4, javax.swing.GroupLayout.PREFERRED_SIZE, 28, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                                .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE))
                                        .addGroup(jPanel6Layout.createSequentialGroup()
                                                .addComponent(jPanelSearch, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                                .addComponent(customTableButton1, javax.swing.GroupLayout.DEFAULT_SIZE, 450, Short.MAX_VALUE)))
                                .addContainerGap())
        );

        getContentPane().add(jPanel6, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 850, 510));

        pack();
    }

    private void customRoundedTextField1ActionPerformed(java.awt.event.ActionEvent evt) {
    }

    private void customRoundedTextField2ActionPerformed(java.awt.event.ActionEvent evt) {
    }

    private void btnAddTypeActionPerformed(java.awt.event.ActionEvent evt) {
        int serviceId = generateNewServiceId();
        customRoundedTextField1.setText(String.valueOf(serviceId));

        String name = customRoundedTextField2.getText().trim();
        String priceStr = customRoundedTextField3.getText().trim();
        String description = jTextArea1.getText().trim();
        String availabilityStr = (String) styledComboBox1.getSelectedItem();

        if (name.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Tên dịch vụ không được để trống!", "Lỗi", JOptionPane.ERROR_MESSAGE);
            return;
        }

        double price;
        try {
            price = Double.parseDouble(priceStr);
            if (price < 0) {
                JOptionPane.showMessageDialog(this, "Giá phải là số không âm!", "Lỗi", JOptionPane.ERROR_MESSAGE);
                return;
            }
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Giá phải là một số hợp lệ!", "Lỗi", JOptionPane.ERROR_MESSAGE);
            return;
        }

        if (serviceDAO.findServiceByID(serviceId) != null) {
            JOptionPane.showMessageDialog(this, "Mã dịch vụ đã tồn tại!", "Lỗi", JOptionPane.ERROR_MESSAGE);
            return;
        }

        Service newService = new Service();
        newService.setServiceId(serviceId);
        newService.setName(name);
        newService.setPrice(price);
        newService.setDescription(description);
        newService.setAvailability(availabilityStr.equals("Có"));

        try {
            serviceDAO.create(newService);
            JOptionPane.showMessageDialog(this, "Thêm dịch vụ thành công!");
            loadServices();
            clearFields();
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Lỗi khi thêm dịch vụ: " + e.getMessage(), "Lỗi", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void btnUpdateTypeActionPerformed(java.awt.event.ActionEvent evt) {
        if (selectedService == null) {
            JOptionPane.showMessageDialog(this, "Vui lòng chọn một dịch vụ để cập nhật!", "Lỗi", JOptionPane.ERROR_MESSAGE);
            return;
        }

        String serviceIdStr = customRoundedTextField1.getText().trim();
        String name = customRoundedTextField2.getText().trim();
        String priceStr = customRoundedTextField3.getText().trim();
        String description = jTextArea1.getText().trim();
        String availabilityStr = (String) styledComboBox1.getSelectedItem();

        if (name.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Tên dịch vụ không được để trống!", "Lỗi", JOptionPane.ERROR_MESSAGE);
            return;
        }

        double price;
        try {
            price = Double.parseDouble(priceStr);
            if (price < 0) {
                JOptionPane.showMessageDialog(this, "Giá phải là số không âm!", "Lỗi", JOptionPane.ERROR_MESSAGE);
                return;
            }
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Giá phải là một số hợp lệ!", "Lỗi", JOptionPane.ERROR_MESSAGE);
            return;
        }

        int serviceId;
        try {
            serviceId = Integer.parseInt(serviceIdStr);
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Mã dịch vụ không hợp lệ!", "Lỗi", JOptionPane.ERROR_MESSAGE);
            return;
        }

        if (serviceId != selectedService.getServiceId()) {
            JOptionPane.showMessageDialog(this, "Không thể thay đổi mã dịch vụ!", "Lỗi", JOptionPane.ERROR_MESSAGE);
            return;
        }

        selectedService.setName(name);
        selectedService.setPrice(price);
        selectedService.setDescription(description);
        selectedService.setAvailability(availabilityStr.equals("Có"));

        try {
            serviceDAO.update(selectedService);
            JOptionPane.showMessageDialog(this, "Cập nhật dịch vụ thành công!");
            loadServices();
            clearFields();
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Lỗi khi cập nhật dịch vụ: " + e.getMessage(), "Lỗi", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void btnResetTypeActionPerformed(java.awt.event.ActionEvent evt) {
        clearFields();
        loadServices();
        customRoundedTextFieldSearch.setText(""); // Xóa trường tìm kiếm khi làm mới
    }

    private void btnExitActionPerformed(java.awt.event.ActionEvent evt) {
        dispose();
    }

    private void customRoundedTextField3ActionPerformed(java.awt.event.ActionEvent evt) {
    }

    private void customRoundedTextFieldSearchActionPerformed(java.awt.event.ActionEvent evt) {
        searchServices(); // Tìm kiếm khi nhấn Enter trong trường tìm kiếm
    }

    private void btnSearchActionPerformed(java.awt.event.ActionEvent evt) {
        searchServices(); // Tìm kiếm khi nhấn nút "Tìm kiếm"
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(Tab_RoomTypeManagement.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(Tab_RoomTypeManagement.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(Tab_RoomTypeManagement.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(Tab_RoomTypeManagement.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }

        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new Tab_ServicesManagement().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify
    private ui.components.button.ButtonCustom btnAddType;
    private ui.components.button.ButtonCancelCustom btnExit;
    private ui.components.button.ButtonCustom btnResetType;
    private ui.components.button.ButtonCustom btnUpdateType;
    private ui.components.button.ButtonCustom btnSearch;
    private ui.components.textfield.CustomRoundedTextField customRoundedTextField1;
    private ui.components.textfield.CustomRoundedTextField customRoundedTextField2;
    private ui.components.textfield.CustomRoundedTextField customRoundedTextField3;
    private ui.components.textfield.CustomRoundedTextField customRoundedTextFieldSearch;
    private ui.components.table.CustomTableButton customTableButton1;
    private ui.components.table.CustomTableButtonBeanInfo customTableButtonBeanInfo1;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabelSearch;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JPanel jPanel5;
    private javax.swing.JPanel jPanel6;
    private javax.swing.JPanel jPanelSearch;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JTextArea jTextArea1;
    private ui.components.combobox.StyledComboBox styledComboBox1;
    // End of variables declaration
}