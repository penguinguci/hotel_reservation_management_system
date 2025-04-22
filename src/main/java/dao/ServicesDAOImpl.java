package dao;

import entities.Service;
import interfaces.ServicesDAO;
import jakarta.persistence.*;
import org.hibernate.Session;
import utils.AppUtil;

import java.io.Serializable;
import java.rmi.RemoteException;
import java.util.List;

public class ServicesDAOImpl extends GenericDAOImpl<Service, String> implements ServicesDAO, Serializable {
    private static final long serialVersionUID = 1L;

    @PersistenceContext
    private EntityManagerFactory entityManagerFactory;

    @PersistenceContext
    private EntityManager entityManager;

    public ServicesDAOImpl() throws RemoteException {
        super(Service.class);
        entityManagerFactory = Persistence.createEntityManagerFactory("mariadb");
        entityManager = entityManagerFactory.createEntityManager();
    }

    // Tạo dịch vụ
    public void createService(Service service) {
        EntityTransaction transaction = entityManager.getTransaction();
        try {
            transaction.begin();
            entityManager.persist(service);
            transaction.commit();
        } catch (Exception e) {
            if (transaction.isActive()) {
                transaction.rollback();
            }
            throw e;
        }
    }

    // Lấy dịch vụ theo ID
    public Service getService(int id) {
        return entityManager.find(Service.class, id);
    }

    // Lấy tất cả dịch vụ
    public List<Service> getAllServices() {
        TypedQuery<Service> query = entityManager.createQuery("SELECT s FROM Service s", Service.class);
        return query.getResultList();
    }

    // Cập nhật thông tin dịch vụ
    public void updateService(Service service) {
        EntityTransaction transaction = entityManager.getTransaction();
        try {
            transaction.begin();
            entityManager.merge(service);
            transaction.commit();
        } catch (Exception e) {
            if (transaction.isActive()) {
                transaction.rollback();
            }
            throw e;
        }
    }

    // Xóa dịch vụ
    public void deleteService(int id) {
        EntityTransaction transaction = entityManager.getTransaction();
        try {
            transaction.begin();
            Service service = entityManager.find(Service.class, id);
            if (service != null) {
                entityManager.remove(service);
            }
            transaction.commit();
        } catch (Exception e) {
            if (transaction.isActive()) {
                transaction.rollback();
            }
            throw e;
        }
    }

    // Đóng kết nối
    public void close() {
        if (entityManager != null) {
            entityManager.close();
        }
        if (entityManagerFactory != null) {
            entityManagerFactory.close();
        }
    }

    @Override
    public List<Service> searchServices(String keyword, boolean availableOnly) {
        EntityManager em = AppUtil.getEntityManager();
        try {
            String jpql = "SELECT s FROM Service s WHERE " +
                    "(LOWER(s.name) LIKE LOWER(:keyword) OR " +
                    "LOWER(s.description) LIKE LOWER(:keyword))";

            if (availableOnly) {
                jpql += " AND s.availability = true";
            }

            return em.createQuery(jpql, Service.class)
                    .setParameter("keyword", "%" + keyword + "%")
                    .getResultList();
        } finally {
            em.close();
        }
    }

    @Override
    public Service findServiceByID(int id) {
        return entityManager.find(Service.class, id);
    }

    public List<Service> searchServicesByName(String keyword) {
        EntityManager em = AppUtil.getEntityManager();
        try {
            String jpql = "SELECT s FROM Service s WHERE " +
                    "(LOWER(s.name) LIKE LOWER(:keyword) OR " +
                    "LOWER(s.description) LIKE LOWER(:keyword))";

            return em.createQuery(jpql, Service.class)
                    .setParameter("keyword", "%" + keyword + "%")
                    .getResultList();
        } finally {
            em.close();
        }
    }
}
