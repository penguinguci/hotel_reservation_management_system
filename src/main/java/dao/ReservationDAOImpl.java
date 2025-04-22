package dao;

import entities.Reservation;
import entities.Room;
import interfaces.ReservationDAO;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityTransaction;
import jakarta.persistence.TypedQuery;
import utils.AppUtil;

import java.io.Serializable;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class ReservationDAOImpl extends GenericDAOImpl<Reservation, String> implements ReservationDAO, Serializable {
    private static final long serialVersionUID = 1L;
    EntityManager em;

    public ReservationDAOImpl() throws RemoteException {
        super(Reservation.class);
        em = AppUtil.getEntityManager();
    }

    @Override
    public boolean createHourlyReservation(Reservation reservation) {
        EntityManager em = AppUtil.getEntityManager();
        EntityTransaction transaction = em.getTransaction();

        try {
            transaction.begin();

            if (!reservation.isValidHourlyBooking()) {
                throw new IllegalArgumentException("Đặt phòng theo giờ không hợp lệ");
            }

            Room room = reservation.getRoom();
            if (room == null || !room.isAvailableForHourlyBooking(reservation.getCheckInTime(), reservation.getCheckOutTime())) {
                throw new IllegalStateException("Phòng không khả dụng trong khoảng thời gian này");
            }

            reservation.calculateTotalPrice();
            reservation.calculateDepositAmount();
            reservation.calculateRemainingAmount();

            em.persist(reservation);
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

    @Override
    public List<Reservation> getReservationsByCustomerId(String customerId) {
        EntityManager em = AppUtil.getEntityManager();
        try {
            String jpql = "SELECT r FROM Reservation r " +
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

    @Override
    public boolean checkIn(String reservationId) {
        EntityManager em = AppUtil.getEntityManager();
        EntityTransaction transaction = em.getTransaction();
        try {
            transaction.begin();
            Reservation reservation = em.find(Reservation.class, reservationId);
            if (reservation != null && reservation.canCheckIn()) {

                reservation.updateStatus(Reservation.STATUS_CHECKED_IN);
                reservation.setCheckInTime(new Date());
                reservation.getRoom().checkIn(reservation);

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
    public boolean checkOut(String reservationId, Date actualCheckOutTime) {
        EntityManager em = AppUtil.getEntityManager();
        EntityTransaction transaction = em.getTransaction();
        try {
            transaction.begin();
            Reservation reservation = em.find(Reservation.class, reservationId);
            if (reservation != null && reservation.canCheckOut()) {
                reservation.calculateOverstayDetails(actualCheckOutTime);
                reservation.setTotalPrice(reservation.calculateTotalPrice());
                reservation.setRemainingAmount(reservation.calculateRemainingAmount());
                reservation.updateStatus(Reservation.STATUS_CHECKED_OUT);
                reservation.setActualCheckOutTime(actualCheckOutTime);
                reservation.getRoom().checkOut(reservation);

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
    public boolean batchCheckIn(List<String> reservationIds) {
        EntityManager em = AppUtil.getEntityManager();
        EntityTransaction transaction = em.getTransaction();
        try {
            transaction.begin();

            // Lấy danh sách reservation và kiểm tra
            List<Reservation> reservations = em.createQuery(
                            "SELECT r FROM Reservation r WHERE r.reservationId IN :ids", Reservation.class)
                    .setParameter("ids", reservationIds)
                    .getResultList();

            if (reservations.isEmpty()) {
                return false;
            }

            // Kiểm tra tất cả reservation có cùng customer không
            String customerId = reservations.get(0).getCustomer().getCustomerId();
            boolean sameCustomer = reservations.stream()
                    .allMatch(r -> r.getCustomer().getCustomerId().equals(customerId));

            if (!sameCustomer) {
                throw new IllegalArgumentException("Các đơn đặt phòng không thuộc cùng một khách hàng");
            }

            // Kiểm tra tất cả có thể check-in không
            boolean allCanCheckIn = reservations.stream()
                    .allMatch(Reservation::canCheckIn);

            if (!allCanCheckIn) {
                throw new IllegalStateException("Một số đơn đặt phòng không thể check-in");
            }

            // Thực hiện check-in
            for (Reservation reservation : reservations) {
                reservation.updateStatus(Reservation.STATUS_CHECKED_IN);
                reservation.getRoom().checkIn(reservation);
                em.merge(reservation);
                em.merge(reservation.getRoom());
            }

            transaction.commit();
            return true;
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
    public boolean batchCheckOut(List<String> reservationIds, Date actualCheckOutTime) {
        EntityManager em = AppUtil.getEntityManager();
        EntityTransaction transaction = em.getTransaction();
        try {
            transaction.begin();

            // Lấy danh sách reservation và kiểm tra
            List<Reservation> reservations = em.createQuery(
                            "SELECT r FROM Reservation r WHERE r.reservationId IN :ids", Reservation.class)
                    .setParameter("ids", reservationIds)
                    .getResultList();

            if (reservations.isEmpty()) {
                return false;
            }

            // Kiểm tra tất cả reservation có cùng customer không
            String customerId = reservations.get(0).getCustomer().getCustomerId();
            boolean sameCustomer = reservations.stream()
                    .allMatch(r -> r.getCustomer().getCustomerId().equals(customerId));

            if (!sameCustomer) {
                throw new IllegalArgumentException("Các đơn đặt phòng không thuộc cùng một khách hàng");
            }

            // Kiểm tra tất cả có thể check-out không
            boolean allCanCheckOut = reservations.stream()
                    .allMatch(Reservation::canCheckOut);

            if (!allCanCheckOut) {
                throw new IllegalStateException("Một số đơn đặt phòng không thể check-out");
            }

            // Thực hiện check-out và tính phụ phí
            for (Reservation reservation : reservations) {
                reservation.calculateOverstayDetails(actualCheckOutTime);
                reservation.setTotalPrice(reservation.getTotalPrice() + reservation.getOverstayFee());
                reservation.updateStatus(Reservation.STATUS_CHECKED_OUT);
                reservation.getRoom().checkOut(reservation);
                em.merge(reservation);
                em.merge(reservation.getRoom());
            }

            transaction.commit();
            return true;
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
    public boolean cancelMultipleReservations(List<Reservation> reservations) {
        EntityManager em = AppUtil.getEntityManager();
        EntityTransaction transaction = em.getTransaction();
        try {
            transaction.begin();
            boolean allSuccess = true;
            for (Reservation reservation : reservations) {
                if (reservation.canCancel()) {
                    reservation.updateStatus(Reservation.STATUS_CANCELLED);
                    reservation.getRoom().cancelReservation();
                    em.merge(reservation);
                    em.merge(reservation.getRoom());
                } else {
                    allSuccess = false;
                }
            }
            transaction.commit();
            return allSuccess;
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
            String jpql = "SELECT r FROM Reservation r " +
                    "LEFT JOIN FETCH r.customer " +
                    "LEFT JOIN FETCH r.room rt " +
                    "LEFT JOIN FETCH rt.roomType " +
                    "LEFT JOIN FETCH r.reservationDetails rd " +
                    "LEFT JOIN FETCH rd.service";
            TypedQuery<Reservation> query = em.createQuery(jpql, Reservation.class);
            return query.getResultList();
        } catch (Exception e) {
            e.printStackTrace();
            return new ArrayList<>();
        } finally {
            em.close();
        }
    }
}