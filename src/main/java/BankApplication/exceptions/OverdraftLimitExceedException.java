package BankApplication.exceptions;


import BankApplication.model.Account;
import BankApplication.model.impl.CheckingAccount;

/**
 * Created by Kir Kolesnikov on 14.01.2015.
 */
public class OverdraftLimitExceedException extends NotEnoughFundsException {
    private CheckingAccount checkingAccount;

    public OverdraftLimitExceedException() {
    }

    public OverdraftLimitExceedException(String message) {
        super(message);
    }

    public OverdraftLimitExceedException(String message, Account account, float amount) {
        super(message, account, amount);
        this.checkingAccount = (CheckingAccount) account;
    }

    @Override
    public String getMessage() {
        return super.getMessage();
    }

}
