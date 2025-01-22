package dao;

import entities.Staff;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityTransaction;
import lombok.AllArgsConstructor;

import java.util.List;

@AllArgsConstructor
public class StaffDAO {
    private EntityManager em;

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
}
