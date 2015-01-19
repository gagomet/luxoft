package BankApplication.exceptions;

import BankApplication.account.impl.AbstractAccount;

/**
 * Created by Kir Kolesnikov on 14.01.2015.
 */
public class NotEnoughFundsException extends BankException {
    private AbstractAccount account;
    private float amount;

    public NotEnoughFundsException() {
    }

    public NotEnoughFundsException(String message) {
        super(message);
    }

    public NotEnoughFundsException(String message, AbstractAccount account, float amount) {
        super(message);
        this.amount = amount;
        this.account = account;
    }

    public float getAmount() {
        return amount;
    }

    public float getPossiblyFunds() {
        return account.getBalance();
    }

}
