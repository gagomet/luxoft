package BankApplication.ui.commander;

import BankApplication.BankApplication;
import BankApplication.exceptions.IllegalArgumentException;
import BankApplication.model.Bank;
import BankApplication.model.client.Client;
import BankApplication.ui.commands.ICommand;
import BankApplication.ui.commands.impl.AddClientCommand;
import BankApplication.ui.commands.impl.DepositCommand;
import BankApplication.ui.commands.impl.FindClientCommand;
import BankApplication.ui.commands.impl.GetAccountCommand;
import BankApplication.ui.commands.impl.ShowHelpCommand;
import BankApplication.ui.commands.impl.TransferCommand;
import BankApplication.ui.commands.impl.WithdrawCommand;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

/**
 * Created by Kir Kolesnikov on 15.01.2015.
 */
public class BankCommander {
    public static Bank currentBank = new Bank();
    public static Client currentClient = null;
    public static BankApplication bankApplication = new BankApplication();


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
        bankApplication.initialize();
        currentBank = bankApplication.getBank();
        //initialization and retrieving of Bank class instance

        while (true) {
            for (int i = 0; i < commands.length; i++) { // show menu
                System.out.print(i + ") ");
                commands[i].printCommandInfo();
            }
            int commandNumber = 0;
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(System.in));
            try {
                System.out.println("Enter number of your choice: ");

                String commandString = bufferedReader.readLine();
                commandNumber = Integer.parseInt(commandString);
            } catch (IOException e) {
                e.printStackTrace();
            }
            try {
                commands[commandNumber].execute();
            } catch (IllegalArgumentException e) {
                e.printStackTrace();
            }
        }
    }
}
