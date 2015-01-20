package BankApplication.ui.commands.impl;

import BankApplication.exceptions.NotEnoughFundsException;
import BankApplication.model.Client;
import BankApplication.service.bankservice.BankServiceEnumSingletone;
import BankApplication.ui.commander.BankCommander;

import java.io.IOException;

/**
 * Created by Kir Kolesnikov on 15.01.2015.
 */
public class WithdrawCommand extends AbstractCommand {
    @Override
    public void execute() {
        String clientName;
        Client currentClient = BankCommander.getCurrentClient();
        float amountToDeposit;
        if (currentClient == null) {
            System.out.println(bundle.getString("separator"));
            System.out.println(errorsBundle.getString("noActiveClient"));
            System.out.println(bundle.getString("separator"));
        } else {
            try {
                while (true) {
                    try {
                        amountToDeposit = validateFunds(console.consoleResponse(bundle.getString("withdrawFunds")));
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
        System.out.println(bundle.getString("withdrawCommand"));
    }

    private void depositFunds(Client client, float amountToDeposit) {
        try {
            System.out.println(bundle.getString("separator"));
            client.getActiveAccount().printReport();
            System.out.println(bundle.getString("separator"));
            BankServiceEnumSingletone.withdrawFunds(client.getActiveAccount(), amountToDeposit);
            System.out.println(bundle.getString("withdrawed"));
            client.getActiveAccount().printReport();
            System.out.println(bundle.getString("separator"));
        } catch (NotEnoughFundsException e) {
            System.out.println(e.getMessage());
        }
    }
}
