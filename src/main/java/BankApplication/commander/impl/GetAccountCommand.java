package BankApplication.commander.impl;

import BankApplication.commander.CommandsManager;
import BankApplication.exceptions.AccountNotFoundException;
import BankApplication.model.Account;
import BankApplication.model.impl.Client;
import BankApplication.network.console.Console;
import BankApplication.service.impl.ServiceFactory;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Created by Kir Kolesnikov on 15.01.2015.
 */
public class GetAccountCommand extends AbstractCommand {
    Client currentClient = null;
    Long clientId;
    private static final Logger logger = Logger.getLogger(GetAccountCommand.class.getName());

    public GetAccountCommand() {
    }

    public GetAccountCommand(Console console, CommandsManager manager) {
        this.console = console;
        setManager(manager);
    }

    @Override
    public void execute() {
        currentClient = getManager().getCurrentClient();
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
                            console.consoleResponse("Enter an account's ID please:");
                            clientId = validateId(console.getMessageFromClient());
                            break;

                        } catch (IllegalArgumentException e) {
                            logger.log(Level.SEVERE, errorsBundle.getString("wrongNumber"), e );
                            System.out.println(errorsBundle.getString("wrongNumber"));
                        } catch (ClassNotFoundException e) {
                            logger.log(Level.SEVERE, "ClassNotFound", e);
                        }
                    }

                    Account account = ServiceFactory.getAccountService().getAccountById(currentClient, clientId);
                    getManager().getCurrentClient().setActiveAccount(account);
                    StringBuilder builder = new StringBuilder();
                    builder.append(account.toString());
                    builder.append("Account ");
                    builder.append(account.getId());
                    builder.append(" is active now. ");
                    console.sendResponse(builder.toString());
                } catch (IOException | AccountNotFoundException e) {
                    console.sendResponse(e.getMessage());
                    logger.log(Level.SEVERE, "Get account command exception", e);
                }
            }

        }
    }

    @Override
    public String toString() {
        return "Set an active account of the client";
    }
}
