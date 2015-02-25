package BankApplication.service.impl;

import BankApplication.DAO.AccountDAO;
import BankApplication.DAO.BankDAO;
import BankApplication.DAO.ClientDAO;
import BankApplication.DAO.impl.AccountDAOImpl;
import BankApplication.DAO.impl.BankDAOImpl;
import BankApplication.DAO.impl.ClientDAOImpl;
import BankApplication.exceptions.AccountNotFoundException;
import BankApplication.exceptions.ClientExceedsException;
import BankApplication.exceptions.ClientNotFoundException;
import BankApplication.exceptions.NotEnoughFundsException;
import BankApplication.model.Account;
import BankApplication.model.impl.Bank;
import BankApplication.model.impl.BankInfo;
import BankApplication.model.impl.Client;
import BankApplication.service.AccountService;
import BankApplication.service.BankService;
import BankApplication.service.ClientService;

import java.io.*;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Created by Kir Kolesnikov on 25.02.2015.
 */
public class FullBankService implements BankService, ClientService, AccountService {
    private static final Logger logger = Logger.getLogger(AccountServiceImpl.class.getName());

    private BankDAO bankDAO;
    private ClientDAO clientDAO;
    private AccountDAO accountDAO;
    private Bank currentBank;


    public void setBankDAO(BankDAOImpl bankDAO) {
    }

    public void setClientDAO(ClientDAOImpl clientDAO) {
    }

    public void setAccountDAO(AccountDAOImpl accountDAO) {
    }


    @Override
    public synchronized void depositeFunds(Account account, float amount) throws IllegalArgumentException {
        try {
            account.deposit(amount);
            Client client = clientDAO.findClientById(account.getClientId());
            accountDAO.save(account, client);
            logger.log(Level.INFO, "Account: " + account.getId() + "was refilled " + amount);
        } catch (ClientNotFoundException e) {
            logger.log(Level.SEVERE, e.getMessage(), e);
        }
    }

    @Override
    public synchronized void withdrawFunds(Account account, float amount) throws NotEnoughFundsException, IllegalArgumentException {
        try {
            account.withdraw(amount);
            Client client = clientDAO.findClientById(account.getClientId());

//            lock.lock();
            accountDAO.save(account, client);
            logger.log(Level.INFO, "Account: " + account.getId() + "was redused " + amount);
//            lock.unlock();

        } catch (ClientNotFoundException e) {
            System.out.println(e.getMessage());
        }
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
    public synchronized void transferFunds(Account sender, Account recipient, float amount) {
        accountDAO.transferFunds(sender, recipient, amount);
        logger.log(Level.INFO, amount + " funds transfered from account id:" + sender.getId() + " to account id: " + recipient.getId());
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
    public Client getClientByName(Bank bank, String clientsName) throws ClientNotFoundException {
        return clientDAO.findClientByName(bank, clientsName);
    }

    @Override
    public Client saveClient(Bank bank, Client client) {
        return clientDAO.save(bank, client);
    }


    @Override
    public List<Client> getClientsByNameAndCity(String clientsName, String city) {
        return clientDAO.getClientsByNameAndCity(clientsName, city);
    }

    @Override
    public Client getClientById(long id) throws ClientNotFoundException {
        return clientDAO.findClientById(id);
    }

    @Override
    public void saveClientToFeedFile(Client client) {
        try (FileOutputStream fileOutputStream = new FileOutputStream("c:\\!toBankApplicationSerialization\\object.ser")) {
            ObjectOutputStream objectOutputStream = new ObjectOutputStream(fileOutputStream);
            objectOutputStream.writeObject(client);
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
        } catch (ClassNotFoundException | IOException e) {
            e.printStackTrace();
        }
        return result;
    }

    @Override
    public Bank getCurrentBank() {
        return currentBank;
    }

    @Override
    public void setCurrentBank(Bank currentBank) {
        this.currentBank = currentBank;
    }

    @Override
    public void addClient(Bank bank, Client client) throws ClientExceedsException {
        clientDAO.save(bank, client);
    } //Update

    @Override
    public Bank getBankByName(String bankName) {
        return bankDAO.getBankByName(bankName);
    }

    @Override
    public void removeClient(Bank bank, Client client) {
        clientDAO.remove(client);
    } //Update

    @Override
    public BankInfo getBankInfo(Bank bank) {
        return bankDAO.getBankInfo(bank);
    }

}
