package BankApplication;

import BankApplication.bank.Bank;
import BankApplication.bank.listeners.IClientRegistrationListener;
import BankApplication.bank.service.impl.BankServiceImpl;
import BankApplication.bank.service.IBankService;
import BankApplication.client.Client;
import BankApplication.exceptions.OverdraftLimitReached;
import BankApplication.type.Gender;

import java.util.ArrayList;
import java.util.List;

public class BankApplication {
    static Bank bank = null;
    static Client client1=null;
    static Client client2=null;
    static IBankService bankService = new BankServiceImpl();

    public static void main(String[] args) {
        initialize();
        printBankReport();
        System.out.println("modifying...");
        modifyBank();
        printBankReport();
    }

    public static void initialize() {
        Bank.PrintClientListener printListener = new Bank.PrintClientListener();
        Bank.EmailClientListener emailListener = new Bank.EmailClientListener();
        List<IClientRegistrationListener> listenersList = new ArrayList<IClientRegistrationListener>();
        listenersList.add(printListener);
        listenersList.add(emailListener);
        bank = new Bank(listenersList);
        client1 = new Client(Gender.FEMALE);
        client1.setName("princess");
        bankService.addClient(bank, client1);
        client2 = new Client(5000, Gender.MALE);
        client2.setName("beggar");
        bankService.addClient(bank, client2);


    }

    public static void printBankReport() {
//        get info about clients and their accounts
        bank.printReport();
    }

    public static void modifyBank() {
//        some modifications into data in model
        client1.getActiveAccount().deposit(1000);
        try {
            client2.getActiveAccount().withdraw(4999);
        } catch (OverdraftLimitReached overdraftLimitReached) {
            overdraftLimitReached.printStackTrace();
        }
        bankService.removeClient(bank, client2);
    }
}
