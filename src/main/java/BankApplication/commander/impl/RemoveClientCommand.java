package BankApplication.commander.impl;

import BankApplication.exceptions.*;
import BankApplication.exceptions.IllegalArgumentException;
import BankApplication.model.impl.Client;
import BankApplication.network.console.Console;
import BankApplication.service.impl.BankServiceImpl;
import BankApplication.service.impl.ClientServiceImpl;

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
    public String toString() {
        return "Remove client";
    }

    private void removeClientFromBank(String clientName){
        try {
            Client client = ClientServiceImpl.getInstance().getClientByName(BankServiceImpl.getInstance().getCurrentBank(), clientName);
            BankServiceImpl.getInstance().removeClient(BankServiceImpl.getInstance().getCurrentBank(), client);
            console.sendResponse(client.toString() + " was removed.");
        } catch (ClientNotFoundException e) {
            e.printStackTrace();
            console.sendResponse(errorsBundle.getString("clientNotFound"));
        }
    }
}
