package BankApplication.model.impl;

import BankApplication.exceptions.*;
import BankApplication.exceptions.IllegalArgumentException;

import java.util.Map;


/**
 * Created by Kir Kolesnikov on 14.01.2015.
 */
public class CheckingAccount extends AbstractAccount {
    private float overdraft;

    public CheckingAccount() {

    }

    public CheckingAccount(float overdraft) {
        this.overdraft = overdraft;
    }

    public void setOverdraft(float overdraft) {
        this.overdraft = overdraft;
    }

    public float getOverdraft() {
        return overdraft;
    }

    @Override
    public void withdraw(float amount) throws NotEnoughFundsException, IllegalArgumentException {
        if (amount < 0) {
            throw new BankApplication.exceptions.IllegalArgumentException(errorsBundle.getString("notNegative"));
        }
        if (balance + overdraft >= amount) {
            balance -= amount;
        } else {
            throw new OverdraftLimitExceedException(errorsBundle.getString("notEnoughFunds"), this, amount);
        }

    }

    public void parseFeed(Map<String, String> feedMap) {
        Float balance = Float.parseFloat(feedMap.get("balance"));
        Float overdraft = Float.parseFloat(feedMap.get("overdraft"));
        setBalance(balance);
        setOverdraft(overdraft);
    }

    @Override
    public void printReport() {
        System.out.println(toString());
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append("Account ID: ");
        builder.append(id);
        builder.append(" Checking account main funds: ");
        builder.append(getBalance());
        builder.append(" Overdraft of client: ");
        builder.append(this.getOverdraft());
        return builder.toString();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof CheckingAccount)) return false;

        CheckingAccount that = (CheckingAccount) o;
        if (Float.compare(that.overdraft, this.overdraft) != 0) return false;
        if (Float.compare(that.balance, this.balance) != 0) return false;
        return true;
    }

    @Override
    public int hashCode() {
        int result = (super.balance != +0.0f ? Float.floatToIntBits(balance) : 0);
        result = 31 * result + (int) (super.clientId ^ (super.clientId >>> 32));
        result = (overdraft != +0.0f ? Float.floatToIntBits(overdraft) : 0);
        return result;
    }
}
