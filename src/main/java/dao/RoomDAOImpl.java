package dao;

import entities.Room;
import interfaces.RoomDAO;
import jakarta.persistence.*;
import utils.AppUtil;

import java.util.Date;
import java.util.List;

public class RoomDAOImpl extends GenericDAOImpl<Room, String> implements RoomDAO {

    @PersistenceContext
    private EntityManagerFactory entityManagerFactory;

    @PersistenceContext
    private EntityManager entityManager;

    public RoomDAOImpl() {
        super(Room.class);
        entityManagerFactory = Persistence.createEntityManagerFactory("mariadb");
        entityManager = entityManagerFactory.createEntityManager();
    }

    // Tạo phòng
    public void createRoom(Room room) {
        EntityTransaction transaction = entityManager.getTransaction();
        try {
            transaction.begin();
            entityManager.persist(room);
            transaction.commit();
        } catch (Exception e) {
            if (transaction.isActive()) {
                transaction.rollback();
            }
            throw e;
        }
    }

    // Lấy phòng theo ID
    public Room getRoom(int id) {
        return entityManager.find(Room.class, id);
    }

    // Lấy tất cả phòng
    public List<Room> getAllRooms() {
        TypedQuery<Room> query = entityManager.createQuery("SELECT r FROM Room r", Room.class);
        return query.getResultList();
    }

    // Cập nhật thông tin phòng
    public void updateRoom(Room room) {
        EntityTransaction transaction = entityManager.getTransaction();
        try {
            transaction.begin();
            entityManager.merge(room);
            transaction.commit();
        } catch (Exception e) {
            if (transaction.isActive()) {
                transaction.rollback();
            }
            throw e;
        }
    }

    // Xóa phòng
    public void deleteRoom(int id) {
        EntityTransaction transaction = entityManager.getTransaction();
        try {
            transaction.begin();
            Room room = entityManager.find(Room.class, id);
            if (room != null) {
                entityManager.remove(room);
            }
            transaction.commit();
        } catch (Exception e) {
            if (transaction.isActive()) {
                transaction.rollback();
            }
            throw e;
        }
    }

    // Đóng kết nối
    public void close() {
        if (entityManager != null) {
            entityManager.close();
        }
        if (entityManagerFactory != null) {
            entityManagerFactory.close();
        }
    }

    @Override
    public List<Room> findAvailableRooms(Date checkInDate, Date checkOutDate, Integer capacity,
                                         String roomType, Double minPrice, Double maxPrice) {
        EntityManager em = entityManagerFactory.createEntityManager();
        try {
            // Xây dựng câu truy vấn cơ bản
            String jpql = "SELECT DISTINCT r FROM Room r " +
                    "LEFT JOIN FETCH r.amenities " +
                    "LEFT JOIN FETCH r.roomType " +
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

            return query.getResultList();
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
            String jpql = "SELECT DISTINCT r FROM Room r";
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
}