package BankApplication.model;

import BankApplication.exceptions.*;
import BankApplication.exceptions.IllegalArgumentException;
import BankApplication.model.Report;

/**
 * Created by Kir Kolesnikov on 14.01.2015.
 */
public interface Account extends Report {
    long getId();
    float getBalance();
    void setBalance(float balance);

    void deposit(float amount) throws IllegalArgumentException;

    void withdraw(float amount) throws NotEnoughFundsException, IllegalArgumentException;
}
