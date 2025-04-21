package dao;

import entities.Orders;
import interfaces.OrderDAO;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityTransaction;
import jakarta.persistence.TypedQuery;
import utils.AppUtil;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class OrderDAOImpl extends GenericDAOImpl<Orders, String> implements OrderDAO {
    private EntityManager em;

    public OrderDAOImpl() {
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
            String jpql = "SELECT o FROM Orders o " +
                    "LEFT JOIN FETCH o.customer c " +
                    "LEFT JOIN FETCH o.staff s " +
                    "LEFT JOIN FETCH o.room r " +
                    "LEFT JOIN FETCH o.orderDetails od " +
                    "LEFT JOIN FETCH od.service " ;
            TypedQuery<Orders> query = em.createQuery(jpql, Orders.class);
            return query.getResultList();
        } catch (Exception e) {
            e.printStackTrace();
            return new ArrayList<>();
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
                    "LEFT JOIN FETCH o.customer c " +
                    "LEFT JOIN FETCH o.staff s " +
                    "LEFT JOIN FETCH o.room r " +
                    "LEFT JOIN FETCH r.roomType rt " +
                    "LEFT JOIN FETCH o.orderDetails od " +
                    "LEFT JOIN FETCH od.service " +
                    "WHERE o.orderId = :orderId";
            TypedQuery<Orders> query = em.createQuery(jpql, Orders.class)
                    .setParameter("orderId", orderId);
            return query.getSingleResult();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        } finally {
            em.close();
        }
    }
}
