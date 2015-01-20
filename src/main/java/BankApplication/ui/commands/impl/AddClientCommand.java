package BankApplication.ui.commands.impl;

import BankApplication.exceptions.ClientExceedsException;
import BankApplication.exceptions.IllegalArgumentException;
import BankApplication.model.client.Client;
import BankApplication.service.bankservice.BankServiceEnumSingletone;
import BankApplication.type.Gender;
import BankApplication.ui.commander.BankCommander;

import java.io.IOException;

/**
 * Created by Kir Kolesnikov on 15.01.2015.
 */
public class AddClientCommand extends AbstractCommand {
    String newClientsName;
    Gender newClientSex;
    Float newClientOverdraft;
    String newClientPhone;
    String newClientEmail;
    String newClientsCity;

    @Override
    public void execute() throws IllegalArgumentException {
        try {

            while (true) {
                try {
                    newClientsName = validateClientsName(console.consoleResponse(bundle.getString("addClientsName")));
                    break;
                } catch (IllegalArgumentException e) {
                    System.out.println(errorsBundle.getString("wrongClientsName"));
                }
            }

            while (true) {
                try {
                    newClientSex = validateClientsSex(console.consoleResponse(bundle.getString("addClientsSex")));
                    break;
                } catch (IllegalArgumentException e) {
                    System.out.println(errorsBundle.getString("wrongGender"));
                }
            }

            while (true) {
                try {
                    newClientOverdraft = validateFunds(console.consoleResponse(bundle.getString("addClientsOverdraft")));
                    break;
                } catch (IllegalArgumentException e) {
                    System.out.println(errorsBundle.getString("wrongNumber"));
                }
            }

            while (true) {
                try {
                    newClientPhone = validateClientsPhone(console.consoleResponse(bundle.getString("addClientsPhone")));
                    break;

                } catch (IllegalArgumentException e) {
                    System.out.println(errorsBundle.getString("wrongPhone"));
                }
            }


            while (true) {
                try {
                    newClientEmail = validateClientsEmail(console.consoleResponse(bundle.getString("addClientsEmail")));
                    break;

                } catch (IllegalArgumentException e) {
                    System.out.println(errorsBundle.getString("wrongEmail"));
                }
            }

            while (true) {
                try {
                    newClientsCity = validateClientsName(console.consoleResponse(bundle.getString("addClientsCity")));
                    break;
                } catch (IllegalArgumentException e) {
                    System.out.println(errorsBundle.getString("wrongClientsCity"));
                }
            }

            addClient( newClientSex);
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("Not valid entry :" + e.getMessage());
        } catch (ClientExceedsException | IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void printCommandInfo() {
        System.out.println(bundle.getString("addClientCommand"));
    }

    private void addClient(Gender newClientGender) throws IllegalArgumentException, ClientExceedsException {

        Client newClient = new Client(newClientGender);
        newClient.setName(newClientsName);
        if(newClientOverdraft != null){
            newClient.setInitialOverdraft(newClientOverdraft);
        }
        if (newClientPhone != null) {
            newClient.setPhone(newClientPhone);
        }
        if (newClientEmail != null) {
            newClient.setEmail(newClientEmail);
        }
        System.out.println(bundle.getString("separator"));
        BankServiceEnumSingletone.addClient(BankCommander.currentBank, newClient);
        System.out.println(bundle.getString("clientAdded"));
        System.out.println(bundle.getString("separator"));
        newClient.printReport();
        StringBuilder builder = new StringBuilder();
        builder.append(bundle.getString("client"));
        builder.append(" ");
        builder.append(newClient.getName());
        builder.append(" ");
        builder.append(bundle.getString("active"));
        System.out.println(bundle.getString("separator"));
        System.out.println(builder.toString());
        System.out.println(bundle.getString("separator"));
    }
}
