INSERT INTO room_type (type_id, type_name, description)
VALUES
    ('RT001', 'Standard', 'Phòng tiêu chuẩn với đầy đủ tiện nghi cơ bản'),
    ('RT002', 'Deluxe', 'Phòng cao cấp với view đẹp và tiện nghi tốt hơn'),
    ('RT003', 'Suite', 'Phòng sang trọng với không gian rộng rãi'),
    ('RT004', 'Family', 'Phòng dành cho gia đình với 2 giường'),
    ('RT005', 'Executive', 'Phòng dành cho doanh nhân với không gian làm việc');

INSERT INTO rooms (room_id, type_id, capacity, floor, price, hourly_base_rate, min_hours, max_hours, room_size, standard_checkout_hour, status, room_image)
VALUES
    ('P101', 'RT001', 2, 1, 800000, 150000, 2, 12, 25, 12, 0, 'https://example.com/room1.jpg'),
    ('P102', 'RT001', 2, 1, 800000, 150000, 2, 12, 25, 12, 0, 'https://example.com/room2.jpg'),
    ('P201', 'RT002', 2, 2, 1200000, 200000, 2, 12, 30, 12, 0, 'https://example.com/room3.jpg'),
    ('P202', 'RT002', 2, 2, 1200000, 200000, 2, 12, 30, 12, 0, 'https://example.com/room4.jpg'),
    ('P301', 'RT003', 2, 3, 2000000, 300000, 2, 12, 45, 14, 0, 'https://example.com/room5.jpg'),
    ('P401', 'RT004', 4, 4, 1800000, 250000, 2, 12, 50, 12, 0, 'https://example.com/room6.jpg'),
    ('P501', 'RT005', 2, 5, 2500000, 350000, 2, 12, 40, 14, 0, 'https://example.com/room7.jpg');

INSERT INTO amentities (room_id, amentities)
VALUES
    ('P101', 'TV'),
    ('P101', 'Điều hòa'),
    ('P101', 'Wifi'),
    ('P201', 'TV'),
    ('P201', 'Điều hòa'),
    ('P201', 'Wifi'),
    ('P201', 'Mini bar'),
    ('P301', 'TV'),
    ('P301', 'Điều hòa'),
    ('P301', 'Wifi'),
    ('P301', 'Mini bar'),
    ('P301', 'Bồn tắm'),
    ('P401', 'TV'),
    ('P401', 'Điều hòa'),
    ('P401', 'Wifi'),
    ('P401', '2 giường đôi'),
    ('P501', 'TV'),
    ('P501', 'Điều hòa'),
    ('P501', 'Wifi'),
    ('P501', 'Mini bar'),
    ('P501', 'Bàn làm việc');

INSERT INTO hourly_price_rules (room_id, hour_range, multiplier)
VALUES
    ('P101', '8-12', 1.2),
    ('P101', '12-18', 1.0),
    ('P101', '18-8', 1.5),
    ('P201', '8-12', 1.2),
    ('P201', '12-18', 1.0),
    ('P201', '18-8', 1.5),
    ('P301', '8-12', 1.2),
    ('P301', '12-18', 1.0),
    ('P301', '18-8', 1.5),
    ('P401', '8-12', 1.2),
    ('P401', '12-18', 1.0),
    ('P401', '18-8', 1.5),
    ('P501', '8-12', 1.2),
    ('P501', '12-18', 1.0),
    ('P501', '18-8', 1.5);

INSERT INTO services (service_id, name, price, availability, description)
VALUES
    (1, 'Bữa sáng', 150000, 1, 'Buffet sáng với nhiều lựa chọn'),
    (2, 'Đưa đón sân bay', 300000, 1, 'Dịch vụ đưa đón sân bay bằng xe riêng'),
    (3, 'Giặt ủi', 100000, 1, 'Dịch vụ giặt ủi 1 bộ đồ'),
    (4, 'Massage', 500000, 1, 'Massage thư giãn 60 phút'),
    (5, 'Thuê xe đạp', 200000, 1, 'Thuê xe đạp trong 1 ngày'),
    (6, 'Phòng họp', 1000000, 1, 'Thuê phòng họp 2 giờ'),
    (7, 'Minibar', 0, 1, 'Đồ uống trong minibar tính phí riêng');

INSERT INTO customers (customer_id, first_name, last_name, phone_number, email, cccd, address, date_of_birth, gender, point)
VALUES
    ('C001', 'Nguyễn Văn', 'A', '0912345678', 'nguyenvana@gmail.com', '001123456789', '123 Đường ABC, Quận 1, TP.HCM', '1990-01-15', 1, 100),
    ('C002', 'Trần Thị', 'B', '0923456789', 'tranthib@gmail.com', '001123456788', '456 Đường XYZ, Quận 2, TP.HCM', '1992-05-20', 0, 50),
    ('C003', 'Lê Văn', 'C', '0934567890', 'levanc@gmail.com', '001123456787', '789 Đường DEF, Quận 3, TP.HCM', '1985-11-30', 1, 200),
    ('C004', 'Phạm Thị', 'D', '0945678901', 'phamthid@gmail.com', '001123456786', '321 Đường GHI, Quận 4, TP.HCM', '1988-07-25', 0, 75),
    ('C005', 'Hoàng Văn', 'E', '0956789012', 'hoangvane@gmail.com', '001123456785', '654 Đường KLM, Quận 5, TP.HCM', '1995-03-10', 1, 150);

INSERT INTO staffs (staff_id, first_name, last_name, email, date_of_birth, gender, date_of_join, address, status, staff_image)
VALUES
    ('ST001', 'Nguyễn Thị', 'Nhân viên 1', 'nhanvien1@hotel.com', '1990-05-15', 0, '2020-01-10', '111 Đường A, Quận 1, TP.HCM', 1, NULL),
    ('ST002', 'Trần Văn', 'Nhân viên 2', 'nhanvien2@hotel.com', '1985-08-20', 1, '2019-06-15', '222 Đường B, Quận 2, TP.HCM', 1, NULL),
    ('ST003', 'Lê Thị', 'Quản lý', 'quanly@hotel.com', '1980-03-25', 0, '2018-01-05', '333 Đường C, Quận 3, TP.HCM', 1, NULL),
    ('ST004', 'Phạm Văn', 'Nhân viên 3', 'nhanvien3@hotel.com', '1992-11-10', 1, '2021-02-20', '444 Đường D, Quận 4, TP.HCM', 1, NULL),
    ('ST005', 'Hoàng Thị', 'Nhân viên 4', 'nhanvien4@hotel.com', '1995-07-30', 0, '2022-03-01', '555 Đường E, Quận 5, TP.HCM', 1, NULL);

INSERT INTO accounts (username, password, role, staff_id)
VALUES
    ('manager', '$2a$10$xJwL5v5Jz5U5Z5e5v5e5ve5v5e5v5e5v5e5v5e5v5e5v5e5v5e5v', 'MANAGER', 'ST003'),
    ('staff1', '$2a$10$xJwL5v5Jz5U5Z5e5v5e5ve5v5e5v5e5v5e5v5e5v5e5v5e5v5e5v', 'STAFF', 'ST001'),
    ('staff2', '$2a$10$xJwL5v5Jz5U5Z5e5v5e5ve5v5e5v5e5v5e5v5e5v5e5v5e5v5e5v', 'STAFF', 'ST002'),
    ('staff3', '$2a$10$xJwL5v5Jz5U5Z5e5v5e5ve5v5e5v5e5v5e5v5e5v5e5v5e5v5e5v', 'STAFF', 'ST004'),
    ('staff4', '$2a$10$xJwL5v5Jz5U5Z5e5v5e5ve5v5e5v5e5v5e5v5e5v5e5v5e5v5e5v', 'STAFF', 'ST005');

INSERT INTO phones (staff_id, phones)
VALUES
    ('ST001', '0911111111'),
    ('ST002', '0922222222'),
    ('ST003', '0933333333'),
    ('ST004', '0944444444'),
    ('ST005', '0955555555');

INSERT INTO reservations (reservation_id, customer_id, room_id, staff_id, booking_date, booking_type, bookingMethod, check_in_date, check_out_date, check_in_time, check_out_time, number_of_nights, duration_hours, hourly_rate, deposit_amount, remaining_amount, total_price, status, reservation_status, overstay_fee, overstay_units, actual_checkout_time)
VALUES
    ('R001', 'C001', 'P101', 'ST001', '2023-05-01 10:00:00', 'NIGHT', 'AT_THE_COUNTER', '2023-05-10 14:00:00', '2023-05-12 12:00:00', NULL, NULL, 2, NULL, NULL, 480000, 1120000, 1600000, 1, 2, 0, 0, NULL),
    ('R002', 'C002', 'P201', 'ST002', '2023-05-02 11:00:00', 'HOUR', 'CONTACT', NULL, NULL, '2023-05-15 13:00:00', '2023-05-15 17:00:00', NULL, 4, 200000, 400000, 400000, 800000, 1, 2, 0, 0, NULL),
    ('R003', 'C003', 'P301', 'ST003', '2023-05-03 09:00:00', 'NIGHT', 'AT_THE_COUNTER', '2023-05-20 14:00:00', '2023-05-22 12:00:00', NULL, NULL, 2, NULL, NULL, 1200000, 2800000, 4000000, 1, 1, 0, 0, NULL),
    ('R004', 'C004', 'P401', 'ST004', '2023-05-04 14:00:00', 'HOUR', 'CONTACT', NULL, NULL, '2023-05-18 20:00:00', '2023-05-19 08:00:00', NULL, 12, 250000, 1500000, 1500000, 3000000, 1, 0, 0, 0, NULL),
    ('R005', 'C005', 'P501', 'ST005', '2023-05-05 15:00:00', 'NIGHT', 'AT_THE_COUNTER', '2023-05-25 14:00:00', '2023-05-27 14:00:00', NULL, NULL, 2, NULL, NULL, 1500000, 3500000, 5000000, 1, 0, 0, 0, NULL);

UPDATE reservations SET duration_hours = 1 WHERE duration_hours IS NULL;
UPDATE reservations SET hourly_rate = 0 WHERE hourly_rate IS NULL;

INSERT INTO reservation_details (reservation_id, service_id, quantity, line_total_amount, note)
VALUES
    ('R001', 1, 2, 300000, 'Bữa sáng cho 2 người'),
    ('R001', 3, 1, 100000, 'Giặt 1 bộ đồ'),
    ('R002', 1, 1, 150000, 'Bữa sáng cho 1 người'),
    ('R003', 1, 2, 300000, 'Bữa sáng cho 2 người'),
    ('R003', 4, 1, 500000, 'Massage 60 phút'),
    ('R004', 1, 4, 600000, 'Bữa sáng cho 4 người'),
    ('R005', 1, 2, 300000, 'Bữa sáng cho 2 người'),
    ('R005', 6, 1, 1000000, 'Thuê phòng họp');

INSERT INTO orders (order_id, customer_id, room_id, staff_id, order_date, number_of_nights, paymentMethod, service_fee, tax_amount, total_price, status)
VALUES
    ('ORD001', 'C001', 'P101', 'ST001', '2023-05-12 12:30:00', 2, 'CASH', 400000, 200000, 2200000, 1),
    ('ORD002', 'C002', 'P201', 'ST002', '2023-05-15 17:15:00', NULL, 'E_WALLET', 150000, 95000, 1045000, 1),
    ('ORD003', 'C003', 'P301', 'ST003', '2023-05-22 12:45:00', 2, 'CASH', 800000, 480000, 5280000, 1),
    ('ORD004', 'C004', 'P401', 'ST004', '2023-05-19 08:30:00', NULL, 'E_WALLET', 600000, 360000, 3960000, 1),
    ('ORD005', 'C005', 'P501', 'ST005', '2023-05-27 14:20:00', 2, 'CASH', 1300000, 630000, 6930000, 1);

INSERT INTO order_details (order_id, service_id, quantity, line_total_amount)
VALUES
    ('ORD001', 1, 2, 300000),
    ('ORD001', 3, 1, 100000),
    ('ORD002', 1, 1, 150000),
    ('ORD003', 1, 2, 300000),
    ('ORD003', 4, 1, 500000),
    ('ORD004', 1, 4, 600000),
    ('ORD005', 1, 2, 300000),
    ('ORD005', 6, 1, 1000000);