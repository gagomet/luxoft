package BankApplication.ui.console.impl;

import java.io.*;

/**
 * Created by Padonag on 15.01.2015.
 */
public class ConsoleImpl implements BankApplication.ui.console.Console {
    private String currentRequest;
    private String currentResponse;
    private static BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(System.in));
    @Override
    public String consoleResponse(String consoleRequest) throws IOException {
        System.out.println(consoleRequest);
        currentRequest = consoleRequest;
        return currentResponse = bufferedReader.readLine();
    }

    @Override
    public String getCurrentRequest() {
        return currentRequest;
    }

    @Override
    public String getCurrentResponse() {
        return currentResponse;
    }


}
