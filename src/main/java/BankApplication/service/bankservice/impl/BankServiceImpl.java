package BankApplication.service.bankservice.impl;

import BankApplication.model.account.impl.AbstractAccount;
import BankApplication.exceptions.AccountNotFoundException;
import BankApplication.exceptions.ClientExceedsException;
import BankApplication.exceptions.ClientNotFoundException;
import BankApplication.exceptions.IllegalArgumentException;
import BankApplication.exceptions.NotEnoughFundsException;
import BankApplication.model.IClientRegistrationListener;
import BankApplication.model.Bank;
import BankApplication.model.Client;
import BankApplication.service.bankservice.IBankService;

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
 * Created by Kir Kolesnikov on 14.01.2015.
 */
public class BankServiceImpl implements IBankService {
    protected ResourceBundle errorsBundle = ResourceBundle.getBundle("errors");

    @Override
    public void addClient(Bank bank, Client client) throws ClientExceedsException {
        Set<Client> clientsList = bank.getClientsList();
        //here must be DB selection :)
        for (Client tempClient : bank.getClientsList()) {
            if (client.equals(tempClient)) {
                throw new ClientExceedsException(errorsBundle.getString("clientAlreadyExceeds"));
            }
        }
        bank.addClient(client);
        for (IClientRegistrationListener listener : bank.getListeners()) {
            listener.onClientAdded(client);
        }
    }

    @Override
    public void removeClient(Bank bank, Client client) {
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
        /*List<Client> clientsList = bank.getClientsList();
        for (int i = 0; i < clientsList.size(); i++) {
            if (clientsList.get(i).equals(client)) {
                System.out.println("removing " + clientsList.get(i).getName());
                clientsList.remove(i);
                break;
            }
        }*/
        bank.setClientsList(clientsList);
    }

    @Override
    public void addAccount(Client client, AbstractAccount account) {
        Set<AbstractAccount> accounts = client.getAccountsList();
        accounts.add(account);
        client.setAccountsList(accounts);
    }

    @Override
    public void setActiveAccount(Client client, AbstractAccount account) {
        client.setActiveAccount(account);
    }

    @Override
    public void depositeFunds(AbstractAccount account, float amount) throws IllegalArgumentException {
        account.deposit(amount);
    }

    @Override
    public void withdrawFunds(AbstractAccount account, float amount) throws NotEnoughFundsException {
        account.withdraw(amount);
    }

    @Override
    public Client getClientByName(Bank bank, String clientsName) throws ClientNotFoundException {
        Client searchResult = null;
        for (Client client : bank.getClientsList()) {
            if (clientsName.equals(client.getName())) {
                searchResult = client;
                break;
            } else {
                throw new ClientNotFoundException(errorsBundle.getString("clientNotFound"));
            }
        }
        return searchResult;
    }

    @Override
    public AbstractAccount getAccountById(Client client, Long id) throws AccountNotFoundException {
        AbstractAccount searchResult = null;
        for (AbstractAccount account : client.getAccountsList()) {
            if (id == account.getId()) {
                searchResult = account;
                break;
            } else {
                throw new AccountNotFoundException(errorsBundle.getString("accountNotFound"));
            }
        }
        return searchResult;
    }

    @Override
    public void saveClient(Client client) {
        try(FileOutputStream fileOutputStream = new FileOutputStream("c:\\!toBankApplicationSerialization\\object.ser")){
            ObjectOutputStream objectOutputStream = new ObjectOutputStream(fileOutputStream);
            objectOutputStream.writeObject(client);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public Client loadClient() {
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


}
