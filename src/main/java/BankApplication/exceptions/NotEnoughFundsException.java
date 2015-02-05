package BankApplication.exceptions;

import BankApplication.model.Account;

/**
 * Created by Kir Kolesnikov on 14.01.2015.
 */
public class NotEnoughFundsException extends BankException {
    private Account account;
    private float amount;

    public NotEnoughFundsException() {
    }

    public NotEnoughFundsException(String message) {
        super(message);
    }

    public NotEnoughFundsException(String message, Account account, float amount) {
        super(message);
        this.amount = amount;
        this.account = account;
    }

    public String getMessage() {
        StringBuilder builder = new StringBuilder();
        builder.append("Not enough funds! Your amount: ");
        builder.append(amount);
        builder.append("  Your funds is: ");
        builder.append(account.getBalance());
        return builder.toString();
    }

}
