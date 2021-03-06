package BankApplication.commander.impl;

import BankApplication.commander.CommandsManager;
import BankApplication.exceptions.NotEnoughFundsException;
import BankApplication.model.impl.Client;
import BankApplication.network.console.Console;
import BankApplication.service.impl.ServiceFactory;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Created by Kir Kolesnikov on 15.01.2015.
 */
public class WithdrawCommand extends AbstractCommand {
    private static final Logger logger = Logger.getLogger(WithdrawCommand.class.getName());
    public WithdrawCommand() {
    }

    public WithdrawCommand(Console console, CommandsManager manager) {
        this.console = console;
        setManager(manager);
    }

    @Override
    public void execute() throws IllegalArgumentException {

        Client currentClient = getManager().getCurrentClient();
        float amountToWithdraw;
        if (currentClient == null) {
            console.sendResponse(errorsBundle.getString("noActiveClient"));
            System.out.println(errorsBundle.getString("noActiveClient"));
        } else {
            try {
                while (true) {
                    try {
                        console.consoleResponse("How much do you want to withdraw :");
                        amountToWithdraw = validateFunds(console.getMessageFromClient());
                        break;

                    } catch (IllegalArgumentException e) {
                        logger.log(Level.INFO, errorsBundle.getString("wrongNumber"));
                        console.sendResponse(errorsBundle.getString("wrongNumber"));
                    } catch (ClassNotFoundException e) {
                        logger.log(Level.SEVERE, "ClassNotFound in WithdrawCommand ", e);
                    }
                }

                withdrawFunds(currentClient, amountToWithdraw);

            } catch (IOException e) {
                logger.log(Level.SEVERE, "I/O in WithdrawCommand ", e);
            }
        }

    }

    @Override
    public String toString() {
        return "Withdraw funds from account (9999999 Money in max)";
    }

    private void withdrawFunds(Client client, float amountToWithdraw) throws IllegalArgumentException {
        try {
            StringBuilder builder = new StringBuilder();
            builder.append(errorsBundle.getString("separator"));
            builder.append(System.getProperty("line.separator"));
            builder.append(client.getActiveAccount().toString());
            builder.append(System.getProperty("line.separator"));
            ServiceFactory.getAccountService().withdrawFunds(client.getActiveAccount(), amountToWithdraw);
            builder.append("Account was successfully reduced");
            builder.append(System.getProperty("line.separator"));
            builder.append(client.getActiveAccount().toString());
            console.sendResponse(builder.toString());
        } catch (NotEnoughFundsException e) {
            logger.log(Level.SEVERE, e.getMessage());
        }
    }
}
