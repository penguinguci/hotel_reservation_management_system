package interfaces;

import entities.Orders;

import java.util.Date;
import java.util.List;

public interface OrderDAO extends GenericDAO<Orders, String> {
    boolean createOrder(Orders order);
    List<Orders> getAllOrders();
    List<Orders> searchOrders(String orderId, String customerId, String staffId,
                              Date fromDate, Date toDate, Integer status,
                              Double priceFrom, Double priceTo);
    Orders getOrderDetails(String orderId);
}
