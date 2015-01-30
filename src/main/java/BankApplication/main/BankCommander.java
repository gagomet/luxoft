package BankApplication.main;

import BankApplication.commander.impl.*;
import BankApplication.exceptions.IllegalArgumentException;
import BankApplication.model.impl.Bank;
import BankApplication.DAO.BankDAO;
import BankApplication.DAO.impl.BankDAOImpl;
import BankApplication.model.impl.Client;
import BankApplication.commander.Command;
import BankApplication.service.impl.ClientServiceImpl;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.*;

/**
 * Created by Kir Kolesnikov on 15.01.2015.
 */
public class BankCommander {
//    public static Client currentClient = null;
    public static Map<String, Command> commandsMap = new TreeMap<>();
    private static final String FEED_FILES_FOLDER = "c:\\!toBankApplication\\";
    static String bankName = "MYBANK";


    static
    Command[] commands = {
            new FindClientCommand(), // 1
            new GetAccountCommand(), // 2
            new WithdrawCommand(), //3
            new DepositCommand(), //4
            new TransferCommand(), //5
            new AddClientCommand(), //6
            new RemoveClientCommand(),
            new ReportCommand(),
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
        composeMapOfCommands();
    }


/*    public static Client getCurrentClient() {
        return currentClient;
    }

    public static void setCurrentClient(Client currentClient) {
        BankCommander.currentClient = currentClient;
//        BankRemoteOffice.setCurrentClient(currentClient);  //uncomment this to remote working
    }*/

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


    public static void main(String args[]) {
        BankCommander bankCommander = new BankCommander();

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
                System.out.println("Active client now is: ");
                if (ClientServiceImpl.getInstance().getCurrentClient() == null) {
                    System.out.println("N/A");
                } else {
                    System.out.println(ClientServiceImpl.getInstance().getCurrentClient().getName());
                }
                System.out.println("Enter number of your choice: ");

                String commandString = bufferedReader.readLine();
                if (commandsMap.containsKey(commandString)) {
                    commandsMap.get(commandString).execute();
                } else {
                    System.out.println("It's not a command! Try again!");
                }
            } catch (IOException | IllegalArgumentException e) {
                System.out.println(e.getMessage());
            }
        }

    }

}
