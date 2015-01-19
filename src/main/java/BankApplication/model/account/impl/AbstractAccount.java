package BankApplication.model.account.impl;

import BankApplication.model.account.IAccount;
import BankApplication.exceptions.IllegalArgumentException;
import BankApplication.exceptions.NotEnoughFundsException;

import java.util.Map;
import java.util.ResourceBundle;

/**
 * Created by Kir Kolesnikov on 14.01.2015.
 */
public abstract class AbstractAccount implements IAccount {
    protected static ResourceBundle errorsBundle = ResourceBundle.getBundle("errors");
    protected static ResourceBundle bundle = ResourceBundle.getBundle("strings");
    protected static ResourceBundle feedBundle = ResourceBundle.getBundle("feedfile");
    protected float balance;
    final protected Long id;

    public AbstractAccount() {
        this.id = System.currentTimeMillis();
    }

    public float getBalance() {
        return this.balance;
    }

    public void setBalance(float balance) {
        this.balance = balance;
    }

    public Long getId() {
        return id;
    }

    public abstract void parseFeed(Map<String, String> feedMap);

    public void deposit(float amount) throws IllegalArgumentException {
        if (amount <= 0) {
            throw new IllegalArgumentException();
        }
        this.balance += amount;
    }

    public void withdraw(float amount) throws NotEnoughFundsException {
        if (this.balance >= amount) {
            this.balance -= amount;
        } else {
            throw new NotEnoughFundsException(errorsBundle.getString("notEnoughFunds"));
        }
    }

    public void balanceDecimalValue() {
        System.out.println(Math.round(balance));
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append("ID: ");
        builder.append(id);
        builder.append(" ");
        if (this instanceof CheckingAccount) {
            builder.append(bundle.getString("checkingAccount"));
        } else {
            builder.append(bundle.getString("savingAccount"));
        }
        builder.append(" ");
        builder.append(this.getBalance());
        return builder.toString();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof AbstractAccount)) return false;

        AbstractAccount that = (AbstractAccount) o;

        if (Float.compare(that.balance, balance) != 0) return false;
        if (!id.equals(that.id)) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = (balance != +0.0f ? Float.floatToIntBits(balance) : 0);
        result = 31 * result + id.hashCode();
        return result;
    }
}
