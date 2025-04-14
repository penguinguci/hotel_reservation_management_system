package dao;

import entities.Customer;
import interfaces.CustomerDAO;
import jakarta.persistence.*;

import java.util.List;

public class CustomerDAOImpl implements CustomerDAO {
    private EntityManagerFactory entityManagerFactory;
    private EntityManager entityManager;

    public CustomerDAOImpl() {
        entityManagerFactory = Persistence.createEntityManagerFactory("mariadb");
        entityManager = entityManagerFactory.createEntityManager();
    }

    // Thêm khách hàng
    @Override
    public void addCustomer(Customer customer) {
        EntityTransaction transaction = entityManager.getTransaction();
        try {
            transaction.begin();
            entityManager.persist(customer);
            transaction.commit();
        } catch (Exception e) {
            if (transaction.isActive()) {
                transaction.rollback();
            }
            throw new RuntimeException("Failed to add customer: " + e.getMessage(), e);
        }
    }

    // Lấy khách hàng theo ID
    @Override
    public List<Customer> searchCustomerById(String id) {
        String queryString = "SELECT c FROM Customer c WHERE c.customerId = :id";
        TypedQuery<Customer> query = entityManager.createQuery(queryString, Customer.class);
        query.setParameter("id", id);
        return query.getResultList();
    }

    // Tìm kiếm khách hàng theo số điện thoại
    @Override
    public List<Customer> searchCustomersByPhone(String phone) {
        String queryString = "SELECT c FROM Customer c WHERE c.phoneNumber = :phone";
        TypedQuery<Customer> query = entityManager.createQuery(queryString, Customer.class);
        query.setParameter("phone", phone);
        return query.getResultList();
    }

    // Tìm kiếm khách hàng theo tên
    @Override
    public List<Customer> searchCustomersByName(String name) {
        String queryString = "SELECT c FROM Customer c WHERE LOWER(c.firstName) LIKE :name OR LOWER(c.lastName) LIKE :name";
        TypedQuery<Customer> query = entityManager.createQuery(queryString, Customer.class);
        query.setParameter("name", "%" + name.toLowerCase() + "%");
        return query.getResultList();
    }

    // Tìm kiếm khách hàng theo số điện thoại hoặc CCCD (dùng cho PopupSearch)
    @Override
    public List<Customer> searchCustomersByPhoneOrCCCD(String keyword) {
        String queryString = "SELECT c FROM Customer c WHERE LOWER(c.phoneNumber) LIKE :keyword OR LOWER(c.CCCD) LIKE :keyword";
        TypedQuery<Customer> query = entityManager.createQuery(queryString, Customer.class);
        query.setParameter("keyword", "%" + keyword.toLowerCase() + "%");
        return query.getResultList();
    }

    // Lấy tất cả khách hàng
    @Override
    public List<Customer> getAllCustomers() {
        TypedQuery<Customer> query = entityManager.createQuery("SELECT c FROM Customer c", Customer.class);
        return query.getResultList();
    }

    // Cập nhật thông tin khách hàng
    @Override
    public void updateCustomer(Customer customer) {
        EntityTransaction transaction = entityManager.getTransaction();
        try {
            transaction.begin();
            entityManager.merge(customer);
            transaction.commit();
        } catch (Exception e) {
            if (transaction.isActive()) {
                transaction.rollback();
            }
            throw new RuntimeException("Failed to update customer: " + e.getMessage(), e);
        }
    }

    // Đóng kết nối
    public void close() {
        if (entityManager != null && entityManager.isOpen()) {
            entityManager.close();
        }
        if (entityManagerFactory != null && entityManagerFactory.isOpen()) {
            entityManagerFactory.close();
        }
    }
}