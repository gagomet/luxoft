package BankApplication.commander.impl;

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

    public DepositCommand(Console console){
        this.console = console;
    }

    @Override
    public void execute() {
        float amountToDeposit;
        if (ServiceFactory.getClientService().getCurrentClient() == null) {
            console.sendResponse(errorsBundle.getString("noActiveClient"));
            System.out.println(errorsBundle.getString("noActiveClient"));
        } else {
            try {
                while (true) {
                    try {
                        amountToDeposit = validateFunds(console.consoleResponse("How much do you want to deposit :"));
                        break;

                    } catch (IllegalArgumentException e) {
                        System.out.println(errorsBundle.getString("wrongNumber"));
                    }
                }

                depositFunds(ServiceFactory.getClientService().getCurrentClient(), amountToDeposit);

            } catch (IOException e) {
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
