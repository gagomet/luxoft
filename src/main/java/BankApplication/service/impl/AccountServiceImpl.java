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
    private static AccountServiceImpl instance;
    protected static ResourceBundle errorsBundle = ResourceBundle.getBundle("errors");

    private AccountServiceImpl() {

    }

    public static AccountServiceImpl getInstance() {
        if (instance == null) {
            return new AccountServiceImpl();
        }
        return instance;
    }

    @Override
    public void depositeFunds(Account account, float amount) throws BankApplication.exceptions.IllegalArgumentException {
        try {
            account.deposit(amount);
//            depositToAccount(account, amount);
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
//            withdrawFromAccount(account, amount);
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

    private void withdrawFromAccount(Account account, float amount) throws IllegalArgumentException, NotEnoughFundsException {
        float tempBalance = account.getBalance();
        if (account instanceof CheckingAccount) {
            if (amount < 0) {
                throw new BankApplication.exceptions.IllegalArgumentException(errorsBundle.getString("notNegative"));
            }
            if (tempBalance + ((CheckingAccount) account).getOverdraft() >= amount) {
                account.setBalance(tempBalance - amount);
            } else {
                throw new OverdraftLimitExceedException(errorsBundle.getString("notEnoughFunds"), account, amount);
            }
        } else {
            if (amount < 0) {
                throw new IllegalArgumentException(errorsBundle.getString("notNegative"));
            }
            if (account.getBalance() >= amount) {
                account.setBalance(tempBalance - amount);
            } else {
                throw new NotEnoughFundsException(errorsBundle.getString("notEnoughFunds"));
            }
        }
    }

    private void depositToAccount(Account account, float amount) throws IllegalArgumentException {
        if (amount <= 0) {
            throw new IllegalArgumentException();
        }
        float currentBalance = account.getBalance();
        account.setBalance(currentBalance + amount);
    }
}
