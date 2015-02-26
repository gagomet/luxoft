package BankApplication.commander.impl;


import BankApplication.commander.CommandsManager;
import BankApplication.exceptions.ClientNotFoundException;
import BankApplication.exceptions.NotEnoughFundsException;
import BankApplication.model.Account;
import BankApplication.model.impl.Client;
import BankApplication.network.console.Console;
import BankApplication.network.console.ConsoleImpl;
import BankApplication.service.impl.BankServiceImpl;
import BankApplication.service.impl.FullBankService;
import BankApplication.service.impl.ServiceFactory;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Created by Kir Kolesnikov on 15.01.2015.
 */
public class TransferCommand extends AbstractCommand {
    Float transferAmount;
    long recepientAccountId;
    String recepientName;
    private static final Logger logger = Logger.getLogger(TransferCommand.class.getName());

    public TransferCommand(ConsoleImpl console, FullBankService fullBankService) {
    }

    public TransferCommand(Console console, CommandsManager manager) {
        this.console = console;
        setManager(manager);
    }

    @Override
    public void execute() {
        if (getManager().getCurrentClient() == null) {
            System.out.println(errorsBundle.getString("noActiveClient"));
            console.sendResponse("Select active client first! Press enter to continue");
        } else {
            try {
                Account senderAccount = getManager().getCurrentClient().getActiveAccount();
                while (true) {
                    try {
                        console.consoleResponse("Enter Client-recipient name, please");
                        recepientName = validateClientsName(console.getMessageFromClient());
                        break;
                    } catch (IllegalArgumentException e) {
                        logger.log(Level.INFO, errorsBundle.getString("wrongClientsName"), e);
                        console.sendResponse(errorsBundle.getString("wrongClientsName"));
                    } catch (ClassNotFoundException e) {
                        logger.log(Level.SEVERE, "Class not found at TransferCommand", e);
                    }
                }
                Client recepient = ServiceFactory.getClientService().getClientByName(BankServiceImpl.getInstance().getCurrentBank()
                        , recepientName);
                System.out.println("Recepient: " + recepient.getName() + " accounts ID ");
                for (Account account : recepient.getAccountsList()) {
                    System.out.println(account.getId());
                }
                while (true) {
                    try {
                        console.consoleResponse("Enter recipient account ID");
                        recepientAccountId = validateId(console.getMessageFromClient());
                        break;

                    } catch (IllegalArgumentException e) {
                        logger.log(Level.INFO, errorsBundle.getString("wrongNumber"), e);
                        console.sendResponse(errorsBundle.getString("wrongNumber"));
                    } catch (ClassNotFoundException e) {
                        logger.log(Level.SEVERE, "Class not found at TransferCommand", e);
                    }
                }

                while (true) {
                    try {
                        console.consoleResponse("How much do you want to transfer :");
                        transferAmount = validateFunds(console.getMessageFromClient());
                        break;

                    } catch (IllegalArgumentException e) {
                        logger.log(Level.INFO, errorsBundle.getString("wrongNumber"), e);
                        console.sendResponse(errorsBundle.getString("wrongNumber"));
                    } catch (ClassNotFoundException e) {
                        logger.log(Level.SEVERE, "Class not found at TransferCommand", e);
                    }
                }

                transferFunds(senderAccount, recepient.getActiveAccount(), transferAmount);
                recepient.printReport();

            } catch (IOException | ClientNotFoundException | IllegalArgumentException | NotEnoughFundsException e) {
                logger.log(Level.SEVERE, "Exception in TransferCommand ", e);
                console.sendResponse(e.getMessage());
            }
        }

    }

    @Override
    public String toString() {
        return "Transfer funds from active account to another account (9999999 Money in max)";
    }

    public void transferFunds(Account sender, Account recepient, Float amount) throws NotEnoughFundsException, IllegalArgumentException {
        ServiceFactory.getAccountService().transferFunds(sender, recepient, amount);
        StringBuilder builder = new StringBuilder();
        builder.append("Transfer funds successfully completed");
        builder.append(System.getProperty("line.separator"));
        builder.append("Use info command to get detailed info");
        console.sendResponse(builder.toString());
    }
}
