package BankApplication.service;

import BankApplication.account.impl.AbstractAccount;
import BankApplication.exceptions.AccountNotFoundException;
import BankApplication.exceptions.ClientExceedsException;
import BankApplication.exceptions.ClientNotFoundException;
import BankApplication.exceptions.IllegalArgumentException;
import BankApplication.exceptions.NotEnoughFundsException;
import BankApplication.listeners.IClientRegistrationListener;
import BankApplication.model.Bank;
import BankApplication.model.client.Client;

import java.util.List;
import java.util.ResourceBundle;

/**
 * Created by Kir Kolesnikov on 16.01.2015.
 */
public enum BankServiceEnumSingletone {
    INSTANCE;

    private static ResourceBundle errorsBundle = ResourceBundle.getBundle("errors");

    public static void addClient(Bank bank, Client client) throws ClientExceedsException {
        List<Client> clientsList = bank.getClientsList();
        //here must be DB selection :)
        for (Client tempClient : bank.getClientsList()) {
            if (client.equals(tempClient)) {
                throw new ClientExceedsException(errorsBundle.getString("clientAlreadyExceeds"));
            }
        }
        clientsList.add(client);
        for (IClientRegistrationListener listener : bank.getListeners()) {
            listener.onClientAdded(client);
        }
        bank.setClientsList(clientsList);
    }

    public static void removeClient(Bank bank, Client client) {
        List<Client> clientsList = bank.getClientsList();
        for (int i = 0; i < clientsList.size(); i++) {
            if (clientsList.get(i).equals(client)) {
                System.out.println("removing " + clientsList.get(i).getName());
                clientsList.remove(i);
                break;
            }
        }
        bank.setClientsList(clientsList);
    }

    public static void addAccount(Client client, AbstractAccount account) {
        List<AbstractAccount> accounts = client.getAccountsList();
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

    public static AbstractAccount getAccountById(Client client, Long id) throws AccountNotFoundException {
        AbstractAccount searchResult = null;
        for (AbstractAccount account : client.getAccountsList()) {
            if (id == account.getId()) {
                searchResult = account;
                break;
            }
        }
        if (searchResult == null) {
            throw new AccountNotFoundException(errorsBundle.getString("accountNotFound"));
        }
        return searchResult;
    }

    public static void setActiveAccount(Client client, AbstractAccount account) {
        client.setActiveAccount(account);
    }

    public static void depositeFunds(AbstractAccount account, float amount) throws IllegalArgumentException {
        account.deposit(amount);
    }

    public static void withdrawFunds(AbstractAccount account, float amount) throws NotEnoughFundsException {
        account.withdraw(amount);
    }
}
