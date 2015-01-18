package BankApplication.ui.commands.impl;

import BankApplication.account.impl.AbstractAccount;
import BankApplication.exceptions.*;
import BankApplication.model.client.Client;
import BankApplication.service.BankServiceEnumSingletone;
import BankApplication.ui.commander.BankCommander;

import java.io.IOException;

/**
 * Created by Kir Kolesnikov on 15.01.2015.
 */
public class GetAccountCommand extends AbstractCommand {
    Client currentClient = null;
    Long clientId;
    @Override
    public void execute() {
        currentClient = BankCommander.getCurrentClient();
        if (currentClient == null) {
            System.out.println(bundle.getString("separator"));
            System.out.println(errorsBundle.getString("noActiveClient"));
            System.out.println(bundle.getString("separator"));
        } else {
            if (currentClient.getAccountsList().size() == 1){
                System.out.println(bundle.getString("oneAccount"));
            } else {
                System.out.println(bundle.getString("accountsList"));
                for(AbstractAccount account : currentClient.getAccountsList()){
                    System.out.println(bundle.getString("separator"));
                    account.printReport();
                    System.out.println(bundle.getString("separator"));
                }
                System.out.println(bundle.getString("selectAccount"));

                try {
                    while (true) {
                        try {
                            clientId = validateId(console.consoleResponse(bundle.getString("enterId")));
                            break;

                        } catch (BankApplication.exceptions.IllegalArgumentException e) {
                            System.out.println(errorsBundle.getString("wrongNumber"));
                            continue;
                        }
                    }

                    AbstractAccount account = BankServiceEnumSingletone.getAccountById(currentClient, clientId);
                    BankCommander.currentClient.setActiveAccount(account);
                    account.printReport();
                    System.out.println(bundle.getString("separator"));
                    StringBuilder builder = new StringBuilder();
                    builder.append(bundle.getString("account"));
                    builder.append(" ");
                    builder.append(account.getId());
                    builder.append(" ");
                    builder.append(bundle.getString("active"));
                    System.out.println(builder.toString());
                    System.out.println(bundle.getString("separator"));
                } catch (IOException e) {
                    e.printStackTrace();
                } catch (AccountNotFoundException e) {
                    e.printStackTrace();
                }
            }

        }
    }

    @Override
    public void printCommandInfo() {
        System.out.println(bundle.getString("getAccountCommand"));
    }
}
