package BankApplication.commander.impl;

import BankApplication.exceptions.*;
import BankApplication.exceptions.IllegalArgumentException;
import BankApplication.model.impl.Client;
import BankApplication.network.console.Console;
import BankApplication.service.impl.AccountServiceImpl;
import BankApplication.service.impl.ClientServiceImpl;
import BankApplication.service.impl.ServiceFactory;

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
    public void execute() throws IllegalArgumentException {

        Client currentClient = ServiceFactory.getClientService().getCurrentClient();
        float amountToWithdraw;
        if (currentClient == null) {
            console.sendResponse(errorsBundle.getString("noActiveClient"));
            System.out.println(errorsBundle.getString("noActiveClient"));
        } else {
            try {
                while (true) {
                    try {
                        amountToWithdraw = validateFunds(console.consoleResponse("How much do you want to withdraw :"));
                        break;

                    } catch (BankApplication.exceptions.IllegalArgumentException e) {
                        System.out.println(errorsBundle.getString("wrongNumber"));
                        console.sendResponse(errorsBundle.getString("wrongNumber"));
                    }
                }

                withdrawFunds(currentClient, amountToWithdraw);

            } catch (IOException e) {
                e.printStackTrace();
            }
        }

    }

    @Override
    public String toString() {
        return "Withdraw funds from account (9999999 Money in max)";
    }

    private void withdrawFunds(Client client, float amountToWithdraw) throws BankApplication.exceptions.IllegalArgumentException {
        try {
            StringBuilder builder = new StringBuilder();
            builder.append(errorsBundle.getString("separator"));
            builder.append(System.getProperty("line.separator"));
            builder.append(client.getActiveAccount().toString());
            builder.append(System.getProperty("line.separator"));
            ServiceFactory.getAccountService().withdrawFunds(client.getActiveAccount(), amountToWithdraw);
            builder.append("Account was successfully reduced");
            builder.append(System.getProperty("line.separator"));
            builder.append(client.getActiveAccount().toString());
            console.sendResponse(builder.toString());
        } catch (NotEnoughFundsException e) {
            System.out.println(e.getMessage());
        }
    }
}
