-- Insert data into `customers`
INSERT INTO customers (customer_id, address, point, date_of_birth, email, first_name, gender, last_name, phone_number)
VALUES
    ('C001', '123 Main Street', 100, '1990-01-15', 'john.doe@example.com', 'John', 1, 'Doe', '1234567890'),
    ('C002', '456 Elm Street', 200, '1985-07-20', 'jane.smith@example.com', 'Jane', 0, 'Smith', '0987654321');

INSERT INTO services (service_id, availability, description, name, price)
VALUES
    (1, 1, 'Dịch vụ ăn sáng buffet', 'Ăn sáng', 100000),
    (2, 1, 'Dịch vụ spa thư giãn', 'Spa', 500000),
    (3, 1, 'Dịch vụ đưa đón sân bay', 'Đưa đón sân bay', 300000),
    (4, 1, 'Dịch vụ giặt là quần áo', 'Giặt là', 200000),
    (5, 1, 'Dịch vụ giải trí tại phòng', 'Giải trí tại phòng', 150000),
    (6, 1, 'Dịch vụ gọi đồ ăn tại phòng', 'Gọi đồ ăn tại phòng', 250000),
    (7, 1, 'Dịch vụ phòng họp và hội thảo', 'Phòng họp', 1000000),
    (8, 1, 'Dịch vụ massage chuyên sâu', 'Massage', 600000),
    (9, 1, 'Dịch vụ hồ bơi', 'Hồ bơi', 200000),
    (10, 1, 'Dịch vụ cho thuê xe đạp', 'Thuê xe đạp', 50000);


-- Insert data into `room_type`
INSERT INTO room_type (type_id, description, type_name)
VALUES
    ('RT01', 'Spacious room with modern amenities', 'Deluxe'),
    ('RT02', 'Budget-friendly room with basic facilities', 'Standard');

-- Insert data into `rooms`
INSERT INTO rooms (room_id, capacity, price, room_image, room_size, status, type_id, floor)
VALUES
    ('R001', 2, 150.0, 'deluxe.jpg', 25.0, 1, 'RT02',1),
    ('R002', 1, 100.0, 'standard.jpg', 18.0, 1, 'RT01',1),
    ('R003', 3, 200.0, 'family.jpg', 30.0, 1, 'RT01',1),
    ('R004', 4, 300.0, 'suite.jpg', 40.0, 1, 'RT01',1),
    ('R005', 1, 80.0, 'single.jpg', 15.0, 1, 'RT01',2),
    ('R006', 2, 120.0, 'standard_plus.jpg', 20.0, 1, 'RT02',2),
    ('R007', 5, 400.0, 'penthouse.jpg', 50.0, 1, 'RT02',2),
    ('R008', 3, 12200.0, 'family.jpg', 30.0, 1, 'RT02',2),
    ('R009', 4, 30000.0, 'suite.jpg', 40.0, 1, 'RT02',3),
    ('R010', 1, 80000.0, 'single.jpg', 15.0, 1, 'RT02',3),
    ('R011', 2, 100000.0, 'standard_plus.jpg', 20.0, 1, 'RT02',3),
    ('R012', 5, 4000000.0, 'penthouse.jpg', 50.0, 1, 'RT02',3),
    ('R013', 3, 12200.0, 'family.jpg', 30.0, 0, 'RT02',4),
    ('R014', 4, 30000.0, 'suite.jpg', 40.0, 0, 'RT01',4),
    ('R015', 1, 80000.0, 'single.jpg', 15.0, 0, 'RT02',4),
    ('R016', 2, 100000.0, 'standard_plus.jpg', 20.0, 0, 'RT01',4),
    ('R017', 5, 4000000.0, 'penthouse.jpg', 50.0, 0, 'RT01',5);

-- Insert data into `amentities`
INSERT INTO amentities (room_id, amentities)
VALUES
    ('R001', 'WiFi'),
    ('R001', 'Air Conditioning'),
    ('R002', 'WiFi');

# -- Insert data into `services`
# INSERT INTO services ( availability, description, name, price)
# VALUES
#     ( 1, 'Breakfast buffet', 'Breakfast', 15.0),
#     (2, 1, 'Spa and wellness services', 'Spa', 50.0);

-- Insert data into `staffs`
INSERT INTO staffs (staff_id, address, date_of_birth, date_of_join, email, first_name, gender, last_name, staff_image, status)
VALUES
    ('S001', '789 Pine Street', '1988-03-10', '2022-01-01', 'alice.johnson@example.com', 'Alice', 0, 'Johnson', NULL, 1),
    ('S002', '101 Maple Avenue', '1992-05-25', '2023-05-01', 'bob.williams@example.com', 'Bob', 1, 'Williams', NULL, 1);

-- Insert data into `accounts`
INSERT INTO accounts (username, password, role, staff_id)
VALUES
    ('alice_j', 'password123', 'MANAGER', 'S001'),
    ('bob_w', 'password456', 'STAFF', 'S002');

-- Insert data into `orders`
INSERT INTO orders (order_id, order_date, paymentMethod, status, total_price, customer_id, staff_id)
VALUES
    ('O001', '2023-04-01 10:00:00', 'CASH', 1, 200.0, 'C001', 'S002'),
    ('O002', '2023-04-02 14:30:00', 'E_WALLET', 1, 150.0, 'C002', 'S001');

-- Insert data into `order_details`
INSERT INTO order_details (line_total_amount, quantity, service_id, order_id, room_id)
VALUES
    (30.0, 2, 1, 'O001', 'R001'),
    (50.0, 1, 2, 'O002', 'R002');

-- Insert data into `phones`
INSERT INTO phones (staff_id, phones)
VALUES
    ('S001', '1122334455'),
    ('S002', '2233445566');

-- Insert data into `reservations`
INSERT INTO reservations (reservation_id, check_in_date, check_out_date, status, total_price, customer_id, staff_id)
VALUES
    ('RES001', '2023-04-05 15:00:00', '2023-04-08 12:00:00', 1, 450.0, 'C001', 'S001'),
    ('RES002', '2023-04-10 14:00:00', '2023-04-12 11:00:00', 1, 300.0, 'C002', 'S002');

INSERT INTO reservations (reservation_id, check_in_date, check_out_date, status, total_price, customer_id, staff_id)
VALUES
    ('RES003', '2023-05-01 14:00:00', '2023-05-04 12:00:00', 1, 600.0, 'C001', 'S002'),
    ('RES004', '2023-05-10 15:00:00', '2023-05-13 11:00:00', 1, 900.0, 'C002', 'S001'),
    ('RES005', '2023-05-20 13:00:00', '2023-05-23 11:00:00', 1, 400.0, 'C001', 'S002'),
    ('RES006', '2023-05-25 14:00:00', '2023-05-28 12:00:00', 1, 500.0, 'C002', 'S001'),
    ('RES007', '2023-06-01 14:00:00', '2023-06-04 12:00:00', 1, 800.0, 'C001', 'S001');


-- Insert data into `reservation_details`
INSERT INTO reservation_details (line_total_amount, quantity, service_id, reservation_id, room_id)
VALUES
    (60.0, 2, 1, 'RES001', 'R001'),
    (50.0, 1, 2, 'RES002', 'R002');
INSERT INTO reservation_details (line_total_amount, quantity, service_id, reservation_id, room_id)
VALUES
    (100.0, 1, 1, 'RES003', 'R003'),
    (300.0, 1, 2, 'RES003', 'R004'),
    (50.0, 1, 1, 'RES004', 'R005'),
    (200.0, 2, 2, 'RES004', 'R006'),
    (80.0, 2, 1, 'RES005', 'R007'),
    (120.0, 1, 2, 'RES006', 'R003'),
    (250.0, 1, 1, 'RES007', 'R004'),
    (300.0, 1, 2, 'RES007', 'R005');