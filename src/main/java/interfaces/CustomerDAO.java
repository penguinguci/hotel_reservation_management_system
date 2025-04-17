package interfaces;

import entities.Customer;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.List;

public interface CustomerDAO extends GenericDAO<Customer, String>, Remote {

  //  void deleteCustomer(Customer customer) throws RemoteException;
    List<Customer> searchCustomerById(String id)  throws RemoteException;

    // Tìm kiếm khách hàng theo số điện thoại
    List<Customer> searchCustomersByPhone(String phone);

    // Tìm kiếm khách hàng theo tên
    List<Customer> searchCustomersByName(String name);

    // Tìm kiếm khách hàng theo số điện thoại hoặc CCCD (dùng cho PopupSearch)
    List<Customer> searchCustomersByPhoneOrCCCD(String keyword);

    List<Customer> getAllCustomers()  throws RemoteException;

    Customer getCustomerByPhone(String phone) throws RemoteException;

    boolean isEmailExists(String email);

    boolean isPhoneExists(String phone);
}
