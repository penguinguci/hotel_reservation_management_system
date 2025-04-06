# select * from staffs
# select * from accounts
# select * from customers
# select * from rooms

# select * from orders
# select * from order_details
INSERT INTO staffs (staff_id, first_name, last_name, gender, date_of_birth, address, email, staff_image, date_of_join, status) VALUES
('S001', 'Hồ', 'Vĩnh Thái', 1, '2004-01-01', '123 Đường ABC, HCM', 'vinhthai@gmail.com', null, '2023-01-01', 1),
('S002', 'Trần', 'Long Vũ', 1, '2004-02-02', '456 Đường DEF, HCM', 'longvu@gmail.com', null, '2023-02-01', 1);
INSERT INTO phones (staff_id, phones) VALUES
('S001','0987624563'),
('S002','0256498720');
INSERT INTO accounts ( username, password, staff_id, role) VALUES
( 'user1', 'password1', 'S001', 'MANAGER'),
( 'user2', 'password2', 'S002','MANAGER');

INSERT INTO customers (customer_id, first_name, last_name, email, phone_number, address, gender,point,date_of_birth ) VALUES
('C001', 'John','Doe', 'john.doe@example.com', '1234567890', '123 Elm St, Springfield',0,0,'2000-11-11'),
('C002', 'Jane','Smith', 'jane.smith@example.com', '0987654321', '456 Oak St, Springfield',1,10,'1997-12-12');

INSERT INTO room_type (type_id, type_name, description) VALUES
('RT1', 'Standard', 'A standard room with basic amenities.'),
('RT2', 'Deluxe', 'A deluxe room with additional features.'),
('RT3', 'Suite', 'A spacious suite with luxury amenities.');

INSERT INTO rooms (room_id, price, capacity, status, room_size, room_image) VALUES
('R101', 100.0, 2, 1, 25.0, null),
('R102', 150.0, 3, 1, 30.0, null),
('R103', 200.0, 4, 1, 40.0, null);

INSERT INTO services (service_id, name, description, price, availability) VALUES
(1, 'Dịch vụ giặt ủi', 'Giặt ủi nhanh chóng và hiệu quả.', 50.0, 1),
(2, 'Dịch vụ ăn uống', 'Bữa ăn ngon miệng với thực đơn đa dạng.', 100.0, 1),
(3, 'Dịch vụ spa', 'Thư giãn với các liệu pháp spa chuyên nghiệp.', 200.0, 1),
(4, 'Dịch vụ đưa đón', 'Đưa đón khách từ sân bay đến khách sạn.', 150.0, 1);

INSERT INTO reservations (reservation_id, customer_id, staff_id, check_in_date, check_out_date, status, total_price) VALUES
('RES1', 'C001', 'S001', '2023-05-01', '2023-05-05', 1, 500.0),
('RES2', 'C002', 'S002', '2023-06-10', '2023-06-15', 1, 750.0);

INSERT INTO reservation_details (reservation_id, room_id, service_id, quantity, line_total_amount) VALUES
('RES1', 'R101', 1, 4, 400.0),
('RES1', 'R102', 2, 2, 300.0),
('RES2', 'R103', 4, 5, 1000.0);

INSERT INTO `manage-hoteldb`.amentities (room_id, amentities) VALUES
('R101', 'WiFi'),
('R101', 'TV'),
('R102', 'WiFi'),
('R102', 'Mini Bar'),
('R103', 'WiFi'),
('R103', 'Jacuzzi');

INSERT INTO orders (order_id, customer_id, staff_id, order_date, status, paymentMethod, total_price) VALUES
('O001', 'C001', 'S001', '2023-05-01 10:00:00', 1, 'CASH', 500.0),
('O002', 'C002', 'S002', '2023-06-10 15:30:00', 1, 'CASH', 750.0);

INSERT INTO order_details (order_id, room_id, service_id, quantity, line_total_amount) VALUES
('O001', 'R101', 1, 2, 200.0),
('O001', 'R101', 2, 1, 50.0),
('O002', 'R102', 1, 1, 150.0),
('O002', 'R102', 2, 2, 200.0);