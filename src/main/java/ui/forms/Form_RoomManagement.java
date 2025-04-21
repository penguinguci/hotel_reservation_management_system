package ui.forms;

import dao.*;
import entities.*;
import interfaces.*;
import jakarta.persistence.EntityManagerFactory;
import ui.components.button.ButtonCustom;
import ui.components.combobox.StyledComboBox;
import ui.components.table.CustomTable;
import ui.components.textfield.CustomRoundedTextField;
import ui.tabs.Tab_ServicesManagement;
import ui.tabs.Tab_RoomTypeManagement;

import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.util.*;
import java.util.List;

import static com.twelvemonkeys.lang.StringUtil.valueOf;

/**
 * @author Lenovo
 */
public class Form_RoomManagement extends JPanel {

    private RoomDAO roomDAO = new RoomDAOImpl();
    private RoomTypesDAO roomTypeDAO = new RoomTypeDAOImpl();
    EntityManagerFactory entityManagerFactory = null;
    private Room selectedRoom;
    Tab_RoomTypeManagement tabRTM = new Tab_RoomTypeManagement();
    Tab_ServicesManagement tabSM = new Tab_ServicesManagement();

    public Form_RoomManagement() {
        initComponents();
        initializeTableModels();
        loadRoomTypes();
        loadRoomData();
        loadAmenityData();
        setupListeners();
    }

    @SuppressWarnings("unchecked")
    private void initComponents() {
        pnl_North = new javax.swing.JPanel();
        lbl_Title = new javax.swing.JLabel();
        pnl_Center = new javax.swing.JPanel();
        pnl_Left = new javax.swing.JPanel();
        pInfo = new javax.swing.JPanel();
        jLabel5 = new javax.swing.JLabel();
        txtRoomName = new ui.components.textfield.CustomRoundedTextField();
        jLabel6 = new javax.swing.JLabel();
        txtPrice = new ui.components.textfield.CustomRoundedTextField();
        jLabel12 = new javax.swing.JLabel();
        txtHourlyPrice = new ui.components.textfield.CustomRoundedTextField();
        jLabel7 = new javax.swing.JLabel();
        txtPosition = new ui.components.textfield.CustomRoundedTextField();
        jLabel8 = new javax.swing.JLabel();
        cb_RoomTypeSearch = new ui.components.combobox.StyledComboBox();
        btnAdd = new ui.components.button.ButtonCustom();
        btnUpdate = new ui.components.button.ButtonCustom();
        jLabel9 = new javax.swing.JLabel();
        txtNumOfPeople = new ui.components.textfield.CustomRoundedTextField();
        btnReset = new ui.components.button.ButtonCustom();
//        jLabel11 = new javax.swing.JLabel();
//        txtPrice1 = new ui.components.textfield.CustomRoundedTextField();
        pFind = new javax.swing.JPanel();
        jPanel1 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        cb_Price = new ui.components.combobox.StyledComboBox();
        jPanel2 = new javax.swing.JPanel();
        jLabel2 = new javax.swing.JLabel();
        cb_Position = new ui.components.combobox.StyledComboBox();
        jPanel3 = new javax.swing.JPanel();
        jLabel3 = new javax.swing.JLabel();
        cb_RoomType = new ui.components.combobox.StyledComboBox();
        jPanel4 = new javax.swing.JPanel();
        jLabel4 = new javax.swing.JLabel();
        cb_Status = new ui.components.combobox.StyledComboBox();
        pAmentities = new javax.swing.JPanel();
        jScrollPane4 = new javax.swing.JScrollPane();
        customTable1 = new ui.components.table.CustomTable();
        jPanel5 = new javax.swing.JPanel();
        jScrollPane3 = new javax.swing.JScrollPane();
        jTextArea1 = new javax.swing.JTextArea();
        jLabel10 = new javax.swing.JLabel();
        btnAddAmentity = new ui.components.button.ButtonCustom();
        btnUpdateAmentity = new ui.components.button.ButtonCustom();
        btnResetAmentity = new ui.components.button.ButtonCustom();
        jPanel6 = new javax.swing.JPanel();
        btnManageRoomType = new ui.components.button.ButtonCustom();
        btnManageService = new ui.components.button.ButtonCustom();
        jScrollPane2 = new javax.swing.JScrollPane();
        customTable = new ui.components.table.CustomTable();

        setBackground(new java.awt.Color(255, 255, 255));
        setLayout(new java.awt.BorderLayout());

        pnl_North.setBackground(new java.awt.Color(255, 255, 255));
        pnl_North.setLayout(new java.awt.BorderLayout());

        lbl_Title.setBackground(new java.awt.Color(255, 255, 255));
        lbl_Title.setFont(new java.awt.Font("Segoe UI", 1, 16));
        lbl_Title.setForeground(new java.awt.Color(127, 122, 239));
        lbl_Title.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lbl_Title.setText(" QUẢN LÝ PHÒNG");
        pnl_North.add(lbl_Title, java.awt.BorderLayout.CENTER);

        add(pnl_North, java.awt.BorderLayout.PAGE_START);

        pnl_Center.setLayout(new java.awt.BorderLayout());

        pnl_Left.setBackground(new java.awt.Color(255, 255, 255));
        pnl_Left.setLayout(null);

        pInfo.setBackground(new java.awt.Color(255, 255, 255));
        pInfo.setBorder(javax.swing.BorderFactory.createTitledBorder("Thông tin phòng"));

        jLabel5.setText("Phòng");

        txtRoomName.setEditable(false);
        txtRoomName.setDisabledTextColor(new java.awt.Color(255, 255, 255));
        txtRoomName.setEnabled(false);
        txtRoomName.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtRoomNameActionPerformed(evt);
            }
        });

        jLabel6.setText("Giá theo đêm:");

        jLabel12.setText("Giá theo giờ:");

        jLabel7.setText("Tầng");

        jLabel8.setText("Loại phòng:");

        btnAdd.setText("Thêm");
        btnAdd.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnAddActionPerformed(evt);
            }
        });

        btnUpdate.setText("Cập nhật");
        btnUpdate.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnUpdateActionPerformed(evt);
            }
        });

        jLabel9.setText("Số người:");

        btnReset.setText("Làm mới");
        btnReset.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnResetActionPerformed(evt);
            }
        });


        javax.swing.GroupLayout pInfoLayout = new javax.swing.GroupLayout(pInfo);
        pInfo.setLayout(pInfoLayout);
        pInfoLayout.setHorizontalGroup(
                pInfoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(pInfoLayout.createSequentialGroup()
                                .addGroup(pInfoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                        .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, pInfoLayout.createSequentialGroup()
                                                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                                .addGroup(pInfoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                                        .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, pInfoLayout.createSequentialGroup()
                                                                .addComponent(btnAdd, javax.swing.GroupLayout.PREFERRED_SIZE, 113, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                                .addGap(18, 18, 18)
                                                                .addComponent(btnReset, javax.swing.GroupLayout.PREFERRED_SIZE, 107, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                                .addGap(18, 18, 18)
                                                                .addComponent(btnUpdate, javax.swing.GroupLayout.PREFERRED_SIZE, 116, javax.swing.GroupLayout.PREFERRED_SIZE))
                                                        .addComponent(txtNumOfPeople, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 257, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                        .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, pInfoLayout.createSequentialGroup()
                                                                .addComponent(cb_RoomTypeSearch, javax.swing.GroupLayout.PREFERRED_SIZE, 236, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                                .addGap(21, 21, 21))))
                                        .addGroup(pInfoLayout.createSequentialGroup()
                                                .addGap(44, 44, 44)
                                                .addGroup(pInfoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                                        .addGroup(pInfoLayout.createSequentialGroup()
                                                                .addComponent(jLabel8)
                                                                .addGap(0, 0, Short.MAX_VALUE))
                                                        .addGroup(pInfoLayout.createSequentialGroup()
                                                                .addGroup(pInfoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                                                        .addGroup(pInfoLayout.createSequentialGroup()
                                                                                .addGroup(pInfoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                                                                        .addComponent(jLabel7, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                                                                        .addComponent(jLabel6, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                                                                        .addComponent(jLabel12, javax.swing.GroupLayout.DEFAULT_SIZE, 77, Short.MAX_VALUE)
                                                                                        )
                                                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED))
                                                                        .addGroup(pInfoLayout.createSequentialGroup()
                                                                                .addComponent(jLabel5, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                                                                .addGap(25, 25, 25))
                                                                        .addGroup(pInfoLayout.createSequentialGroup()
                                                                                .addComponent(jLabel9, javax.swing.GroupLayout.PREFERRED_SIZE, 62, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                                                                .addGroup(pInfoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                                                        .addComponent(txtRoomName, javax.swing.GroupLayout.PREFERRED_SIZE, 233, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                                        .addComponent(txtPrice, javax.swing.GroupLayout.PREFERRED_SIZE, 257, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                                        .addComponent(txtHourlyPrice, javax.swing.GroupLayout.PREFERRED_SIZE, 257, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                                        .addComponent(txtPosition, javax.swing.GroupLayout.PREFERRED_SIZE, 239, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                                        )))))
                                .addContainerGap())
        );
        pInfoLayout.setVerticalGroup(
                pInfoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(pInfoLayout.createSequentialGroup()
                                .addGap(14, 14, 14)
                                .addGroup(pInfoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                        .addComponent(jLabel5)
                                        .addComponent(txtRoomName, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                                .addGap(22, 22, 22)
                                .addGroup(pInfoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                        .addComponent(jLabel7)
                                        .addComponent(txtPosition, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addGap(22, 22, 22)
                                .addGroup(pInfoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                        .addComponent(jLabel6)
                                        .addComponent(txtPrice, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addGap(22, 22, 22)
                                .addGroup(pInfoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                        .addComponent(jLabel12)
                                        .addComponent(txtHourlyPrice, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addGap(22, 22, 22)

                                .addGroup(pInfoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                        .addComponent(jLabel9)
                                        .addComponent(txtNumOfPeople, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addGap(22, 22, 22)
                                .addGroup(pInfoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                        .addComponent(jLabel8)
                                        .addComponent(cb_RoomTypeSearch, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addGap(22, 22, 22)
                                .addGroup(pInfoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                        .addComponent(btnReset, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addComponent(btnUpdate, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addComponent(btnAdd, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addGap(204, 204, 204))
        );

        pnl_Left.add(pInfo);
        pInfo.setBounds(870, 20, 400, 420);

        pFind.setBackground(new java.awt.Color(255, 255, 255));
        pFind.setBorder(javax.swing.BorderFactory.createTitledBorder("Tìm kiếm"));

        jPanel1.setBackground(new java.awt.Color(255, 255, 255));

        jLabel1.setText("Giá:");

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
                jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(jPanel1Layout.createSequentialGroup()
                                .addContainerGap()
                                .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 68, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(cb_Price, javax.swing.GroupLayout.DEFAULT_SIZE, 254, Short.MAX_VALUE)
                                .addContainerGap())
        );
        jPanel1Layout.setVerticalGroup(
                jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(jPanel1Layout.createSequentialGroup()
                                .addContainerGap()
                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                        .addComponent(jLabel1, javax.swing.GroupLayout.DEFAULT_SIZE, 38, Short.MAX_VALUE)
                                        .addComponent(cb_Price, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addContainerGap())
        );

        jPanel2.setBackground(new java.awt.Color(255, 255, 255));

        jLabel2.setText("Tầng:");

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
                jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(jPanel2Layout.createSequentialGroup()
                                .addContainerGap()
                                .addComponent(jLabel2, javax.swing.GroupLayout.PREFERRED_SIZE, 68, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(cb_Position, javax.swing.GroupLayout.DEFAULT_SIZE, 254, Short.MAX_VALUE)
                                .addContainerGap())
        );
        jPanel2Layout.setVerticalGroup(
                jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(jPanel2Layout.createSequentialGroup()
                                .addContainerGap()
                                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                        .addComponent(jLabel2, javax.swing.GroupLayout.DEFAULT_SIZE, 38, Short.MAX_VALUE)
                                        .addComponent(cb_Position, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addContainerGap())
        );

        jPanel3.setBackground(new java.awt.Color(255, 255, 255));

        jLabel3.setBackground(new java.awt.Color(255, 255, 255));
        jLabel3.setText("Loại phòng:");

        cb_RoomType.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cb_RoomTypeActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
                jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(jPanel3Layout.createSequentialGroup()
                                .addContainerGap()
                                .addComponent(jLabel3, javax.swing.GroupLayout.PREFERRED_SIZE, 68, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(cb_RoomType, javax.swing.GroupLayout.PREFERRED_SIZE, 255, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addContainerGap(15, Short.MAX_VALUE))
        );
        jPanel3Layout.setVerticalGroup(
                jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(jPanel3Layout.createSequentialGroup()
                                .addContainerGap()
                                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                        .addComponent(jLabel3, javax.swing.GroupLayout.DEFAULT_SIZE, 38, Short.MAX_VALUE)
                                        .addComponent(cb_RoomType, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addContainerGap())
        );

        jPanel4.setBackground(new java.awt.Color(255, 255, 255));

        jLabel4.setText("Trạng thái:");

        cb_Status.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cb_StatusActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel4Layout = new javax.swing.GroupLayout(jPanel4);
        jPanel4.setLayout(jPanel4Layout);
        jPanel4Layout.setHorizontalGroup(
                jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(jPanel4Layout.createSequentialGroup()
                                .addContainerGap()
                                .addComponent(jLabel4)
                                .addGap(18, 18, 18)
                                .addComponent(cb_Status, javax.swing.GroupLayout.PREFERRED_SIZE, 258, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addContainerGap(12, Short.MAX_VALUE))
        );
        jPanel4Layout.setVerticalGroup(
                jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(jPanel4Layout.createSequentialGroup()
                                .addContainerGap()
                                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                        .addComponent(jLabel4, javax.swing.GroupLayout.DEFAULT_SIZE, 38, Short.MAX_VALUE)
                                        .addComponent(cb_Status, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addContainerGap())
        );

        javax.swing.GroupLayout pFindLayout = new javax.swing.GroupLayout(pFind);
        pFind.setLayout(pFindLayout);
        pFindLayout.setHorizontalGroup(
                pFindLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(pFindLayout.createSequentialGroup()
                                .addGap(46, 46, 46)
                                .addGroup(pFindLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                        .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 62, Short.MAX_VALUE)
                                .addGroup(pFindLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                        .addComponent(jPanel3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addComponent(jPanel4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addGap(32, 32, 32))
        );
        pFindLayout.setVerticalGroup(
                pFindLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, pFindLayout.createSequentialGroup()
                                .addContainerGap(21, Short.MAX_VALUE)
                                .addGroup(pFindLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                        .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addComponent(jPanel3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addGap(10, 10, 10)
                                .addGroup(pFindLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                        .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addComponent(jPanel4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addContainerGap())
        );

        pnl_Left.add(pFind);
        pFind.setBounds(10, 20, 840, 160);
        pFind.getAccessibleContext().setAccessibleDescription("");

        pAmentities.setBackground(new java.awt.Color(255, 255, 255));
        pAmentities.setBorder(javax.swing.BorderFactory.createTitledBorder("Tiện nghi"));

        customTable1.setModel(new javax.swing.table.DefaultTableModel(
                new Object [][] {
                        {null, null},
                        {null, null},
                        {null, null},
                        {null, null}
                },
                new String [] {
                        "STT", "Tiện nghi"
                }
        ));
        jScrollPane4.setViewportView(customTable1);

        jPanel5.setBackground(new java.awt.Color(255, 255, 255));

        jTextArea1.setColumns(20);
        jTextArea1.setRows(5);
        jScrollPane3.setViewportView(jTextArea1);

        jLabel10.setText("Nhập tiện nghi:");

        javax.swing.GroupLayout jPanel5Layout = new javax.swing.GroupLayout(jPanel5);
        jPanel5.setLayout(jPanel5Layout);
        jPanel5Layout.setHorizontalGroup(
                jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(jPanel5Layout.createSequentialGroup()
                                .addContainerGap()
                                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                        .addComponent(jLabel10)
                                        .addComponent(jScrollPane3, javax.swing.GroupLayout.PREFERRED_SIZE, 168, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addContainerGap(27, Short.MAX_VALUE))
        );
        jPanel5Layout.setVerticalGroup(
                jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel5Layout.createSequentialGroup()
                                .addContainerGap(14, Short.MAX_VALUE)
                                .addComponent(jLabel10)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jScrollPane3, javax.swing.GroupLayout.PREFERRED_SIZE, 140, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addContainerGap())
        );


        btnAddAmentity.setText("Thêm");
        btnAddAmentity.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnAddAmentityActionPerformed(evt);
            }
        });


        btnUpdateAmentity.setText("Cập nhật");
        btnUpdateAmentity.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnUpdateAmentityActionPerformed(evt);
            }
        });

        btnResetAmentity.setText("Làm mới");
        btnResetAmentity.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnResetAmentityActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout pAmentitiesLayout = new javax.swing.GroupLayout(pAmentities);
        pAmentities.setLayout(pAmentitiesLayout);
        pAmentitiesLayout.setHorizontalGroup(
                pAmentitiesLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(pAmentitiesLayout.createSequentialGroup()
                                .addContainerGap()
                                .addComponent(jScrollPane4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(29, 29, 29)
                                .addComponent(jPanel5, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addGroup(pAmentitiesLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                        .addComponent(btnAddAmentity, javax.swing.GroupLayout.PREFERRED_SIZE, 140, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addComponent(btnUpdateAmentity, javax.swing.GroupLayout.PREFERRED_SIZE, 140, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addComponent(btnResetAmentity, javax.swing.GroupLayout.PREFERRED_SIZE, 140, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addContainerGap())
        );
        pAmentitiesLayout.setVerticalGroup(
                pAmentitiesLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(pAmentitiesLayout.createSequentialGroup()
                                .addContainerGap()
                                .addGroup(pAmentitiesLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                        .addGroup(pAmentitiesLayout.createSequentialGroup()
                                                .addComponent(btnAddAmentity, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                .addGap(18, 18, 18)
                                                .addComponent(btnUpdateAmentity, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                .addGap(18, 18, 18)
                                                .addComponent(btnResetAmentity, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE))
                                        .addGroup(pAmentitiesLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                                .addComponent(jScrollPane4, javax.swing.GroupLayout.PREFERRED_SIZE, 182, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                .addComponent(jPanel5, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                                .addContainerGap(25, Short.MAX_VALUE))
        );

        pnl_Left.add(pAmentities);
        pAmentities.setBounds(0, 450, 850, 240);

        jPanel6.setBackground(new java.awt.Color(255, 255, 255));
        jPanel6.setBorder(javax.swing.BorderFactory.createTitledBorder("Quản lí loại phòng - dịch vụ"));


        btnManageRoomType.setText("Quản lý loại phòng");
        btnManageRoomType.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnManageRoomTypeActionPerformed(evt);
            }
        });


        btnManageService.setText("Cập nhật");
        btnManageService.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnManageServiceActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel6Layout = new javax.swing.GroupLayout(jPanel6);
        jPanel6.setLayout(jPanel6Layout);
        jPanel6Layout.setHorizontalGroup(
                jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(jPanel6Layout.createSequentialGroup()
                                .addGap(49, 49, 49)
                                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                        .addComponent(btnManageRoomType, javax.swing.GroupLayout.DEFAULT_SIZE, 277, Short.MAX_VALUE)
                                        .addComponent(btnManageService, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                                .addContainerGap(64, Short.MAX_VALUE))
        );
        jPanel6Layout.setVerticalGroup(
                jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel6Layout.createSequentialGroup()
                                .addContainerGap(39, Short.MAX_VALUE)
                                .addComponent(btnManageRoomType, javax.swing.GroupLayout.PREFERRED_SIZE, 55, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(33, 33, 33)
                                .addComponent(btnManageService, javax.swing.GroupLayout.PREFERRED_SIZE, 55, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(25, 25, 25))
        );

        pnl_Left.add(jPanel6);
        jPanel6.setBounds(870, 450, 400, 230);

        customTable.setModel(new javax.swing.table.DefaultTableModel(
                new Object [][] {
                        {null, null, null, null, null, null, null, null},
                        {null, null, null, null, null, null, null, null},
                        {null, null, null, null, null, null, null, null},
                        {null, null, null, null, null, null, null, null}
                },
                new String [] {
                        "Mã phòng", "Loại phòng", "Trạng thái", "Giá/Đêm", "Giá/Giờ", "Số người", "Giờ tối thiểu", "Giờ tối đa"
                }
        ));
        jScrollPane2.setViewportView(customTable);

        pnl_Left.add(jScrollPane2);
        jScrollPane2.setBounds(10, 180, 840, 260);

        pnl_Center.add(pnl_Left, java.awt.BorderLayout.CENTER);

        add(pnl_Center, java.awt.BorderLayout.CENTER);
    }

    private void cb_StatusActionPerformed(ActionEvent evt) {
    }

    private void cb_RoomTypeActionPerformed(ActionEvent evt) {
    }

    private void btnAddActionPerformed(java.awt.event.ActionEvent evt) {
        String newRoomId = generateRoomId();
        if (newRoomId == null) {
            JOptionPane.showMessageDialog(this, "Không thể sinh mã phòng mới! Đã đạt giới hạn mã phòng (R999).");
            return;
        }
        txtRoomName.setText(newRoomId);

        if (!validData(false)) {
            return;
        }

        Room room = new Room();
        room.setRoomId(txtRoomName.getText());
        room.setPrice(Double.parseDouble(txtPrice.getText()));

        // Xử lý hourlyBaseRate
        String hourlyPriceText = txtHourlyPrice.getText().trim();
        room.setHourlyBaseRate(hourlyPriceText.isEmpty() ? 0.0 : Double.parseDouble(hourlyPriceText));

        // Mặc định minHours và maxHours
        room.setMinHours(1);
        room.setMaxHours(12);

        // Xử lý capacity
        String capacityText = txtNumOfPeople.getText().trim();
        room.setCapacity(capacityText.isEmpty() ? 0 : Integer.parseInt(capacityText));

        // Xử lý floor
        String floorText = txtPosition.getText().trim();
        room.setFloor(floorText.isEmpty() ? 0 : Integer.parseInt(floorText));

        room.setStatus(Room.STATUS_AVAILABLE);
        room.setRoomSize(0.0);
        room.setRoomImage("");
        room.setAmenities(new ArrayList<>());

        String roomTypeName = (String) cb_RoomTypeSearch.getSelectedItem();
        RoomType roomType = roomDAO.getAllRoomTypes().stream()
                .filter(rt -> rt.getRoomType().getTypeName().equals(roomTypeName))
                .findFirst()
                .orElse(null).getRoomType();
        if (roomType != null) {
            room.setRoomType(roomType);
        } else {
            JOptionPane.showMessageDialog(this, "Không tìm thấy loại phòng!");
            return;
        }

        try {
            roomDAO.create(room);
            loadRoomData();
            JOptionPane.showMessageDialog(this, "Thêm phòng thành công!");
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Lỗi khi thêm phòng: " + e.getMessage());
        }
    }

    private void btnUpdateActionPerformed(java.awt.event.ActionEvent evt) {
        if (selectedRoom == null) {
            JOptionPane.showMessageDialog(this, "Vui lòng chọn một phòng để cập nhật!");
            return;
        }

        if (!validData(true)) {
            return;
        }

        selectedRoom.setRoomId(txtRoomName.getText());
        selectedRoom.setPrice(Double.parseDouble(txtPrice.getText()));

        // Cập nhật hourlyBaseRate
        String hourlyPriceText = txtHourlyPrice.getText().trim();
        selectedRoom.setHourlyBaseRate(hourlyPriceText.isEmpty() ? 0.0 : Double.parseDouble(hourlyPriceText));

        // Xử lý capacity
        String capacityText = txtNumOfPeople.getText().trim();
        selectedRoom.setCapacity(capacityText.isEmpty() ? 0 : Integer.parseInt(capacityText));

        // Xử lý floor
        String floorText = txtPosition.getText().trim();
        selectedRoom.setFloor(floorText.isEmpty() ? 0 : Integer.parseInt(floorText));

        String roomTypeName = (String) cb_RoomTypeSearch.getSelectedItem();
        RoomType roomType = roomDAO.getAllRoomTypes().stream()
                .filter(rt -> rt.getRoomType().getTypeName().equals(roomTypeName))
                .findFirst()
                .orElse(null).getRoomType();
        if (roomType != null) {
            selectedRoom.setRoomType(roomType);
        } else {
            JOptionPane.showMessageDialog(this, "Không tìm thấy loại phòng!");
            return;
        }

        try {
            roomDAO.update(selectedRoom);
            loadRoomData();
            JOptionPane.showMessageDialog(this, "Cập nhật phòng thành công!");
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Lỗi khi cập nhật phòng: " + e.getMessage());
        }
    }

    private void btnResetActionPerformed(java.awt.event.ActionEvent evt) {
        String newRoomId = generateRoomId();
        if (newRoomId == null) {
            JOptionPane.showMessageDialog(this, "Không thể sinh mã phòng mới! Đã đạt giới hạn mã phòng (R999).");
            txtRoomName.setText("");
        } else {
            txtRoomName.setText(newRoomId);
        }
        cb_RoomTypeSearch.setSelectedIndex(0);
        txtPrice.setText("");
        txtHourlyPrice.setText("");
        txtPosition.setText("");
        txtNumOfPeople.setText("");
        selectedRoom = null;
        cb_Status.setSelectedItem(0);
        cb_Position.setSelectedItem(0);
        cb_Price.setSelectedItem(0);
        cb_RoomType.setSelectedIndex(0);
        loadRoomData();
        jTextArea1.setText("");
    }

    private void btnManageServiceActionPerformed(java.awt.event.ActionEvent evt) {
        tabSM.setLocationRelativeTo(null);
        tabSM.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        tabSM.setVisible(true);
    }

    private void btnManageRoomTypeActionPerformed(java.awt.event.ActionEvent evt) {
        tabRTM.setLocationRelativeTo(null);
        tabRTM.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        tabRTM.setVisible(true);
    }

    private void txtRoomNameActionPerformed(java.awt.event.ActionEvent evt) {
    }

    private void btnAddAmentityActionPerformed(java.awt.event.ActionEvent evt) {
        if (selectedRoom != null) {
            String amenity = jTextArea1.getText().trim();
            if (!amenity.isEmpty()) {
                try {
                    List<String> updatedAmenities = roomDAO.addAmenity(selectedRoom.getRoomId(), amenity.trim());
                    if (updatedAmenities == null) {
                        throw new IllegalStateException("Không thể thêm tiện nghi!");
                    }
                    selectedRoom.setAmenities(updatedAmenities);
                    updateAmenitiesTable(updatedAmenities);
                    customTable1.repaint();
                    customTable1.revalidate();
                    JOptionPane.showMessageDialog(this, "Thêm tiện nghi thành công!");
                } catch (Exception e) {
                    JOptionPane.showMessageDialog(this, "Lỗi khi thêm tiện nghi: " + e.getMessage());
                }
            } else {
                JOptionPane.showMessageDialog(this, "Vui lòng nhập tiện nghi!");
            }
        } else {
            JOptionPane.showMessageDialog(this, "Vui lòng chọn một phòng!");
        }
    }

    private void btnUpdateAmentityActionPerformed(java.awt.event.ActionEvent evt) {
        if (selectedRoom == null) {
            JOptionPane.showMessageDialog(this, "Vui lòng chọn một phòng trước khi cập nhật tiện nghi!");
            return;
        }

        int selectedRow = customTable1.getSelectedRow();
        if (selectedRow < 0) {
            JOptionPane.showMessageDialog(this, "Vui lòng chọn một tiện nghi để cập nhật!");
            return;
        }

        String currentAmenity = (String) customTable1.getValueAt(selectedRow, 1);
        String updatedAmenity = jTextArea1.getText().trim();
        if (updatedAmenity == null || updatedAmenity.trim().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Tiện nghi không được để trống!");
            return;
        }

        try {
            List<String> updatedAmenities = roomDAO.updateAmenity(selectedRoom.getRoomId(), selectedRow, updatedAmenity.trim());
            if (updatedAmenities == null) {
                throw new IllegalStateException("Không thể cập nhật tiện nghi!");
            }
            selectedRoom.setAmenities(updatedAmenities);
            updateAmenitiesTable(updatedAmenities);
            customTable1.repaint();
            customTable1.revalidate();
            jTextArea1.setText(updatedAmenity);
            JOptionPane.showMessageDialog(this, "Cập nhật tiện nghi thành công!");
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Lỗi khi cập nhật tiện nghi: " + e.getMessage());
        }
    }

    private void btnResetAmentityActionPerformed(java.awt.event.ActionEvent evt) {
        jTextArea1.setText("");
        updateAmenitiesTable(selectedRoom != null ? selectedRoom.getAmenities() : null);
        loadRoomData();
        loadAmenityData();
    }

    private void initializeTableModels() {
        ((DefaultTableModel) customTable.getModel()).setRowCount(0);
        ((DefaultTableModel) customTable1.getModel()).setRowCount(0);
    }

    private void loadRoomTypes() {
        List<RoomType> roomTypes = roomTypeDAO.getAllRoomTypes();
        cb_RoomType.removeAllItems();
        cb_RoomTypeSearch.removeAllItems();
        cb_RoomType.addItem("Tất cả");
        cb_RoomTypeSearch.addItem("Tất cả");
        for (RoomType rt : roomTypes) {
            cb_RoomType.addItem(rt.getTypeName());
            cb_RoomTypeSearch.addItem(rt.getTypeName());
        }
    }

    private void updateRoomTable(List<Room> rooms) {
        DefaultTableModel model = (DefaultTableModel) customTable.getModel();
        model.setRowCount(0);
        for (Room room : rooms) {
            String statusText;
            switch (room.getStatus()) {
                case Room.STATUS_AVAILABLE:
                    statusText = "Có sẵn";
                    break;
                case Room.STATUS_RESERVED:
                    statusText = "Đã đặt trước";
                    break;
                case Room.STATUS_OCCUPIED:
                    statusText = "Đang sử dụng";
                    break;
                case Room.STATUS_MAINTENANCE:
                    statusText = "Bảo trì";
                    break;
                default:
                    statusText = "Không xác định";
            }
            model.addRow(new Object[]{
                    room.getRoomId(),
                    room.getRoomType() != null ? room.getRoomType().getTypeName() : "N/A",
                    statusText,
                    room.getPrice(),
                    room.getHourlyBaseRate(),
                    room.getCapacity(),
                    room.getMinHours(),
                    room.getMaxHours()
            });
        }
    }

    public void loadRoomData() {
        List<Room> rooms = roomDAO.getAllRooms();
        updateRoomTable(rooms);
        cb_Status.removeAllItems();
        cb_Status.addItem("Tất cả");
        cb_Status.addItem("Có sẵn");
        cb_Status.addItem("Đang sử dụng");
        cb_Status.addItem("Bảo trì");
        cb_Status.addItem("Đã đặt trước");

        cb_Price.removeAllItems();
        cb_Price.addItem("Tất cả");
        cb_Price.addItem("Dưới 500,000");
        cb_Price.addItem("500,000 - 1,000,000");
        cb_Price.addItem("Trên 1,000,000");

        cb_Position.removeAllItems();
        cb_Position.addItem("Tất cả");
        List<Integer> floors = roomDAO.getAllFloors();
        for (Integer floor : floors) {
            cb_Position.addItem("Tầng " + floor);
        }
    }

    private void setupListeners() {
        customTable.getSelectionModel().addListSelectionListener(new ListSelectionListener() {
            @Override
            public void valueChanged(ListSelectionEvent e) {
                int selectedRow = customTable.getSelectedRow();
                if (selectedRow >= 0) {
                    String roomId = (String) customTable.getValueAt(selectedRow, 0);
                    selectedRoom = roomDAO.findById(roomId);
                    if (selectedRoom != null) {
                        txtRoomName.setText(selectedRoom.getRoomId());
                        txtPosition.setText(valueOf(selectedRoom.getFloor()));
                        txtPrice.setText(String.valueOf(selectedRoom.getPrice()));
                        txtHourlyPrice.setText(String.valueOf(selectedRoom.getHourlyBaseRate()));
                        txtNumOfPeople.setText(valueOf(selectedRoom.getCapacity()));
                        cb_RoomTypeSearch.setSelectedItem(selectedRoom.getRoomType() != null ? selectedRoom.getRoomType().getTypeName() : "N/A");
                        updateAmenitiesTable(selectedRoom.getAmenities());
                        jTextArea1.setText("");
                    } else {
                        updateAmenitiesTable(null);
                        jTextArea1.setText("");
                    }
                } else {
                    updateAmenitiesTable(null);
                    jTextArea1.setText("");
                }
            }
        });
        customTable1.getSelectionModel().addListSelectionListener(new ListSelectionListener() {
            @Override
            public void valueChanged(ListSelectionEvent e) {
                if (!e.getValueIsAdjusting()) {
                    int selectedRow = customTable1.getSelectedRow();
                    if (selectedRow >= 0) {
                        String selectedAmenity = (String) customTable1.getValueAt(selectedRow, 1);
                        jTextArea1.setText(selectedAmenity != null ? selectedAmenity : "");
                    } else {
                        jTextArea1.setText("");
                    }
                }
            }
        });
        cb_Status.addActionListener(e -> searchRooms());
        cb_RoomType.addActionListener(e -> searchRooms());
        cb_Price.addActionListener(e -> searchRooms());
        cb_Position.addActionListener(e -> searchRooms());
    }

    void updateAmenitiesTable(List<String> amenities) {
        DefaultTableModel model = (DefaultTableModel) customTable1.getModel();
        model.setRowCount(0);
        int i = 0;
        if (amenities != null && !amenities.isEmpty()) {
            for (String amenity : amenities) {
                i++;
                model.addRow(new Object[]{i, amenity});
            }
        }
        customTable1.repaint();
        customTable1.revalidate();
    }

    void loadAmenityData() {
        if (selectedRoom != null) {
            loadRoomData();
            setupListeners();
        }
    }

    private void searchRooms() {
        Map<String, Object> criteria = new HashMap<>();

        String priceFilter = (String) cb_Price.getSelectedItem();
        if (priceFilter != null && !priceFilter.equals("Tất cả")) {
            if (priceFilter.equals("Dưới 500,000")) {
                criteria.put("priceMin", 0.0);
                criteria.put("priceMax", 500000.0);
            } else if (priceFilter.equals("500,000 - 1,000,000")) {
                criteria.put("priceMin", 500000.0);
                criteria.put("priceMax", 1000000.0);
            } else {
                criteria.put("priceMin", 1000000.0);
                criteria.put("priceMax", Double.MAX_VALUE);
            }
        }

        String positionFilter = (String) cb_Position.getSelectedItem();
        if (positionFilter != null && !positionFilter.equals("Tất cả")) {
            criteria.put("position", positionFilter.replace("Tầng ", ""));
        }

        String roomTypeFilter = (String) cb_RoomType.getSelectedItem();
        if (roomTypeFilter != null && !roomTypeFilter.equals("Tất cả")) {
            criteria.put("roomType", roomTypeFilter);
        }

        String statusFilter = (String) cb_Status.getSelectedItem();
        if (statusFilter != null && !statusFilter.equals("Tất cả")) {
            Integer status = null;
            switch (statusFilter) {
                case "Có sẵn":
                    status = Room.STATUS_AVAILABLE;
                    break;
                case "Đang sử dụng":
                    status = Room.STATUS_OCCUPIED;
                    break;
                case "Bảo trì":
                    status = Room.STATUS_MAINTENANCE;
                    break;
                case "Đã đặt trước":
                    status = Room.STATUS_RESERVED;
                    break;
            }
            if (status != null) {
                criteria.put("status", status);
            }
        }

        List<Room> rooms = roomDAO.findByCriteria(criteria);
        updateRoomTable(rooms);
    }

    private String generateRoomId() {
        List<Room> rooms = roomDAO.getAllRooms();
        int maxNumber = 0;

        for (Room room : rooms) {
            String roomId = room.getRoomId();
            if (roomId != null && roomId.matches("R\\d{3}")) {
                int number = Integer.parseInt(roomId.substring(1));
                maxNumber = Math.max(maxNumber, number);
            }
        }

        maxNumber++;
        if (maxNumber > 999) {
            return null;
        }
        return String.format("R%03d", maxNumber);
    }

    private boolean validData(boolean isUpdate) {
        String roomId = txtRoomName.getText().trim();
        String priceText = txtPrice.getText().trim();
        String hourlyPriceText = txtHourlyPrice.getText().trim();
        String floorText = txtPosition.getText().trim();
        String capacityText = txtNumOfPeople.getText().trim();
        String roomType = (String) cb_RoomTypeSearch.getSelectedItem();

        if (roomId.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Mã phòng không được để trống!");
            txtRoomName.requestFocus();
            return false;
        }

        if (priceText.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Giá phòng không được để trống!");
            txtPrice.requestFocus();
            return false;
        }
        try {
            double price = Double.parseDouble(priceText);
            if (price <= 100000) {
                JOptionPane.showMessageDialog(this, "Giá phòng phải lớn hơn 100,000!");
                txtPrice.requestFocus();
                return false;
            }
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Giá phòng phải là số hợp lệ!");
            txtPrice.requestFocus();
            return false;
        }

        if (!hourlyPriceText.isEmpty()) {
            try {
                double hourlyPrice = Double.parseDouble(hourlyPriceText);
                if (hourlyPrice < 0) {
                    JOptionPane.showMessageDialog(this, "Giá theo giờ không được âm!");
                    txtHourlyPrice.requestFocus();
                    return false;
                }
            } catch (NumberFormatException e) {
                JOptionPane.showMessageDialog(this, "Giá theo giờ phải là số hợp lệ!");
                txtHourlyPrice.requestFocus();
                return false;
            }
        }

        if (floorText.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Tầng không được để trống!");
            txtPosition.requestFocus();
            return false;
        }
        try {
            int floor = Integer.parseInt(floorText);
            if (floor <= 0) {
                JOptionPane.showMessageDialog(this, "Tầng phải là số nguyên dương!");
                txtPosition.requestFocus();
                return false;
            }
            List<Integer> floors = roomDAO.getAllFloors();
            if (!floors.contains(floor) && !isUpdate) {
                int confirm = JOptionPane.showConfirmDialog(this,
                        "Tầng " + floor + " chưa tồn tại. Bạn có muốn thêm tầng mới không?",
                        "Xác nhận", JOptionPane.YES_NO_OPTION);
                if (confirm != JOptionPane.YES_OPTION) {
                    txtPosition.requestFocus();
                    return false;
                }
            }
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Tầng phải là số nguyên hợp lệ!");
            txtPosition.requestFocus();
            return false;
        }

        if (!capacityText.isEmpty()) {
            try {
                int capacity = Integer.parseInt(capacityText);
                if (capacity < 0) {
                    JOptionPane.showMessageDialog(this, "Sức chứa không được âm!");
                    txtNumOfPeople.requestFocus();
                    return false;
                }
            } catch (NumberFormatException e) {
                JOptionPane.showMessageDialog(this, "Sức chứa phải là số nguyên hợp lệ!");
                txtNumOfPeople.requestFocus();
                return false;
            }
        }

        if (roomType == null || roomType.equals("Tất cả")) {
            JOptionPane.showMessageDialog(this, "Vui lòng chọn loại phòng!");
            cb_RoomTypeSearch.requestFocus();
            return false;
        }

        return true;
    }

    private ui.components.button.ButtonCustom btnAdd;
    private ui.components.button.ButtonCustom btnAddAmentity;
    private ui.components.button.ButtonCustom btnManageRoomType;
    private ui.components.button.ButtonCustom btnManageService;
    private ui.components.button.ButtonCustom btnReset;
    private ui.components.button.ButtonCustom btnResetAmentity;
    private ui.components.button.ButtonCustom btnUpdate;
    private ui.components.button.ButtonCustom btnUpdateAmentity;
    private ui.components.combobox.StyledComboBox cb_Position;
    private ui.components.combobox.StyledComboBox cb_Price;
    private ui.components.combobox.StyledComboBox cb_RoomType;
    private ui.components.combobox.StyledComboBox cb_RoomTypeSearch;
    private ui.components.combobox.StyledComboBox cb_Status;
    private ui.components.table.CustomTable customTable;
    private ui.components.table.CustomTable customTable1;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
   // private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel12;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JPanel jPanel5;
    private javax.swing.JPanel jPanel6;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JScrollPane jScrollPane4;
    private javax.swing.JTextArea jTextArea1;
    private javax.swing.JLabel lbl_Title;
    private javax.swing.JPanel pAmentities;
    private javax.swing.JPanel pFind;
    private javax.swing.JPanel pInfo;
    private javax.swing.JPanel pnl_Center;
    private javax.swing.JPanel pnl_Left;
    private javax.swing.JPanel pnl_North;
    private ui.components.textfield.CustomRoundedTextField txtNumOfPeople;
    private ui.components.textfield.CustomRoundedTextField txtPosition;
    private ui.components.textfield.CustomRoundedTextField txtPrice;
    //private ui.components.textfield.CustomRoundedTextField txtPrice1;
    private ui.components.textfield.CustomRoundedTextField txtHourlyPrice;
    private ui.components.textfield.CustomRoundedTextField txtRoomName;
}