package dao;

import entities.Orders;
import interfaces.OrderDAO;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityTransaction;
import utils.AppUtil;

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
}
