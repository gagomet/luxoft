package BankApplication.model.impl;

import BankApplication.annotation.NoDB;
import BankApplication.model.Account;
import BankApplication.exceptions.NotEnoughFundsException;
import BankApplication.service.Persistable;

import java.io.Serializable;
import java.util.Map;
import java.util.ResourceBundle;

/**
 * Created by Kir Kolesnikov on 14.01.2015.
 */
public abstract class AbstractAccount implements Account, Serializable, Persistable, Comparable<AbstractAccount> {
    protected static ResourceBundle errorsBundle = ResourceBundle.getBundle("errors");
    protected float balance;
    @NoDB
    protected long id;
    protected long clientId;


    public AbstractAccount() {
    }

    @Override
    public float getBalance() {
        return this.balance;
    }

    @Override
    public void setBalance(float balance) {
        this.balance = balance;
    }

    @Override
    public long getId() {
        return id;
    }

    @Override
    public void setId(long id) {
        this.id = id;
    }

    @Override
    public long getClientId() {
        return clientId;
    }

    @Override
    public void setClientId(long clientId) {
        this.clientId = clientId;
    }

    public abstract void parseFeed(Map<String, String> feedMap);

    @Override
    public void deposit(float amount) {
        if (amount <= 0) {
            throw new IllegalArgumentException();
        }
        this.balance += amount;
    }

    @Override
    public abstract void withdraw(float amount) throws NotEnoughFundsException;

    public void balanceDecimalValue() {
        System.out.println(Math.round(balance));
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append("Active Account ID: ");
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
    public int compareTo(AbstractAccount o){
        if(balance > o.balance){
            return 1;
        } else if(balance < o.balance){
            return -1;
        }
        return 0;
    }

}
