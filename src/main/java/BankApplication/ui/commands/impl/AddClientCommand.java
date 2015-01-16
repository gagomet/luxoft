package BankApplication.ui.commands.impl;

import BankApplication.exceptions.ClientExceedsException;
import BankApplication.exceptions.IllegalArgumentException;
import BankApplication.model.client.Client;
import BankApplication.service.BankServiceEnumSingletone;
import BankApplication.type.Gender;
import BankApplication.ui.commander.BankCommander;

import java.io.IOException;
import java.util.List;

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
            newClientsName = validateClientsName(console.consoleResponse(bundle.getString("addClientsName")));
            newClientSex = validateClientsSex(console.consoleResponse(bundle.getString("addClientsSex")));
            newClientOverdraft = validateFloat(console.consoleResponse(bundle.getString("addClientsOverdraft")));
            newClientPhone = validateClientsPhone(console.consoleResponse(bundle.getString("addClientsPhone")));
            newClientEmail = validateClientsEmail(console.consoleResponse(bundle.getString("addClientsEmail")));
            addClient(newClientOverdraft, newClientSex);
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("Not valid entry :" + e.getMessage());
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClientExceedsException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void printCommandInfo() {
        System.out.println(bundle.getString("addClientCommand"));
    }

    private void addClient(Float newClientOverdraft, Gender newClientGender) throws IllegalArgumentException, ClientExceedsException {
        Client newClient = null;
        if (newClientOverdraft == 0.0f) {
            newClient = new Client(newClientGender);
        } else {
            newClient = new Client(newClientOverdraft, newClientGender);
        }
        newClient.setName(newClientsName);
        if (newClientPhone != null) {
            newClient.setPhone(newClientPhone);
        }
        if (newClientEmail != null){
            newClient.setEmail(newClientEmail);
        }

        BankServiceEnumSingletone.addClient(BankCommander.currentBank, newClient);
    }

}
