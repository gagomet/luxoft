package BankApplication.service.bankservice;

import BankApplication.model.account.impl.AbstractAccount;
import BankApplication.exceptions.*;
import BankApplication.exceptions.IllegalArgumentException;
import BankApplication.model.Bank;
import BankApplication.model.client.Client;

/**
 * Created by Kir Kolesnikov on 14.01.2015.
 */
public interface IBankService {
    public void addClient(Bank bank, Client client) throws ClientExceedsException;

    public void removeClient(Bank bank, Client client);

    public void addAccount(Client client, AbstractAccount account);

    public void setActiveAccount(Client client, AbstractAccount account);

    public void depositeFunds(AbstractAccount account, float amount) throws IllegalArgumentException;

    public void withdrawFunds(AbstractAccount account, float amount) throws NotEnoughFundsException;

    public Client getClientByName(Bank bank, String clientsName) throws ClientNotFoundException;

    public AbstractAccount getAccountById(Client client, Long id) throws AccountNotFoundException;
}
