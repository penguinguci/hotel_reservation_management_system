package interfaces;

import entities.Customer;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.List;

public interface CustomerDAO extends GenericDAO<Customer, String>, Remote {
    List<Customer> searchCustomerById(String id)  throws RemoteException;
    List<Customer> searchCustomersByPhone(String phone);
    List<Customer> searchCustomersByName(String name);
    List<Customer> searchCustomersByPhoneOrCCCD(String keyword);
    List<Customer> getAllCustomers()  throws RemoteException;
    Customer getCustomerByPhone(String phone) throws RemoteException;
    boolean isEmailExists(String email);
    boolean isPhoneExists(String phone);
    List<Customer> searchCustomerAdvanced(String id, String name, String phone, Boolean gender, String cccd);
    List<String> getAllCustomerIds();
    List<Customer> searchCustomers(String keyword);
}
