package BankApplication.service.impl;

import BankApplication.exceptions.ClientNotFoundException;
import BankApplication.exceptions.IllegalArgumentException;
import BankApplication.exceptions.NotEnoughFundsException;
import BankApplication.model.Account;
import BankApplication.model.impl.Client;
import BankApplication.network.BankRemoteOffice;
import BankApplication.network.console.Console;
import BankApplication.service.BankServiceEnumSingletone;

import java.io.IOException;

/**
 * Created by Kir Kolesnikov on 15.01.2015.
 */
public class TransferCommand extends AbstractCommand {
    Float transferFunds;
    long recepientAccountId;
    String recepientName;

    public TransferCommand() {
    }

    public TransferCommand(Console console) {
        this.console = console;
    }

    @Override
    //TODO refactor to remote console
    public void execute() {
        if (/*BankCommander*/BankRemoteOffice.getCurrentClient() == null) {
            System.out.println(errorsBundle.getString("noActiveClient"));
            console.sendResponse("Select active client first! Press enter to continue");
        } else {
            try {
                Account senderAccount = /*BankCommander*/BankRemoteOffice.getCurrentClient().getActiveAccount();
                while (true) {
                    try {
                        recepientName = validateClientsName(console.consoleResponse("Enter Client-recipient name, please"));
                        break;
                    } catch (IllegalArgumentException e) {
                        System.out.println(errorsBundle.getString("wrongClientsName"));
                        console.sendResponse(errorsBundle.getString("wrongClientsName"));
                    }
                }
                Client recepient = BankServiceEnumSingletone.getClientByName(/*BankCommander.*/BankRemoteOffice.getCurrentBank()
                        , recepientName);
                System.out.println("Recepient: " + recepient.getName() + " accounts ID ");
                for (Account account : recepient.getAccountsList()) {
                    System.out.println(account.getId());
                }
                while (true) {
                    try {
                        recepientAccountId = validateId(console.consoleResponse("Enter recipient account ID"));
                        break;

                    } catch (BankApplication.exceptions.IllegalArgumentException e) {
                        System.out.println(errorsBundle.getString("wrongNumber"));
                        console.sendResponse(errorsBundle.getString("wrongNumber"));
                    }
                }

                while (true) {
                    try {
                        transferFunds = validateFunds(console.consoleResponse("How much do you want to transfer :"));
                        break;

                    } catch (BankApplication.exceptions.IllegalArgumentException e) {
                        System.out.println(errorsBundle.getString("wrongNumber"));
                        console.sendResponse(errorsBundle.getString("wrongNumber"));
                    }
                }

                transferFunds(senderAccount, recepient.getActiveAccount(), transferFunds);
//                /*BankCommander*/BankRemoteOffice.getCurrentClient().printReport();
                recepient.printReport();

            } catch (IOException | ClientNotFoundException | IllegalArgumentException | NotEnoughFundsException e) {
                e.printStackTrace();
                console.sendResponse(e.getMessage());
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
        StringBuilder builder = new StringBuilder();
        builder.append("Transfer funds successfully completed");
        builder.append(System.getProperty("line.separator"));
        builder.append("Use info command to get detailed info");
        console.sendResponse(builder.toString());
    }
}
