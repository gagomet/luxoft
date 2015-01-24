package BankApplication.service.impl;

import BankApplication.exceptions.*;
import BankApplication.exceptions.IllegalArgumentException;
import BankApplication.model.impl.Client;
import BankApplication.network.BankRemoteOffice;
import BankApplication.network.console.Console;
import BankApplication.service.BankServiceEnumSingletone;

import java.io.IOException;

/**
 * Created by Kir Kolesnikov on 21.01.2015.
 */
public class RemoveClientCommand extends AbstractCommand {

    public RemoveClientCommand() {
    }

    public RemoveClientCommand(Console console){
        this.console = console;
    }
    @Override
    public void execute() throws IllegalArgumentException {
        String clientName;
        try {
            while (true) {
                try {
                    clientName = validateClientsName(console.consoleResponse("Enter client's name: "));
                    break;
                } catch (IllegalArgumentException e) {
                    System.out.println(errorsBundle.getString("wrongClientsName"));
                    console.sendResponse(errorsBundle.getString("wrongClientsName"));
                }
            }

            removeClientFromBank(clientName);

        } catch (IOException e) {
            e.printStackTrace();
            console.sendResponse(e.getMessage());
        }
    }

    @Override
    public void printCommandInfo() {
        System.out.println("Remove client");
    }

    private void removeClientFromBank(String clientName){
        try {
            Client client = BankServiceEnumSingletone.getClientByName(BankRemoteOffice.getCurrentBank(), clientName);
            BankServiceEnumSingletone.removeClient(BankRemoteOffice.getCurrentBank(), client);
            console.sendResponse(client.toString() + " was removed.");
        } catch (ClientNotFoundException e) {
            e.printStackTrace();
            console.sendResponse(errorsBundle.getString("clientNotFound"));
        }
    }
}
