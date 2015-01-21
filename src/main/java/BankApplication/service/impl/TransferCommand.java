package BankApplication.service.impl;

import BankApplication.model.Account;
import BankApplication.exceptions.ClientNotFoundException;
import BankApplication.exceptions.IllegalArgumentException;
import BankApplication.exceptions.NotEnoughFundsException;
import BankApplication.model.impl.Client;
import BankApplication.service.BankServiceEnumSingletone;
import BankApplication.BankCommander;

import java.io.IOException;

/**
 * Created by Kir Kolesnikov on 15.01.2015.
 */
public class TransferCommand extends AbstractCommand {
    Float transferFunds;
    long recepientAccountId;
    String recepientName;

    @Override
    //TODO refactor to remote console
    public void execute() {
        if (BankCommander.currentClient == null) {
            System.out.println(errorsBundle.getString("noActiveClient"));
            console.sendResponse(errorsBundle.getString("noActiveClient"));
        } else {
            try {
                Account senderAccount = BankCommander.getCurrentClient().getActiveAccount();
                while (true) {
                    try {
                        recepientName = validateClientsName(console.consoleResponse("Enter Client-recipient name, please"));
                        break;
                    } catch (IllegalArgumentException e) {
                        System.out.println(errorsBundle.getString("wrongClientsName"));
                    }
                }
                Client recepient = BankServiceEnumSingletone.getClientByName(BankCommander.currentBank, recepientName);
                System.out.println("Recepient: " + recepient.getName() +" accounts ID ");
                for(Account account : recepient.getAccountsList()){
                    System.out.println(account.getId());
                }
                while (true) {
                    try {
                        recepientAccountId = validateId(console.consoleResponse("Enter recipient account ID"));
                        break;

                    } catch (BankApplication.exceptions.IllegalArgumentException e) {
                        System.out.println(errorsBundle.getString("wrongNumber"));
                    }
                }

                while (true) {
                    try {
                        transferFunds = validateFunds(console.consoleResponse("How much do you want to transfer :"));
                        break;

                    } catch (BankApplication.exceptions.IllegalArgumentException e) {
                        System.out.println(errorsBundle.getString("wrongNumber"));
                    }
                }

                transferFunds(senderAccount, recepient.getActiveAccount(), transferFunds);
                BankCommander.getCurrentClient().printReport();
                recepient.printReport();

            } catch (IOException | ClientNotFoundException | IllegalArgumentException | NotEnoughFundsException e) {
                e.printStackTrace();
            }
        }

    }

    @Override
    public void printCommandInfo() {
        System.out.println("Transfer funds from active account to another account (9999999 Money in max)");
    }

    public void transferFunds(Account sender, Account recepient, Float amount) throws NotEnoughFundsException, IllegalArgumentException {
        BankServiceEnumSingletone.withdrawFunds(sender, amount);
        BankServiceEnumSingletone.depositeFunds(recepient, amount);
        System.out.println("Transfer funds succesfully completed");

    }
}
