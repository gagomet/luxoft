package BankApplication.service.impl;

import BankApplication.exceptions.IllegalArgumentException;
import BankApplication.model.impl.Client;
import BankApplication.service.BankServiceEnumSingletone;
import BankApplication.BankCommander;

import java.io.IOException;

/**
 * Created by Kir Kolesnikov on 15.01.2015.
 */
public class DepositCommand extends AbstractCommand {

    @Override
    //TODO refactor to remote console
    public void execute() {
        String clientName;
        Client currentClient = BankCommander.getCurrentClient();
        float amountToDeposit;
        if (currentClient == null) {
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
            System.out.println(errorsBundle.getString("separator"));
            client.getActiveAccount().printReport();
            BankServiceEnumSingletone.depositeFunds(client.getActiveAccount(), amountToDeposit);
            System.out.println("Account was successfully refilled");
            client.getActiveAccount().printReport();
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
        }
    }
}
