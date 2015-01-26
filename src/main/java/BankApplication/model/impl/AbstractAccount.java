package BankApplication.model.impl;

import BankApplication.model.Account;
import BankApplication.exceptions.IllegalArgumentException;
import BankApplication.exceptions.NotEnoughFundsException;

import java.io.Serializable;
import java.util.Map;
import java.util.ResourceBundle;

/**
 * Created by Kir Kolesnikov on 14.01.2015.
 */
public abstract class AbstractAccount implements Account, Serializable {
    protected static ResourceBundle errorsBundle = ResourceBundle.getBundle("errors");
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

    public long getId() {
        return id;
    }

    public abstract void parseFeed(Map<String, String> feedMap);

    public void deposit(float amount) throws IllegalArgumentException {
        if (amount <= 0) {
            throw new IllegalArgumentException();
        }
        this.balance += amount;
    }

    public abstract void withdraw(float amount) throws NotEnoughFundsException, IllegalArgumentException;

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
            builder.append("Checking account main funds:");
        } else {
            builder.append("Saving account funds:");
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
