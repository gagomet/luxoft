package BankApplication.commander.impl;

import BankApplication.exceptions.ClientExceedsException;
import BankApplication.model.impl.Client;
import BankApplication.network.console.Console;
import BankApplication.service.impl.BankServiceImpl;
import BankApplication.service.impl.ServiceFactory;
import BankApplication.type.Gender;

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

    public AddClientCommand() {
    }

    public AddClientCommand(Console console) {
        this.console = console;
    }

    @Override
    public void execute() throws IllegalArgumentException {
        try {

            while (true) {
                try {
                    newClientsName = validateClientsName(console.consoleResponse("Enter client's name:"));
                    break;
                } catch (IllegalArgumentException e) {
                    System.out.println(errorsBundle.getString("wrongClientsName"));
                    console.sendResponse(errorsBundle.getString("wrongClientsName"));
                }
            }

            while (true) {
                try {
                    newClientSex = validateClientsSex(console.consoleResponse("Enter client's gender:"));
                    break;
                } catch (IllegalArgumentException e) {
                    System.out.println(errorsBundle.getString("wrongGender"));
                    console.sendResponse(errorsBundle.getString("wrongGender"));
                }
            }

            while (true) {
                try {
                    newClientOverdraft = validateFunds(console.consoleResponse("Enter client's overdraft (live it empty if client has no overdraft):"));
                    break;
                } catch (IllegalArgumentException e) {
                    System.out.println(errorsBundle.getString("wrongNumber"));
                    console.sendResponse(errorsBundle.getString("wrongNumber"));
                }
            }

            while (true) {
                try {
                    newClientPhone = validateClientsPhone(console.consoleResponse("Enter client's phone number (live it empty if client has no phone):"));
                    break;

                } catch (IllegalArgumentException e) {
                    System.out.println(errorsBundle.getString("wrongPhone"));
                    console.sendResponse(errorsBundle.getString("wrongPhone"));
                }
            }


            while (true) {
                try {
                    newClientEmail = validateClientsEmail(console.consoleResponse("Enter client's email (live it empty if client has no email):"));
                    break;

                } catch (IllegalArgumentException e) {
                    System.out.println(errorsBundle.getString("wrongEmail"));
                    console.sendResponse(errorsBundle.getString("wrongEmail"));
                }
            }

            while (true) {
                try {
                    newClientsCity = validateClientsCity(console.consoleResponse("Enter client's city (live it empty if city is unknown):"));
                    break;
                } catch (IllegalArgumentException e) {
                    System.out.println(errorsBundle.getString("wrongClientsCity"));
                    console.sendResponse(errorsBundle.getString("wrongClientsCity"));
                }
            }

            addClient();
        } catch (NumberFormatException e) {
            console.sendResponse("Not valid entry :" + e.getMessage());
            throw new IllegalArgumentException("Not valid entry :" + e.getMessage());

        } catch (ClientExceedsException | IOException e) {
            console.sendResponse(errorsBundle.getString("clientAlreadyExceeds"));
            e.printStackTrace();
        }
    }

    @Override
    public String toString() {
        return "Add client to Bank System";
    }

    private void addClient() throws IllegalArgumentException, ClientExceedsException {

        Client newClient = new Client();

        newClient.setName(newClientsName);
        if (newClientSex != null) {
            newClient.setSex(newClientSex);
        }
        if (newClientOverdraft != null) {
            newClient.setInitialOverdraft(newClientOverdraft);
        }
        if (newClientPhone != null) {
            newClient.setPhone(newClientPhone);
        }
        if (newClientEmail != null) {
            newClient.setEmail(newClientEmail);
        }
        if (newClientsCity != null) {
            newClient.setCity(newClientsCity);
        }
        System.out.println(errorsBundle.getString("separator"));
        ServiceFactory.getBankService().addClient(BankServiceImpl.getInstance().getCurrentBank(), newClient);
        System.out.println("New Client successfully added!");
        newClient.printReport();
        StringBuilder builder = new StringBuilder();
        builder.append("Client ");
        builder.append(newClient.getName());
        builder.append(" is active now");
        System.out.println(builder.toString());
        console.sendResponse(builder.toString());
    }
}
