package interfaces;

import entities.Customer;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.List;

public interface CustomerDAO extends Remote {
    void addCustomer(Customer customer) throws RemoteException;
    void updateCustomer(Customer customer) throws RemoteException;
  //  void deleteCustomer(Customer customer) throws RemoteException;
    Customer getCustomer(String id)  throws RemoteException;
    List<Customer> getAllCustomers()  throws RemoteException;
}
