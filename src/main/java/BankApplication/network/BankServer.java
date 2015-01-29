package BankApplication.network;

import BankApplication.exceptions.*;
import BankApplication.exceptions.IllegalArgumentException;
import BankApplication.model.ClientRegistrationListener;
import BankApplication.model.impl.Bank;
import BankApplication.model.impl.Client;
import BankApplication.service.*;
import BankApplication.service.impl.*;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by Padonag on 20.01.2015.
 */
public class BankServer {
    ServerSocket providerSocket;
    Socket connection = null;
    ObjectOutputStream out;
    ObjectInputStream in;
    String message;

    private Bank currentBank;
    private Client currentClient;
    private BankService bankService = new BankServiceImpl();
    private ClientService clientService = new ClientServiceImpl();
    private AccountService accountService = new AccountServiceImpl();

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
                } catch (IllegalArgumentException e) {
                    e.printStackTrace();
                }
            }
        }
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
                try {
                    StringBuilder builder = new StringBuilder();
                    sendMessage("Enter Client's Name: ");
                    message = receiveMessage();
                    currentClient = clientService.getClientByName(currentBank, message);
                    //get active Client from BankClient

                    builder.append("Press 1 to deposit funds");
                    builder.append(System.getProperty("line.separator"));
                    builder.append("Press 2 to withdraw funds");
                    builder.append(System.getProperty("line.separator"));
                    builder.append("Press 3 to get Client's info");
                    builder.append(System.getProperty("line.separator"));
                    builder.append("Press 0 to exit");
                    sendMessage(builder.toString());
                    message = receiveMessage();

                    if(message.equals("1")){
                        sendMessage("Enter amount to deposit:");
                        message = receiveMessage();
                        accountService.depositeFunds(currentClient.getActiveAccount(), Float.parseFloat(message));
                    } else if (message.equals("2")){
                        sendMessage("Enter amount to withdraw:");
                        message = receiveMessage();
                        accountService.withdrawFunds(currentClient.getActiveAccount(), Float.parseFloat(message));
                    } else if (message.equals("3")){
                        sendMessage(currentClient.toString());
                    } else if(message.equals("0")){
                        sendMessage("0");
                    }

                } catch (ClientNotFoundException | NotEnoughFundsException | IllegalArgumentException e) {
                    e.printStackTrace();
                }
            } while (!message.equals("0"));
        } catch (IOException e) {
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

    public String receiveMessage(){
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


    public static void main(final String args[]) {
        BankServer server = new BankServer();
        server.initialize();
        while (true) {
            server.run();
        }
    }



}
