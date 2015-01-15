package BankApplication.service;

import BankApplication.account.impl.AbstractAccount;
import BankApplication.model.Bank;
import BankApplication.model.client.Client;

/**
 * Created by Kir Kolesnikov on 14.01.2015.
 */
public interface IBankService {
    public void addClient(Bank bank,Client client);
    public void removeClient(Bank bank,Client client);
    public void addAccount(Client client, AbstractAccount account);
    public void setActiveAccount(Client client, AbstractAccount account);
}
