package BankApplication.service;

import BankApplication.exceptions.ClientNotFoundException;
import BankApplication.model.Account;
import BankApplication.model.impl.Bank;
import BankApplication.model.impl.Client;

import java.util.List;

/**
 * Created by Kir Kolesnikov on 29.01.2015.
 */
public interface ClientService {

    public void addAccount(Client client, Account account);

    public void setActiveAccount(Client client, Account account);

    public Client getClientByName(Bank bank, String clientsName) throws ClientNotFoundException;

    public Client saveClient(Bank bank, Client client);

    public void saveClientToFeedFile(Client client);

    public Client loadClientFromFeedFile();

    public List<Client> getClientsByNameAndCity(String clientsName, String city);

    public Client getClientById(long id) throws ClientNotFoundException;

/*    public Client getCurrentClient();

    public void setCurrentClient(Client client);*/
}
