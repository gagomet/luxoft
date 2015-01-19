package BankApplication.ui.commands.impl;

import BankApplication.exceptions.IllegalArgumentException;
import BankApplication.model.client.Client;
import BankApplication.service.bankservice.BankServiceEnumSingletone;
import BankApplication.ui.commander.BankCommander;

import java.io.IOException;

/**
 * Created by Kir Kolesnikov on 15.01.2015.
 */
public class DepositCommand extends AbstractCommand {

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
                        amountToDeposit = validateFunds(console.consoleResponse(bundle.getString("depositFunds")));
                        break;

                    } catch (IllegalArgumentException e) {
                        System.out.println(errorsBundle.getString("wrongNumber"));
                        continue;
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
        System.out.println(bundle.getString("depositCommand"));
    }

    private void depositFunds(Client client, float amountToDeposit) {
        try {
            System.out.println(bundle.getString("separator"));
            client.getActiveAccount().printReport();
            System.out.println(bundle.getString("separator"));
            BankServiceEnumSingletone.depositeFunds(client.getActiveAccount(), amountToDeposit);
            System.out.println(bundle.getString("deposit"));
            client.getActiveAccount().printReport();
            System.out.println(bundle.getString("separator"));
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
        }
    }
}
