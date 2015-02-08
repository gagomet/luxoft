package BankApplication.commander.impl;

import BankApplication.commander.CommandsManager;
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

    public AddClientCommand(Console console, CommandsManager manager) {
        this.console = console;
        setManager(manager);
    }

    @Override
    public void execute() {
        try {

            while (true) {
                try {
                    console.consoleResponse("Enter client's name:");
                    newClientsName = validateClientsName(console.getMessageFromClient());
                    break;
                } catch (IllegalArgumentException e) {
                    System.out.println(errorsBundle.getString("wrongClientsName"));
                    console.sendResponse(errorsBundle.getString("wrongClientsName"));
                } catch (ClassNotFoundException e) {
                    e.printStackTrace();
                }
            }

            while (true) {
                try {
                    console.consoleResponse("Enter client's gender:");
                    newClientSex = validateClientsSex(console.getMessageFromClient());
                    break;
                } catch (IllegalArgumentException e) {
                    System.out.println(errorsBundle.getString("wrongGender"));
                    console.sendResponse(errorsBundle.getString("wrongGender"));
                } catch (ClassNotFoundException e) {
                    e.printStackTrace();
                }
            }

            while (true) {
                try {
                    console.consoleResponse("Enter client's overdraft (live it empty if client has no overdraft):");
                    newClientOverdraft = validateFunds(console.getMessageFromClient());
                    break;
                } catch (IllegalArgumentException e) {
                    System.out.println(errorsBundle.getString("wrongNumber"));
                    console.sendResponse(errorsBundle.getString("wrongNumber"));
                } catch (ClassNotFoundException e) {
                    e.printStackTrace();
                }
            }

            while (true) {
                try {
                    console.consoleResponse("Enter client's phone number (live it empty if client has no phone):");
                    newClientPhone = validateClientsPhone(console.getMessageFromClient());
                    break;

                } catch (IllegalArgumentException e) {
                    System.out.println(errorsBundle.getString("wrongPhone"));
                    console.sendResponse(errorsBundle.getString("wrongPhone"));
                } catch (ClassNotFoundException e) {
                    e.printStackTrace();
                }
            }


            while (true) {
                try {
                    console.consoleResponse("Enter client's email (live it empty if client has no email):");
                    newClientEmail = validateClientsEmail(console.getMessageFromClient());
                    break;

                } catch (IllegalArgumentException e) {
                    System.out.println(errorsBundle.getString("wrongEmail"));
                    console.sendResponse(errorsBundle.getString("wrongEmail"));
                } catch (ClassNotFoundException e) {
                    e.printStackTrace();
                }
            }

            while (true) {
                try {
                    console.consoleResponse("Enter client's city (live it empty if city is unknown):");
                    newClientsCity = validateClientsCity(console.getMessageFromClient());
                    break;
                } catch (IllegalArgumentException e) {
                    System.out.println(errorsBundle.getString("wrongClientsCity"));
                    console.sendResponse(errorsBundle.getString("wrongClientsCity"));
                } catch (ClassNotFoundException e) {
                    e.printStackTrace();
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

    private void addClient() throws ClientExceedsException {

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
        getManager().setCurrentClient(newClient);
        System.out.println(builder.toString());
        console.sendResponse(builder.toString());

    }
}
