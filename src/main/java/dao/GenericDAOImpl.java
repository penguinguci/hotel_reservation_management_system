package dao;

import entities.Staff;
import interfaces.GenericDAO;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityTransaction;
import jakarta.persistence.Id;
import net.datafaker.providers.base.App;
import utils.AppUtil;

import java.lang.reflect.Field;
import java.util.List;

public class GenericDAOImpl<T, ID> implements GenericDAO<T, ID> {
    private final EntityManager em;
    private final Class<T> entityClass;

    public GenericDAOImpl(EntityManager em, Class<T> entityClass) {
        this.em = em;
        this.entityClass = entityClass;
    }

    public GenericDAOImpl(Class<T> entityClass) {
        this.em = AppUtil.getEntityManager();
        this.entityClass = entityClass;
    }

    // Create
    @Override
    public boolean create(T entity) {
        try {
            em.getTransaction().begin();
            em.persist(entity);
            em.getTransaction().commit();
            return true;
        } catch (Exception e) {
            em.getTransaction().rollback();
            e.printStackTrace();
            return false;
        }
    }

    // Update
    @Override
    public boolean update(T entity) {
        try {
            em.getTransaction().begin();
            em.merge(entity);
            em.getTransaction().commit();
            return true;
        } catch (Exception ex) {
            em.getTransaction().rollback();
            ex.printStackTrace();
            return false;
        }
    }

    // Delete
    @Override
    public boolean delete(ID id) {
        try {
            em.getTransaction().begin();
            T entity = em.find(entityClass, id);
            if (entity != null) {
                em.remove(entity);
            }
            em.getTransaction().commit();
            return true;
        } catch (Exception ex) {
            em.getTransaction().rollback();
            ex.printStackTrace();
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
