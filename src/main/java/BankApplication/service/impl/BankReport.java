package BankApplication.service.impl;

import BankApplication.model.Account;
import BankApplication.model.impl.Bank;
import BankApplication.model.impl.Client;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import java.util.TreeSet;

/**
 * Created by Kir Kolesnikov on 19.01.2015.
 */
public class BankReport {
    public int getNumberOfClients(Bank bank) {
        return bank.getClientSet().size();
    }

    public int getAccountsNumber(Bank bank) {
        int result = 0;
        for (Client client : bank.getClientSet()) {
            for (Account account : client.getAccountsList()) {
                result++;
            }
        }
        return result;
    }

    public float getBankCreditSum(Bank bank) {
        float resultSum = 0;
        for (Client client : bank.getClientSet()) {
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
        result.put("Unknown", new ArrayList<Client>());
        for (Client client : bank.getClientSet()) {
            if (client.getCity() != null) {
                if (result.keySet().contains(client.getCity())) {
                    result.get(client.getCity()).add(client);
                } else {
                    List<Client> tempClients = new ArrayList<>();
                    tempClients.add(client);
                    result.put(client.getCity(), tempClients);
                }
            } else {
                List<Client> unknowns = result.get("Unknown");
                unknowns.add(client);
                result.put("Unknown", unknowns);
            }
        }
        return result;
    }

    public Set getClientsSorted(Bank bank) {
        Set<Client> result = new TreeSet<Client>(new Comparator<Client>() {
            @Override
            public int compare(Client o1, Client o2) {
                if (o1.getActiveAccount().getBalance() > o2.getActiveAccount().getBalance()) {
                    return 1;
                } else if (o1.getActiveAccount().getBalance() > o2.getActiveAccount().getBalance()) {
                    return -1;
                }
                return 0;
            }
        });
        return result;
    }

    public static Map<String, List<Client>> getClientsFromListToMapByCity(List<Client> clientList) {
        Map<String, List<Client>> result = new TreeMap<>();
        result.put("Unknown", new ArrayList<Client>());
        for (Client client : clientList) {
            if (client.getCity() != null) {
                if (result.keySet().contains(client.getCity())) {
                    result.get(client.getCity()).add(client);
                } else {
                    List<Client> tempClients = new ArrayList<>();
                    tempClients.add(client);
                    result.put(client.getCity(), tempClients);
                }
            } else {
                List<Client> unknowns = result.get("Unknown");
                unknowns.add(client);
                result.put("Unknown", unknowns);
            }
        }
        return result;
    }
}
