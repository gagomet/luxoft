package BankApplication.account.impl;

import BankApplication.exceptions.*;
import BankApplication.exceptions.IllegalArgumentException;


/**
 * Created by Kir Kolesnikov on 14.01.2015.
 */
public class CheckingAccount extends AbstractAccount {
    private float overdraft;

    public CheckingAccount(float overdraft) throws IllegalArgumentException {
        super();
        if (overdraft > 0) {
            this.overdraft = overdraft;
        } else {
            throw new IllegalArgumentException(errorsBundle.getString("negativeOverdraft"));
        }
    }

    public void setOverdraft(float overdraft) {
        this.overdraft = overdraft;
    }

    public float getOverdraft() {
        return overdraft;
    }

    @Override
    public void withdraw(float amount) throws NotEnoughFundsException {
        float balance = this.getBalance();
        if (balance >= amount) {
            super.withdraw(amount);
        } else {
            float difference = balance - amount;
            if (Math.abs(difference) < overdraft) {
                balance -= amount;
                overdraft = overdraft + difference;
                setBalance(balance);
                setOverdraft(overdraft);
            } else {
                throw new OverdraftLimitExceedException(errorsBundle.getString("notEnoughFunds"), this, amount);
            }
        }
    }

    @Override
    public void printReport() {
        System.out.println(toString());
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append("ID: ");
        builder.append(id);
        builder.append(" ");
        builder.append(bundle.getString("checkingAccount"));
        builder.append(" ");
        builder.append(this.getBalance());
        builder.append(" ");
        builder.append(bundle.getString("overdraft"));
        builder.append(" ");
        builder.append(this.getOverdraft());
        return builder.toString();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof CheckingAccount)) return false;
        if (!super.equals(o)) return false;

        CheckingAccount that = (CheckingAccount) o;

        if (Float.compare(that.overdraft, overdraft) != 0) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = super.hashCode();
        result = 31 * result + (overdraft != +0.0f ? Float.floatToIntBits(overdraft) : 0);
        return result;
    }
}
