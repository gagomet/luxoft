package BankApplication.bank.service;

import BankApplication.account.IAccount;
import BankApplication.account.impl.AbstractAccount;
import BankApplication.bank.Bank;
import BankApplication.client.Client;

/**
 * Created by Kir Kolesnikov on 14.01.2015.
 */
public interface IBankService {
    public void addClient(Bank bank,Client client);
    public void removeClient(Bank bank,Client client);
    public void addAccount(Client client, AbstractAccount account);
    public void setActiveAccount(Client client, AbstractAccount account);
}
