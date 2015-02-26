package BankApplication.service.impl;


import BankApplication.DAO.AccountDAO;
import BankApplication.DAO.ClientDAO;
import BankApplication.DAO.impl.DAOFactory;
import BankApplication.commander.BankHolder;
import BankApplication.exceptions.ClientNotFoundException;
import BankApplication.model.Account;
import BankApplication.model.impl.Bank;
import BankApplication.model.impl.Client;
import BankApplication.service.ClientService;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.List;

/**
 * Created by Kir Kolesnikov on 29.01.2015.
 */
public class ClientServiceImpl implements ClientService {

    private AccountDAO accountDAO;
    private ClientDAO clientDAO;
    private BankHolder holder;

    private ClientServiceImpl() {

    }

    private static class LazyHolder {
        private static final ClientServiceImpl INSTANCE = new ClientServiceImpl();
    }

    public AccountDAO getAccountDAO() {
        return accountDAO;
    }

    public void setAccountDAO(AccountDAO accountDAO) {
        this.accountDAO = accountDAO;
    }

    public ClientDAO getClientDAO() {
        return clientDAO;
    }

    public void setClientDAO(ClientDAO clientDAO) {
        this.clientDAO = clientDAO;
    }

    public BankHolder getHolder() {
        return holder;
    }

    public void setHolder(BankHolder holder) {
        this.holder = holder;
    }

    public static ClientServiceImpl getInstance() {
        return LazyHolder.INSTANCE;
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

}
