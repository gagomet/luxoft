package BankApplication.ui.commands.impl;

import BankApplication.exceptions.ClientNotFoundException;
import BankApplication.exceptions.IllegalArgumentException;
import BankApplication.model.client.Client;
import BankApplication.service.bankservice.BankServiceEnumSingletone;
import BankApplication.ui.commander.BankCommander;

import java.io.IOException;

/**
 * Created by Kir Kolesnikov on 15.01.2015.
 */
public class FindClientCommand extends AbstractCommand {
    @Override
    public void execute() {
        String clientName;
        try {
            while (true) {
                try {
                    clientName = validateClientsName(console.consoleResponse(bundle.getString("addClientsName")));
                    break;
                } catch (IllegalArgumentException e) {
                    System.out.println(errorsBundle.getString("wrongClientsName"));
                    continue;
                }
            }

            findClientInBank(clientName);

        } catch (IOException e) {
            e.printStackTrace();
        }


    }

    @Override
    public void printCommandInfo() {
        System.out.println(bundle.getString("findClientCommand"));
    }

    private void findClientInBank(String clientName) {
        try {
            Client client = BankServiceEnumSingletone.getClientByName(BankCommander.currentBank, clientName);
            BankCommander.setCurrentClient(client);
            StringBuilder builder = new StringBuilder();
            builder.append(bundle.getString("client"));
            builder.append(" ");
            builder.append(client.getName());
            builder.append(" ");
            builder.append(bundle.getString("active"));
            System.out.println(bundle.getString("separator"));
            System.out.println(builder.toString());
            System.out.println(bundle.getString("separator"));
            client.printReport();
            System.out.println(bundle.getString("separator"));
        } catch (ClientNotFoundException e) {
            e.printStackTrace();
        }
    }
}
