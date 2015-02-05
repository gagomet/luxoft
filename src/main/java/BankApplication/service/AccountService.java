package BankApplication.service;

import BankApplication.exceptions.*;
import BankApplication.model.Account;
import BankApplication.model.impl.Client;

/**
 * Created by Kir Kolesnikov on 29.01.2015.
 */
public interface AccountService {

    public void depositeFunds(Account account, float amount) ;

    public void withdrawFunds(Account account, float amount) throws NotEnoughFundsException;

    public Account getAccountById(Client client, Long id) throws AccountNotFoundException;

    public void transferFunds(Account sender, Account recipient, float amount);
}
