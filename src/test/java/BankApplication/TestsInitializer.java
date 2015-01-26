package BankApplication;

import BankApplication.exceptions.*;
import BankApplication.exceptions.IllegalArgumentException;
import BankApplication.model.Account;
import BankApplication.model.ClientRegistrationListener;
import BankApplication.model.impl.Bank;
import BankApplication.model.impl.CheckingAccount;
import BankApplication.model.impl.Client;
import BankApplication.model.impl.SavingAccount;
import BankApplication.service.BankFeedService;
import BankApplication.service.impl.*;
import BankApplication.type.Gender;

import java.util.*;

/**
 * Created by Kir Kolesnikov on 26.01.2015.
 */
public class TestsInitializer {
    private static final String FEED_FILES_FOLDER = "c:\\!toBankApplication\\";

    public static Bank getDummyBankInstance() {
        Bank currentBank;
        Bank.PrintClientListener printListener = new Bank.PrintClientListener();
        Bank.EmailClientListener emailListener = new Bank.EmailClientListener();
        List<ClientRegistrationListener> listenersList = new ArrayList<ClientRegistrationListener>();
        listenersList.add(printListener);
        listenersList.add(emailListener);
        currentBank = new Bank(listenersList);

        BankFeedService feedService = new BankFeedServiceImpl();
        List<String[]> feeds = feedService.loadFeeds(FEED_FILES_FOLDER);
        for (String[] fileFeed : feeds) {
            for (String tempFeedLine : fileFeed) {
                Map<String, String> feedMap = feedService.parseFeed(tempFeedLine);
                try {
                    Client tempClient = currentBank.parseFeed(feedMap);
                    currentBank.addClient(tempClient);
                } catch (IllegalArgumentException e) {
                    e.printStackTrace();
                }
            }
        }
        return currentBank;
    }
}
