package BankApplication.bank.service.impl;

import BankApplication.account.impl.AbstractAccount;
import BankApplication.bank.Bank;
import BankApplication.bank.listeners.IClientRegistrationListener;
import BankApplication.bank.service.IBankService;
import BankApplication.client.Client;

import java.util.List;

/**
 * Created by Kir Kolesnikov on 14.01.2015.
 */
public class BankServiceImpl implements IBankService {
    @Override
    public void addClient(Bank bank, Client client) {
        List<Client> clientsList = bank.getClientsList();
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
}
