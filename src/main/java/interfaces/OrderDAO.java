package interfaces;

import entities.Orders;

public interface OrderDAO extends GenericDAO<Orders, String> {
    boolean createOrder(Orders order);
}
