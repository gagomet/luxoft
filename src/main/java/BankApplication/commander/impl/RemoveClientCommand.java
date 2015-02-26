package BankApplication.commander.impl;

import BankApplication.commander.CommandsManager;
import BankApplication.exceptions.ClientNotFoundException;
import BankApplication.model.impl.Client;
import BankApplication.network.console.Console;
import BankApplication.network.console.ConsoleImpl;
import BankApplication.service.impl.BankServiceImpl;
import BankApplication.service.impl.FullBankService;
import BankApplication.service.impl.ServiceFactory;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Created by Kir Kolesnikov on 21.01.2015.
 */
public class RemoveClientCommand extends AbstractCommand {
    private static final Logger logger = Logger.getLogger(RemoveClientCommand.class.getName());

    public RemoveClientCommand(ConsoleImpl console, FullBankService fullBankService) {
    }

    public RemoveClientCommand(Console console, CommandsManager manager) {
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
                    logger.log(Level.SEVERE, "Class not found in RemoveClient command", e);
                }
            }

            removeClientFromBank(clientName);

        } catch (IOException e) {
            logger.log(Level.SEVERE, "I/0 exception in RemoveClient command", e);
            console.sendResponse(e.getMessage());
        }
    }

    @Override
    public String toString() {
        return "Remove client";
    }

    private void removeClientFromBank(String clientName) {
        try {
            Client client = ServiceFactory.getClientService().getClientByName(BankServiceImpl.getInstance().getCurrentBank(), clientName);
            ServiceFactory.getBankService().removeClient(BankServiceImpl.getInstance().getCurrentBank(), client);
            console.sendResponse(client.toString() + " was removed. Current client set to null");
            getManager().setCurrentClient(null);
        } catch (ClientNotFoundException e) {
            logger.log(Level.SEVERE, errorsBundle.getString("clientNotFound"));
            console.sendResponse(errorsBundle.getString("clientNotFound"));
        }
    }
}
