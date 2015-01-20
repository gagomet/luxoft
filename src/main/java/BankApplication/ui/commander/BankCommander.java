package BankApplication.ui.commander;

import BankApplication.BankApplication;
import BankApplication.exceptions.ClientNotFoundException;
import BankApplication.exceptions.IllegalArgumentException;
import BankApplication.model.Bank;
import BankApplication.model.BankReport;
import BankApplication.model.client.Client;
import BankApplication.service.bankservice.BankServiceEnumSingletone;
import BankApplication.ui.commands.ICommand;
import BankApplication.ui.commands.impl.AddClientCommand;
import BankApplication.ui.commands.impl.DepositCommand;
import BankApplication.ui.commands.impl.FindClientCommand;
import BankApplication.ui.commands.impl.GetAccountCommand;
import BankApplication.ui.commands.impl.ShowHelpCommand;
import BankApplication.ui.commands.impl.TransferCommand;
import BankApplication.ui.commands.impl.WithdrawCommand;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Iterator;
import java.util.Map;
import java.util.TreeMap;

/**
 * Created by Kir Kolesnikov on 15.01.2015.
 */
public class BankCommander {
    public static Bank currentBank = new Bank();
    public static Client currentClient = null;
    public static BankApplication bankApplication = new BankApplication();
    public static Map<String, ICommand> commandsMap = new TreeMap<>();


    static
    ICommand[] commands = {
            new FindClientCommand(), // 1
            new GetAccountCommand(), // 2
            new WithdrawCommand(), //3
            new DepositCommand(), //4
            new TransferCommand(), //5
            new AddClientCommand(), //6
            new ShowHelpCommand(), //7
            new ICommand() {
                public void execute() {
                    System.exit(0);
                }

                public void printCommandInfo() {
                    System.out.println("Exit");
                }
            }
    };

    public static Client getCurrentClient() {
        return currentClient;
    }

    public static void setCurrentClient(Client currentClient) {
        BankCommander.currentClient = currentClient;
    }

    public static void main(String args[]) {
        //initialization and retrieving of Bank class instance
        bankApplication.initialize();
        currentBank = bankApplication.getBank();

        if (args[0].equals("report")) {
            BankReport bankReport = new BankReport();
            System.out.println("***");
            System.out.println(bankReport.getAccountsNumber(currentBank));
            System.out.println("***");
            System.out.println(bankReport.getBankCreditSum(currentBank));
            System.out.println("***");
            System.out.println(bankReport.getNumberOfClients(currentBank));
            System.out.println("***");
//            System.out.println(bankReport.getClientsByCity(currentBank));
        }


        //serialization/deserialization
            serializationTest();


            composeMapOfCommands();

            while (true) {
                Iterator iterator = commandsMap.entrySet().iterator();
                while (iterator.hasNext()) {
                    Map.Entry<String, ICommand> entry = (Map.Entry<String, ICommand>) iterator.next();
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
                } catch (IOException e) {
                    e.printStackTrace();
                } catch (IllegalArgumentException e) {
                    e.printStackTrace();
                }
            }

    }

    public void registerCommand(String name, ICommand command) {
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

    private static void composeMapOfCommands() {
        Integer i = 0;
        for (ICommand command : commands) {
            commandsMap.put(i.toString(), command);
            i++;
        }
    }

    private static void serializationTest(){
        try {
            Client testClient = BankServiceEnumSingletone.getClientByName(currentBank, "Beggar");
            testClient.printReport();
            BankServiceEnumSingletone.saveClient(testClient);
            System.out.println("Saving done!");
            System.out.println("Now reading from file");
            Client readedClient = BankServiceEnumSingletone.loadClient();
            System.out.println("Reading done");
            readedClient.printReport();
        } catch (ClientNotFoundException e) {
            e.printStackTrace();
        }
    }

}
