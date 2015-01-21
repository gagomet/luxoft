package BankApplication.service.impl;

import BankApplication.exceptions.ClientNotFoundException;
import BankApplication.exceptions.IllegalArgumentException;
import BankApplication.model.impl.Client;
import BankApplication.network.BankRemoteOffice;
import BankApplication.network.console.Console;
import BankApplication.service.BankServiceEnumSingletone;
import BankApplication.BankCommander;

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
        }


    }

    @Override
    public void printCommandInfo() {
        System.out.println("Find client by name (also makes found Client active)");
    }

    private void findClientInBank(String clientName) {
        try {
            Client client = BankServiceEnumSingletone.getClientByName(/*BankCommander.currentBank*/  BankRemoteOffice.getCurrentBank(), clientName);
            BankCommander.setCurrentClient(client);
            StringBuilder builder = new StringBuilder();
            builder.append("Client ");
            builder.append(client.getName());
            builder.append(" is active now.");
            System.out.println(errorsBundle.getString("separator"));
            System.out.println(builder.toString());
            console.sendResponse(builder.toString() + client.toString());
            client.printReport();
        } catch (ClientNotFoundException e) {
            e.printStackTrace();
        }
    }
}
