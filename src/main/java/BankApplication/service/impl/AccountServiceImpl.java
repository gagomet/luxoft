package BankApplication.service.impl;

import BankApplication.DAO.impl.DAOFactory;
import BankApplication.exceptions.*;
import BankApplication.exceptions.IllegalArgumentException;
import BankApplication.model.Account;
import BankApplication.model.impl.CheckingAccount;
import BankApplication.model.impl.Client;
import BankApplication.service.AccountService;

import java.util.List;
import java.util.ResourceBundle;

/**
 * Created by Kir Kolesnikov on 29.01.2015.
 */
public class AccountServiceImpl implements AccountService {
    protected static ResourceBundle errorsBundle = ResourceBundle.getBundle("errors");

    private AccountServiceImpl() {

    }

    private static class LazyHolder {
        private static final AccountServiceImpl INSTANCE = new AccountServiceImpl();
    }

    public static AccountServiceImpl getInstance() {
        return LazyHolder.INSTANCE;
    }

    @Override
    public void depositeFunds(Account account, float amount) throws BankApplication.exceptions.IllegalArgumentException {
        try {
            account.deposit(amount);
            Client client = DAOFactory.getClientDAO().findClientById(account.getClientId());
            DAOFactory.getAccountDAO().save(account, client);
        } catch (ClientNotFoundException e) {
            System.out.println(e.getMessage());
        }
    }

    @Override
    public void withdrawFunds(Account account, float amount) throws NotEnoughFundsException, IllegalArgumentException {
        try {
            account.withdraw(amount);
            Client client = DAOFactory.getClientDAO().findClientById(account.getClientId());
            DAOFactory.getAccountDAO().save(account, client);
        } catch (ClientNotFoundException e) {
            System.out.println(e.getMessage());
        }
    }

    @Override
    public Account getAccountById(Client client, Long id) throws AccountNotFoundException {
        List<Account> accountList = DAOFactory.getAccountDAO().getClientAccounts(client.getId());
        Account result = null;
        for (Account account : accountList) {
            if (account.getId() == id) {
                result = account;
            }
        }
        return result;
    }

    public void transferFunds(Account sender, Account recipient, float amount) {
        DAOFactory.getAccountDAO().transferFunds(sender, recipient, amount);
    }
}
