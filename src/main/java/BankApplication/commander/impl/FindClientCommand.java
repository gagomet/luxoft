package BankApplication.commander.impl;

import BankApplication.exceptions.ClientNotFoundException;
import BankApplication.exceptions.IllegalArgumentException;
import BankApplication.model.impl.Client;
import BankApplication.network.console.Console;
import BankApplication.main.BankCommander;

import java.io.IOException;

/**
 * Created by Kir Kolesnikov on 15.01.2015.
 */
public class FindClientCommand extends AbstractCommand {

    public FindClientCommand() {

    }

    public FindClientCommand(Console console) {
        this.console = console;
    }

    @Override
    public void execute() {
        String clientName;
        try {
            while (true) {
                try {
                    clientName = validateClientsName(console.consoleResponse("Enter client's name: "));
                    break;
                } catch (IllegalArgumentException e) {
                    System.out.println(errorsBundle.getString("wrongClientsName"));
                }
            }

            findClientInBank(clientName);
        } catch (IOException e) {
            e.printStackTrace();
            console.sendResponse(e.getMessage());
        }


    }

    @Override
    public String toString() {
        return "Find client by name (also makes found Client active)";
    }

    private void findClientInBank(String clientName) {
        try {
            Client client = getClientService().getClientByName(BankCommander.currentBank /* BankRemoteOffice.getCurrentBank()*/, clientName);
            StringBuilder builder = new StringBuilder();
            builder.append("Client ");
            builder.append(client.getName());
            builder.append(" is active now.");
            builder.append(System.getProperty("line.separator"));
            System.out.println(builder.toString());
            console.sendResponse(client.toString() + builder.toString());
            client.printReport();
            BankCommander.setCurrentClient(client);
        } catch (ClientNotFoundException e) {
            e.printStackTrace();
            console.sendResponse(errorsBundle.getString("clientNotFound"));
        }
    }
}