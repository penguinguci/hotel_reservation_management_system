package interfaces;

import entities.Reservation;

import java.util.Date;
import java.util.List;

public interface ReservationDAO extends GenericDAO<Reservation, String> {
    boolean createHourlyReservation(Reservation reservation);
    List<Reservation> searchReservations(String keyword);
    List<Reservation> getReservationsByCustomerId(String customerId);
    boolean checkIn(String reservationId);
    boolean checkOut(String reservationId, Date actualCheckOutTime);
    boolean batchCheckIn(List<String> reservationIds);
    boolean batchCheckOut(List<String> reservationIds, Date actualCheckOutTime);
    boolean cancelReservation(String reservationId);
    boolean cancelMultipleReservations(List<Reservation> reservations);
    List<Reservation> getAllReservations();
}
