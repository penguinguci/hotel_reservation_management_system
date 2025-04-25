package dao;

import entities.Staff;
import interfaces.StaffDAO;
import interfaces.GenericDAO;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityTransaction;
import jakarta.persistence.Id;
import jakarta.persistence.TypedQuery;
import utils.AppUtil;

import java.io.Serializable;
import java.lang.reflect.Field;
import java.rmi.Remote;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.List;

public class StaffDAOImpl extends GenericDAOImpl<Staff, String> implements StaffDAO, Serializable, Remote {
    private static final long serialVersionUID = 1L;
    private EntityManager em;
    private GenericDAO genericDAO;

    public StaffDAOImpl() throws RemoteException {
        super(Staff.class);
        em = AppUtil.getEntityManager();
    }

    public void setGenericDAO(GenericDAO genericDAO) {
        this.genericDAO = genericDAO;
    }

    // create
    public boolean create(Staff staff) {
        EntityTransaction tr = em.getTransaction();
        try {
            tr.begin();
            em.persist(staff);
            tr.commit();
            if (genericDAO != null) {
                genericDAO.notifyClients("Staff created: " + staff.getStaffId());
            }
            return true;
        } catch (Exception ex) {
            ex.printStackTrace();
            tr.rollback();
            return false;
        }
    }

    // update
    public boolean update(Staff staff) {
        EntityTransaction tr = em.getTransaction();
        try {
            tr.begin();
            em.merge(staff);
            tr.commit();
            if (genericDAO != null) {
                genericDAO.notifyClients("Staff updated: " + staff.getStaffId());
            }
            return true;
        } catch (Exception ex) {
            ex.printStackTrace();
            tr.rollback();
            return false;
        }
    }

    // delete
    public boolean delete(String id) {
        EntityTransaction tr = em.getTransaction();
        try {
            tr.begin();
            Staff staff = em.find(Staff.class, id);
            if (staff != null) {
                em.remove(staff);
                tr.commit();
                if (genericDAO != null) {
                    genericDAO.notifyClients("Staff deleted: " + id);
                }
                return true;
            }
            tr.commit();
            return false;
        } catch (Exception ex) {
            ex.printStackTrace();
            tr.rollback();
            return false;
        }
    }

    // tìm kiếm nhân viên theo tên
    public List<Staff> listStaffByName(String name) {
        String query = "select s from Staff s where lower(s.firstName) like lower(:name)" +
                " or lower(s.lastName) like lower(:name) ";

        return em.createQuery(query, Staff.class)
                .setParameter("name", "%" + name + "%")
                .getResultList();
    }

    // Hàm hỗ trợ lấy tên trường ID
    private String getIdFieldName() {
        for (Field field : Staff.class.getDeclaredFields()) {
            if (field.isAnnotationPresent(Id.class)) {
                return field.getName();
            }
        }
        throw new IllegalStateException("No @Id field found in " + Staff.class.getSimpleName());
    }

    @Override
    public boolean isEmailExists(String email) {
        if (email == null || email.isEmpty()) return false;

        Long count = em.createQuery("SELECT COUNT(s) FROM Staff s WHERE s.email = :email", Long.class)
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
                        "SELECT COUNT(s) FROM Staff s JOIN s.phoneNumbers p WHERE p = :phone",
                        Long.class)
                .setParameter("phone", phone)
                .getSingleResult();

        return count > 0;
    }

    @Override
    public List<Staff> searchStaffAdvanced(String id, String name, String phone, Boolean gender) {
        StringBuilder queryStr = new StringBuilder(
                "SELECT DISTINCT s FROM Staff s LEFT JOIN FETCH s.phoneNumbers WHERE 1=1"
        );

        if (id != null && !id.isEmpty()) {
            queryStr.append(" AND s.staffId LIKE :id");
        }
        if (name != null && !name.isEmpty()) {
            queryStr.append(" AND (LOWER(s.firstName) LIKE LOWER(:name) OR LOWER(s.lastName) LIKE LOWER(:name))");
        }
        if (phone != null && !phone.isEmpty()) {
            queryStr.append(" AND EXISTS (SELECT p FROM s.phoneNumbers p WHERE p LIKE :phone)");
        }
        if (gender != null) {
            queryStr.append(" AND s.gender = :gender");
        }

        TypedQuery<Staff> query = em.createQuery(queryStr.toString(), Staff.class);

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

        return query.getResultList();
    }

    // Count by prefix
    @Override
    public long countByPrefix(String prefix) {
        try {
            String idFieldName = getIdFieldName();
            String queryStr = "SELECT COUNT(s) FROM Staff s WHERE LOWER(s." + idFieldName + ") LIKE LOWER(:prefix)";
            return em.createQuery(queryStr, Long.class)
                    .setParameter("prefix", prefix + "%")
                    .getSingleResult();
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("Error counting by prefix", e);
        }
    }

    @Override
    public void updateStatus(String staffId, boolean status) throws RemoteException {
        EntityTransaction transaction = em.getTransaction();
        try {
            transaction.begin();
            Staff staff = em.find(Staff.class, staffId);
            if (staff != null) {
                staff.setStatus(status);
                em.merge(staff);
                transaction.commit();
                if (genericDAO != null) {
                    genericDAO.notifyClients("Staff status updated: " + staffId + ", new status: " + status);
                }
            } else {
                transaction.commit();
            }
        } catch (Exception e) {
            if (transaction.isActive()) {
                transaction.rollback();
            }
            throw e;
        }
    }
}