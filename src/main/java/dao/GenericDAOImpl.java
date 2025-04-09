package dao;

import interfaces.GenericDAO;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityTransaction;
import utils.AppUtil;

import java.util.List;

public class GenericDAOImpl<T, ID> implements GenericDAO<T, ID> {
    private final EntityManager em;
    private final Class<T> entityClass;

    public GenericDAOImpl() {
        em = AppUtil.getEntityManager();
        this.entityClass = null;
    }

    public GenericDAOImpl(EntityManager em, Class<T> entityClass) {
        this.em = em;
        this.entityClass = entityClass;
    }

    // Create
    @Override
    public boolean create(T entity) {
        EntityTransaction tr = em.getTransaction();
        try {
            tr.begin();
            em.persist(entity);
            tr.commit();
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            tr.rollback();
            return false;
        }
    }

    // Update
    @Override
    public boolean update(T entity) {
        EntityTransaction tr = em.getTransaction();
        try {
            tr.begin();
            em.merge(entity);
            tr.commit();
            return true;
        } catch (Exception ex) {
            ex.printStackTrace();
            tr.rollback();
            return false;
        }
    }

    // Delete
    @Override
    public boolean delete(ID id) {
        EntityTransaction tr = em.getTransaction();
        try {
            tr.begin();
            T entity = em.find(entityClass, id);
            if (entity != null) {
                em.remove(entity);
            }
            tr.commit();
            return true;
        } catch (Exception ex) {
            ex.printStackTrace();
            tr.rollback();
            return false;
        }
    }

    // Find by ID
    @Override
    public T findById(ID id) {
        return em.find(entityClass, id);
    }

    // Find all
    @Override
    public List<T> findAll() {
        String query = "SELECT e FROM " + entityClass.getSimpleName() + " e";
        return em.createQuery(query, entityClass).getResultList();
    }
}
