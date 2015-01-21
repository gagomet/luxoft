package BankApplication.service.impl;

import BankApplication.exceptions.NotEnoughFundsException;
import BankApplication.model.impl.Client;
import BankApplication.service.BankServiceEnumSingletone;
import BankApplication.BankCommander;

import java.io.IOException;

/**
 * Created by Kir Kolesnikov on 15.01.2015.
 */
public class WithdrawCommand extends AbstractCommand {
    @Override
    public void execute() {

        Client currentClient = BankCommander.getCurrentClient();
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
            System.out.println(errorsBundle.getString("separator"));
            client.getActiveAccount().printReport();
            BankServiceEnumSingletone.withdrawFunds(client.getActiveAccount(), amountToDeposit);
            System.out.println("Account was successfully reduced");
            client.getActiveAccount().printReport();
        } catch (NotEnoughFundsException e) {
            System.out.println(e.getMessage());
        }//TODO refactor to remote console
    }
}
