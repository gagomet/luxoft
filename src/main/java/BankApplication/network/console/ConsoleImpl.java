package BankApplication.network.console;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

/**
 * Created by Kir Kolesnikov on 21.01.2015.
 */
public class ConsoleImpl implements Console {
    private BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(System.in));

    @Override
    public void consoleResponse(String consoleRequest) throws IOException {
        System.out.println(consoleRequest);
    }

    @Override
    public void sendResponse(String response) {
        System.out.println(response);
    }

    @Override
    public String getMessageFromClient() throws IOException, ClassNotFoundException {
        return bufferedReader.readLine();
    }


}
