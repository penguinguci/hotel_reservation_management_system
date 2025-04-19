INSERT INTO customers (customer_id, cccd, address, point, date_of_birth, email, first_name, gender, last_name, phone_number)
VALUES
    ('C001', '079200000001', '123 Đường Lê Lợi, Quận 1, TP.HCM', 100.5, '1990-05-15', 'nguyenvana@gmail.com', 'Văn', 1, 'Nguyễn', '0903111222'),
    ('C002', '079200000002', '456 Đường Nguyễn Huệ, Quận 1, TP.HCM', 50.0, '1985-08-20', 'tranthib@gmail.com', 'Thị', 0, 'Trần', '0903111333'),
    ('C003', '079200000003', '789 Đường Cách Mạng Tháng 8, Quận 3, TP.HCM', 75.2, '1995-03-10', 'levanc@gmail.com', 'Văn', 1, 'Lê', '0903111444'),
    ('C004', '079200000004', '321 Đường Hai Bà Trưng, Quận 1, TP.HCM', 120.0, '1988-11-25', 'phamthid@gmail.com', 'Thị', 0, 'Phạm', '0903111555'),
    ('C005', '079200000005', '654 Đường Lê Văn Sỹ, Quận Phú Nhuận, TP.HCM', 30.7, '1992-07-30', 'hoangvane@gmail.com', 'Văn', 1, 'Hoàng', '0903111666');

INSERT INTO room_type (type_id, description, type_name)
VALUES
    ('RT001', 'Phòng tiêu chuẩn với giường đơn, phù hợp cho 1-2 người', 'Phòng Tiêu Chuẩn'),
    ('RT002', 'Phòng cao cấp với giường đôi, view thành phố', 'Phòng Cao Cấp'),
    ('RT003', 'Phòng VIP với không gian rộng rãi, tiện nghi sang trọng', 'Phòng VIP'),
    ('RT004', 'Phòng gia đình với 2 giường, phù hợp cho 4 người', 'Phòng Gia Đình'),
    ('RT005', 'Phòng Suite với phòng khách riêng biệt', 'Phòng Suite');


INSERT INTO rooms (room_id, capacity, floor, hourly_base_rate, max_hours, min_hours, price, room_image, room_size, status, type_id)
VALUES
    ('R101', 2, 1, 100000, 12, 2, 800000, 'https://example.com/room1.jpg', 20.5, 0, 'RT001'),
    ('R102', 2, 1, 120000, 12, 2, 900000, 'https://example.com/room2.jpg', 22.0, 0, 'RT001'),
    ('R201', 2, 2, 150000, 12, 2, 1200000, 'https://example.com/room3.jpg', 25.0, 0, 'RT002'),
    ('R202', 2, 2, 180000, 12, 2, 1500000, 'https://example.com/room4.jpg', 28.0, 0, 'RT003'),
    ('R301', 4, 3, 200000, 12, 2, 2000000, 'https://example.com/room5.jpg', 35.0, 0, 'RT004'),
    ('R302', 2, 3, 250000, 12, 2, 2500000, 'https://example.com/room6.jpg', 40.0, 0, 'RT005');


INSERT INTO amentities (room_id, amentities)
VALUES
    ('R101', 'TV'),
    ('R101', 'Điều hòa'),
    ('R101', 'Wifi'),
    ('R102', 'TV'),
    ('R102', 'Điều hòa'),
    ('R102', 'Wifi'),
    ('R201', 'TV'),
    ('R201', 'Điều hòa'),
    ('R201', 'Wifi'),
    ('R201', 'Mini bar'),
    ('R202', 'TV'),
    ('R202', 'Điều hòa'),
    ('R202', 'Wifi'),
    ('R202', 'Mini bar'),
    ('R202', 'Bồn tắm'),
    ('R301', 'TV'),
    ('R301', 'Điều hòa'),
    ('R301', 'Wifi'),
    ('R301', '2 giường'),
    ('R302', 'TV'),
    ('R302', 'Điều hòa'),
    ('R302', 'Wifi'),
    ('R302', 'Mini bar'),
    ('R302', 'Bồn tắm'),
    ('R302', 'Phòng khách riêng');

INSERT INTO hourly_price_rules (room_id, multiplier, hour_range)
VALUES
    ('R101', 1.0, '0-6'),
    ('R101', 0.9, '6-12'),
    ('R101', 0.8, '12-24'),
    ('R102', 1.0, '0-6'),
    ('R102', 0.9, '6-12'),
    ('R102', 0.8, '12-24'),
    ('R201', 1.0, '0-6'),
    ('R201', 0.9, '6-12'),
    ('R201', 0.8, '12-24'),
    ('R202', 1.0, '0-6'),
    ('R202', 0.9, '6-12'),
    ('R202', 0.8, '12-24'),
    ('R301', 1.0, '0-6'),
    ('R301', 0.9, '6-12'),
    ('R301', 0.8, '12-24'),
    ('R302', 1.0, '0-6'),
    ('R302', 0.9, '6-12'),
    ('R302', 0.8, '12-24');


INSERT INTO services (service_id, availability, description, name, price)
VALUES
    (1, 1, 'Dịch vụ ăn sáng buffet phong phú', 'Ăn sáng', 150000),
    (2, 1, 'Dịch vụ giặt ủi nhanh chóng', 'Giặt ủi', 100000),
    (3, 1, 'Dịch vụ massage thư giãn', 'Massage', 300000),
    (4, 1, 'Dịch vụ đưa đón sân bay', 'Đưa đón sân bay', 500000),
    (5, 1, 'Dịch vụ phòng gym 24/7', 'Phòng gym', 0),
    (6, 1, 'Dịch vụ hồ bơi', 'Hồ bơi', 0),
    (7, 1, 'Dịch vụ thuê xe tự lái', 'Thuê xe', 800000);


INSERT INTO staffs (staff_id, address, date_of_birth, date_of_join, email, first_name, gender, last_name, staff_image, status)
VALUES
    ('ST001', '111 Đường Nguyễn Trãi, Quận 5, TP.HCM', '1980-01-10', '2020-05-15', 'manager@hotel.com', 'Minh', 1, 'Nguyễn', NULL, 1),
    ('ST002', '222 Đường Trần Hưng Đạo, Quận 1, TP.HCM', '1985-06-20', '2021-02-10', 'reception1@hotel.com', 'Hồng', 0, 'Lê', NULL, 1),
    ('ST003', '333 Đường Lý Thường Kiệt, Quận 10, TP.HCM', '1990-03-15', '2021-07-22', 'reception2@hotel.com', 'Tuấn', 1, 'Trần', NULL, 1),
    ('ST004', '444 Đường Cộng Hòa, Quận Tân Bình, TP.HCM', '1992-11-05', '2022-01-05', 'housekeeping1@hotel.com', 'Lan', 0, 'Phạm', NULL, 1),
    ('ST005', '555 Đường Lê Văn Việt, Quận 9, TP.HCM', '1995-09-30', '2022-03-18', 'housekeeping2@hotel.com', 'Khánh', 1, 'Hoàng', NULL, 1);


INSERT INTO accounts (username, password, role, staff_id)
VALUES
    ('manager', '123456', 'MANAGER', 'ST001'),
    ('reception1', '123456', 'STAFF', 'ST002'),
    ('reception2', '123456', 'STAFF', 'ST003'),
    ('housekeeping1', '123456', 'STAFF', 'ST004'),
    ('housekeeping2', '123456', 'STAFF', 'ST005');


INSERT INTO phones (staff_id, phones)
VALUES
    ('ST001', '0909111222'),
    ('ST002', '0909111333'),
    ('ST003', '0909111444'),
    ('ST004', '0909111555'),
    ('ST005', '0909111666');


INSERT INTO orders (order_id, number_of_nights, order_date, paymentMethod, status, total_price, customer_id, room_id, staff_id)
VALUES
    ('ORD001', 2, '2023-10-01 14:30:00', 'CASH', 1, 1600000, 'C001', 'R101', 'ST002'),
    ('ORD002', 1, '2023-10-02 15:45:00', 'E_WALLET', 1, 1200000, 'C002', 'R201', 'ST003'),
    ('ORD003', 3, '2023-10-03 10:15:00', 'CASH', 1, 6000000, 'C003', 'R301', 'ST002'),
    ('ORD004', 2, '2023-10-04 16:20:00', 'E_WALLET', 1, 3000000, 'C004', 'R202', 'ST003'),
    ('ORD005', 1, '2023-10-05 11:30:00', 'CASH', 1, 2500000, 'C005', 'R302', 'ST002');


INSERT INTO order_details (line_total_amount, quantity, service_id, order_id)
VALUES
    (300000, 2, 1, 'ORD001'),
    (200000, 2, 2, 'ORD001'),
    (300000, 1, 3, 'ORD002'),
    (500000, 1, 4, 'ORD003'),
    (300000, 1, 1, 'ORD004'),
    (300000, 1, 3, 'ORD005');


INSERT INTO reservations (reservation_id, booking_date, bookingMethod, booking_type, check_in_date, check_in_time, check_out_date, check_out_time, deposit_amount, duration_hours, hourly_rate, number_of_nights, remaining_amount, status, total_price, customer_id, room_id, staff_id)
VALUES
    ('RES001', '2023-10-01 10:00:00', 'AT_THE_COUNTER', 'HOUR', '2023-10-01', '2023-10-01 14:00:00', '2023-10-01', '2023-10-01 18:00:00', 200000, 4, 100000, 1, 200000, 1, 400000, 'C001', 'R101', 'ST002'),
    ('RES002', '2023-10-02 09:30:00', 'CONTACT', 'NIGHT', '2023-10-02', '2023-10-02 14:00:00', '2023-10-03', '2023-10-03 12:00:00', 600000, 1, 100000, 1, 600000, 1, 1200000, 'C002', 'R201', 'ST003'),
    ('RES003', '2023-10-03 11:15:00', 'AT_THE_COUNTER', 'HOUR', '2023-10-03', '2023-10-03 16:00:00', '2023-10-04', '2023-10-04 08:00:00', 500000, 16, 120000, 1, 1420000, 1, 1920000, 'C003', 'R102', 'ST002'),
    ('RES004', '2023-10-04 14:20:00', 'CONTACT', 'NIGHT', '2023-10-04', '2023-10-04 14:00:00', '2023-10-06', '2023-10-06 12:00:00', 750000, 1, 100000, 2, 2250000, 1, 3000000, 'C004', 'R202', 'ST003'),
    ('RES005', '2023-10-05 10:30:00', 'AT_THE_COUNTER', 'NIGHT', '2023-10-05', '2023-10-05 14:00:00', '2023-10-07', '2023-10-07 12:00:00', 1250000, 1, 100000, 2, 3750000, 1, 5000000, 'C005', 'R302', 'ST002');


INSERT INTO reservation_details (line_total_amount, note, quantity, service_id, reservation_id)
VALUES
    (150000, 'Ăn sáng cho 1 người', 1, 1, 'RES001'),
    (300000, 'Massage 60 phút', 1, 3, 'RES002'),
    (300000, 'Ăn sáng cho 2 người', 2, 1, 'RES003'),
    (500000, 'Đưa đón sân bay', 1, 4, 'RES004'),
    (600000, 'Massage cho 2 người', 2, 3, 'RES005');


