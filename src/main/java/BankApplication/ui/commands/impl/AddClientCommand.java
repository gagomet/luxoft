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

    @Override
    public void execute() throws IllegalArgumentException {
        try {

            //TODO add client's city dialogue and validation
            while (true) {
                try {
                    newClientsName = validateClientsName(console.consoleResponse(bundle.getString("addClientsName")));
                    break;
                } catch (IllegalArgumentException e) {
                    System.out.println(errorsBundle.getString("wrongClientsName"));
                    continue;
                }
            }

            while (true) {
                try {
                    newClientSex = validateClientsSex(console.consoleResponse(bundle.getString("addClientsSex")));
                    break;
                } catch (IllegalArgumentException e) {
                    System.out.println(errorsBundle.getString("wrongGender"));
                    continue;
                }
            }

            while (true) {
                try {
                    newClientOverdraft = validateFunds(console.consoleResponse(bundle.getString("addClientsOverdraft")));
                    break;
                } catch (IllegalArgumentException e) {
                    System.out.println(errorsBundle.getString("wrongNumber"));
                    continue;
                }
            }

            while (true) {
                try {
                    newClientPhone = validateClientsPhone(console.consoleResponse(bundle.getString("addClientsPhone")));
                    break;

                } catch (IllegalArgumentException e) {
                    System.out.println(errorsBundle.getString("wrongPhone"));
                    continue;
                }
            }


            while (true) {
                try {
                    newClientEmail = validateClientsEmail(console.consoleResponse(bundle.getString("addClientsEmail")));
                    break;

                } catch (IllegalArgumentException e) {
                    System.out.println(errorsBundle.getString("wrongEmail"));
                    continue;
                }
            }


            addClient(/*newClientOverdraft,*/ newClientSex);
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("Not valid entry :" + e.getMessage());
        } catch (ClientExceedsException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void printCommandInfo() {
        System.out.println(bundle.getString("addClientCommand"));
    }

    private void addClient(/*Float newClientOverdraft,*/ Gender newClientGender) throws IllegalArgumentException, ClientExceedsException {
        /*Client newClient = null;
        if (newClientOverdraft == null) {
            newClient = new Client(newClientGender);
        } else {
            newClient = new Client(newClientOverdraft, newClientGender);
        }*/
        Client newClient = new Client(newClientGender);
        newClient.setName(newClientsName);
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
