package BankApplication.network.console;

import BankApplication.network.ServerThread;

import java.io.IOException;

/**
 * Created by Padonag on 01.02.2015.
 */
public class MultithreadServerConsoleImpl implements Console {
    ServerThread serverThread;
    private static String PRESS_ENTER = "   Press Enter to continue";

    public MultithreadServerConsoleImpl(ServerThread serverThread) {
        this.serverThread = serverThread;
    }

    @Override
    public String consoleResponse(String consoleRequest) throws IOException {
        String result = null;
        serverThread.getOut().writeObject(consoleRequest);
        try {
            result = (String) serverThread.getIn().readObject();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        return result;
    }

    @Override
    public void sendResponse(String response) {
        try {
            response = response + PRESS_ENTER;
            serverThread.getOut().writeObject(response);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
