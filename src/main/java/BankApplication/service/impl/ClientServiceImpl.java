package BankApplication.service.impl;

import BankApplication.DAO.AccountDAO;
import BankApplication.DAO.ClientDAO;
import BankApplication.DAO.impl.AccountDAOImpl;
import BankApplication.DAO.impl.ClientDAOImpl;
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

    private ClientDAO clientDAO = new ClientDAOImpl();
    private AccountDAO accountDAO = new AccountDAOImpl();

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
