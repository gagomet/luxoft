package BankApplication.network;


import BankApplication.model.ClientRegistrationListener;
import BankApplication.model.impl.Bank;
import BankApplication.model.impl.Client;
import BankApplication.network.console.Console;
import BankApplication.network.console.RemoteConsoleImpl;
import BankApplication.service.BankFeedService;
import BankApplication.service.Command;
import BankApplication.service.impl.AddClientCommand;
import BankApplication.service.impl.BankFeedServiceImpl;
import BankApplication.service.impl.FindClientCommand;
import BankApplication.service.impl.RemoveClientCommand;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.*;

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
                if(commandsMap.containsKey(message)){
                   Command cmd = commandsMap.get(message);
                   cmd.execute();
                } else if(message.equals("info")){
                    //TODO add object instanciate and send it to client
                }
                System.out.println("Client> " + message);


            } while (!message.equals("0"));
        } catch (IOException e) {
            e.printStackTrace();
        } catch (BankApplication.exceptions.IllegalArgumentException e) {
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

    public static void main(String[] args) {
        BankRemoteOffice remoteOffice = new BankRemoteOffice();
        remoteOffice.initialize();
        remoteOffice.run();
    }

    private String composeUserMenu(){
        StringBuilder builder = new StringBuilder();
        Iterator iterator = commandsMap.entrySet().iterator();
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
