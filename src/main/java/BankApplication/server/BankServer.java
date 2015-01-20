package BankApplication.server;

import BankApplication.BankApplication;
import BankApplication.exceptions.ClientExceedsException;
import BankApplication.exceptions.ClientNotFoundException;
import BankApplication.exceptions.NotEnoughFundsException;
import BankApplication.model.Bank;
import BankApplication.model.client.Client;
import BankApplication.model.info.BankInfo;
import BankApplication.service.bankservice.BankServiceEnumSingletone;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * Created by Padonag on 20.01.2015.
 */
public class BankServer {
    ServerSocket providerSocket;
    Socket connection = null;
    ObjectOutputStream out;
    ObjectInputStream in;
    String message;


    public static Bank currentBank = new Bank();
    public static Client currentClient = null;
    public static BankApplication bankApplication = new BankApplication();

    void run() {
        //initialization and retrieving of Bank class instance
        bankApplication.initialize();
        currentBank = bankApplication.getBank();

        try {
            providerSocket = new ServerSocket(2004, 10);
            System.out.println("Waiting for connection");
            connection = providerSocket.accept();
            System.out.println("Connection received from " + connection.getInetAddress().getHostName());
            out = new ObjectOutputStream(connection.getOutputStream());
            out.flush();
            in = new ObjectInputStream(connection.getInputStream());
            sendMessage("Connection successful");
            do {
                try {

                    message = (String) in.readObject();
                    System.out.println("client>" + message);

                    if (message.startsWith("balance")) {
                        sendMessage(getBalance(message));
                    }
                    if (message.startsWith("withdraw")) {
                        sendMessage(withdrawFunds(message));
                    }
                    if (message.equals("getinfo")) {
                        sendMessage("bankinfo");
                        sendBankInfo(new BankInfo(currentBank));
                    }
                    if (message.startsWith("removeclient")) {
                        sendMessage(removeClient(message));
                    }
                    if(message.equals("client")){
                        currentClient = (Client) in.readObject();
                        BankServiceEnumSingletone.addClient(currentBank, currentClient);
                        sendMessage(addedClientMessage());
                    }
                    if (message.equals("bye"))
                        sendMessage("bye");
                } catch (ClassNotFoundException classnot) {
                    System.err.println("Data received in unknown format");
                } catch (ClientExceedsException e) {
                    e.printStackTrace();
                }
            } while (!message.equals("bye"));
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

    void sendMessage(final String msg) {
        try {
            out.writeObject(msg);
            out.flush();
            System.out.println("server>" + msg);
        } catch (IOException ioException) {
            ioException.printStackTrace();
        }
    }

    void sendBankInfo(final BankInfo bankInfo) {
        try {
            out.writeObject(bankInfo);
            out.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void main(final String args[]) {
        BankServer server = new BankServer();
        while (true) {
            server.run();
        }
    }

    private String getBalance(String message) {
        String[] command = message.split(" ");
        String clientsName = null;
        StringBuilder builder = new StringBuilder();
        if (command.length == 2) {
            clientsName = command[1];
            Client client = null;
            try {
                client = BankServiceEnumSingletone.getClientByName(currentBank, clientsName);
                builder.append("Client ");
                builder.append(client.getName());
                builder.append(" balance is ");
                builder.append(client.getBalance());
            } catch (ClientNotFoundException e) {
                e.printStackTrace();
            }
            currentClient = client;
            System.out.println("Client " + currentClient.getName() + " active now");
        }
        return builder.toString();
    }

    private String withdrawFunds(String message) {
        String[] command = message.split(" ");
        Float withdrawAmount = null;
        StringBuilder builder = new StringBuilder();
        if (command.length == 2) {
            withdrawAmount = Float.parseFloat(command[1]);
        }
        if (currentClient != null) {
            try {
                BankServiceEnumSingletone.withdrawFunds(currentClient.getActiveAccount(), withdrawAmount);
                builder.append(withdrawAmount);
                builder.append(" money has been successfully withdrawed from ");
                builder.append(currentClient.getName());
                builder.append(" account ID: ");
                builder.append(currentClient.getActiveAccount().getId());
                builder.append("  new balance = ");
                builder.append(currentClient.getActiveAccount().getBalance());
            } catch (NotEnoughFundsException e) {
                e.printStackTrace();
            }
        }
        return builder.toString();
    }

    private String removeClient(String message) {
        String[] command = message.split(" ");
        String clientsName = null;
        StringBuilder builder = new StringBuilder();
        if (command.length == 2){
            clientsName = command[1];
            try {
                Client clientToRemove = BankServiceEnumSingletone.getClientByName(currentBank, clientsName);
                BankServiceEnumSingletone.removeClient(currentBank, clientToRemove);
                builder.append("Client ");
                builder.append(clientToRemove.getName());
                builder.append(" has been successfully removed from Bank");
            } catch (ClientNotFoundException e) {
                e.printStackTrace();
            }
        }
        return builder.toString();
    }

    private String addedClientMessage(){
        StringBuilder builder = new StringBuilder();
        builder.append("Client ");
        builder.append(currentClient.getName());
        builder.append(" successully added");
        builder.append(currentBank.getClientsList().toString());
        return builder.toString();
    }


}
