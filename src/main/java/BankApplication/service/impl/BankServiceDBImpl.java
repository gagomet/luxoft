package BankApplication.service.impl;

import BankApplication.exceptions.*;
import BankApplication.exceptions.IllegalArgumentException;
import BankApplication.model.Account;
import BankApplication.model.impl.Bank;
import BankApplication.model.impl.Client;
import BankApplication.service.BankService;
import BankApplication.service.DAO.AccountDAO;
import BankApplication.service.DAO.ClientDAO;
import BankApplication.service.DAO.impl.AccountDAOImpl;
import BankApplication.service.DAO.impl.ClientDAOImpl;

import java.io.*;
import java.util.List;

/**
 * Created by Kir Kolesnikov on 28.01.2015.
 */
public class BankServiceDBImpl implements BankService {
    private ClientDAO clientDAO = new ClientDAOImpl();
    private AccountDAO accountDAO = new AccountDAOImpl();


    @Override
    public void addClient(Bank bank, Client client) throws ClientExceedsException {
        clientDAO.save(bank, client);
    }

    @Override
    public void removeClient(Bank bank, Client client) {
        clientDAO.remove(client);
    }

    @Override
    public void addAccount(Client client, Account account) {
        accountDAO.save(account, client);
    }

    @Override
    public void setActiveAccount(Client client, Account account) {
        client.setActiveAccount(account);
    }

    @Override
    public void depositeFunds(Account account, float amount) throws BankApplication.exceptions.IllegalArgumentException {
        try {
            account.deposit(amount);
            Client client = clientDAO.findClientById(account.getClientId());
            accountDAO.save(account, client);
        } catch (ClientNotFoundException e) {
            e.printStackTrace();
        }

    }

    @Override
    public void withdrawFunds(Account account, float amount) throws NotEnoughFundsException, IllegalArgumentException {
        try {
            account.withdraw(amount);
            Client client = clientDAO.findClientById(account.getClientId());
            accountDAO.save(account, client);
        } catch (ClientNotFoundException e) {
            e.printStackTrace();
        }
    }

    @Override
    public Client getClientByName(Bank bank, String clientsName) throws ClientNotFoundException {
        return clientDAO.findClientByName(bank, clientsName);
    }

    @Override
    public Account getAccountById(Client client, Long id) throws AccountNotFoundException {
        List<Account> accountList = accountDAO.getClientAccounts(client.getId());
        Account result = null;
        for (Account account : accountList) {
            if (account.getId() == id) {
                result = account;
            }
        }
        return result;
    }

    @Override
    public void saveClientToFeedFile(Client client) {
        try (FileOutputStream fileOutputStream = new FileOutputStream("c:\\!toBankApplicationSerialization\\object.ser")) {
            ObjectOutputStream objectOutputStream = new ObjectOutputStream(fileOutputStream);
            objectOutputStream.writeObject(client);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public Client loadClientFromFeedFile() {
        Client result = null;
        try (FileInputStream fileInputStream = new FileInputStream("c:\\!toBankApplicationSerialization\\object.ser")) {
            ObjectInputStream objectInputStream = new ObjectInputStream(fileInputStream);
            result = (Client) objectInputStream.readObject();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        return result;
    }
}
