package BankApplication.network;

import BankApplication.model.impl.BankInfo;

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
    BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(System.in));
    static final String SERVER = "localhost";

    void run() {
        try {
            // 1. creating a socket to connect to the server
            requestSocket = new Socket(SERVER, 15555);
            System.out.println("Connected to localhost in port 15555");
            // 2. get Input and Output streams
            out = new ObjectOutputStream(requestSocket.getOutputStream());
            out.flush();
            in = new ObjectInputStream(requestSocket.getInputStream());
            // 3: Communicating with the server
            do {
                message = in.readObject().toString();
                System.out.println("Server> " + message);
                if (!message.equals("0")) {
                    String command = bufferedReader.readLine();
                    out.writeObject(command);
                }

            } while (!message.equals("0"));
        } catch (UnknownHostException unknownHost) {
            System.err.println("You are trying to connect to an unknown host!");
        } catch (IOException | ClassNotFoundException ioException) {
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


    public ObjectOutputStream getOut() {
        return out;
    }

    public ObjectInputStream getIn() {
        return in;
    }

    public static void main(final String args[]) {
        BankClient client = new BankClient();
        client.run();
    }
}
