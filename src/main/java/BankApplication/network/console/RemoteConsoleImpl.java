package BankApplication.network.console;

import BankApplication.network.BankRemoteOffice;

import java.io.IOException;

/**
 * Created by Padonag on 15.01.2015.
 */
public class RemoteConsoleImpl implements Console {
    private BankRemoteOffice bankRemoteOffice;
    private static String PRESS_ENTER = "   Press Enter to continue";

    public RemoteConsoleImpl(BankRemoteOffice bankRemoteOffice) {
        this.bankRemoteOffice = bankRemoteOffice;
    }

    @Override
    public void consoleResponse(String consoleRequest) throws IOException {
        bankRemoteOffice.getOut().writeObject(consoleRequest);
    }

    @Override
    public void sendResponse(String response) {
        response = response + PRESS_ENTER;
        try {
            bankRemoteOffice.getOut().writeObject(response);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public String getMessageFromClient() throws IOException, ClassNotFoundException {
        String message = null;
        message = (String) bankRemoteOffice.getIn().readObject();
        return null;
    }


}
