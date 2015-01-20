package BankApplication.service.bankservice;

import BankApplication.model.account.Account;
import BankApplication.model.account.impl.AbstractAccount;
import BankApplication.exceptions.*;
import BankApplication.exceptions.IllegalArgumentException;
import BankApplication.model.Bank;
import BankApplication.model.Client;

/**
 * Created by Kir Kolesnikov on 14.01.2015.
 */
public interface BankService {
    public void addClient(Bank bank, Client client) throws ClientExceedsException;

    public void removeClient(Bank bank, Client client);

    public void addAccount(Client client, Account account);

    public void setActiveAccount(Client client, Account account);

    public void depositeFunds(Account account, float amount) throws IllegalArgumentException;

    public void withdrawFunds(Account account, float amount) throws NotEnoughFundsException;

    public Client getClientByName(Bank bank, String clientsName) throws ClientNotFoundException;

    public Account getAccountById(Client client, Long id) throws AccountNotFoundException;

    public void saveClient(Client client);

    public Client loadClient();
}
