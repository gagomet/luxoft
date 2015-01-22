package BankApplication.service.impl;

import BankApplication.exceptions.NotEnoughFundsException;
import BankApplication.model.impl.Client;
import BankApplication.network.BankRemoteOffice;
import BankApplication.network.console.Console;
import BankApplication.service.BankServiceEnumSingletone;
import BankApplication.BankCommander;

import java.io.IOException;

/**
 * Created by Kir Kolesnikov on 15.01.2015.
 */
public class WithdrawCommand extends AbstractCommand {
    public WithdrawCommand() {
    }

    public WithdrawCommand(Console console){
        this.console = console;
    }
    @Override
    public void execute() {

        Client currentClient = /*BankCommander.*/BankRemoteOffice.getCurrentClient();
        float amountToDeposit;
        if (currentClient == null) {
            System.out.println(errorsBundle.getString("noActiveClient"));
        } else {
            try {
                while (true) {
                    try {
                        amountToDeposit = validateFunds(console.consoleResponse("How much do you want to withdraw :"));
                        break;

                    } catch (BankApplication.exceptions.IllegalArgumentException e) {
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
        System.out.println("Withdraw funds from account (9999999 Money in max)");
    }

    private void depositFunds(Client client, float amountToDeposit) {
        try {
            StringBuilder builder = new StringBuilder();
            builder.append(errorsBundle.getString("separator"));
            builder.append(System.getProperty("line.separator"));
            builder.append(client.getActiveAccount().toString());
            builder.append(System.getProperty("line.separator"));
            BankServiceEnumSingletone.withdrawFunds(client.getActiveAccount(), amountToDeposit);
            builder.append("Account was successfully reduced");
            builder.append(System.getProperty("line.separator"));
            builder.append(client.getActiveAccount().toString());
            builder.append(System.getProperty("line.separator"));
            builder.append("Press Enter to continue");
            console.sendResponse(builder.toString());
        } catch (NotEnoughFundsException e) {
            System.out.println(e.getMessage());
        }
    }
}
