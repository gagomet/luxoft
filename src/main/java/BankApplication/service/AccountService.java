package BankApplication.service;

import BankApplication.exceptions.*;
import BankApplication.exceptions.IllegalArgumentException;
import BankApplication.model.Account;
import BankApplication.model.impl.Client;

/**
 * Created by Kir Kolesnikov on 29.01.2015.
 */
public interface AccountService {

    public void depositeFunds(Account account, float amount) throws BankApplication.exceptions.IllegalArgumentException;

    public void withdrawFunds(Account account, float amount) throws NotEnoughFundsException, IllegalArgumentException;

    public Account getAccountById(Client client, Long id) throws AccountNotFoundException;

    public void transferFunds(Account sender, Account recipient, float amount);
}
