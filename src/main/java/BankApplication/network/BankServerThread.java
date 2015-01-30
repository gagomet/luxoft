package BankApplication.network;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Created by Kir Kolesnikov on 30.01.2015.
 */
public class BankServerThread {
    ServerSocket serverSocket;
    ExecutorService pool = Executors.newFixedThreadPool(50);

    public BankServerThread(){
        try {
            serverSocket = new ServerSocket(15000);
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }

    public void runServer(){
        while (true) {
            Socket clientSocket = null;
            try {
                clientSocket = serverSocket.accept();
                pool.execute(new ServerThread(clientSocket));
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
