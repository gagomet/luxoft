package BankApplication.network;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Created by Kir Kolesnikov on 30.01.2015.
 */
public class BankServerThreaded {
    private ServerSocket serverSocket;
    private ExecutorService pool = Executors.newFixedThreadPool(50);
    private static AtomicInteger connectedNow = new AtomicInteger();

    public static int getConnectedNow() {
        return connectedNow.get();
    }

    public BankServerThreaded(){
        try {
            serverSocket = new ServerSocket(15555);
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }

    public void runServer(){
        System.out.println("Waiting to connections");
        while (true) {
            Socket clientSocket = null;
            try {
                clientSocket = serverSocket.accept();
                pool.execute(new ServerThread(clientSocket));
                connectedNow.incrementAndGet();
                System.out.println(connectedNow.toString());
            } catch (IOException e) {
                e.printStackTrace();
                pool.shutdown();
            }
        }
    }

    public static void main(String[] args) {
        BankServerThreaded bankServerThreaded = new BankServerThreaded();
        bankServerThreaded.runServer();
    }
}
