# staffs
CREATE INDEX idx_staffs_name ON staffs (last_name, first_name);
CREATE INDEX idx_staffs_gender ON staffs (gender);

# customers
CREATE INDEX idx_customers_name ON customers (last_name, first_name);
CREATE INDEX idx_customers_gender ON customers (gender);
CREATE INDEX idx_customers_name_phone ON customers (last_name, first_name, phone_number);

# rooms
CREATE INDEX idx_rooms_floor_status ON rooms (floor, status);
CREATE INDEX idx_rooms_type_price ON rooms (type_id, price);
CREATE INDEX idx_rooms_hourly_rate ON rooms (hourly_base_rate);
CREATE INDEX idx_rooms_status ON rooms (status);

# room_type
CREATE INDEX idx_room_type_name ON room_type (type_name);

# services
CREATE INDEX idx_services_name ON services (name);
CREATE INDEX idx_services_price ON services (price);

# orders
CREATE INDEX idx_orders_date ON orders (order_date);
CREATE INDEX idx_orders_status ON orders (status);
CREATE INDEX idx_orders_customer_date ON orders (customer_id, order_date);
CREATE INDEX idx_orders_room_date ON orders (room_id, order_date);
CREATE INDEX idx_orders_staff ON orders (staff_id);

# reservations
CREATE INDEX idx_reservations_date ON reservations (booking_date);
CREATE INDEX idx_reservations_status ON reservations (status);
CREATE INDEX idx_reservations_customer_date ON reservations (customer_id, booking_date);
CREATE INDEX idx_reservations_room_date ON reservations (room_id, booking_date);