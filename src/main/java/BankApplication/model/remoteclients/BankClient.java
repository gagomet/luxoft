package BankApplication.model.remoteclients;

import BankApplication.model.client.Client;
import BankApplication.model.info.BankInfo;
import BankApplication.type.Gender;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.net.UnknownHostException;

/**
 * Created by Padonag on 20.01.2015.
 */
public class BankClient {
    Socket requestSocket;
    ObjectOutputStream out;
    ObjectInputStream in;
    String message;
    BankInfo bankInfo;
    Object plainObject;
    static final String SERVER = "localhost";

    void run() {
        try {
            // 1. creating a socket to connect to the server
            requestSocket = new Socket(SERVER, 20004);
            System.out.println("Connected to localhost in port 20004");
            // 2. get Input and Output streams
            out = new ObjectOutputStream(requestSocket.getOutputStream());
            out.flush();
            in = new ObjectInputStream(requestSocket.getInputStream());
            // 3: Communicating with the server
            System.out.println("To add client use: addclient gender(m|f),name");
            do {
                try {
                    BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(System.in));
                    message = (String) in.readObject();
                    System.out.println("server>" + message);
                    if (message.equals("bankinfo")) {
                        bankInfo = (BankInfo) in.readObject();
                        System.out.println(bankInfo.toString());
                    }
                    message = bufferedReader.readLine();
                    if (message.startsWith("addclient")) {
                        Client client = null;
                        String[] command = message.split(" ");
                        String[] arguments = command[1].split(",");
                        if (arguments[0].equalsIgnoreCase("m")) {
                            client = new Client(Gender.MALE);
                        }
                        if (arguments[0].equalsIgnoreCase("f")) {
                            client = new Client(Gender.FEMALE);
                        }
                        if (client != null) {
                            client.setName(arguments[1]);
                        }
                        sendMessage("client");
                        sendClient(client);

                    }
                    sendMessage(message);
                } catch (ClassNotFoundException classNot) {
                    System.err.println("data received in unknown format");
                } catch (BankApplication.exceptions.IllegalArgumentException e) {
                    e.printStackTrace();
                }
            } while (!message.equals("bye"));
        } catch (UnknownHostException unknownHost) {
            System.err.println("You are trying to connect to an unknown host!");
        } catch (IOException ioException) {
            ioException.printStackTrace();
        } finally {
            // 4: Closing connection
            try {
                in.close();
                out.close();
                requestSocket.close();
            } catch (IOException ioException) {
                ioException.printStackTrace();
            }
        }
    }

    void sendMessage(final String msg) {
        try {
            out.writeObject(msg);
            out.flush();
            System.out.println("client>" + msg);
        } catch (IOException ioException) {
            ioException.printStackTrace();
        }
    }

    void sendClient(final Client client){
        try{
            out.writeObject(client);
            out.flush();
            System.out.println(client.toString() + " was sent");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void main(final String args[]) {
        BankClient client = new BankClient();
        client.run();
    }
}
