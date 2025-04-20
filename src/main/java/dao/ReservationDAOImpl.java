package dao;

import entities.Reservation;
import entities.Room;
import interfaces.ReservationDAO;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityTransaction;
import jakarta.persistence.TypedQuery;
import utils.AppUtil;

import java.util.Date;
import java.util.List;

public class ReservationDAOImpl extends GenericDAOImpl<Reservation, String> implements ReservationDAO {
    EntityManager em;

    public ReservationDAOImpl() {
        super(Reservation.class);
        em = AppUtil.getEntityManager();
    }

    @Override
    public boolean createHourlyReservation(Reservation reservation) {
        EntityManager em = AppUtil.getEntityManager();
        EntityTransaction transaction = em.getTransaction();

        try {
            transaction.begin();

            // Kiểm tra hợp lệ
            if (!reservation.isValidHourlyBooking()) {
                throw new IllegalArgumentException("Đặt phòng theo giờ không hợp lệ");
            }

            // Kiểm tra phòng có khả dụng không
            Room room = reservation.getRoom();
            if (room == null || !room.isAvailableForHourlyBooking(reservation.getCheckInTime(), reservation.getCheckOutTime())) {
                throw new IllegalStateException("Phòng không khả dụng trong khoảng thời gian này");
            }

            // Tính toán giá
            reservation.calculateTotalPrice();
            reservation.calculateDepositAmount();
            reservation.calculateRemainingAmount();

            // Lưu reservation
            em.persist(reservation);

            // Cập nhật trạng thái phòng
            room.setStatus(Room.STATUS_RESERVED);
            em.merge(room);

            transaction.commit();
            return true;
        } catch (Exception e) {
            if (transaction != null && transaction.isActive()) {
                transaction.rollback();
            }
            throw e;
        } finally {
            em.close();
        }
    }

    // Tìm kiếm đặt phòng theo SĐT, CCCD, hoặc tên khách hàng
    @Override
    public List<Reservation> searchReservations(String keyword) {
        EntityManager em = AppUtil.getEntityManager();
        try {
            String jpql = "SELECT r FROM Reservation r JOIN r.customer c " +
                    "WHERE c.phoneNumber LIKE :keyword " +
                    "OR c.CCCD LIKE :keyword " +
                    "OR c.firstName LIKE :keyword " +
                    "OR c.lastName LIKE :keyword ";
            TypedQuery<Reservation> query = em.createQuery(jpql, Reservation.class)
                    .setParameter("keyword", "%" + keyword + "%");
            return query.getResultList();
        } finally {
            em.close();
        }
    }

    // Tìm kiếm đặt phòng theo mã khách hàng
    @Override
    public List<Reservation> getReservationsByCustomerId(String customerId) {
        EntityManager em = AppUtil.getEntityManager();
        try {
            String jpql = "SELECT r " +
                    "FROM Reservation r " +
                    "JOIN FETCH r.room room " +
                    "JOIN FETCH r.reservationDetails rd " +
                    "JOIN FETCH rd.service s " +
                    "WHERE r.customer.customerId = :customerId";
            TypedQuery<Reservation> query = em.createQuery(jpql, Reservation.class)
                    .setParameter("customerId", customerId);
            return query.getResultList();
        } finally {
            em.close();
        }
    }

    // Check-in đặt phòng
    @Override
    public boolean checkIn(String reservationId) {
        EntityManager em = AppUtil.getEntityManager();
        EntityTransaction transaction = em.getTransaction();
        try {
            transaction.begin();
            Reservation reservation = em.find(Reservation.class, reservationId);
            if (reservation != null && reservation.canCheckIn()) {
                reservation.updateStatus(Reservation.STATUS_CHECKED_IN);
                reservation.getRoom().checkIn();
                em.merge(reservation);
                em.merge(reservation.getRoom());
                transaction.commit();
                return true;
            }
            return false;
        } catch (Exception e) {
            if (transaction.isActive()) {
                transaction.rollback();
            }
            throw e;
        } finally {
            em.close();
        }
    }

    // Check-out đặt phòng
    @Override
    public boolean checkOut(String reservationId, Date actualCheckOutTime) {
        EntityManager em = AppUtil.getEntityManager();
        EntityTransaction transaction = em.getTransaction();
        try {
            transaction.begin();
            Reservation reservation = em.find(Reservation.class, reservationId);
            if (reservation != null && reservation.canCheckOut()) {
                double overstayFee = reservation.calculateOverstayFee(actualCheckOutTime);
                reservation.setTotalPrice(reservation.getTotalPrice() + overstayFee);
                reservation.updateStatus(Reservation.STATUS_CHECKED_OUT);
                reservation.getRoom().checkOut();
                em.merge(reservation);
                em.merge(reservation.getRoom());
                transaction.commit();
                return true;
            }
            return false;
        } catch (Exception e) {
            if (transaction.isActive()) {
                transaction.rollback();
            }
            throw e;
        } finally {
            em.close();
        }
    }

    // Hủy đặt phòng
    @Override
    public boolean cancelReservation(String reservationId) {
        EntityManager em = AppUtil.getEntityManager();
        EntityTransaction transaction = em.getTransaction();
        try {
            transaction.begin();
            Reservation reservation = em.find(Reservation.class, reservationId);
            if (reservation != null && reservation.canCancel()) {
                reservation.updateStatus(Reservation.STATUS_CANCELLED);
                reservation.getRoom().cancelReservation();
                em.merge(reservation);
                em.merge(reservation.getRoom());
                transaction.commit();
                return true;
            }
            return false;
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
    public List<Reservation> getAllReservations() {
        EntityManager em = AppUtil.getEntityManager();
        try {
            String jpql = "SELECT r " +
                    "FROM Reservation r " +
                    "JOIN FETCH r.room room " +
                    "JOIN FETCH r.customer c " +
                    "JOIN FETCH r.reservationDetails rd " +
                    "JOIN FETCH rd.service s";
            TypedQuery<Reservation> query = em.createQuery(jpql, Reservation.class);
            return query.getResultList();
        } finally {
            em.close();
        }
    }
}
