package BankApplication.model.impl;

import BankApplication.model.Account;

import java.util.*;

/**
 * Created by Kir Kolesnikov on 19.01.2015.
 */
public class BankReport {
    public int getNumberOfClients(Bank bank) {
        return bank.getClientsList().size();
    }

    public int getAccountsNumber(Bank bank) {
        int result = 0;
        for (Client client : bank.getClientsList()) {
            for (Account account : client.getAccountsList()) {
                result++;
            }
        }
        return result;
    }

    public float getBankCreditSum(Bank bank) {
        float resultSum = 0;
        for (Client client : bank.getClientsList()) {
            for (Account account : client.getAccountsList()) {
                if (account.getBalance() < 0) {
                    resultSum += -account.getBalance();
                }
            }
        }
        return resultSum;
    }

    public Map<String, List<Client>> getClientsByCity(Bank bank) {
        Map<String, List<Client>> result = new TreeMap<>();
        for (Client client : bank.getClientsList()) {
            if (result.keySet().contains(client.getCity())) {
                result.get(client.getCity()).add(client);
            } else {
                List<Client> tempClients = new ArrayList<>();
                tempClients.add(client);
                result.put(client.getCity(), tempClients);
            }
        }
        return result;
    }
}
