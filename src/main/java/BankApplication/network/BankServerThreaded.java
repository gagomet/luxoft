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
    private static final int POOL_SIZE = 10;
    private ServerSocket serverSocket;
    private ExecutorService pool = Executors.newFixedThreadPool(POOL_SIZE);
    private static AtomicInteger waitForConnection = new AtomicInteger();

    public static AtomicInteger getWaitForConnection() {
        return waitForConnection;
    }

    public BankServerThreaded(){
        try {
            serverSocket = new ServerSocket(15555);
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }

    public void runServer(){
        Thread bankMonitor = new Thread(new BankServerMonitor());
        bankMonitor.setDaemon(true);
        bankMonitor.start();
        System.out.println("Waiting to connections");
        while (true) {
            Socket clientSocket = null;
            try {
                clientSocket = serverSocket.accept();
                pool.execute(new ServerThread(clientSocket));
                System.out.println(waitForConnection.toString());
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
