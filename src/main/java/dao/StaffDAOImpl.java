package dao;

import entities.Staff;
import interfaces.StaffDAO;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityTransaction;
import jakarta.persistence.Id;
import jakarta.persistence.TypedQuery;
import lombok.AllArgsConstructor;
import utils.AppUtil;

import java.lang.reflect.Field;
import java.util.List;

@AllArgsConstructor
public class StaffDAOImpl implements StaffDAO {
    private EntityManager em;

    public StaffDAOImpl () {
        em = AppUtil.getEntityManager();
    }

    // create
    public boolean create(Staff staff) {
        EntityTransaction tr = em.getTransaction();
        try {
            tr.begin();
                em.persist(staff);
            tr.commit();
            return true;
        } catch (Exception ex) {
            ex.printStackTrace();
            tr.rollback();
        }
        return false;
    }

    // update
    public boolean update(Staff staff) {
        EntityTransaction tr = em.getTransaction();
        try {
            tr.begin();
            em.merge(staff);
            tr.commit();
            return true;
        } catch (Exception ex) {
            ex.printStackTrace();
            tr.rollback();
        }
        return false;
    }

    // delete
    public boolean delete(String id) {
        EntityTransaction tr = em.getTransaction();
        try {
            tr.begin();
            Staff staff = em.find(Staff.class, id);
            em.remove(staff);
            tr.commit();
            return true;
        } catch (Exception ex) {
            ex.printStackTrace();
            tr.rollback();
        }
        return false;
    }

    // tìm kiếm nhân viên theo tên
    public List<Staff> listStaffByName(String name) {
        String query = "select s from Staff s where lower(s.firstName) like lower(:name)" +
                "or lower(s.lastName) like lower(:name) ";

        return em.createQuery(query, Staff.class)
                .setParameter("name", "%" + name + "%")
                .getResultList();
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
}
