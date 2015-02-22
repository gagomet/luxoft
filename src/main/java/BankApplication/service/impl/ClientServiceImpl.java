package BankApplication.service.impl;


import BankApplication.DAO.impl.DAOFactory;
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

    private ClientServiceImpl() {

    }

    private static class LazyHolder {
        private static final ClientServiceImpl INSTANCE = new ClientServiceImpl();
    }

    public static ClientServiceImpl getInstance() {
        return LazyHolder.INSTANCE;
    }

    @Override
    public void addAccount(Client client, Account account) {
        DAOFactory.getAccountDAO().save(account, client);
    }

    @Override
    public void setActiveAccount(Client client, Account account) {
        client.setActiveAccount(account);
    }

    @Override
    public Client getClientByName(Bank bank, String clientsName) throws ClientNotFoundException {
        return DAOFactory.getClientDAO().findClientByName(bank, clientsName);
    }

    @Override
    public Client saveClient(Bank bank, Client client) {
        return DAOFactory.getClientDAO().save(bank, client);
    }


    @Override
    public List<Client> getClientsByNameAndCity(String clientsName, String city) {
        return DAOFactory.getClientDAO().getClientsByNameAndCity(clientsName, city);
    }

    @Override
    public Client getClientById(long id) throws ClientNotFoundException {
        return DAOFactory.getClientDAO().findClientById(id);
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
