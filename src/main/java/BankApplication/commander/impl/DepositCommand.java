package BankApplication.commander.impl;

import BankApplication.commander.CommandsManager;
import BankApplication.model.impl.Client;
import BankApplication.network.console.Console;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Created by Kir Kolesnikov on 15.01.2015.
 */
public class DepositCommand extends AbstractCommand {

    private static final Logger logger = Logger.getLogger(DepositCommand.class.getName());

    public DepositCommand() {
    }

    public DepositCommand(Console console, CommandsManager manager) {
        this.console = console;
        setManager(manager);
    }

    @Override
    public void execute() {
        float amountToDeposit;
        if (getManager().getCurrentClient() == null) {
            console.sendResponse(errorsBundle.getString("noActiveClient"));
            System.out.println(errorsBundle.getString("noActiveClient"));
        } else {
            try {
                while (true) {
                    console.consoleResponse("How much do you want to deposit :");
                    amountToDeposit = validateFunds(console.getMessageFromClient());
                    break;
                }
                depositFunds(getManager().getCurrentClient(), amountToDeposit);

            } catch (IOException | ClassNotFoundException e) {
                logger.log(Level.SEVERE, "Exception in DepositCommand", e);
            }
        }

    }

    @Override
    public String toString() {
        return "Deposit funds to account (9999999 Money in max)";
    }

    private void depositFunds(Client client, float amountToDeposit) {
        try {
            StringBuilder builder = new StringBuilder();
            builder.append(errorsBundle.getString("separator"));
            builder.append(System.getProperty("line.separator"));
            builder.append(client.getActiveAccount().toString());
            builder.append(System.getProperty("line.separator"));
            getAccountService().depositeFunds(client.getActiveAccount(), amountToDeposit);
            builder.append("Account was successfully refilled");
            builder.append(System.getProperty("line.separator"));
            builder.append(client.getActiveAccount().toString());
            console.sendResponse(builder.toString());
        } catch (IllegalArgumentException e) {
            logger.log(Level.SEVERE, "Runtime exception illegal argument", e);
        }
    }
}
