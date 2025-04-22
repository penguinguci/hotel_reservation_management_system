package dao;

import entities.Orders;
import entities.Reservation;
import interfaces.OrderDAO;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityTransaction;
import jakarta.persistence.NoResultException;
import jakarta.persistence.TypedQuery;
import utils.AppUtil;

import java.io.Serializable;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class OrderDAOImpl extends GenericDAOImpl<Orders, String> implements OrderDAO, Serializable {
    private static final long serialVersionUID = 1L;
    private EntityManager em;

    public OrderDAOImpl() throws RemoteException {
        super(Orders.class);
        em = AppUtil.getEntityManager();
    }

    @Override
    public boolean createOrder(Orders order) {
        EntityManager em = AppUtil.getEntityManager();
        EntityTransaction transaction = em.getTransaction();
        try {
            transaction.begin();
            em.persist(order);
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
    public List<Orders> getAllOrders() {
        EntityManager em = AppUtil.getEntityManager();
        try {
            String jpql = "SELECT DISTINCT o FROM Orders o " +
                    "LEFT JOIN FETCH o.customer " +
                    "LEFT JOIN FETCH o.staff " +
                    "LEFT JOIN FETCH o.room r " +
                    "LEFT JOIN FETCH r.roomType " +
                    "LEFT JOIN FETCH o.orderDetails od " +
                    "LEFT JOIN FETCH od.service";
            return em.createQuery(jpql, Orders.class).getResultList();
        } finally {
            em.close();
        }
    }

    @Override
    public List<Orders> searchOrders(String orderId, String customerId, String staffId,
                                     Date fromDate, Date toDate, Integer status,
                                     Double priceFrom, Double priceTo) {
        EntityManager em = AppUtil.getEntityManager();
        try {
            StringBuilder jpql = new StringBuilder("SELECT o FROM Orders o " +
                    "LEFT JOIN FETCH o.customer c " +
                    "LEFT JOIN FETCH o.staff s " +
                    "LEFT JOIN FETCH o.room r " +
                    "LEFT JOIN FETCH r.reservations res " +
                    "LEFT JOIN FETCH o.orderDetails od " +
                    "LEFT JOIN FETCH od.service " +
                    "WHERE 1=1");

            List<String> conditions = new ArrayList<>();
            List<String> paramNames = new ArrayList<>();
            List<Object> paramValues = new ArrayList<>();

            if (orderId != null && !orderId.trim().isEmpty()) {
                conditions.add("o.orderId LIKE :orderId");
                paramNames.add("orderId");
                paramValues.add("%" + orderId + "%");
            }
            if (customerId != null && !customerId.trim().isEmpty()) {
                conditions.add("o.customer.customerId LIKE :customerId");
                paramNames.add("customerId");
                paramValues.add("%" + customerId + "%");
            }
            if (staffId != null && !staffId.trim().isEmpty()) {
                conditions.add("o.staff.staffId LIKE :staffId");
                paramNames.add("staffId");
                paramValues.add("%" + staffId + "%");
            }
            if (fromDate != null) {
                conditions.add("o.orderDate >= :fromDate");
                paramNames.add("fromDate");
                paramValues.add(fromDate);
            }
            if (toDate != null) {
                conditions.add("o.orderDate <= :toDate");
                paramNames.add("toDate");
                paramValues.add(toDate);
            }
            if (status != null) {
                conditions.add("o.status = :status");
                paramNames.add("status");
                paramValues.add(status);
            }
            if (priceFrom != null) {
                conditions.add("o.totalPrice >= :priceFrom");
                paramNames.add("priceFrom");
                paramValues.add(priceFrom);
            }
            if (priceTo != null) {
                conditions.add("o.totalPrice <= :priceTo");
                paramNames.add("priceTo");
                paramValues.add(priceTo);
            }

            for (String condition : conditions) {
                jpql.append(" AND ").append(condition);
            }

            TypedQuery<Orders> query = em.createQuery(jpql.toString(), Orders.class);
            for (int i = 0; i < paramNames.size(); i++) {
                query.setParameter(paramNames.get(i), paramValues.get(i));
            }

            return query.getResultList();
        } finally {
            em.close();
        }
    }

    @Override
    public Orders getOrderDetails(String orderId) {
        EntityManager em = AppUtil.getEntityManager();
        try {
            String jpql = "SELECT o FROM Orders o " +
                    "LEFT JOIN FETCH o.customer " +
                    "LEFT JOIN FETCH o.staff " +
                    "LEFT JOIN FETCH o.room r " +
                    "LEFT JOIN FETCH r.roomType " +
                    "LEFT JOIN FETCH o.orderDetails od " +
                    "LEFT JOIN FETCH od.service " +
                    "WHERE o.orderId = :orderId";
            return em.createQuery(jpql, Orders.class)
                    .setParameter("orderId", orderId)
                    .getSingleResult();
        } catch (NoResultException e) {
            return null;
        } finally {
            em.close();
        }
    }

    @Override
    public Reservation findReservationForOrder(Orders order) {
        if (order.getRoom() == null || order.getCustomer() == null) return null;

        EntityManager em = AppUtil.getEntityManager();
        try {
            String jpql = "SELECT r FROM Reservation r " +
                    "WHERE r.room.roomId = :roomId " +
                    "AND r.customer.customerId = :customerId " +
                    "AND r.reservationStatus IN (1, 2) " +
                    "AND ((r.checkInDate IS NOT NULL AND r.checkInDate <= :orderDate AND " +
                    "(r.checkOutDate IS NULL OR r.checkOutDate >= :orderDate)) OR " +
                    "(r.checkInTime IS NOT NULL AND r.checkInTime <= :orderDate AND " +
                    "(r.checkOutTime IS NULL OR r.checkOutTime >= :orderDate))) " +
                    "ORDER BY r.checkInTime DESC, r.checkInDate DESC";

            List<Reservation> reservations = em.createQuery(jpql, Reservation.class)
                    .setParameter("roomId", order.getRoom().getRoomId())
                    .setParameter("customerId", order.getCustomer().getCustomerId())
                    .setParameter("orderDate", order.getOrderDate())
                    .setMaxResults(1)
                    .getResultList();

            return reservations.isEmpty() ? null : reservations.get(0);
        } finally {
            em.close();
        }
    }
}
