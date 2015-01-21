package BankApplication.network.console;

import BankApplication.network.BankRemoteOffice;

import java.io.*;

/**
 * Created by Padonag on 15.01.2015.
 */
public class RemoteConsoleImpl implements Console {
    BankRemoteOffice bankRemoteOffice;
    private static String EMPTY_STRING = "";
    public RemoteConsoleImpl(BankRemoteOffice bankRemoteOffice) {
        this.bankRemoteOffice = bankRemoteOffice;
    }

    @Override
    public String consoleResponse(String consoleRequest) throws IOException {
        String result = null;
        bankRemoteOffice.getOut().writeObject(consoleRequest);
        try {
            result = (String)bankRemoteOffice.getIn().readObject();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        return result;
    }

    public void sendResponse(String response){
        try {
           bankRemoteOffice.getOut().writeObject(response);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
