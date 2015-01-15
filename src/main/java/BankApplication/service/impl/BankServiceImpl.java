package BankApplication.service.impl;

import BankApplication.account.impl.AbstractAccount;
import BankApplication.exceptions.*;
import BankApplication.exceptions.IllegalArgumentException;
import BankApplication.model.Bank;
import BankApplication.listeners.IClientRegistrationListener;
import BankApplication.service.IBankService;
import BankApplication.model.client.Client;

import java.util.List;
import java.util.ResourceBundle;

/**
 * Created by Kir Kolesnikov on 14.01.2015.
 */
public class BankServiceImpl implements IBankService {
    protected ResourceBundle errorsBundle = ResourceBundle.getBundle("errors");
    @Override
    public void addClient(Bank bank, Client client) throws ClientExceedsException{
        List<Client> clientsList = bank.getClientsList();
        //here must be DB selection :)
        for(Client tempClient : bank.getClientsList()){
            if(client.equals(tempClient)){
                throw new ClientExceedsException(errorsBundle.getString("clientAlreadyExceeds"));
            }
        }
        clientsList.add(client);
        for(IClientRegistrationListener listener : bank.getListeners()){
            listener.onClientAdded(client);
        }
        bank.setClientsList(clientsList);
    }

    @Override
    public void removeClient(Bank bank, Client client) {
        List<Client> clientsList = bank.getClientsList();
        for(int i = 0; i < clientsList.size(); i++){
            if(clientsList.get(i).equals(client)){
                System.out.println("removing " + clientsList.get(i).getName());
                clientsList.remove(i);
                break;
            }
        }
        bank.setClientsList(clientsList);
    }

    @Override
    public void addAccount(Client client, AbstractAccount account) {
        List<AbstractAccount> accounts = client.getAccountsList();
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
    public void withdrawFunds(AbstractAccount account, float amount) throws NotEnoughFundsException{
        account.withdraw(amount);
    }
}
