package BankApplication.account.impl;

import BankApplication.exceptions.OverdraftLimitReached;

import java.util.ResourceBundle;

/**
 * Created by Kir Kolesnikov on 14.01.2015.
 */
public class CheckingAccount extends AbstractAccount {
    private float overdraft;
    private static ResourceBundle bundle = ResourceBundle.getBundle("strings");

    public CheckingAccount(float overdraft) {
        super();
        if (overdraft > 0) {
            this.overdraft = overdraft;
        } else {
            throw  new NumberFormatException("Overdraft value can't be negative");
        }
    }

    public void setOverdraft(float overdraft) {
        this.overdraft = overdraft;
    }

    public float getOverdraft() {
        return overdraft;
    }

/*    boolean deposit(float amount){
        if(getBalance() < 0){

        }
    }*/

    @Override
    public boolean withdraw(float amount) throws OverdraftLimitReached {
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
                return true;
            } else {
                throw new OverdraftLimitReached();
            }
        }
        return false;
    }

    @Override
    public void printReport() {
        System.out.println(bundle.getString("checkingAccount") + " " + this.getBalance());
        System.out.println(bundle.getString("overdraft") + " " + this.getOverdraft());
    }
}
