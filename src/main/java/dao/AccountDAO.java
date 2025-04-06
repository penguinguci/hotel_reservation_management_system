package dao;

import entities.Account;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.List;

public interface AccountDAO extends Remote {
    public void deleteAccount(String username) throws RemoteException;
    public void updateAccount(Account account) throws RemoteException;
    public List<Account> getAllAccounts() throws RemoteException;
    public Account getAccount(String username) throws RemoteException;
    public void createAccount(Account account) throws RemoteException;
}
