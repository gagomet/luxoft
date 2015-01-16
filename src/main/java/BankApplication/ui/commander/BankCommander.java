package BankApplication.ui.commander;

import BankApplication.BankApplication;
import BankApplication.exceptions.IllegalArgumentException;
import BankApplication.model.Bank;
import BankApplication.model.client.Client;
import BankApplication.ui.commands.ICommand;
import BankApplication.ui.commands.impl.*;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

/**
 * Created by Kir Kolesnikov on 15.01.2015.
 */
public class BankCommander {
    public static Bank currentBank = new Bank();
    public static Client client1;
    public static Client client2;
    public static BankApplication bankApplication = new BankApplication();


    static
    ICommand[] commands = {
            new FindClientCommand(), // 1
            new GetAccountCommand(), // 2
            new WithdrawCommand(), //3
            new DepositCommand(), //4
            new TransferCommand(), //5
            new AddClientCommand(), //6
            new ICommand() { // 7 - Exit Command
                public void execute() {
                    System.exit(0);
                }

                public void printCommandInfo() {
                    System.out.println("Exit");
                }
            }
    };


    public static void main(String args[]) {
        bankApplication.initialize();
        currentBank = bankApplication.getBank();
        client1 = bankApplication.getClient1();
        client2 = bankApplication.getClient2();

        while (true) {
            for (int i = 0; i < commands.length; i++) { // show menu
                System.out.print(i + ") ");
                commands[i].printCommandInfo();
            }
            int commandNumber = 0;
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(System.in));
            try {
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
