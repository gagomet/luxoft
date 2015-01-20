package BankApplication.model.account;

import BankApplication.exceptions.*;
import BankApplication.exceptions.IllegalArgumentException;
import BankApplication.model.IReport;

/**
 * Created by Kir Kolesnikov on 14.01.2015.
 */
public interface IAccount extends IReport {
    float getBalance();

    void deposit(float amount) throws IllegalArgumentException;

    void withdraw(float amount) throws NotEnoughFundsException;
}
