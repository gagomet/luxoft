package BankApplication.main;

import BankApplication.commander.Command;
import BankApplication.commander.CommandsManager;
import BankApplication.model.impl.Client;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Map;

/**
 * Created by Kir Kolesnikov on 15.01.2015.
 */

public class BankCommander implements CommandsManager {
    private static Map<Integer, Command> commandsMap;
    private static Client currentClient;

    public static void main(String[] args) {
        ApplicationContext context =
                new ClassPathXmlApplicationContext("application-context.xml");
        BankCommander bankCommander = context.getBean(BankCommander.class);

        while (true) {
            for (Integer i = 0; i < bankCommander.getCommandsMap().size(); i++) {
                System.out.print((i) + ") ");
                commandsMap.get(i.toString()).printCommandInfo();
            }
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(System.in));
            try {
                System.out.println("Active client now is: ");
                if (currentClient == null) {
                    System.out.println("N/A");
                } else {
                    System.out.println(currentClient.getName());
                }
                System.out.println("Enter number of your choice: ");

                String commandString = bufferedReader.readLine();
                if (commandsMap.containsKey(commandString)) {
                    commandsMap.get(commandString).execute();
                } else {
                    System.out.println("It's not a command! Try again!");
                }
            } catch (IOException e) {
                System.out.println(e.getMessage());
            }
        }
    }

    public Map<Integer, Command> getCommandsMap() {
        return commandsMap;
    }

    public void setCommandsMap(Map<Integer, Command> commandsMap) {
        this.commandsMap = commandsMap;
    }

    @Override
    public Client getCurrentClient() {
        return currentClient;
    }

    @Override
    public void setCurrentClient(Client client) {
         currentClient = client;
    }
}
