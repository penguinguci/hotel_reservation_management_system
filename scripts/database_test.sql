INSERT INTO customers (customer_id, cccd, address, point, date_of_birth, email, first_name, gender, last_name, phone_number)
VALUES
    ('C001', '001123456789', '123 Đường Lê Lợi, Quận 1, TP.HCM', 150.5, '1990-05-15', 'nguyenvana@gmail.com', 'Văn', 1, 'Nguyễn', '0903111222'),
    ('C002', '001987654321', '456 Đường Nguyễn Huệ, Quận 1, TP.HCM', 75.0, '1985-08-20', 'tranthib@gmail.com', 'Thị', 0, 'Trần', '0903111333'),
    ('C003', '002112233445', '789 Đường Cách Mạng Tháng 8, Quận 3, TP.HCM', 200.0, '1995-02-10', 'lehoangc@gmail.com', 'Hoàng', 1, 'Lê', '0903111444'),
    ('C004', '002556677889', '321 Đường Võ Văn Tần, Quận 3, TP.HCM', 50.0, '1988-11-25', 'phamthid@gmail.com', 'Thị', 0, 'Phạm', '0903111555'),
    ('C005', '003998877665', '654 Đường Hai Bà Trưng, Quận 1, TP.HCM', 300.0, '1992-07-30', 'vuquoce@gmail.com', 'Quốc', 1, 'Vũ', '0903111666');

INSERT INTO room_type (type_id, description, type_name)
VALUES
    ('RT001', 'Phòng tiêu chuẩn với giường đơn, phòng tắm riêng', 'Phòng Standard'),
    ('RT002', 'Phòng cao cấp với giường đôi, view thành phố', 'Phòng Deluxe'),
    ('RT003', 'Phòng hạng sang với giường king size, bồn tắm', 'Phòng Suite'),
    ('RT004', 'Phòng gia đình với 2 giường đơn', 'Phòng Family'),
    ('RT005', 'Phòng tổng thống với đầy đủ tiện nghi cao cấp', 'Phòng President');

-- Insert data into `rooms`
INSERT INTO rooms (room_id, capacity, price, room_image, room_size, status, type_id, floor)
VALUES
    ('R001', 2, 150.0, 'deluxe.jpg', 25.0, 1, 'RT002',1),
    ('R002', 1, 100.0, 'standard.jpg', 18.0, 1, 'RT001',1),
    ('R003', 3, 200.0, 'family.jpg', 30.0, 1, 'RT001',1),
    ('R004', 4, 300.0, 'suite.jpg', 40.0, 1, 'RT001',1),
    ('R005', 1, 80.0, 'single.jpg', 15.0, 1, 'RT001',2),
    ('R006', 2, 120.0, 'standard_plus.jpg', 20.0, 1, 'RT002',2),
    ('R007', 5, 400.0, 'penthouse.jpg', 50.0, 1, 'RT002',2),
    ('R008', 3, 12200.0, 'family.jpg', 30.0, 1, 'RT002',2),
    ('R009', 4, 30000.0, 'suite.jpg', 40.0, 1, 'RT002',3),
    ('R010', 1, 80000.0, 'single.jpg', 15.0, 1, 'RT002',3),
    ('R011', 2, 100000.0, 'standard_plus.jpg', 20.0, 1, 'RT002',3),
    ('R012', 5, 4000000.0, 'penthouse.jpg', 50.0, 1, 'RT002',3),
    ('R013', 3, 12200.0, 'family.jpg', 30.0, 0, 'RT002',4),
    ('R014', 4, 30000.0, 'suite.jpg', 40.0, 0, 'RT001',4),
    ('R015', 1, 80000.0, 'single.jpg', 15.0, 0, 'RT002',4),
    ('R016', 2, 100000.0, 'standard_plus.jpg', 20.0, 0, 'RT001',4),
    ('R017', 5, 4000000.0, 'penthouse.jpg', 50.0, 0, 'RT001',5);

-- Insert data into `amentities`
INSERT INTO amentities (room_id, amentities)
VALUES
    ('R001', 'TV'),
    ('R001', 'Điều hòa'),
    ('R001', 'Wifi'),
    ('R002', 'TV'),
    ('R002', 'Điều hòa'),
    ('R002', 'Wifi'),
    ('R002', 'Mini bar'),
    ('R003', 'TV'),
    ('R003', 'Điều hòa'),
    ('R003', 'Wifi'),
    ('R003', '2 giường đơn'),
    ('R004', 'TV màn hình phẳng'),
    ('R004', 'Điều hòa'),
    ('R004', 'Wifi tốc độ cao'),
    ('R004', 'Bồn tắm'),
    ('R004', 'Mini bar'),
    ('R005', 'TV màn hình phẳng 55 inch'),
    ('R005', 'Điều hòa'),
    ('R005', 'Wifi tốc độ cao'),
    ('R005', 'Bồn tắm Jacuzzi'),
    ('R005', 'Mini bar cao cấp'),
    ('R005', 'Phòng làm việc');

INSERT INTO services (service_id, availability, description, name, price)
VALUES
    (1, 1, 'Bữa sáng buffet với nhiều lựa chọn', 'Bữa sáng', 150000),
    (2, 1, 'Dịch vụ giặt ủi trong ngày', 'Giặt ủi', 100000),
    (3, 1, 'Đưa đón sân bay bằng xe hơi đời mới', 'Đưa đón sân bay', 300000),
    (4, 1, 'Massage thư giãn 60 phút', 'Massage', 500000),
    (5, 1, 'Dịch vụ phòng 24/24', 'Dịch vụ phòng', 0),
    (6, 1, 'Thuê xe máy tham quan thành phố', 'Thuê xe máy', 200000),
    (7, 1, 'Bể bơi vô cực trên tầng thượng', 'Bể bơi', 100000);

INSERT INTO staffs (staff_id, address, date_of_birth, date_of_join, email, first_name, gender, last_name, staff_image, status)
VALUES
    ('ST001', '111 Đường Nguyễn Văn Linh, Quận 7, TP.HCM', '1980-03-10', '2015-06-15', 'manager@hotel.com', 'Văn', 1, 'Trần', NULL, 1),
    ('ST002', '222 Đường Lê Văn Việt, Quận 9, TP.HCM', '1985-07-20', '2018-02-10', 'reception1@hotel.com', 'Thị', 0, 'Nguyễn', NULL, 1),
    ('ST003', '333 Đường Nguyễn Thị Minh Khai, Quận 3, TP.HCM', '1990-11-15', '2019-05-20', 'reception2@hotel.com', 'Hồng', 0, 'Lê', NULL, 1),
    ('ST004', '444 Đường Võ Văn Ngân, Thủ Đức, TP.HCM', '1992-04-25', '2020-08-05', 'housekeeping1@hotel.com', 'Minh', 1, 'Phạm', NULL, 1),
    ('ST005', '555 Đường Phạm Văn Đồng, Bình Thạnh, TP.HCM', '1988-09-30', '2017-11-12', 'housekeeping2@hotel.com', 'Thanh', 0, 'Vũ', NULL, 1);

INSERT INTO accounts (username, password, role, staff_id)
VALUES
    ('manager', '123456', 'MANAGER', 'ST001'),
    ('reception1', '123456', 'STAFF', 'ST002'),
    ('reception2', '123456', 'STAFF', 'ST003'),
    ('housekeep1', '123456', 'STAFF', 'ST004'),
    ('housekeep2', '123456', 'STAFF', 'ST005');

INSERT INTO phones (staff_id, phones)
VALUES
    ('ST001', '0905111222'),
    ('ST002', '0905111333'),
    ('ST002', '02838223344'),
    ('ST003', '0905111444'),
    ('ST004', '0905111555'),
    ('ST005', '0905111666');

INSERT INTO orders (order_id, number_of_nights, order_date, paymentMethod, status, total_price, customer_id, room_id, staff_id)
VALUES
    ('ORD001', 3, '2023-10-01 14:30:00', 'CASH', 1, 3600000, 'C001', 'R001', 'ST002'),
    ('ORD002', 2, '2023-10-02 15:45:00', 'E_WALLET', 1, 3600000, 'C002', 'R002', 'ST003'),
    ('ORD003', 1, '2023-10-03 10:15:00', 'CASH', 1, 2500000, 'C003', 'R003', 'ST002'),
    ('ORD004', 5, '2023-10-04 16:20:00', 'E_WALLET', 1, 17500000, 'C004', 'R004', 'ST003'),
    ('ORD005', 2, '2023-10-05 11:30:00', 'CASH', 0, 16000000, 'C005', 'R005', 'ST002');

INSERT INTO order_details (line_total_amount, quantity, service_id, order_id)
VALUES
    (450000, 3, 1, 'ORD001'),
    (200000, 2, 6, 'ORD001'),
    (300000, 1, 3, 'ORD002'),
    (500000, 1, 4, 'ORD004'),
    (100000, 5, 2, 'ORD004'),
    (300000, 1, 3, 'ORD005');

INSERT INTO reservations (reservation_id, bookingMethod, check_in_date, check_out_date, deposit_amount, number_of_nights, remaining_amount, status, total_price, customer_id, room_id, staff_id, booking_date)
VALUES
    ('RES001', 'AT_THE_COUNTER', '2023-11-01 14:00:00', '2023-11-04 12:00:00', 1000000, 3, 2600000, 1, 3600000, 'C001', 'R001', 'ST002', '2023-11-01 14:00:00'),
    ('RES002', 'CONTACT', '2023-11-05 14:00:00', '2023-11-07 12:00:00', 1800000, 2, 1800000, 1, 3600000, 'C002', 'R002', 'ST003', '2023-11-01 14:00:00'),
    ('RES003', 'AT_THE_COUNTER', '2023-11-10 14:00:00', '2023-11-11 12:00:00', 1000000, 1, 1500000, 0, 2500000, 'C003', 'R003', 'ST002', '2023-11-01 14:00:00'),
    ('RES004', 'CONTACT', '2023-11-15 14:00:00', '2023-11-20 12:00:00', 5000000, 5, 12500000, 1, 17500000, 'C004', 'R004', 'ST003', '2023-11-01 14:00:00'),
    ('RES005', 'AT_THE_COUNTER', '2023-11-25 14:00:00', '2023-11-27 12:00:00', 8000000, 2, 8000000, 1, 16000000, 'C005', 'R005', 'ST002', '2023-11-01 14:00:00');


INSERT INTO reservation_details (line_total_amount, note, quantity, service_id, reservation_id)
VALUES
    (450000, 'Bữa sáng mỗi ngày', 3, 1, 'RES001'),
    (300000, 'Đón từ sân bay Tân Sơn Nhất', 1, 3, 'RES002'),
    (500000, 'Massage buổi tối', 1, 4, 'RES004'),
    (1000000, 'Giặt ủi mỗi ngày', 5, 2, 'RES004'),
    (400000, 'Thuê 2 xe máy', 2, 6, 'RES005');