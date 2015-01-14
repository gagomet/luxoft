package BankApplication.account.impl;

import BankApplication.account.IAccount;
import BankApplication.exceptions.OverdraftLimitReached;

import java.util.ResourceBundle;

/**
 * Created by Kir Kolesnikov on 14.01.2015.
 */
public abstract class AbstractAccount implements IAccount {
    private float balance;

    public AbstractAccount(){}

    public float getBalance() {
        return this.balance;
    }

    public void setBalance(float balance) {
        this.balance = balance;
    }

    public boolean deposit(float amount) {
        this.balance += amount;
        return true;
    }

    public boolean withdraw(float amount) throws OverdraftLimitReached {
        if (this.balance >= amount) {
            this.balance -= amount;
            return true;
        }
        return false;
    }
}
