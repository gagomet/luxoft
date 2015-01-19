package BankApplication.exceptions;


import BankApplication.model.account.impl.AbstractAccount;
import BankApplication.model.account.impl.CheckingAccount;

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

    public OverdraftLimitExceedException(String message, AbstractAccount account, float amount) {
        super(message, account, amount);
        this.checkingAccount = (CheckingAccount) account;
        String oldMessage = getMessage();
        StringBuilder builder = new StringBuilder();
        builder.append(oldMessage);
        builder.append(" to withdraw ");
        builder.append(getAmount());
        builder.append(" can withdraw ");
        builder.append(getPossiblyFunds());
        setMessage(builder.toString());
    }

    public float getPossiblyFunds() {
        return checkingAccount.getBalance() + checkingAccount.getOverdraft();
    }
}
