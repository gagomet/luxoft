package BankApplication;

import BankApplication.exceptions.*;
import BankApplication.exceptions.IllegalArgumentException;
import BankApplication.listeners.IClientRegistrationListener;
import BankApplication.model.Bank;
import BankApplication.model.BankReport;
import BankApplication.service.bankservice.impl.BankServiceImpl;
import BankApplication.service.bankservice.IBankService;
import BankApplication.model.client.Client;
import BankApplication.type.Gender;

import java.util.ArrayList;
import java.util.List;

public class BankApplication {
    static Bank bank = null;
    static Client client1 = null;
    static Client client2 = null;
    static Client client3 = null;
    static IBankService bankService = new BankServiceImpl();

    public static void main(String[] args) {
        BankReport bankReport = new BankReport();

        initialize();
        printBankReport();
        System.out.println("modifying...");
        modifyBank();
        printBankReport();

        System.out.println("***");
        System.out.println(bankReport.getAccountsNumber(bank));
        System.out.println("***");
        System.out.println(bankReport.getBankCreditSum(bank));
        System.out.println("***");
        System.out.println(bankReport.getNumberOfClients(bank));
        System.out.println("***");
        System.out.println(bankReport.getClientsByCity(bank));
    }

    public static void initialize() {
        Bank.PrintClientListener printListener = new Bank.PrintClientListener();
        Bank.EmailClientListener emailListener = new Bank.EmailClientListener();
        List<IClientRegistrationListener> listenersList = new ArrayList<IClientRegistrationListener>();
        listenersList.add(printListener);
        listenersList.add(emailListener);
        bank = new Bank(listenersList);
        try {
            client1 = new Client(Gender.FEMALE);
            client1.setName("princess");
            bankService.addClient(bank, client1);
            bankService.depositeFunds(client1.getActiveAccount(), 1000);
            client2 = new Client(5000, Gender.MALE);
            client2.setName("beggar");
            client3 = new Client(3000, Gender.MALE);
            client3.setName("neighbour");
            bankService.addClient(bank, client2);
            bankService.addClient(bank, client3);
            client1.setCity("Moscow");
            client2.setCity("Paris");
            client3.setCity("Paris");
        } catch (IllegalArgumentException e) {
            System.out.println(e.getMessage());
            e.printStackTrace();
        } catch (ClientExceedsException e) {
            System.out.println(e.getMessage());
            e.printStackTrace();
        }


    }

    public static void printBankReport() {
//        get info about clients and their accounts
        bank.printReport();
    }

    public static void modifyBank() {
//        some modifications into data in model
        try {
            bankService.depositeFunds(client1.getActiveAccount(), 1000);
            bankService.withdrawFunds(client2.getActiveAccount(), 4999);
            bankService.depositeFunds(client2.getActiveAccount(), 1000);
            bankService.depositeFunds(client3.getActiveAccount(), 4000);
        } catch (IllegalArgumentException e) {
            System.out.println(e.getMessage());
            e.printStackTrace();
        } catch (OverdraftLimitExceedException e) {
            System.out.println(e.getMessage());
            e.printStackTrace();
        } catch (NotEnoughFundsException e) {
            System.out.println(e.getMessage());
            e.printStackTrace();
        }

//        bankService.removeClient(bank, client2);
    }

    public static Bank getBank() {
        return bank;
    }

    public static Client getClient1() {
        return client1;
    }

    public static Client getClient2() {
        return client2;
    }

    public static Client getClient3() {
        return client3;
    }

    public static IBankService getBankService() {
        return bankService;
    }
}
