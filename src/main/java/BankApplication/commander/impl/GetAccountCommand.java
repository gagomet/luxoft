package BankApplication.commander.impl;

import BankApplication.main.BankCommander;
import BankApplication.exceptions.AccountNotFoundException;
import BankApplication.model.Account;
import BankApplication.model.impl.Client;
import BankApplication.network.console.Console;

import java.io.IOException;

/**
 * Created by Kir Kolesnikov on 15.01.2015.
 */
public class GetAccountCommand extends AbstractCommand {
    Client currentClient = null;
    Long clientId;

    public GetAccountCommand() {
    }

    public GetAccountCommand(Console console) {
        this.console = console;
    }

    @Override
    //TODO refactor to remote console
    public void execute() {
        currentClient = BankCommander.getCurrentClient()/*BankRemoteOffice.getCurrentClient()*/;
        if (currentClient == null) {
            System.out.println(errorsBundle.getString("noActiveClient"));
            console.sendResponse(errorsBundle.getString("noActiveClient"));
        } else {
            System.out.println("*****Accounts list*****");
            if (currentClient.getAccountsList().size() == 1) {
                System.out.println("Client has only 1 account and it's active by default.");
            } else if (currentClient.getAccountsList().size() == 0) {
                System.out.println("Client has no any account creating account by default");
            } else {
                for (Account tempAccount : currentClient.getAccountsList()) {
                    tempAccount.printReport();
                }
                System.out.println("Select an account from the Accounts list by entering it ID");

                try {
                    while (true) {
                        try {
                            clientId = validateId(console.consoleResponse("Enter an account's ID please:"));
                            break;

                        } catch (BankApplication.exceptions.IllegalArgumentException e) {
                            System.out.println(errorsBundle.getString("wrongNumber"));
                        }
                    }

                    Account account = getAccountService().getAccountById(currentClient, clientId);
                    BankCommander.getCurrentClient()/*BankRemoteOffice.getCurrentClient()*/.setActiveAccount(account);
                    StringBuilder builder = new StringBuilder();
                    builder.append(account.toString());
                    builder.append("Account ");
                    builder.append(account.getId());
                    builder.append(" is active now. ");
                    console.sendResponse(builder.toString());
                } catch (IOException | AccountNotFoundException e) {
                    console.sendResponse(e.getMessage());
                    e.printStackTrace();
                }
            }

        }
    }

    @Override
    public String toString() {
        return "Set an active account of the client";
    }
}