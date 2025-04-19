package dao;

import entities.Reservation;
import entities.Room;
import interfaces.ReservationDAO;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityTransaction;
import utils.AppUtil;

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
}
