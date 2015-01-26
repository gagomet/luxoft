package BankApplication.network;


import BankApplication.model.ClientRegistrationListener;
import BankApplication.model.impl.Bank;
import BankApplication.model.impl.BankInfo;
import BankApplication.model.impl.Client;
import BankApplication.network.console.Console;
import BankApplication.network.console.RemoteConsoleImpl;
import BankApplication.service.BankFeedService;
import BankApplication.service.Command;
import BankApplication.service.impl.AddClientCommand;
import BankApplication.service.impl.BankFeedServiceImpl;
import BankApplication.service.impl.DepositCommand;
import BankApplication.service.impl.FindClientCommand;
import BankApplication.service.impl.GetAccountCommand;
import BankApplication.service.impl.RemoveClientCommand;
import BankApplication.service.impl.ShowHelpCommand;
import BankApplication.service.impl.TransferCommand;
import BankApplication.service.impl.WithdrawCommand;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

/**
 * Created by Kir Kolesnikov on 20.01.2015.
 */
public class BankRemoteOffice {
    ServerSocket providerSocket;
    Socket connection = null;
    ObjectOutputStream out;
    ObjectInputStream in;
    String message;
    Console console = new RemoteConsoleImpl(this);

    //TODO implement it

    private static Bank currentBank;
    private static Client currentClient;
    private Map<String, Command> commandsMap = new TreeMap<>();
    private static final String FEED_FILES_FOLDER = "c:\\!toBankApplication\\";

    public void initialize() {
        Bank.PrintClientListener printListener = new Bank.PrintClientListener();
        Bank.EmailClientListener emailListener = new Bank.EmailClientListener();
        List<ClientRegistrationListener> listenersList = new ArrayList<ClientRegistrationListener>();
        listenersList.add(printListener);
        listenersList.add(emailListener);
        currentBank = new Bank(listenersList);

        BankFeedService feedService = new BankFeedServiceImpl();
        List<String[]> feeds = feedService.loadFeeds(FEED_FILES_FOLDER);
        for (String[] fileFeed : feeds) {
            for (String tempFeedLine : fileFeed) {
                Map<String, String> feedMap = feedService.parseFeed(tempFeedLine);
                try {
                    Client tempClient = currentBank.parseFeed(feedMap);
                    currentBank.addClient(tempClient);
                } catch (BankApplication.exceptions.IllegalArgumentException e) {
                    e.printStackTrace();
                }
            }
        }

        //init commands with remote console
        commandsMap.put("1", new FindClientCommand(console));
        commandsMap.put("2", new AddClientCommand(console));
        commandsMap.put("3", new RemoveClientCommand(console));
        commandsMap.put("4", new WithdrawCommand(console));
        commandsMap.put("5", new DepositCommand(console));
        commandsMap.put("6", new GetAccountCommand(console));
        commandsMap.put("7", new TransferCommand(console));
        commandsMap.put("8", new ShowHelpCommand(console));
    }


    void run() {
        try {
            providerSocket = new ServerSocket(20004, 10);
            System.out.println("Waiting for connection");
            connection = providerSocket.accept();
            System.out.println("Connection received from " + connection.getInetAddress().getHostName());
            out = new ObjectOutputStream(connection.getOutputStream());
            out.flush();
            in = new ObjectInputStream(connection.getInputStream());
            do {
                message = console.consoleResponse(composeUserMenu());
                if (commandsMap.containsKey(message)) {
                    Command cmd = commandsMap.get(message);
                    cmd.execute();
                } else if (message.equals("info")) {
                    BankInfo bankInfo = new BankInfo(currentBank);
                    out.writeObject(bankInfo);
                    //TODO add object instantiate and send it to client
                } else if (message.equals("0")) {
                    sendMessage("0");
                }
                System.out.println("Client> " + message);


            } while (!message.equals("0"));

        } catch (IOException | BankApplication.exceptions.IllegalArgumentException e) {
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

    public static Client getCurrentClient() {
        return currentClient;
    }

    public static void setCurrentClient(Client currentClient) {
        BankRemoteOffice.currentClient = currentClient;
    }

    public static Bank getCurrentBank() {
        return currentBank;
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
        remoteOffice.initialize();
        remoteOffice.run();
    }

    private String composeUserMenu() {

        //TODO fix composeMenu to toString() method of commands
        StringBuilder builder = new StringBuilder();
        Iterator iterator = commandsMap.entrySet().iterator();
        builder.append(System.getProperty("line.separator"));
        builder.append("Active client now is: ");
        if (getCurrentClient() == null) {
            builder.append("N/A");
        } else {
            builder.append(getCurrentClient().toString());
        }
        builder.append(System.getProperty("line.separator"));
        while (iterator.hasNext()) {
            Map.Entry<String, Command> entry = (Map.Entry<String, Command>) iterator.next();
            builder.append(entry.getKey());
            builder.append("      -->    ");
            builder.append(entry.getValue().getClass().getName());
            builder.append(System.getProperty("line.separator"));
        }
        builder.append("info   -->    Get bank info");
        builder.append(System.getProperty("line.separator"));
        builder.append("0      -->    Exit");
        return builder.toString();
    }
}
