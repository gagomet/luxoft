package BankApplication.service;

import BankApplication.exceptions.ClientExceedsException;
import BankApplication.model.impl.Bank;
import BankApplication.model.impl.BankInfo;
import BankApplication.model.impl.Client;

/**
 * Created by Kir Kolesnikov on 29.01.2015.
 */
public interface BankService {

    public void addClient(Bank bank, Client client) throws ClientExceedsException;

    public void removeClient(Bank bank, Client client);

    public BankInfo getBankInfo(Bank bank);

    public Bank getCurrentBank();

    public void setCurrentBank(Bank bank);

}
