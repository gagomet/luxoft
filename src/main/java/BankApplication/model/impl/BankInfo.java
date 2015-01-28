package BankApplication.model.impl;

import BankApplication.service.impl.BankReport;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

/**
 * Created by Padonag on 20.01.2015.
 */
public class BankInfo implements Serializable {
    int totalClients;
    int totalAccounts;
    float totalCredit;
    Map<String, List<Client>> clientsByCities;
    public BankInfo(){

    }

    public int getTotalClients() {
        return totalClients;
    }

    public void setTotalClients(int totalClients) {
        this.totalClients = totalClients;
    }

    public int getTotalAccounts() {
        return totalAccounts;
    }

    public void setTotalAccounts(int totalAccounts) {
        this.totalAccounts = totalAccounts;
    }

    public float getTotalCredit() {
        return totalCredit;
    }

    public void setTotalCredit(float totalCredit) {
        this.totalCredit = totalCredit;
    }

    public Map<String, List<Client>> getClientsByCities() {
        return clientsByCities;
    }

    public void setClientsByCities(Map<String, List<Client>> clientsByCities) {
        this.clientsByCities = clientsByCities;
    }

    public BankInfo(Bank bank){
        BankReport bankReport = new BankReport();
        totalClients = bankReport.getNumberOfClients(bank);
        totalAccounts = bankReport.getAccountsNumber(bank);
        totalCredit = bankReport.getBankCreditSum(bank);
        clientsByCities = bankReport.getClientsByCity(bank);
    }

    @Override
    public String toString() {
        return "BankInfo{" +
                "totalClients=" + totalClients +
                ", totalAccounts=" + totalAccounts +
                ", totalCredit=" + totalCredit +
                ", clientsByCities=" + clientsByCities.toString() +
                '}';
    }
}
