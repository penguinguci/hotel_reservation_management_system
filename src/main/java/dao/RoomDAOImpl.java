    package dao;

    import entities.Room;
    import entities.RoomType;
    import interfaces.RoomDAO;
    import jakarta.persistence.*;
    import utils.AppUtil;

    import java.io.Serializable;
    import java.rmi.RemoteException;
    import java.util.ArrayList;
    import java.util.Date;
    import java.util.List;
    import java.util.Map;

    public class  RoomDAOImpl extends GenericDAOImpl<Room, String> implements RoomDAO, Serializable {
        private static final long serialVersionUID = 1L;
        private EntityManager em;

        public RoomDAOImpl() throws RemoteException {
            super(Room.class);
            em = AppUtil.getEntityManager();
        }

        // Tạo phòng
        public void createRoom(Room room) {
            EntityManager em = AppUtil.getEntityManager();
            EntityTransaction transaction = em.getTransaction();
            try {
                transaction.begin();
                em.persist(room);
                transaction.commit();
            } catch (Exception e) {
                if (transaction.isActive()) {
                    transaction.rollback();
                }
                throw e;
            } finally {
                em.close();
            }
        }

        // Lấy phòng theo ID
        public Room getRoom(String id) {
            EntityManager em = AppUtil.getEntityManager();
            try {
                return em.find(Room.class, id);
            } finally {
                em.close();
            }
        }

        // Lấy tất cả phòng
        public List<Room> getAllRooms() {
            EntityManager em = AppUtil.getEntityManager();
            try {
                TypedQuery<Room> query = em.createQuery("SELECT r FROM Room r", Room.class);
                return query.getResultList();
            } finally {
                em.close();
            }
        }

        @Override
        public List<Integer> getAllFloors() {
            EntityManager em = AppUtil.getEntityManager();
            try {
                // Truy vấn để lấy danh sách các giá trị floor duy nhất từ bảng rooms
                return em.createQuery("SELECT DISTINCT r.floor FROM Room r ORDER BY r.floor", Integer.class)
                        .getResultList();
            } finally {
                em.close();
            }
        }

        @Override
        public boolean updateRoomStatus(String roomId, int status) {
            EntityTransaction transaction = null;
            try {
                transaction = em.getTransaction();
                transaction.begin();

                String jpql = "UPDATE Room r SET r.status = :status WHERE r.roomId = :roomId";
                int updatedCount = em.createQuery(jpql)
                        .setParameter("status", status)
                        .setParameter("roomId", roomId)
                        .executeUpdate();

                transaction.commit();

                return updatedCount > 0;
            } catch (Exception e) {
                if (transaction != null && transaction.isActive()) {
                    transaction.rollback();
                }
                e.printStackTrace();
                return false;
            }
        }

        // Tìm phòng trống theo giờ
        @Override
        public List<Room> findAvailableRoomsForHourlyBooking(Date startTime, Date endTime,
                                                             Integer capacity, String roomType) {
            EntityManager em = AppUtil.getEntityManager();
            try {
                String jpql = "SELECT DISTINCT r FROM Room r " +
                        "LEFT JOIN FETCH r.roomType " +
                        "LEFT JOIN FETCH r.hourlyPriceRules " +
                        "LEFT JOIN FETCH r.reservations " +
                        "WHERE r.status = :availableStatus " +
                        "AND (:startTime IS NULL OR :endTime IS NULL OR " +
                        "NOT EXISTS (SELECT res FROM Reservation res " +
                        "WHERE res.room = r " +
                        "AND res.bookingType = 'HOUR' " +
                        "AND res.checkInTime < :endTime " +
                        "AND res.checkOutTime > :startTime))";

                if (capacity != null) {
                    jpql += " AND r.capacity >= :capacity";
                }

                if (roomType != null && !roomType.isEmpty()) {
                    jpql += " AND r.roomType.typeName = :roomType";
                }

                TypedQuery<Room> query = em.createQuery(jpql, Room.class)
                        .setParameter("availableStatus", Room.STATUS_AVAILABLE);

                if (startTime != null && endTime != null) {
                    query.setParameter("startTime", startTime)
                            .setParameter("endTime", endTime);
                } else {
                    query.setParameter("startTime", null)
                            .setParameter("endTime", null);
                }

                if (capacity != null) {
                    query.setParameter("capacity", capacity);
                }

                if (roomType != null && !roomType.isEmpty()) {
                    query.setParameter("roomType", roomType);
                }

                return query.getResultList();
            } finally {
                em.close();
            }
        }

        // Kiểm tra phòng có khả dụng trong khoảng thời gian không (theo giờ)
        @Override
        public boolean isRoomAvailableForHourlyBooking(String roomId, Date startTime, Date endTime) {
            EntityManager em = AppUtil.getEntityManager();
            try {
                String jpql = "SELECT COUNT(res) FROM Reservation res " +
                        "WHERE res.room.roomId = :roomId " +
                        "AND res.bookingType = 'HOUR' " +
                        "AND ((res.checkInTime < :endTime) " +
                        "AND (res.checkOutTime > :startTime)) ";

                Long count = em.createQuery(jpql, Long.class)
                        .setParameter("roomId", roomId)
                        .setParameter("startTime", startTime)
                        .setParameter("endTime", endTime)
                        .getSingleResult();

                return count == 0;
            } finally {
                em.close();
            }
        }

        // Cập nhật thông tin phòng
        public void updateRoom(Room room) {
            EntityManager em = AppUtil.getEntityManager();
            EntityTransaction transaction = em.getTransaction();
            try {
                transaction.begin();
                em.merge(room);
                transaction.commit();
            } catch (Exception e) {
                if (transaction.isActive()) {
                    transaction.rollback();
                }
                throw e;
            } finally {
                em.close();
            }
        }

        @Override
        public List<Room> findAvailableRooms(Date checkInDate, Date checkOutDate, Integer capacity,
                                             String roomType, Double minPrice, Double maxPrice) {
            EntityManager em = AppUtil.getEntityManager();
            try {
                // Xây dựng câu truy vấn cơ bản
                String jpql = "SELECT DISTINCT r FROM Room r " +
                        "LEFT JOIN FETCH r.roomType " +  // Chỉ fetch roomType
                        "WHERE r.status = :availableStatus";

                // Thêm điều kiện cho ngày nếu có
                if (checkInDate != null && checkOutDate != null) {
                    jpql += " AND r NOT IN (SELECT res.room FROM Reservation res " +
                            "WHERE (res.checkInDate < :checkOutDate) " +
                            "AND (res.checkOutDate > :checkInDate))";
                }

                // Thêm điều kiện cho sức chứa nếu có
                if (capacity != null) {
                    jpql += " AND r.capacity >= :capacity";
                }

                // Thêm điều kiện cho loại phòng nếu có
                if (roomType != null && !roomType.isEmpty()) {
                    jpql += " AND r.roomType.typeName = :roomType";
                }

                // Thêm điều kiện cho khoảng giá nếu có
                if (minPrice != null) {
                    jpql += " AND r.price >= :minPrice";
                }
                if (maxPrice != null) {
                    jpql += " AND r.price <= :maxPrice";
                }

                // Tạo query
                TypedQuery<Room> query = em.createQuery(jpql, Room.class)
                        .setParameter("availableStatus", Room.STATUS_AVAILABLE);

                // Thiết lập các tham số
                if (checkInDate != null && checkOutDate != null) {
                    query.setParameter("checkInDate", checkInDate)
                            .setParameter("checkOutDate", checkOutDate);
                }

                if (capacity != null) {
                    query.setParameter("capacity", capacity);
                }

                if (roomType != null && !roomType.isEmpty()) {
                    query.setParameter("roomType", roomType);
                }

                if (minPrice != null) {
                    query.setParameter("minPrice", minPrice);
                }

                if (maxPrice != null) {
                    query.setParameter("maxPrice", maxPrice);
                }

                List<Room> rooms = query.getResultList();

                // Nếu cần amenities, có thể fetch riêng trong một query khác
                if (!rooms.isEmpty()) {
                    // Fetch amenities cho các phòng đã tìm được
                    String amenitiesQuery = "SELECT r FROM Room r LEFT JOIN FETCH r.amenities WHERE r IN :rooms";
                    rooms = em.createQuery(amenitiesQuery, Room.class)
                            .setParameter("rooms", rooms)
                            .getResultList();
                }

                return rooms;
            } finally {
                em.close();
            }
        }

        @Override
        public boolean isRoomAvailable(String roomId, Date checkInDate, Date checkOutDate) {
            EntityManager em = AppUtil.getEntityManager();
            try {
                String jpql = "SELECT COUNT(rd) FROM ReservationDetails rd " +
                        "JOIN rd.reservation res " +
                        "WHERE res.room.id = :roomId " +
                        "AND ((res.checkInDate < :checkOutDate) AND (res.checkOutDate > :checkInDate))";

                Long count = em.createQuery(jpql, Long.class)
                        .setParameter("roomId", roomId)
                        .setParameter("checkInDate", checkInDate)
                        .setParameter("checkOutDate", checkOutDate)
                        .getSingleResult();

                return count == 0;
            } finally {
                em.close();
            }
        }

        @Override
        public double calculateRoomPrice(String roomId, Date checkInDate, Date checkOutDate) {
            Room room = findById(roomId);
            if (room == null) {
                throw new IllegalArgumentException("Room not found");
            }

            long diffInMillis = checkOutDate.getTime() - checkInDate.getTime();
            long days = diffInMillis / (1000 * 60 * 60 * 24);
            days = days == 0 ? 1 : days; // minimum 1 day

            return room.getPrice() * days;
        }

        @Override
        public List<Room> getAllRoomTypes() {
            EntityManager em = AppUtil.getEntityManager();
            try {
                String jpql = "SELECT DISTINCT r FROM Room r LEFT JOIN FETCH r.roomType";
                return em.createQuery(jpql, Room.class).getResultList();
            } finally {
                em.close();
            }
        }



        @Override
        public List<Room> getRoomsByStatus(int status) {
            EntityManager em = AppUtil.getEntityManager();
            try {
                String jpql = "SELECT r FROM Room r WHERE r.status = :status";
                return em.createQuery(jpql, Room.class)
                        .setParameter("status", status)
                        .getResultList();
            } finally {
                em.close();
            }
        }

        @Override
        public List<String> addAmenity(String roomId, String amenity) {
            EntityManager em = AppUtil.getEntityManager();
            EntityTransaction transaction = em.getTransaction();
            List<String> updatedAmenities = null;

            try {
                transaction.begin();
                Room room = em.find(Room.class, roomId);
                if (room != null) {
                    List<String> amenities = room.getAmenities();
                    if (amenities == null) {
                        amenities = new ArrayList<>();
                    }
                    amenities.add(amenity);
                    room.setAmenities(amenities);
                    em.merge(room);
                    transaction.commit();
                    updatedAmenities = new ArrayList<>(amenities); // Sao chép danh sách mới
                } else {
                    throw new IllegalArgumentException("Phòng không tồn tại!");
                }
            } catch (Exception e) {
                if (transaction.isActive()) {
                    transaction.rollback();
                }
                throw new RuntimeException("Lỗi khi thêm tiện nghi: " + e.getMessage(), e);
            } finally {
                em.close();
            }
            return updatedAmenities;
        }

        @Override
        public List<String> updateAmenity(String roomId, int amenityIndex, String newAmenity) {
            EntityManager em = AppUtil.getEntityManager();
            EntityTransaction transaction = em.getTransaction();
            List<String> updatedAmenities = null;

            try {
                transaction.begin();
                Room room = em.find(Room.class, roomId);
                if (room != null && room.getAmenities() != null && amenityIndex >= 0 && amenityIndex < room.getAmenities().size()) {
                    List<String> amenities = room.getAmenities();
                    amenities.set(amenityIndex, newAmenity);
                    room.setAmenities(amenities);
                    em.merge(room);
                    transaction.commit();
                    updatedAmenities = new ArrayList<>(amenities); // Sao chép danh sách mới
                } else {
                    throw new IllegalArgumentException("Phòng hoặc tiện nghi không hợp lệ!");
                }
            } catch (Exception e) {
                if (transaction.isActive()) {
                    transaction.rollback();
                }
                throw new RuntimeException("Lỗi khi cập nhật tiện nghi: " + e.getMessage(), e);
            } finally {
                em.close();
            }
            return updatedAmenities;
        }

        @Override
        public List<Room> findByCriteria(Map<String, Object> criteria) {
            EntityManager em = AppUtil.getEntityManager();
            List<Room> rooms = new ArrayList<>();

            try {
                StringBuilder jpql = new StringBuilder("SELECT r FROM Room r WHERE 1=1");
                List<String> conditions = new ArrayList<>();
                List<String> paramNames = new ArrayList<>();
                List<Object> paramValues = new ArrayList<>();

                // Tiêu chí giá
                if (criteria.containsKey("priceMin") && criteria.containsKey("priceMax")) {
                    conditions.add("r.price BETWEEN :priceMin AND :priceMax");
                    paramNames.add("priceMin");
                    paramValues.add(criteria.get("priceMin"));
                    paramNames.add("priceMax");
                    paramValues.add(criteria.get("priceMax"));
                }

                // Tiêu chí tầng
                if (criteria.containsKey("position")) {
                    conditions.add("r.floor = :floor");
                    paramNames.add("floor");
                    paramValues.add(Integer.parseInt((String) criteria.get("position")));
                }

                // Tiêu chí loại phòng
                if (criteria.containsKey("roomType")) {
                    conditions.add("r.roomType.typeName = :roomType");
                    paramNames.add("roomType");
                    paramValues.add(criteria.get("roomType"));
                }

                // Tiêu chí trạng thái
                if (criteria.containsKey("status")) {
                    conditions.add("r.status = :status");
                    paramNames.add("status");
                    paramValues.add(criteria.get("status"));
                }

                // Xây dựng truy vấn JPQL
                for (String condition : conditions) {
                    jpql.append(" AND ").append(condition);
                }

                TypedQuery<Room> query = em.createQuery(jpql.toString(), Room.class);
                for (int i = 0; i < paramNames.size(); i++) {
                    query.setParameter(paramNames.get(i), paramValues.get(i));
                }

                rooms = query.getResultList();
            } catch (Exception e) {
                throw e;
            } finally {
                em.close();
            }

            return rooms;
        }
        // Đóng kết nối
        public void close() {
            // Không cần đóng EntityManagerFactory vì AppUtil quản lý nó
        }


    }