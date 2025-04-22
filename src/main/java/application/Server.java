package application;

import dao.*;
import interfaces.*;

import javax.naming.Context;
import javax.naming.InitialContext;
import java.rmi.registry.LocateRegistry;

public class Server {
    private static final String HOST = "localhost";
    private static final int PORT = 2004;
    public static void main(String[] args) {
        try {
            Context context = new InitialContext();

            AccountDAO accountDAO = new AccountDAOImpl();
            AmountCustomerDAO amountCustomerDAO = new AmountCustomerDAOImpl();
            CustomerDAO customerDAO = new CustomerDAOImpl();
            OrderDAO orderDAO = new OrderDAOImpl();
            ReservationDAO reservationDAO = new ReservationDAOImpl();
            RevenueDAO revenueDAO = new RevenueDAOImpl();
            RoomDAO roomDAO = new RoomDAOImpl();
            RoomTypesDAO roomTypesDAO = new RoomTypeDAOImpl();
            ServicesDAO servicesDAO = new ServicesDAOImpl();
            StaffDAO staffDAO = new StaffDAOImpl();
            GenericDAO genericDAO = new GenericDAOImpl();

            LocateRegistry.createRegistry(PORT);

            context.bind("rmi://" + HOST + ":" + PORT + "/AccountDAO", accountDAO);
            context.bind("rmi://" + HOST + ":" + PORT + "/AmountCustomerDAO", amountCustomerDAO);
            context.bind("rmi://" + HOST + ":" + PORT + "/CustomerDAO", customerDAO);
            context.bind("rmi://" + HOST + ":" + PORT + "/OrderDAO", orderDAO);
            context.bind("rmi://" + HOST + ":" + PORT + "/ReservationDAO", reservationDAO);
            context.bind("rmi://" + HOST + ":" + PORT + "/RevenueDAO", revenueDAO);
            context.bind("rmi://" + HOST + ":" + PORT + "/RoomDAO", roomDAO);
            context.bind("rmi://" + HOST + ":" + PORT + "/RoomTypesDAO", roomTypesDAO);
            context.bind("rmi://" + HOST + ":" + PORT + "/ServicesDAO", servicesDAO);
            context.bind("rmi://" + HOST + ":" + PORT + "/StaffDAO", staffDAO);
            context.bind("rmi://" + HOST + ":" + PORT + "/GenericDAO", genericDAO);
              System.out.println("Server is running on " + HOST + ": " + PORT);

           } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
