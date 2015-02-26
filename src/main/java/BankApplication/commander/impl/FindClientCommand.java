package BankApplication.commander.impl;

import BankApplication.commander.CommandsManager;
import BankApplication.exceptions.ClientNotFoundException;
import BankApplication.model.impl.Client;
import BankApplication.network.console.Console;
import BankApplication.network.console.ConsoleImpl;
import BankApplication.service.impl.ClientServiceImpl;
import BankApplication.service.impl.FullBankService;
import BankApplication.service.impl.ServiceFactory;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Created by Kir Kolesnikov on 15.01.2015.
 */
public class FindClientCommand extends AbstractCommand {

    private static final Logger logger = Logger.getLogger(FindClientCommand.class.getName());

    public FindClientCommand(ConsoleImpl console, FullBankService fullBankService) {

    }

    public FindClientCommand(Console console, CommandsManager manager) {
        this.console = console;
        setManager(manager);
    }


    @Override
    public void execute() {
        String clientName;
        try {
            while (true) {
                try {
                    console.consoleResponse("Enter client's name: ");
                    clientName = validateClientsName(console.getMessageFromClient());
                    break;
                } catch (IllegalArgumentException e) {
                    logger.log(Level.INFO, errorsBundle.getString("wrongClientsName"), e);
                    console.sendResponse(errorsBundle.getString("wrongClientsName"));
                } catch (ClassNotFoundException e) {
                    e.printStackTrace();
                }
            }
            findClientInBank(clientName);
        } catch (IOException e) {
            logger.log(Level.SEVERE, "IO exception", e);
            console.sendResponse(e.getMessage());
        }


    }

    @Override
    public String toString() {
        return "Find client by name (also makes found Client active)";
    }

    private void findClientInBank(String clientName) {
        try {
            Client client = ClientServiceImpl.getInstance().getClientByName(ServiceFactory.getBankService().getCurrentBank(), clientName);
            StringBuilder builder = new StringBuilder();
            builder.append("Client ");
            builder.append(client.getName());
            builder.append(" is active now.");
            getManager().setCurrentClient(client);
            builder.append(System.getProperty("line.separator"));
            System.out.println(builder.toString());
            console.sendResponse(client.toString() + builder.toString());
            client.printReport();
        } catch (ClientNotFoundException e) {
            logger.log(Level.INFO, errorsBundle.getString("clientNotFound"));
            console.sendResponse(errorsBundle.getString("clientNotFound"));
        }
    }
}
