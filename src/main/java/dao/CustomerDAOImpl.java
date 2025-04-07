package dao;

import entities.Customer;
import interfaces.CustomerDAO;
import jakarta.persistence.*;

import java.util.List;

public class CustomerDAOImpl implements CustomerDAO {
    @PersistenceContext
    private EntityManagerFactory entityManagerFactory;

    @PersistenceContext
    private EntityManager entityManager;

    public CustomerDAOImpl() {
        entityManagerFactory = Persistence.createEntityManagerFactory("mariadb");
        entityManager = entityManagerFactory.createEntityManager();
    }

    // Them khách hàng
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
            throw e;
        }
    }

    // Lấy khách hàng theo ID
    public Customer getCustomer(int id) {
        return entityManager.find(Customer.class, id);
    }

    // Lấy tất cả khách hàng
    public List<Customer> getAllCustomers() {
        TypedQuery<Customer> query = entityManager.createQuery("SELECT c FROM Customer c", Customer.class);
        return query.getResultList();
    }

    // Cập nhật thông tin khách hàng
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
            throw e;
        }
    }

    // Xóa khách hàng
//    public void deleteCustomer(int id) {
//        EntityTransaction transaction = entityManager.getTransaction();
//        try {
//            transaction.begin();
//            Customer customer = entityManager.find(Customer.class, id);
//            if (customer != null) {
//                entityManager.remove(customer);
//            }
//            transaction.commit();
//        } catch (Exception e) {
//            if (transaction.isActive()) {
//                transaction.rollback();
//            }
//            throw e;
//        }
//    }

    // Đóng kết nối
    public void close() {
        if (entityManager != null) {
            entityManager.close();
        }
        if (entityManagerFactory != null) {
            entityManagerFactory.close();
        }
    }
}
