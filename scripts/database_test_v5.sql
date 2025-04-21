-- 1. Thêm dữ liệu vào customers
INSERT INTO customers (customer_id, cccd, address, point, date_of_birth, email, first_name, gender, last_name, phone_number) VALUES
                                                                                                                                 ('C001', '123456789012', '123 Đường Láng, Đống Đa, Hà Nội', 100, '1990-05-15 00:00:00', 'nguyenvana@gmail.com', 'Văn', 1, 'Nguyễn', '0901234567'),
                                                                                                                                 ('C002', '987654321012', '456 Lê Lợi, Quận 1, TP.HCM', 50, '1985-10-20 00:00:00', 'tranb@gmail.com', 'Thị', 0, 'Trần', '0912345678'),
                                                                                                                                 ('C003', '456789123012', '789 Nguyễn Huệ, Đà Nẵng', 0, '1995-03-10 00:00:00', 'lethic@gmail.com', 'Thị', 0, 'Lê', '0923456789'),
                                                                                                                                 ('C004', '321654987012', '101 Hai Bà Trưng, Huế', 200, '1988-07-25 00:00:00', 'phamd@gmail.com', 'Đình', 1, 'Phạm', '0934567890'),
                                                                                                                                 ('C005', '654123789012', '202 Trần Phú, Nha Trang', 150, '1992-11-30 00:00:00', 'hoangvane@gmail.com', 'Văn', 1, 'Hoàng', '0945678901');

-- 2. Thêm dữ liệu vào room_type
INSERT INTO room_type (type_id, description, type_name) VALUES
                                                            ('RT001', 'Phòng tiêu chuẩn với 1 giường đôi, thích hợp cho cặp đôi', 'Phòng Đôi Tiêu Chuẩn'),
                                                            ('RT002', 'Phòng cao cấp với 2 giường đơn, có ban công', 'Phòng Đôi Cao Cấp'),
                                                            ('RT003', 'Phòng VIP với giường king, bồn tắm và view biển', 'Phòng VIP'),
                                                            ('RT004', 'Phòng gia đình với 2 giường đôi, thích hợp cho 4 người', 'Phòng Gia Đình'),
                                                            ('RT005', 'Phòng đơn nhỏ gọn, thích hợp cho khách đi công tác', 'Phòng Đơn');

-- 3. Thêm dữ liệu vào rooms
INSERT INTO rooms (room_id, capacity, floor, hourly_base_rate, max_hours, min_hours, price, room_image, room_size, standard_checkout_hour, status, type_id) VALUES
                                                                                                                                                                ('R001', 2, 1, 100000, 6, 2, 500000, '/images/room001.jpg', 20.5, 12, 1, 'RT001'),
                                                                                                                                                                ('R002', 2, 2, 120000, 6, 2, 600000, '/images/room002.jpg', 22.0, 12, 1, 'RT002'),
                                                                                                                                                                ('R003', 2, 3, 150000, 6, 2, 800000, '/images/room003.jpg', 25.0, 12, 1, 'RT003'),
                                                                                                                                                                ('R004', 4, 4, 180000, 8, 3, 1000000, '/images/room004.jpg', 30.0, 12, 1, 'RT004'),
                                                                                                                                                                ('R005', 1, 5, 80000, 5, 2, 400000, '/images/room005.jpg', 15.0, 12, 1, 'RT005');

INSERT INTO rooms (room_id, capacity, floor, hourly_base_rate, max_hours, min_hours, price, room_image, room_size, standard_checkout_hour, status, type_id) VALUES
                                                                                                                                                                ('R011', 2, 1, 100000, 6, 2, 500000, '/images/room001.jpg', 20.5, 12, 0, 'RT001'),
                                                                                                                                                                ('R012', 2, 2, 120000, 6, 2, 600000, '/images/room002.jpg', 22.0, 12, 0, 'RT002'),
                                                                                                                                                                ('R013', 2, 3, 150000, 6, 2, 800000, '/images/room003.jpg', 25.0, 12, 0, 'RT003'),
                                                                                                                                                                ('R014', 4, 4, 180000, 8, 3, 1000000, '/images/room004.jpg', 30.0, 12, 0, 'RT004'),
                                                                                                                                                                ('R015', 1, 5, 80000, 5, 2, 400000, '/images/room005.jpg', 15.0, 12, 0, 'RT005');

-- 4. Thêm dữ liệu vào amentities
INSERT INTO amentities (room_id, amentities) VALUES
                                                 ('R001', 'Wi-Fi miễn phí'), ('R001', 'Điều hòa'), ('R001', 'TV màn hình phẳng'),
                                                 ('R002', 'Wi-Fi miễn phí'), ('R002', 'Ban công'), ('R002', 'Tủ lạnh'),
                                                 ('R003', 'Wi-Fi miễn phí'), ('R003', 'Bồn tắm'), ('R003', 'View biển'),
                                                 ('R004', 'Wi-Fi miễn phí'), ('R004', 'Điều hòa'), ('R004', 'Bàn ăn'),
                                                 ('R005', 'Wi-Fi miễn phí'), ('R005', 'Bàn làm việc'), ('R005', 'TV màn hình phẳng');

-- 5. Thêm dữ liệu vào hourly_price_rules
INSERT INTO hourly_price_rules (room_id, multiplier, hour_range) VALUES
                                                                     ('R001', 1.0, '06:00-18:00'), ('R001', 1.2, '18:00-06:00'),
                                                                     ('R002', 1.0, '06:00-18:00'), ('R002', 1.3, '18:00-06:00'),
                                                                     ('R003', 1.0, '06:00-18:00'), ('R003', 1.4, '18:00-06:00'),
                                                                     ('R004', 1.0, '06:00-18:00'), ('R004', 1.2, '18:00-06:00'),
                                                                     ('R005', 1.0, '06:00-18:00'), ('R005', 1.1, '18:00-06:00');

-- 6. Thêm dữ liệu vào services
INSERT INTO services (service_id, availability, description, name, price) VALUES
                                                                              (1, 1, 'Nước suối đóng chai 500ml', 'Nước suối', 20000),
                                                                              (2, 1, 'Bia Tiger chai 330ml', 'Bia Tiger', 30000),
                                                                              (3, 1, 'Giặt là quần áo theo kg', 'Giặt là', 50000),
                                                                              (4, 1, 'Combo ăn sáng buffet', 'Bữa sáng', 100000),
                                                                              (5, 1, 'Thuê xe máy 24 giờ', 'Thuê xe máy', 150000);

-- 7. Thêm dữ liệu vào staffs
INSERT INTO staffs (staff_id, address, date_of_birth, date_of_join, email, first_name, gender, last_name, staff_image, status) VALUES
                                                                                                                                   ('ST001', '123 Nguyễn Trãi, Hà Nội', '1990-03-10 00:00:00', '2020-01-01 00:00:00', 'tranb@hotel.com', 'Thị', 0, 'Trần', '/images/staff001.jpg', 1),
                                                                                                                                   ('ST002', '456 Lê Văn Sỹ, TP.HCM', '1988-07-15 00:00:00', '2019-06-01 00:00:00', 'nguyenc@hotel.com', 'Văn', 1, 'Nguyễn', '/images/staff002.jpg', 1),
                                                                                                                                   ('ST003', '789 Trần Hưng Đạo, Đà Nẵng', '1992-11-20 00:00:00', '2021-03-01 00:00:00', 'lethid@hotel.com', 'Thị', 0, 'Lê', '/images/staff003.jpg', 1),
                                                                                                                                   ('ST004', '101 Hai Bà Trưng, Huế', '1985-05-25 00:00:00', '2018-09-01 00:00:00', 'phame@hotel.com', 'Đình', 1, 'Phạm', '/images/staff004.jpg', 1),
                                                                                                                                   ('ST005', '202 Trần Phú, Nha Trang', '1995-09-30 00:00:00', '2022-01-01 00:00:00', 'hoangf@hotel.com', 'Văn', 1, 'Hoàng', '/images/staff005.jpg', 1);

-- 8. Thêm dữ liệu vào phones
INSERT INTO phones (staff_id, phones) VALUES
                                          ('ST001', '0901234567'), ('ST001', '0912345678'),
                                          ('ST002', '0923456789'),
                                          ('ST003', '0934567890'),
                                          ('ST004', '0945678901'),
                                          ('ST005', '0956789012');

-- 9. Thêm dữ liệu vào accounts
INSERT INTO accounts (username, password, role, staff_id)
VALUES
    ('manager', '$2a$10$xJwL5v5Jz5U5Z5e5v5e5ve5v5e5v5e5v5e5v5e5v5e5v5e5v5e5v', 'MANAGER', 'ST001'),
    ('staff1', '$2a$10$xJwL5v5Jz5U5Z5e5v5e5ve5v5e5v5e5v5e5v5e5v5e5v5e5v5e5v', 'STAFF', 'ST002'),
    ('staff2', '$2a$10$xJwL5v5Jz5U5Z5e5v5e5ve5v5e5v5e5v5e5v5e5v5e5v5e5v5e5v', 'STAFF', 'ST003'),
    ('staff3', '$2a$10$xJwL5v5Jz5U5Z5e5v5e5ve5v5e5v5e5v5e5v5e5v5e5v5e5v5e5v', 'STAFF', 'ST004'),
    ('staff4', '$2a$10$xJwL5v5Jz5U5Z5e5v5e5ve5v5e5v5e5v5e5v5e5v5e5v5e5v5e5v', 'STAFF', 'ST005');

-- 10. Thêm dữ liệu vào reservations
INSERT INTO reservations (reservation_id, actual_checkout_time, booking_date, bookingMethod, booking_type, check_in_date, check_in_time, check_out_date, check_out_time, deposit_amount, duration_hours, hourly_rate, number_of_nights, overstay_fee, overstay_units, remaining_amount, reservation_status, status, total_price, customer_id, room_id, staff_id) VALUES
                                                                                                                                                                                                                                                                                                                                                                     ('RES001', '2023-10-03 14:00:00', '2023-09-30 10:00:00', 'AT_THE_COUNTER', 'NIGHT', '2023-10-01 14:00:00', NULL, '2023-10-03 12:00:00', NULL, 300000, NULL, NULL, 2, 100000, 1, 770000, 2, 1, 1070000, 'C001', 'R001', 'ST001'),
                                                                                                                                                                                                                                                                                                                                                                     ('RES002', '2023-10-05 14:00:00', '2023-10-01 09:00:00', 'CONTACT', 'NIGHT', '2023-10-03 14:00:00', NULL, '2023-10-05 12:00:00', NULL, 650000, NULL, NULL, 2, 0, 0, 650000, 2, 1, 1300000, 'C002', 'R002', 'ST002'),
                                                                                                                                                                                                                                                                                                                                                                     ('RES003', '2023-10-02 20:00:00', '2023-10-01 08:00:00', 'AT_THE_COUNTER', 'HOUR', NULL, '2023-10-02 14:00:00', NULL, '2023-10-02 18:00:00', 90000, 4, 100000, NULL, 24000, 1, 186000, 2, 1, 276000, 'C003', 'R001', 'ST003'),
                                                                                                                                                                                                                                                                                                                                                                     ('RES004', '2023-10-07 15:00:00', '2023-10-03 11:00:00', 'CONTACT', 'NIGHT', '2023-10-05 14:00:00', NULL, '2023-10-07 12:00:00', NULL, 1000000, NULL, NULL, 2, 200000, 1, 1200000, 2, 1, 2200000, 'C004', 'R004', 'ST004'),
                                                                                                                                                                                                                                                                                                                                                                     ('RES005', '2023-10-02 12:00:00', '2023-10-01 07:00:00', 'AT_THE_COUNTER', 'NIGHT', '2023-10-01 14:00:00', NULL, '2023-10-02 12:00:00', NULL, 120000, NULL, NULL, 1, 0, 0, 280000, 2, 1, 400000, 'C005', 'R005', 'ST005');

UPDATE reservations SET duration_hours = 0 WHERE duration_hours IS NULL;
UPDATE reservations SET hourly_rate = 0 WHERE hourly_rate IS NULL;
UPDATE reservations SET number_of_nights = 0 WHERE hourly_rate IS NULL;
UPDATE reservations SET overstay_fee = 0 WHERE overstay_fee IS NULL;
UPDATE reservations SET overstay_units = 0 WHERE overstay_units IS NULL;


-- 11. Thêm dữ liệu vào reservation_details
INSERT INTO reservation_details (reservation_id, service_id, line_total_amount, note, quantity) VALUES
                                                                                                    ('RES001', 1, 40000, '2 chai nước suối', 2),
                                                                                                    ('RES001', 2, 30000, '1 chai bia', 1),
                                                                                                    ('RES002', 4, 100000, 'Bữa sáng cho 1 người', 1),
                                                                                                    ('RES003', 1, 20000, '1 chai nước suối', 1),
                                                                                                    ('RES004', 3, 100000, 'Giặt 2kg quần áo', 2),
                                                                                                    ('RES004', 4, 200000, 'Bữa sáng cho 2 người', 2),
                                                                                                    ('RES005', 5, 150000, 'Thuê xe máy 1 ngày', 1);



-- 12. Thêm dữ liệu vào orders
INSERT INTO orders (order_id, check_in_date, check_in_time, check_out_date, check_out_time, deposit_amount, number_of_nights, order_date, overstay_fee, paymentMethod, remaining_amount, service_fee, status, tax_amount, total_price, customer_id, room_id, staff_id, duration_hours) VALUES
                                                                                                                                                                                                                                                                           ('O001', '2023-10-01 14:00:00', NULL, '2023-10-03 12:00:00', NULL, 300000, 2, '2023-10-03 14:00:00', 100000, 'CASH', 883500, 3500, 1, 110000, 1000000, 'C001', 'R001', 'ST001', 0),
                                                                                                                                                                                                                                                                           ('O002', '2023-10-03 14:00:00', NULL, '2023-10-05 12:00:00', NULL, 650000, 2, '2023-10-05 14:00:00', 0, 'CREDIT_CARD', 750000, 5000, 1, 120000, 1200000, 'C002', 'R002', 'ST002', 0),
                                                                                                                                                                                                                                                                           ('O003', NULL, '2023-10-02 14:00:00', NULL, '2023-10-02 18:00:00', 90000, NULL, '2023-10-02 20:00:00', 24000, 'CASH', 201400, 1000, 1, 22400, 200000, 'C003', 'R001', 'ST003', 1),
                                                                                                                                                                                                                                                                           ('O004', '2023-10-05 14:00:00', NULL, '2023-10-07 12:00:00', NULL, 1000000, 2, '2023-10-07 15:00:00', 200000, 'BANK_TRANSFER', 1355000, 15000, 1, 220000, 2000000, 'C004', 'R004', 'ST004', 0),
                                                                                                                                                                                                                                                                           ('O005', '2023-10-01 14:00:00', NULL, '2023-10-02 12:00:00', NULL, 120000, 1, '2023-10-02 12:00:00', 0, 'E_WALLET', 445000, 7500, 1, 40000, 400000, 'C005', 'R005', 'ST005', 0);

-- 13. Thêm dữ liệu vào order_details
INSERT INTO order_details (order_id, service_id, line_total_amount, quantity) VALUES
                                                                                  ('O001', 1, 40000, 2),
                                                                                  ('O001', 2, 30000, 1),
                                                                                  ('O002', 4, 100000, 1),
                                                                                  ('O003', 1, 20000, 1),
                                                                                  ('O004', 3, 100000, 2),
                                                                                  ('O004', 4, 200000, 2),
                                                                                  ('O005', 5, 150000, 1);