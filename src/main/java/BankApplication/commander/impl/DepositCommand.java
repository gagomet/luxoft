package BankApplication.commander.impl;

import BankApplication.commander.CommandsManager;
import BankApplication.model.impl.Client;
import BankApplication.network.console.Console;
import BankApplication.service.impl.ServiceFactory;

import java.io.IOException;

/**
 * Created by Kir Kolesnikov on 15.01.2015.
 */
public class DepositCommand extends AbstractCommand {

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
                e.printStackTrace();
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
            ServiceFactory.getAccountService().depositeFunds(client.getActiveAccount(), amountToDeposit);
            builder.append("Account was successfully refilled");
            builder.append(System.getProperty("line.separator"));
            builder.append(client.getActiveAccount().toString());
            console.sendResponse(builder.toString());
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
        }
    }
}
