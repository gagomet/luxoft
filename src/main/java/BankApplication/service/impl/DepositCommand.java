package BankApplication.service.impl;

import BankApplication.exceptions.IllegalArgumentException;
import BankApplication.model.impl.Client;
import BankApplication.network.BankRemoteOffice;
import BankApplication.network.console.Console;
import BankApplication.service.BankServiceEnumSingletone;
import BankApplication.BankCommander;

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
    //TODO refactor to remote console
    public void execute() {
        String clientName;
        Client currentClient = /*BankCommander.*/BankRemoteOffice.getCurrentClient();
        float amountToDeposit;
        if (currentClient == null) {
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

                depositFunds(currentClient, amountToDeposit);

            } catch (IOException e) {
                e.printStackTrace();
            }
        }

    }

    @Override
    public void printCommandInfo() {
        System.out.println("Deposit funds to account (9999999 Money in max)");
    }

    private void depositFunds(Client client, float amountToDeposit) {
        try {
            StringBuilder builder = new StringBuilder();
            builder.append(errorsBundle.getString("separator"));
            builder.append(System.getProperty("line.separator"));
            builder.append(client.getActiveAccount().toString());
            builder.append(System.getProperty("line.separator"));
            BankServiceEnumSingletone.depositeFunds(client.getActiveAccount(), amountToDeposit);
            builder.append("Account was successfully refilled");
            builder.append(System.getProperty("line.separator"));
            builder.append(client.getActiveAccount().toString());
            console.sendResponse(builder.toString());
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
        }
    }
}
