package BankApplication.model.info;

import BankApplication.model.Bank;

import java.io.Serializable;

/**
 * Created by Padonag on 20.01.2015.
 */
public class BankInfo implements Serializable {
    int totalClients;
    int totalAccounts;
    float totalCredit;
    //TODO clients by cities
    public BankInfo(){

    }

    public BankInfo(Bank bank){
        BankReport bankReport = new BankReport();
        totalClients = bankReport.getNumberOfClients(bank);
        totalAccounts = bankReport.getAccountsNumber(bank);
        totalCredit = bankReport.getBankCreditSum(bank);
    }

    @Override
    public String toString() {
        return "BankInfo{" +
                "totalClients=" + totalClients +
                ", totalAccounts=" + totalAccounts +
                ", totalCredit=" + totalCredit +
                '}';
    }
}
