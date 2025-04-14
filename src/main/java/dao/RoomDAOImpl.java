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
    public List<Room> searchAvailableRooms(Date checkInDate, Date checkOutDate, int capacity, String roomType, double minPrice, double maxPrice) {
        EntityManager em = AppUtil.getEntityManager();
        try {
            String jpql = "SELECT r FROM Room r WHERE r.status = 0 " + // 0 means available
                    "AND r.capacity >= :capacity " +
                    "AND r.price BETWEEN :minPrice AND :maxPrice " +
                    "AND NOT EXISTS (SELECT rd FROM ReservationDetails rd " +
                    "JOIN rd.reservation res " +
                    "WHERE rd.room = r " +
                    "AND ((res.CheckInDate < :checkOutDate) AND (res.CheckOutDate > :checkInDate)))";

            if (roomType != null && !roomType.isEmpty()) {
                jpql += " AND r.roomType.typeID = :roomType";
            }

            TypedQuery<Room> query = em.createQuery(jpql, Room.class)
                    .setParameter("capacity", capacity)
                    .setParameter("minPrice", minPrice)
                    .setParameter("maxPrice", maxPrice)
                    .setParameter("checkInDate", checkInDate)
                    .setParameter("checkOutDate", checkOutDate);

            if (roomType != null && !roomType.isEmpty()) {
                query.setParameter("roomType", roomType);
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
                    "WHERE rd.room.roomId = :roomId " +
                    "AND ((res.CheckInDate < :checkOutDate) AND (res.CheckOutDate > :checkInDate))";

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