package BankApplication.commander.impl;

import BankApplication.commander.CommandsManager;
import BankApplication.exceptions.ClientExceedsException;
import BankApplication.model.impl.Client;
import BankApplication.network.console.Console;
import BankApplication.service.impl.BankServiceImpl;
import BankApplication.service.impl.ServiceFactory;
import BankApplication.type.Gender;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Created by Kir Kolesnikov on 15.01.2015.
 */
public class AddClientCommand extends AbstractCommand {
    private String newClientsName;
    private Gender newClientSex;
    private Float newClientOverdraft;
    private String newClientPhone;
    private String newClientEmail;
    private String newClientsCity;
    private static final Logger logger = Logger.getLogger(AbstractCommand.class.getName());

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
                    logger.log(Level.INFO, "Not valid client's name was entered by user" );
                    console.sendResponse(errorsBundle.getString("wrongClientsName"));
                } catch (ClassNotFoundException e) {
                    logger.log(Level.SEVERE, "ClassNotFound", e);
                }
            }

            while (true) {
                try {
                    console.consoleResponse("Enter client's gender:");
                    newClientSex = validateClientsSex(console.getMessageFromClient());
                    break;
                } catch (IllegalArgumentException e) {
                    logger.log(Level.INFO, "Not valid client's gender was entered by user" );
                    console.sendResponse(errorsBundle.getString("wrongGender"));
                } catch (ClassNotFoundException e) {
                    logger.log(Level.SEVERE, "ClassNotFound", e);
                }
            }

            while (true) {
                try {
                    console.consoleResponse("Enter client's overdraft (live it empty if client has no overdraft):");
                    newClientOverdraft = validateFunds(console.getMessageFromClient());
                    break;
                } catch (IllegalArgumentException e) {
                    logger.log(Level.INFO, "Not valid client's overdraft was entered by user" );
                    console.sendResponse(errorsBundle.getString("wrongNumber"));
                } catch (ClassNotFoundException e) {
                    logger.log(Level.SEVERE, "ClassNotFound", e);
                }
            }

            while (true) {
                try {
                    console.consoleResponse("Enter client's phone number (live it empty if client has no phone):");
                    newClientPhone = validateClientsPhone(console.getMessageFromClient());
                    break;

                } catch (IllegalArgumentException e) {
                    logger.log(Level.INFO, "Not valid client's phone number was entered by user" );
                    console.sendResponse(errorsBundle.getString("wrongPhone"));
                } catch (ClassNotFoundException e) {
                    logger.log(Level.SEVERE, "ClassNotFound", e);
                }
            }


            while (true) {
                try {
                    console.consoleResponse("Enter client's email (live it empty if client has no email):");
                    newClientEmail = validateClientsEmail(console.getMessageFromClient());
                    break;

                } catch (IllegalArgumentException e) {
                    logger.log(Level.INFO, "Not valid client's email was entered by user" );
                    console.sendResponse(errorsBundle.getString("wrongEmail"));
                } catch (ClassNotFoundException e) {
                    logger.log(Level.SEVERE, "ClassNotFound", e);
                }
            }

            while (true) {
                try {
                    console.consoleResponse("Enter client's city (live it empty if city is unknown):");
                    newClientsCity = validateClientsCity(console.getMessageFromClient());
                    break;
                } catch (IllegalArgumentException e) {
                    logger.log(Level.INFO, "Not valid client's city was entered by user" );
                    console.sendResponse(errorsBundle.getString("wrongClientsCity"));
                } catch (ClassNotFoundException e) {
                    logger.log(Level.SEVERE, "ClassNotFound", e);
                }
            }

            addClient();
        } catch (NumberFormatException e) {
            console.sendResponse("Not valid entry :" + e.getMessage());
            throw new IllegalArgumentException("Not valid entry :" + e.getMessage());

        } catch (ClientExceedsException | IOException e) {
            logger.log(Level.INFO, "Client already exist. Can't add same client!" );
            console.sendResponse(errorsBundle.getString("clientAlreadyExceeds"));
            logger.log(Level.SEVERE, "ClassNotFound", e);
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
