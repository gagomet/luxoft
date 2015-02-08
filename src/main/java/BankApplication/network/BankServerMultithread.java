package BankApplication.network;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Created by Padonag on 08.02.2015.
 */
public class BankServerMultithread implements Runnable {
    protected int serverPort = 15555;
    protected ServerSocket serverSocket = null;
    protected boolean isStopped = false;
    protected Thread runningThread = null;
    protected ExecutorService threadPool = Executors.newFixedThreadPool(10);
    private static AtomicInteger waitForConnection = new AtomicInteger(0);

    public BankServerMultithread(int port) {
        this.serverPort = port;
    }

    public void run() {
        synchronized (this) {
            this.runningThread = Thread.currentThread();
        }
        Thread bankMonitor = new Thread(new BankServerMonitor());
        bankMonitor.setDaemon(true);
        bankMonitor.start();
        openServerSocket();
        System.out.println("Waiting to connections");
        while (!isStopped()) {
            Socket clientSocket = null;
            try {
                clientSocket = this.serverSocket.accept();
                System.out.println("Thread connected from " + clientSocket.getInetAddress().getHostName());
                waitForConnection.incrementAndGet();
            } catch (IOException e) {
                if (isStopped()) {
                    System.out.println("Server Stopped.");
                    break;
                }
                throw new RuntimeException("Error accepting client connection", e);
            }
            threadPool.execute(new ServerThread(clientSocket));
        }
        threadPool.shutdown();
        System.out.println("Server Stopped.");
    }

    public static AtomicInteger getWaitForConnection() {
        return waitForConnection;
    }


    private synchronized boolean isStopped() {
        return this.isStopped;
    }

    public synchronized void stop() {
        this.isStopped = true;
        try {
            this.serverSocket.close();
        } catch (IOException e) {
            throw new RuntimeException("Error closing server", e);
        }
    }

    public static synchronized void threadIsRunning(){
        waitForConnection.decrementAndGet();
    }

    private void openServerSocket() {
        try {
            this.serverSocket = new ServerSocket(this.serverPort);
        } catch (IOException e) {
            throw new RuntimeException("Cannot open port 15555", e);
        }
    }

    public static void main(String[] args) {
        BankServerMultithread bankServerMultithread = new BankServerMultithread(15555);
        Thread serverThread = new Thread(bankServerMultithread);
        serverThread.start();
    }
}
