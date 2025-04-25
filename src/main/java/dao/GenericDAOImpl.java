package dao;

import entities.Room;
import interfaces.ClientCallback;
import interfaces.GenericDAO;
import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import utils.AppUtil;

import java.io.Serializable;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;
import java.util.List;

public class GenericDAOImpl<T, ID> extends UnicastRemoteObject  implements GenericDAO<T, ID>, Serializable {
    private static final long serialVersionUID = 1L;
    private final EntityManager em;
    private final Class<T> entityClass;
    private List<ClientCallback> clients = new ArrayList<>();

    public GenericDAOImpl(EntityManager em, Class<T> entityClass) throws RemoteException {
        super();
        this.em = em;
        this.entityClass = entityClass;
    }

    public GenericDAOImpl(Class<T> entityClass) throws RemoteException {
        super();
        this.em = AppUtil.getEntityManager();
        this.entityClass = entityClass;
    }

    public GenericDAOImpl() throws RemoteException {
        super();
        this.em = AppUtil.getEntityManager();
        this.entityClass = null;
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


    // Find with pagination
    public List<T> findAll(int page, int pageSize) {
        String queryStr = "SELECT e FROM " + entityClass.getSimpleName() + " e";
        TypedQuery<T> query = em.createQuery(queryStr, entityClass)
                .setFirstResult((page - 1) * pageSize)
                .setMaxResults(pageSize);
        return query.getResultList();
    }

    // Count all records
    public long countAll() {
        String queryStr = "SELECT COUNT(e) FROM " + entityClass.getSimpleName() + " e";
        return em.createQuery(queryStr, Long.class).getSingleResult();
    }


    @Override
    public void registerClient(ClientCallback client) throws RemoteException {
        synchronized (clients) {
            if (!clients.contains(client)) {
                clients.add(client);
                System.out.println("Client registered. Total clients: " + clients.size());
            }
        }
    }

    @Override
    public void unregisterClient(ClientCallback client) throws RemoteException {
        synchronized (clients) {
            clients.remove(client);
            System.out.println("Client unregistered. Total clients: " + clients.size());
        }
    }

    @Override
    public void notifyClients(String message) throws RemoteException {
        synchronized (clients) {
            List<ClientCallback> toRemove = new ArrayList<>();
            for (ClientCallback client : clients) {
                try {
                    client.onDataChange(message);
                } catch (RemoteException e) {
                    // Nếu client không thể truy cập được, loại bỏ khỏi danh sách
                    toRemove.add(client);
                }
            }
            clients.removeAll(toRemove);
        }
    }
}
