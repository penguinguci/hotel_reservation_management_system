package dao;

import entities.Account;
import interfaces.AccountDAO;
import jakarta.persistence.*;

import java.rmi.RemoteException;
import java.util.List;
public class AccountDAOImpl implements AccountDAO {

    @PersistenceContext
    private EntityManagerFactory entityManagerFactory;

    @PersistenceContext
    private EntityManager entityManager;

    public AccountDAOImpl() {
        entityManagerFactory = Persistence.createEntityManagerFactory("mariadb");
        entityManager = entityManagerFactory.createEntityManager();
    }

    public void createAccount(Account account) {
        EntityTransaction transaction = entityManager.getTransaction();
        try {
            transaction.begin();
            entityManager.persist(account);
            transaction.commit();
        } catch (Exception e) {
            if (transaction.isActive()) {
                transaction.rollback();
            }
            throw e;
        }
    }

    public Account getAccount(String username) {
        return entityManager.find(Account.class, username);
    }

    public List<Account> getAllAccounts() {
        TypedQuery<Account> query = entityManager.createQuery("SELECT a FROM Account a", Account.class);
        return query.getResultList();
    }

    public void updateAccount(Account account) {
        EntityTransaction transaction = entityManager.getTransaction();
        try {
            transaction.begin();
            entityManager.merge(account);
            transaction.commit();
        } catch (Exception e) {
            if (transaction.isActive()) {
                transaction.rollback();
            }
            throw e;
        }
    }

    public void deleteAccount(String username) {
        EntityTransaction transaction = entityManager.getTransaction();
        try {
            transaction.begin();
            Account account = entityManager.find(Account.class, username);
            if (account != null) {
                entityManager.remove(account);
            }
            transaction.commit();
        } catch (Exception e) {
            if (transaction.isActive()) {
                transaction.rollback();
            }
            throw e;
        }
    }

    public void close() {
        if (entityManager != null) {
            entityManager.close();
        }
        if (entityManagerFactory != null) {
            entityManagerFactory.close();
        }
    }

    @Override
    public Account findAccoutByStaffID(String staffID) throws RemoteException {
        String query = "SELECT a FROM Account a WHERE a.staff.id = :staffID";
        TypedQuery<Account> typedQuery = entityManager.createQuery(query, Account.class);
        typedQuery.setParameter("staffID", staffID);
        List<Account> accounts = typedQuery.getResultList();
        return accounts.isEmpty() ? null : accounts.get(0);
    }

    @Override
    public boolean isUsernameExists(String username) {
        if (username == null || username.isEmpty()) return false;

        Long count = entityManager.createQuery("SELECT COUNT(a) FROM Account a WHERE a.username = :username", Long.class)
                .setParameter("username", username)
                .getSingleResult();
        return count > 0;
    }

}