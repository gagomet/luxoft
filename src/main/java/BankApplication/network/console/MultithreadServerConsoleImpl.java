package BankApplication.network.console;

import BankApplication.network.ServerThread;

import java.io.IOException;

/**
 * Created by Padonag on 01.02.2015.
 */
public class MultithreadServerConsoleImpl implements Console {
    private ServerThread serverThread;
    private static String PRESS_ENTER = "   Press Enter to continue";

    public MultithreadServerConsoleImpl(ServerThread serverThread) {
        this.serverThread = serverThread;
    }

    @Override
    public void consoleResponse(String consoleRequest) throws IOException {
        serverThread.getOut().writeObject(consoleRequest);
    }

    @Override
    public void sendResponse(String response) {
        response = response + PRESS_ENTER;
        try {
            serverThread.getOut().writeObject(response);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public String getMessageFromClient() throws IOException, ClassNotFoundException {
        String message = null;
        message = (String) serverThread.getIn().readObject();
        return message;
    }

}
