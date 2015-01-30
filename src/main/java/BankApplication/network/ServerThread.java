package BankApplication.network;

import java.net.Socket;

/**
 * Created by Kir Kolesnikov on 30.01.2015.
 */
public class ServerThread implements Runnable {
    private Socket socket;

    public ServerThread(Socket socket) {
        this.socket = socket;
    }

    @Override
    public void run() {



    }
}
