package BankApplication.commander.impl;

import BankApplication.commander.CommandsManager;
import BankApplication.exceptions.*;
import BankApplication.model.impl.Client;
import BankApplication.network.console.Console;
import BankApplication.service.impl.BankServiceImpl;
import BankApplication.service.impl.ServiceFactory;

import java.io.IOException;

/**
 * Created by Kir Kolesnikov on 21.01.2015.
 */
public class RemoveClientCommand extends AbstractCommand {

    public RemoveClientCommand() {
    }

    public RemoveClientCommand(Console console, CommandsManager manager) {
        this.console = console;
        setManager(manager);
    }
    @Override
    public void execute()  {
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
            Client client = ServiceFactory.getClientService().getClientByName(BankServiceImpl.getInstance().getCurrentBank(), clientName);
            ServiceFactory.getBankService().removeClient(BankServiceImpl.getInstance().getCurrentBank(), client);
            console.sendResponse(client.toString() + " was removed. Current client set to null");
            getManager().setCurrentClient(null);
        } catch (ClientNotFoundException e) {
            e.printStackTrace();
            console.sendResponse(errorsBundle.getString("clientNotFound"));
        }
    }
}
