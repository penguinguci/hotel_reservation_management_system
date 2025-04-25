package dao;

import entities.Account;
import interfaces.AccountDAO;
import interfaces.GenericDAO;
import jakarta.persistence.*;
import utils.AppUtil;

import java.io.Serializable;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.List;

public class AccountDAOImpl extends GenericDAOImpl<Account, String> implements AccountDAO, Serializable {
    private static final long serialVersionUID = 1L;
    private EntityManager em;
    private GenericDAO genericDAO;

    public AccountDAOImpl() throws RemoteException {
        super(Account.class);
        em = AppUtil.getEntityManager();
    }

    public void setGenericDAO(GenericDAO genericDAO) {
        this.genericDAO = genericDAO;
    }

    public void createAccount(Account account) {
        EntityTransaction transaction = em.getTransaction();
        try {
            transaction.begin();
            em.persist(account);
            transaction.commit();
            if (genericDAO != null) {
                genericDAO.notifyClients("Account created: " + account.getUsername());
            }
        } catch (Exception e) {
            if (transaction.isActive()) {
                transaction.rollback();
            }
            throw new RuntimeException(e);
        }
    }

    public Account getAccount(String username) {
        return em.find(Account.class, username);
    }

    @Override
    public List<Account> getAllAccounts() {
        TypedQuery<Account> query = em.createQuery("SELECT a FROM Account a", Account.class);
        return query.getResultList();
    }

    public void updateAccount(Account account) {
        EntityTransaction transaction = em.getTransaction();
        try {
            transaction.begin();
            em.merge(account);
            transaction.commit();
            if (genericDAO != null) {
                genericDAO.notifyClients("Account updated: " + account.getUsername());
            }
        } catch (Exception e) {
            if (transaction.isActive()) {
                transaction.rollback();
            }
            throw new RuntimeException(e);
        }
    }

    public void deleteAccount(String username) {
        EntityTransaction transaction = em.getTransaction();
        try {
            transaction.begin();
            Account account = em.find(Account.class, username);
            if (account != null) {
                em.remove(account);
                transaction.commit();
                if (genericDAO != null) {
                    genericDAO.notifyClients("Account deleted: " + username);
                }
            } else {
                transaction.commit();
            }
        } catch (Exception e) {
            if (transaction.isActive()) {
                transaction.rollback();
            }
            throw new RuntimeException(e);
        }
    }

    @Override
    public Account findAccoutByStaffID(String staffID) throws RemoteException {
        String query = "SELECT a FROM Account a WHERE a.staff.id = :staffID";
        TypedQuery<Account> typedQuery = em.createQuery(query, Account.class);
        typedQuery.setParameter("staffID", staffID);
        List<Account> accounts = typedQuery.getResultList();
        return accounts.isEmpty() ? null : accounts.get(0);
    }

    @Override
    public boolean isUsernameExists(String username) throws RemoteException {
        if (username == null || username.isEmpty()) return false;

        Long count = em.createQuery("SELECT COUNT(a) FROM Account a WHERE a.username = :username", Long.class)
                .setParameter("username", username)
                .getSingleResult();
        return count > 0;
    }

    @Override
    public Account getAccountByEmail(String email) throws RemoteException {
        TypedQuery<Account> query = em.createQuery(
                "SELECT a FROM Account a JOIN a.staff s WHERE s.email = :email",
                Account.class
        );
        query.setParameter("email", email);
        try {
            return query.getSingleResult();
        } catch (Exception e) {
            return null;
        }
    }
}