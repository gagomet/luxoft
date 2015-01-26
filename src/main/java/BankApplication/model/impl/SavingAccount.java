package BankApplication.model.impl;

import BankApplication.exceptions.*;
import BankApplication.exceptions.IllegalArgumentException;

import java.util.Map;

/**
 * Created by Kir Kolesnikov on 14.01.2015.
 */
public class SavingAccount extends AbstractAccount {

    public SavingAccount() {
        super();
    }

    public void parseFeed(Map<String, String> feedMap){
        Float balance = Float.parseFloat(feedMap.get("balance"));
        setBalance(balance);
    }

    @Override
    public void withdraw(float amount) throws NotEnoughFundsException, IllegalArgumentException {
        {
            if(amount<0){
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



}
