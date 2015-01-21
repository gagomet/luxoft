package BankApplication;

import BankApplication.exceptions.IllegalArgumentException;
import BankApplication.exceptions.NotEnoughFundsException;
import BankApplication.exceptions.OverdraftLimitExceedException;
import BankApplication.model.ClientRegistrationListener;
import BankApplication.model.impl.Bank;
import BankApplication.service.impl.BankReport;
import BankApplication.model.impl.Client;
import BankApplication.service.BankFeedService;
import BankApplication.service.impl.BankFeedServiceImpl;
import BankApplication.service.BankService;
import BankApplication.service.impl.BankServiceImpl;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class BankApplication {
    static Bank bank = null;
    static Client client1 = null;
    static Client client2 = null;
    static Client client3 = null;
    static BankService bankService = new BankServiceImpl();
    private static final String FEED_FILES_FOLDER = "c:\\!toBankApplication\\";

    public static void main(String[] args) {
        BankReport bankReport = new BankReport();

        initialize();
        printBankReport();
//        System.out.println("modifying...");
//        modifyBank();
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
        List<ClientRegistrationListener> listenersList = new ArrayList<ClientRegistrationListener>();
        listenersList.add(printListener);
        listenersList.add(emailListener);
        bank = new Bank(listenersList);

        //initialize from feed files
        BankFeedService feedService = new BankFeedServiceImpl();
        List<String[]> feeds = feedService.loadFeeds(FEED_FILES_FOLDER);
        for (String[] fileFeed : feeds) {
            for (String tempFeedLine : fileFeed) {
                Map<String, String> feedMap = feedService.parseFeed(tempFeedLine);
                try {
                    Client tempClient = bank.parseFeed(feedMap);
                    bank.addClient(tempClient);
                } catch (IllegalArgumentException e) {
                    e.printStackTrace();
                }
            }
        }

        /*try {
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
        }*/


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

}
