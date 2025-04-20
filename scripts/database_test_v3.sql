INSERT INTO customers (customer_id, cccd, address, point, date_of_birth, email, first_name, gender, last_name, phone_number)
VALUES
    ('KH001', '079123456789', '123 Đường Lê Lợi, Quận 1, TP.HCM', 150.5, '1985-05-15', 'nguyenvanA@gmail.com', 'Văn', 1, 'Nguyễn', '0903123456'),
    ('KH002', '079987654321', '456 Đường Nguyễn Huệ, Quận 1, TP.HCM', 75.0, '1990-08-20', 'tranthiB@gmail.com', 'Thị', 0, 'Trần', '0903987654'),
    ('KH003', '079112233445', '789 Đường Cách Mạng Tháng 8, Quận 3, TP.HCM', 200.0, '1982-11-30', 'lehoangC@gmail.com', 'Hoàng', 1, 'Lê', '0905111222'),
    ('KH004', '079556677889', '321 Đường Võ Văn Tần, Quận 3, TP.HCM', 50.0, '1995-03-25', 'phamthuyD@gmail.com', 'Thùy', 0, 'Phạm', '0906777888'),
    ('KH005', '079998877665', '654 Đường Pasteur, Quận 1, TP.HCM', 300.0, '1978-07-10', 'vuquangE@gmail.com', 'Quang', 1, 'Vũ', '0908999000');

INSERT INTO room_type (type_id, description, type_name)
VALUES
    ('LT001', 'Phòng tiêu chuẩn với giường đơn, phù hợp cho 1-2 người', 'Phòng Tiêu Chuẩn'),
    ('LT002', 'Phòng cao cấp với giường đôi, view đẹp', 'Phòng Deluxe'),
    ('LT003', 'Phòng sang trọng với nhiều tiện nghi cao cấp', 'Phòng Suite'),
    ('LT004', 'Phòng gia đình với 2 giường, phù hợp cho 4 người', 'Phòng Gia Đình'),
    ('LT005', 'Phòng cao cấp nhất với view toàn cảnh thành phố', 'Phòng Tổng Thống');

INSERT INTO rooms (room_id, capacity, floor, hourly_base_rate, max_hours, min_hours, price, room_image, room_size, status, type_id)
VALUES
    ('P101', 2, 1, 150000, 12, 2, 1200000, 'https://example.com/p101.jpg', 25.5, 1, 'LT001'),
    ('P201', 2, 2, 200000, 12, 2, 1800000, 'https://example.com/p201.jpg', 30.0, 1, 'LT002'),
    ('P301', 4, 3, 250000, 12, 2, 2500000, 'https://example.com/p301.jpg', 45.0, 1, 'LT004'),
    ('P401', 2, 4, 300000, 12, 2, 3500000, 'https://example.com/p401.jpg', 50.0, 1, 'LT003'),
    ('P501', 2, 5, 500000, 12, 2, 5000000, 'https://example.com/p501.jpg', 80.0, 1, 'LT005');

INSERT INTO rooms (room_id, capacity, floor, hourly_base_rate, max_hours, min_hours, price, room_image, room_size, status, type_id)
VALUES
    ('P102', 2, 1, 150000, 12, 2, 1200000, 'https://example.com/p101.jpg', 25.5, 0, 'LT001'),
    ('P202', 2, 2, 200000, 12, 2, 1800000, 'https://example.com/p201.jpg', 30.0, 0, 'LT002'),
    ('P302', 4, 3, 250000, 12, 2, 2500000, 'https://example.com/p301.jpg', 45.0, 0, 'LT004'),
    ('P402', 2, 4, 300000, 12, 2, 3500000, 'https://example.com/p401.jpg', 50.0, 0, 'LT003'),
    ('P502', 2, 5, 500000, 12, 2, 5000000, 'https://example.com/p501.jpg', 80.0, 0, 'LT005');

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
    ('P301', '2 giường đôi'),
    ('P401', 'TV màn hình phẳng'),
    ('P401', 'Điều hòa'),
    ('P401', 'Wifi tốc độ cao'),
    ('P401', 'Mini bar cao cấp'),
    ('P501', 'TV màn hình lớn'),
    ('P501', 'Điều hòa'),
    ('P501', 'Wifi tốc độ cao'),
    ('P501', 'Bồn tắm Jacuzzi'),
    ('P501', 'Quầy bar riêng');

INSERT INTO hourly_price_rules (room_id, multiplier, hour_range)
VALUES
    ('P101', 1.0, '0-4'),
    ('P101', 0.9, '4-8'),
    ('P101', 0.8, '8-12'),
    ('P201', 1.0, '0-4'),
    ('P201', 0.9, '4-8'),
    ('P201', 0.8, '8-12'),
    ('P301', 1.0, '0-4'),
    ('P301', 0.9, '4-8'),
    ('P301', 0.8, '8-12'),
    ('P401', 1.0, '0-4'),
    ('P401', 0.9, '4-8'),
    ('P401', 0.8, '8-12'),
    ('P501', 1.0, '0-4'),
    ('P501', 0.9, '4-8'),
    ('P501', 0.8, '8-12');

INSERT INTO services (service_id, availability, description, name, price)
VALUES
    (1, 1, 'Dịch vụ ăn sáng buffet đa dạng', 'Ăn sáng buffet', 150000),
    (2, 1, 'Dịch vụ giặt ủi nhanh trong ngày', 'Giặt ủi', 100000),
    (3, 1, 'Dịch vụ massage thư giãn', 'Massage', 300000),
    (4, 1, 'Dịch vụ đưa đón sân bay', 'Đưa đón sân bay', 500000),
    (5, 1, 'Dịch vụ phòng gym 24/7', 'Phòng gym', 0);

INSERT INTO staffs (staff_id, address, date_of_birth, date_of_join, email, first_name, gender, last_name, staff_image, status)
VALUES
    ('NV001', '111 Đường Trần Hưng Đạo, Quận 5, TP.HCM', '1990-01-15', '2020-05-10', 'nguyenvanX@gmail.com', 'Văn', 1, 'Nguyễn', NULL, 1),
    ('NV002', '222 Đường Nguyễn Trãi, Quận 5, TP.HCM', '1992-03-20', '2021-02-15', 'tranthiY@gmail.com', 'Thị', 0, 'Trần', NULL, 1),
    ('NV003', '333 Đường Lý Thường Kiệt, Quận 10, TP.HCM', '1988-07-25', '2019-11-01', 'lehoangZ@gmail.com', 'Hoàng', 1, 'Lê', NULL, 1),
    ('NV004', '444 Đường 3/2, Quận 10, TP.HCM', '1995-05-30', '2022-01-20', 'phamthuyT@gmail.com', 'Thùy', 0, 'Phạm', NULL, 1),
    ('NV005', '555 Đường Lê Văn Sỹ, Quận 3, TP.HCM', '1985-09-10', '2018-06-15', 'vuquangK@gmail.com', 'Quang', 1, 'Vũ', NULL, 1);

INSERT INTO accounts (username, password, role, staff_id)
VALUES
    ('manager', '$2a$10$xJwL5v5Jz5U5Z5U5Z5U5Ze', 'MANAGER', 'NV001'),
    ('staff1', '$2a$10$xJwL5v5Jz5U5Z5U5Z5U5Ze', 'STAFF', 'NV002'),
    ('staff2', '$2a$10$xJwL5v5Jz5U5Z5U5Z5U5Ze', 'STAFF', 'NV003'),
    ('staff3', '$2a$10$xJwL5v5Jz5U5Z5U5Z5U5Ze', 'STAFF', 'NV004'),
    ('staff4', '$2a$10$xJwL5v5Jz5U5Z5U5Z5U5Ze', 'STAFF', 'NV005');

INSERT INTO phones (staff_id, phones)
VALUES
    ('NV001', '0901111222'),
    ('NV002', '0902333444'),
    ('NV003', '0903555666'),
    ('NV004', '0904777888'),
    ('NV005', '0905999000');

INSERT INTO orders (order_id, number_of_nights, order_date, paymentMethod, status, total_price, customer_id, room_id, staff_id, service_fee, tax_amount)
VALUES
    ('HD001', 2, '2023-10-01 14:30:00', 'CASH', 1, 3000000, 'KH001', 'P201', 'NV002', 300000, 300000),
    ('HD002', 1, '2023-10-02 15:45:00', 'E_WALLET', 1, 1800000, 'KH002', 'P201', 'NV003', 150000, 180000),
    ('HD003', 3, '2023-10-03 10:15:00', 'CASH', 1, 7500000, 'KH003', 'P301', 'NV002', 450000, 750000),
    ('HD004', 2, '2023-10-04 16:20:00', 'E_WALLET', 1, 7000000, 'KH004', 'P401', 'NV004', 300000, 700000),
    ('HD005', 1, '2023-10-05 12:00:00', 'CASH', 1, 5000000, 'KH005', 'P501', 'NV005', 500000, 500000);

INSERT INTO order_details (line_total_amount, quantity, service_id, order_id)
VALUES
    (300000, 2, 1, 'HD001'),
    (200000, 2, 2, 'HD002'),
    (450000, 3, 1, 'HD003'),
    (300000, 2, 1, 'HD004'),
    (500000, 1, 3, 'HD005');

INSERT INTO reservations (reservation_id, booking_date, bookingMethod, booking_type, check_in_date, check_in_time, check_out_date, check_out_time, deposit_amount, duration_hours, hourly_rate, number_of_nights, remaining_amount, status, total_price, customer_id, room_id, staff_id, reservation_status)
VALUES
    ('DP001', '2023-10-01 10:00:00', 'AT_THE_COUNTER', 'NIGHT', '2023-10-05', '2023-10-05 14:00:00', '2023-10-07', '2023-10-07 12:00:00', 1000000, NULL, NULL, 2, 2000000, 1, 3000000, 'KH001', 'P201', 'NV002', 1),
    ('DP002', '2023-10-02 11:30:00', 'CONTACT', 'HOUR', '2023-10-06', '2023-10-06 08:00:00', NULL, '2023-10-06 20:00:00', 500000, 12, 200000, NULL, 1900000, 1, 2400000, 'KH002', 'P201', 'NV003', 1),
    ('DP003', '2023-10-03 09:15:00', 'AT_THE_COUNTER', 'NIGHT', '2023-10-07', '2023-10-07 14:00:00', '2023-10-10', '2023-10-10 12:00:00', 1500000, NULL, NULL, 3, 6000000, 1, 7500000, 'KH003', 'P301', 'NV002', 1),
    ('DP004', '2023-10-04 14:20:00', 'CONTACT', 'NIGHT', '2023-10-08', '2023-10-08 14:00:00', '2023-10-10', '2023-10-10 12:00:00', 2000000, NULL, NULL, 2, 5000000, 1, 7000000, 'KH004', 'P401', 'NV004', 1),
    ('DP005', '2023-10-05 13:00:00', 'AT_THE_COUNTER', 'NIGHT', '2023-10-09', '2023-10-09 14:00:00', '2023-10-10', '2023-10-10 12:00:00', 2500000, NULL, NULL, 1, 2500000, 1, 5000000, 'KH005', 'P501', 'NV005', 1);

UPDATE reservations SET duration_hours = 1 WHERE duration_hours IS NULL;
UPDATE reservations SET hourly_rate = 70000 WHERE hourly_rate IS NULL;

INSERT INTO reservation_details (line_total_amount, note, quantity, service_id, reservation_id)
VALUES
    (300000, 'Yêu cầu ăn chay', 2, 1, 'DP001'),
    (200000, NULL, 2, 2, 'DP002'),
    (450000, 'Yêu cầu đồ ăn trẻ em', 3, 1, 'DP003'),
    (300000, NULL, 2, 1, 'DP004'),
    (500000, 'Massage vào buổi tối', 1, 3, 'DP005');