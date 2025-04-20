package dao;

import entities.Orders;
import interfaces.OrderDAO;
import jakarta.persistence.EntityManager;
import utils.AppUtil;

public class OrderDAOImpl extends GenericDAOImpl<Orders, String> implements OrderDAO {
    private EntityManager em;

    public OrderDAOImpl() {
        super(Orders.class);
        em = AppUtil.getEntityManager();
    }
}
