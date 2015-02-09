package BankApplication.main;

import BankApplication.model.ClientRegistrationListener;
import BankApplication.model.impl.Bank;
import BankApplication.model.impl.Client;
import BankApplication.service.BankFeedService;
import BankApplication.service.impl.BankFeedServiceImpl;
import BankApplication.service.impl.BankReport;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class BankApplication {
    static Bank bank = null;
    static Client client1 = null;
    static Client client2 = null;
    static Client client3 = null;
    //    static AllBankServices allBankServices = new AllBankServicesImpl();
    private static final String FEED_FILES_FOLDER = "c:\\!toBankApplication\\";

    public static void main(String[] args) {
        BankReport bankReport = new BankReport();

//        initialize();
//        printBankReport();
//        System.out.println("modifying...");
//        modifyBank();
//        printBankReport();

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

    }

    public static void printBankReport() {
        bank.printReport();
    }


    public static Bank getBank() {
        return bank;
    }

}
