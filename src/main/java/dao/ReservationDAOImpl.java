package dao;

import entities.Reservation;
import interfaces.ReservationDAO;
import jakarta.persistence.EntityManager;
import utils.AppUtil;

public class ReservationDAOImpl extends GenericDAOImpl<Reservation, String> implements ReservationDAO {
    EntityManager em;

    public ReservationDAOImpl() {
        super(Reservation.class);
        em = AppUtil.getEntityManager();
    }
}
