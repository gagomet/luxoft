package BankApplication.network;


import BankApplication.DAO.impl.DAOFactory;
import BankApplication.commander.Command;
import BankApplication.commander.CommandsManager;
import BankApplication.commander.impl.AddClientCommand;
import BankApplication.commander.impl.DepositCommand;
import BankApplication.commander.impl.FindClientCommand;
import BankApplication.commander.impl.GetAccountCommand;
import BankApplication.commander.impl.RemoveClientCommand;
import BankApplication.commander.impl.ReportCommand;
import BankApplication.commander.impl.ShowHelpCommand;
import BankApplication.commander.impl.TransferCommand;
import BankApplication.commander.impl.WithdrawCommand;
import BankApplication.model.impl.Bank;
import BankApplication.model.impl.Client;
import BankApplication.network.console.Console;
import BankApplication.network.console.RemoteConsoleImpl;
import BankApplication.service.impl.ServiceFactory;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Iterator;
import java.util.Map;
import java.util.TreeMap;

/**
 * Created by Kir Kolesnikov on 20.01.2015.
 */
public class BankRemoteOffice implements CommandsManager {
    private ServerSocket providerSocket;
    private Socket clientSocket = null;
    private ObjectOutputStream out;
    private ObjectInputStream in;
    private String message;
    private Console console = new RemoteConsoleImpl(this);
    private Client currentClient;

    private Map<String, Command> commandsMap = new TreeMap<>();
    private static final String FEED_FILES_FOLDER = "c:\\!toBankApplication\\";
    //    private static String bankName = "MyBank";
    static String bankName = "MYBANK";

    public void initialize() {

        //init commands with remote console
        commandsMap.put("1", new FindClientCommand(console, this));
        commandsMap.put("2", new AddClientCommand(console, this));
        commandsMap.put("3", new RemoveClientCommand(console, this));
        commandsMap.put("4", new WithdrawCommand(console, this));
        commandsMap.put("5", new DepositCommand(console, this));
        commandsMap.put("6", new GetAccountCommand(console, this));
        commandsMap.put("7", new TransferCommand(console, this));
        commandsMap.put("8", new ReportCommand(console, this));
        commandsMap.put("9", new ShowHelpCommand(console, this));
    }

    void run() {
        try {
            providerSocket = new ServerSocket(15555, 10);
            System.out.println("Waiting for clientSocket");
            clientSocket = providerSocket.accept();
            System.out.println("Connection received from " + clientSocket.getInetAddress().getHostName());
            out = new ObjectOutputStream(clientSocket.getOutputStream());
            out.flush();
            in = new ObjectInputStream(clientSocket.getInputStream());
            console.consoleResponse(composeUserMenu());
            do {
                message = console.getMessageFromClient();
                if (commandsMap.containsKey(message)) {
                    Command cmd = commandsMap.get(message);
                    cmd.execute();
                    console.consoleResponse(composeUserMenu());
                } else if (message.equals("0")) {
                    sendMessage("0");
                }
                System.out.println("Client> " + message);


            } while (!message.equals("exit"));

        } catch (IOException | IllegalArgumentException | ClassNotFoundException e) {
            e.printStackTrace();
        } finally {
            try {
                in.close();
                out.close();
                providerSocket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public ObjectOutputStream getOut() {
        return out;
    }

    public ObjectInputStream getIn() {
        return in;
    }

    public String receiveMessage() {
        String result = null;
        try {
            result = (String) in.readObject();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            System.err.println("Data received in unknown format");
        }
        return result;
    }

    void sendMessage(final String msg) {
        try {
            out.writeObject(msg);
            out.flush();
            System.out.println("server>" + msg);
        } catch (IOException ioException) {
            ioException.printStackTrace();
        }
    }

    public static void main(String[] args) {
        BankRemoteOffice remoteOffice = new BankRemoteOffice();
        Bank bank = DAOFactory.getBankDAO().getBankByName(bankName);
        ServiceFactory.getBankService().setCurrentBank(bank);
        remoteOffice.initialize();
        remoteOffice.run();
    }

    private String composeUserMenu() {

        StringBuilder builder = new StringBuilder();
        Iterator iterator = commandsMap.entrySet().iterator();
        builder.append(System.getProperty("line.separator"));
        builder.append("Active client now is: ");
        if (currentClient == null) {
            builder.append("N/A");
        } else {
            builder.append(currentClient.toString());
        }
        builder.append(System.getProperty("line.separator"));
        while (iterator.hasNext()) {
            Map.Entry<String, Command> entry = (Map.Entry<String, Command>) iterator.next();
            builder.append(entry.getKey());
            builder.append("      -->    ");
            builder.append(entry.getValue().toString());
            builder.append(System.getProperty("line.separator"));
        }
        builder.append(System.getProperty("line.separator"));
        builder.append("exit      -->    Exit");
        return builder.toString();
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
