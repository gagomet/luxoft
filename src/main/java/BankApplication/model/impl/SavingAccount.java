package BankApplication.model.impl;

import BankApplication.exceptions.NotEnoughFundsException;

import java.util.Map;

/**
 * Created by Kir Kolesnikov on 14.01.2015.
 */
public class SavingAccount extends AbstractAccount {

    public SavingAccount() {
        super();
    }

    public void parseFeed(Map<String, String> feedMap) {
        Float balance = Float.parseFloat(feedMap.get("balance"));
        setBalance(balance);
    }

    @Override
    public synchronized void withdraw(float amount) throws NotEnoughFundsException {
        {
            if (amount < 0) {
                throw new IllegalArgumentException(errorsBundle.getString("notNegative"));
            }
            if (balance >= amount) {
                balance -= amount;
            } else {
                throw new NotEnoughFundsException(errorsBundle.getString("notEnoughFunds"));
            }
        }
    }

    @Override
    public void printReport() {
        System.out.println(super.toString());
    }

    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof SavingAccount)) return false;

        SavingAccount that = (SavingAccount) o;
        if (Float.compare(that.balance, balance) != 0) return false;
        if (clientId != that.clientId) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = (super.balance != +0.0f ? Float.floatToIntBits(balance) : 0);
        result = 31 * result + (int) (super.clientId ^ (super.clientId >>> 32));
        return result;
    }

}
