package BankApplication.service;

import BankApplication.model.ClientRegistrationListener;
import BankApplication.model.Account;
import BankApplication.model.impl.AbstractAccount;
import BankApplication.exceptions.AccountNotFoundException;
import BankApplication.exceptions.ClientExceedsException;
import BankApplication.exceptions.ClientNotFoundException;
import BankApplication.exceptions.IllegalArgumentException;
import BankApplication.exceptions.NotEnoughFundsException;
import BankApplication.model.impl.Bank;
import BankApplication.model.impl.Client;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.Iterator;
import java.util.ResourceBundle;
import java.util.Set;

/**
 * Created by Kir Kolesnikov on 16.01.2015.
 */
public enum BankServiceEnumSingletone {
    INSTANCE;

    private static ResourceBundle errorsBundle = ResourceBundle.getBundle("errors");

    public static void addClient(Bank bank, Client client) throws ClientExceedsException {
        Set<Client> clientsList = bank.getClientsList();
        for (Client tempClient : bank.getClientsList()) {
            if (client.equals(tempClient)) {
                throw new ClientExceedsException(errorsBundle.getString("clientAlreadyExceeds"));
            }
        }
        bank.addClient(client);
        for (ClientRegistrationListener listener : bank.getListeners()) {
            listener.onClientAdded(client);
        }
    }

    public static void removeClient(Bank bank, Client client) {
        Set<Client> clientsList = bank.getClientsList();
        Iterator iterator = clientsList.iterator();
        while (iterator.hasNext()) {
            Client tempClient = (Client) iterator.next();
            if (client.equals(tempClient)) {
                System.out.println("removing " + tempClient.getName());
                bank.removeClient(tempClient);
                break;
            }
        }
    }

    public static void addAccount(Client client, AbstractAccount account) {
        Set<Account> accounts = client.getAccountsList();
        accounts.add(account);
        client.setAccountsList(accounts);
    }

    public static Client getClientByName(Bank bank, String clientsName) throws ClientNotFoundException {
        Client searchResult = null;
        for (Client client : bank.getClientsList()) {
            if (clientsName.equals(client.getName())) {
                searchResult = client;
                break;
            }
        }
        if (searchResult == null) {
            throw new ClientNotFoundException(errorsBundle.getString("clientNotFound"));
        }
        return searchResult;
    }

    public static Account getAccountById(Client client, Long id) throws AccountNotFoundException {
        Account searchResult = null;
        for (Account account : client.getAccountsList()) {
            if (id.equals(account.getId())) {
                searchResult = account;
                break;
            }
        }
        if (searchResult == null) {
            throw new AccountNotFoundException(errorsBundle.getString("accountNotFound"));
        }
        return searchResult;
    }

    public static void saveClient(Client client) {
        try(FileOutputStream fileOutputStream = new FileOutputStream("c:\\!toBankApplicationSerialization\\object.ser")){
            ObjectOutputStream objectOutputStream = new ObjectOutputStream(fileOutputStream);
            objectOutputStream.writeObject(client);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static Client loadClient() {
        Client result = null;
        try(FileInputStream fileInputStream = new FileInputStream("c:\\!toBankApplicationSerialization\\object.ser")){
            ObjectInputStream objectInputStream = new ObjectInputStream(fileInputStream);
            result = (Client)objectInputStream.readObject();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        return result;
    }

    public static void setActiveAccount(Client client, AbstractAccount account) {
        client.setActiveAccount(account);
    }

    public static void depositeFunds(Account account, float amount) throws IllegalArgumentException {
        account.deposit(amount);
    }

    public static void withdrawFunds(Account account, float amount) throws NotEnoughFundsException, IllegalArgumentException {
        account.withdraw(amount);
    }
}
