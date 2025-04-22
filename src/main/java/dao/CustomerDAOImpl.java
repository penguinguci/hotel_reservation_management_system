package dao;

import entities.Customer;
import entities.Staff;
import interfaces.CustomerDAO;
import jakarta.persistence.*;
import utils.AppUtil;

import java.io.Serializable;
import java.rmi.RemoteException;
import java.util.List;

public class CustomerDAOImpl extends GenericDAOImpl<Customer, String> implements CustomerDAO, Serializable {
    private static final long serialVersionUID = 1L;
    private EntityManager em;

    public CustomerDAOImpl() throws RemoteException {
        super(Customer.class);
        em = AppUtil.getEntityManager();
    }


    // Lấy khách hàng theo ID
    @Override
    public List<Customer> searchCustomerById(String id) {
        String queryString = "SELECT c FROM Customer c WHERE c.customerId = :id";
        TypedQuery<Customer> query = em.createQuery(queryString, Customer.class);
        query.setParameter("id", id);
        return query.getResultList();
    }

    // Tìm kiếm khách hàng theo số điện thoại
    @Override
    public List<Customer> searchCustomersByPhone(String phone) {
        String queryString = "SELECT c FROM Customer c WHERE c.phoneNumber = :phone";
        TypedQuery<Customer> query = em.createQuery(queryString, Customer.class);
        query.setParameter("phone", phone);
        return query.getResultList();
    }

    // Tìm kiếm khách hàng theo tên
    @Override
    public List<Customer> searchCustomersByName(String name) {
        String queryString = "SELECT c FROM Customer c WHERE LOWER(c.firstName) LIKE :name OR LOWER(c.lastName) LIKE :name";
        TypedQuery<Customer> query = em.createQuery(queryString, Customer.class);
        query.setParameter("name", "%" + name.toLowerCase() + "%");
        return query.getResultList();
    }

    // Tìm kiếm khách hàng theo số điện thoại hoặc CCCD (dùng cho PopupSearch)
    @Override
    public List<Customer> searchCustomersByPhoneOrCCCD(String keyword) {
        String queryString = "SELECT c FROM Customer c WHERE LOWER(c.phoneNumber) LIKE :keyword OR LOWER(c.CCCD) LIKE :keyword";
        TypedQuery<Customer> query = em.createQuery(queryString, Customer.class);
        query.setParameter("keyword", "%" + keyword.toLowerCase() + "%");
        return query.getResultList();
    }

    // Lấy tất cả khách hàng
    @Override
    public List<Customer> getAllCustomers() {
        TypedQuery<Customer> query = em.createQuery("SELECT c FROM Customer c", Customer.class);
        return query.getResultList();
    }

    @Override
    public Customer getCustomerByPhone(String phone) throws RemoteException {
        String queryString = "SELECT c FROM Customer c WHERE c.phoneNumber = :phone";
        TypedQuery<Customer> query = em.createQuery(queryString, Customer.class);
        query.setParameter("phone", phone);
        List<Customer> customers = query.getResultList();
        return customers.isEmpty() ? null : customers.get(0);
    }


    @Override
    public boolean isEmailExists(String email) {
        if (email == null || email.isEmpty()) return false;

        Long count = em.createQuery("SELECT COUNT(c) FROM Customer c WHERE c.email = :email", Long.class)
                .setParameter("email", email)
                .getSingleResult();
        return count > 0;
    }

    @Override
    public boolean isPhoneExists(String phone) {
        if (phone == null || phone.isEmpty()) {
            return false;
        }

        Long count = em.createQuery(
                        "SELECT COUNT(c) FROM Customer c WHERE c.phoneNumber = :phone",
                        Long.class)
                .setParameter("phone", phone)
                .getSingleResult();

        return count > 0;
    }

    @Override
    public List<Customer> searchCustomerAdvanced(String id, String name, String phone, Boolean gender, String cccd) {
        StringBuilder queryStr = new StringBuilder(
                "SELECT DISTINCT c FROM Customer c WHERE 1=1"
        );

        if (id != null && !id.isEmpty()) {
            queryStr.append(" AND c.customerId LIKE :id");
        }
        if (name != null && !name.isEmpty()) {
            queryStr.append(" AND (LOWER(c.firstName) LIKE LOWER(:name) OR LOWER(c.lastName) LIKE LOWER(:name))");
        }
        if (phone != null && !phone.isEmpty()) {
            queryStr.append(" AND c.phoneNumber LIKE :phone");
        }
        if (gender != null) {
            queryStr.append(" AND c.gender = :gender");
        }
        if (cccd != null && !cccd.isEmpty()) {
            queryStr.append(" AND c.CCCD LIKE :cccd");
        }

        TypedQuery<Customer> query = em.createQuery(queryStr.toString(), Customer.class);

        if (id != null && !id.isEmpty()) {
            query.setParameter("id", "%" + id + "%");
        }
        if (name != null && !name.isEmpty()) {
            query.setParameter("name", "%" + name + "%");
        }
        if (phone != null && !phone.isEmpty()) {
            query.setParameter("phone", "%" + phone + "%");
        }
        if (gender != null) {
            query.setParameter("gender", gender);
        }
        if (cccd != null && !cccd.isEmpty()) {
            query.setParameter("cccd", "%" + cccd + "%");
        }

        return query.getResultList();
    }

    @Override
    public List<String> getAllCustomerIds() {
        EntityManager em = AppUtil.getEntityManager();
        try {
            String jpql = "SELECT c.customerId FROM Customer c";
            return em.createQuery(jpql, String.class).getResultList();
        } finally {
            em.close();
        }
    }

    @Override
    public List<Customer> searchCustomers(String keyword) {
        EntityManager em = AppUtil.getEntityManager();
        try {
            String jpql = "SELECT c FROM Customer c WHERE c.phoneNumber LIKE :keyword " +
                    "OR c.CCCD LIKE :keyword OR c.firstName LIKE :keyword OR c.lastName LIKE :keyword";
            TypedQuery<Customer> query = em.createQuery(jpql, Customer.class)
                    .setParameter("keyword", "%" + keyword + "%");
            return query.getResultList();
        } finally {
            em.close();
        }
    }
}