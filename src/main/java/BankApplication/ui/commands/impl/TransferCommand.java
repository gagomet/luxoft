package BankApplication.ui.commands.impl;

import BankApplication.model.account.impl.AbstractAccount;
import BankApplication.exceptions.ClientNotFoundException;
import BankApplication.exceptions.IllegalArgumentException;
import BankApplication.exceptions.NotEnoughFundsException;
import BankApplication.model.Client;
import BankApplication.service.bankservice.BankServiceEnumSingletone;
import BankApplication.ui.commander.BankCommander;

import java.io.IOException;

/**
 * Created by Kir Kolesnikov on 15.01.2015.
 */
public class TransferCommand extends AbstractCommand {
    Float transferFunds;
    Long recepientAccountId;
    String recepientName;

    @Override
    public void execute() {
        if (BankCommander.currentClient == null) {
            System.out.println(bundle.getString("separator"));
            System.out.println(errorsBundle.getString("noActiveClient"));
            System.out.println(bundle.getString("separator"));
        } else {
            try {
                AbstractAccount senderAccount = BankCommander.getCurrentClient().getActiveAccount();
                while (true) {
                    try {
                        recepientName = validateClientsName(console.consoleResponse(bundle.getString("recipientName")));
                        break;
                    } catch (IllegalArgumentException e) {
                        System.out.println(errorsBundle.getString("wrongClientsName"));
                    }
                }
                Client recepient = BankServiceEnumSingletone.getClientByName(BankCommander.currentBank, recepientName);
                System.out.println(bundle.getString("separator"));
                recepient.printReport();
                System.out.println(bundle.getString("separator"));

                while (true) {
                    try {
                        recepientAccountId = validateId(console.consoleResponse(bundle.getString("recipientAccountId")));
                        break;

                    } catch (BankApplication.exceptions.IllegalArgumentException e) {
                        System.out.println(errorsBundle.getString("wrongNumber"));
                    }
                }

                while (true) {
                    try {
                        transferFunds = validateFunds(console.consoleResponse(bundle.getString("transferFunds")));
                        break;

                    } catch (BankApplication.exceptions.IllegalArgumentException e) {
                        System.out.println(errorsBundle.getString("wrongNumber"));
                    }
                }

                transferFunds(senderAccount, recepient.getActiveAccount(), transferFunds);

                System.out.println(bundle.getString("separator"));
                System.out.println(bundle.getString("complete"));
                System.out.println(bundle.getString("separator"));
                BankCommander.getCurrentClient().printReport();
                recepient.printReport();

            } catch (IOException | ClientNotFoundException | IllegalArgumentException | NotEnoughFundsException e) {
                e.printStackTrace();
            }
        }

    }

    @Override
    public void printCommandInfo() {
        System.out.println(bundle.getString("transferCommand"));
    }

    public void transferFunds(AbstractAccount sender, AbstractAccount recepient, Float amount) throws NotEnoughFundsException, IllegalArgumentException {
        BankServiceEnumSingletone.withdrawFunds(sender, amount);
        BankServiceEnumSingletone.depositeFunds(recepient, amount);

    }
}
