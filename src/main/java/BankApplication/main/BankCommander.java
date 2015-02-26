package BankApplication.main;

import BankApplication.commander.BankHolder;
import BankApplication.commander.Command;
import BankApplication.commander.CommandsManager;
import BankApplication.model.impl.Bank;
import BankApplication.model.impl.Client;
import BankApplication.service.AccountService;
import BankApplication.service.BankService;
import BankApplication.service.ClientService;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Map;

/**
 * Created by Kir Kolesnikov on 15.01.2015.
 */

public class BankCommander implements CommandsManager {
    private Map<Integer, Command> commandsMap;
    private Client currentClient;
    private BankHolder holder;
    private BankService bankService;
    private ClientService clientService;
    private AccountService accountService;

            private static final String BANK_NAME = "MYBANK"; //office
//    private static final String BANK_NAME = "MyBank";

    public static void main(String[] args) {
        ApplicationContext context =
                new ClassPathXmlApplicationContext("application-context.xml");
        BankCommander bankCommander = context.getBean(BankCommander.class);
        Bank bank = bankCommander.getBankService().getBankByName(BANK_NAME);
        bankCommander.getHolder().setBank(bank);

        while (true) {
            for (Integer i = 1; i <= bankCommander.getCommandsMap().size(); i++) {
                System.out.print((i) + ") ");
                bankCommander.getCommandsMap().get(i).printCommandInfo();
            }
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(System.in));
            try {
                System.out.println("Active client now is: ");
                if (bankCommander.getCurrentClient() == null) {
                    System.out.println("N/A");
                } else {
                    System.out.println(bankCommander.getCurrentClient().getName());
                }
                System.out.println("Enter number of your choice: ");

                String commandString = bufferedReader.readLine();
                Integer currentKey = Integer.parseInt(commandString);
                if (bankCommander.getCommandsMap().containsKey(currentKey)) {
                    bankCommander.getCommandsMap().get(currentKey).execute();
                } else {
                    System.out.println("It's not a command! Try again!");
                }
            } catch (IOException e) {
                System.out.println(e.getMessage());
            }
        }
    }

    public Map<Integer, Command> getCommandsMap() {
        return commandsMap;
    }

    public void setCommandsMap(Map<Integer, Command> commandsMap) {
        this.commandsMap = commandsMap;
    }

    public BankHolder getHolder() {
        return holder;
    }

    public void setHolder(BankHolder holder) {
        this.holder = holder;
    }

    public BankService getBankService() {
        return bankService;
    }

    public void setBankService(BankService bankService) {
        this.bankService = bankService;
    }

    public ClientService getClientService() {
        return clientService;
    }

    public void setClientService(ClientService clientService) {
        this.clientService = clientService;
    }

    public AccountService getAccountService() {
        return accountService;
    }

    public void setAccountService(AccountService accountService) {
        this.accountService = accountService;
    }

    @Override
    public Client getCurrentClient() {
        return currentClient;
    }

    @Override
    public void setCurrentClient(Client client) {
         currentClient = client;
    }
}
