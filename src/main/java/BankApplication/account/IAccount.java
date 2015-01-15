package BankApplication.account;

import BankApplication.model.client.IReport;
import BankApplication.exceptions.OverdraftLimitReached;

/**
 * Created by Kir Kolesnikov on 14.01.2015.
 */
public interface  IAccount extends IReport {
    float getBalance();

    boolean deposit(float amount);

    boolean withdraw(float amount) throws OverdraftLimitReached;
}
