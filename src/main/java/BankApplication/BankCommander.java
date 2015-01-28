package BankApplication;

import BankApplication.exceptions.ClientNotFoundException;
import BankApplication.exceptions.IllegalArgumentException;
import BankApplication.model.ClientRegistrationListener;
import BankApplication.model.impl.Bank;
import BankApplication.network.BankRemoteOffice;
import BankApplication.service.BankFeedService;
import BankApplication.service.BankService;
import BankApplication.service.DAO.BankDAO;
import BankApplication.service.DAO.impl.BankDAOImpl;
import BankApplication.service.impl.*;
import BankApplication.model.impl.Client;
import BankApplication.service.Command;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.*;

/**
 * Created by Kir Kolesnikov on 15.01.2015.
 */
public class BankCommander {
    public static Bank currentBank = null;
    public static Client currentClient = null;
    public static Map<String, Command> commandsMap = new TreeMap<>();
    private static final String FEED_FILES_FOLDER = "c:\\!toBankApplication\\";
    static String bankName = "MYBANK";

    //abstractCommand

    static
    Command[] commands = {
            new FindClientCommand(), // 1
            new GetAccountCommand(), // 2
            new WithdrawCommand(), //3
            new DepositCommand(), //4
            new TransferCommand(), //5
            new AddClientCommand(), //6
            new RemoveClientCommand(),
            new ShowHelpCommand(), //7
            new Command() {
                public void execute() {
                    System.exit(0);
                }

                public void printCommandInfo() {
                    System.out.println("Exit");
                }
            }
    };


    public BankCommander() {
        BankDAO bankDAO = new BankDAOImpl();
        currentBank = bankDAO.getBankByName(bankName);
        composeMapOfCommands();
    }

    public void initialize() {
        Bank.PrintClientListener printListener = new Bank.PrintClientListener();
        Bank.EmailClientListener emailListener = new Bank.EmailClientListener();
        List<ClientRegistrationListener> listenersList = new ArrayList<ClientRegistrationListener>();
        listenersList.add(printListener);
        listenersList.add(emailListener);
        currentBank = new Bank(listenersList);

        //initialize from feed files
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
    }


    public static Client getCurrentClient() {
        return currentClient;
    }

    public static void setCurrentClient(Client currentClient) {
        BankCommander.currentClient = currentClient;
//        BankRemoteOffice.setCurrentClient(currentClient);
    }

    public void registerCommand(String name, Command command) {
        commandsMap.put(name, command);
    }

    public void removeCommand(String name) {
        commandsMap.remove(name);
/*remove
public V remove(Object key)
Removes the mapping for this key from this TreeMap if present.
Throws:
ClassCastException - if the specified key cannot be compared with the keys currently in the map
NullPointerException - if the specified key is null and this map uses natural ordering, or its comparator does not permit null keys*/
    }

    public void composeMapOfCommands() {
        Integer i = 0;
        for (Command command : commands) {
            commandsMap.put(i.toString(), command);
            i++;
        }
    }

    private static void serializationTest() {
        try {
            BankService service = new BankServiceImpl();
            Client testClient = service.getClientByName(currentBank, "Beggar");
            testClient.printReport();
            service.saveClientToFeedFile(testClient);
            System.out.println("Saving done!");
            System.out.println("Now reading from file");
            Client readedClient = service.loadClientFromFeedFile();
            System.out.println("Reading done");
            readedClient.printReport();
        } catch (ClientNotFoundException e) {
            e.printStackTrace();
        }
    }

    public static void main(String args[]) {
        BankCommander bankCommander = new BankCommander();
//        bankCommander.initialize();  uncomment this string to return to noDB impl

        if (args[0].equals("report")) {
            BankReport bankReport = new BankReport();
            System.out.println("***");
            System.out.println(bankReport.getAccountsNumber(currentBank));
            System.out.println("***");
            System.out.println(bankReport.getBankCreditSum(currentBank));
            System.out.println("***");
            System.out.println(bankReport.getNumberOfClients(currentBank));
            System.out.println("***");
            System.out.println(bankReport.getClientsByCity(currentBank));
        }


        //serialization/deserialization
//        serializationTest();


        while (true) {
            Iterator iterator = commandsMap.entrySet().iterator();
            while (iterator.hasNext()) {
                Map.Entry<String, Command> entry = (Map.Entry<String, Command>) iterator.next();
                StringBuilder builder = new StringBuilder();
                builder.append(entry.getKey());
                builder.append("   -->    ");
                System.out.print(builder.toString());
                entry.getValue().printCommandInfo();
            }
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(System.in));
            try {
                System.out.println("Enter number of your choice: ");

                String commandString = bufferedReader.readLine();
                commandsMap.get(commandString).execute();
            } catch (IOException | IllegalArgumentException e) {
                e.printStackTrace();
                System.out.println("Not a command");
            }
        }

    }

}
