package BankApplication.service.impl;


import BankApplication.DAO.impl.DAOFactory;
import BankApplication.exceptions.ClientNotFoundException;
import BankApplication.model.Account;
import BankApplication.model.impl.Bank;
import BankApplication.model.impl.Client;
import BankApplication.service.ClientService;

import java.io.*;

/**
 * Created by Kir Kolesnikov on 29.01.2015.
 */
public class ClientServiceImpl implements ClientService {
    private static ClientServiceImpl instance;
    private static Client currentClient;

    private ClientServiceImpl() {

    }

    public static ClientServiceImpl getInstance() {
        if (instance == null) {
            return new ClientServiceImpl();
        }
        return instance;
    }

    public Client getCurrentClient() {
        return currentClient;
    }

    public void setCurrentClient(Client currentClient) {

        this.currentClient = currentClient;
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
