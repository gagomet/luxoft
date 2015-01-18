package BankApplication.ui.console.impl;

import BankApplication.ui.console.IConsole;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

/**
 * Created by Padonag on 15.01.2015.
 */
public class ConsoleImpl implements IConsole {
    private static BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(System.in));
    @Override
    public String consoleResponse(String consoleRequest) throws IOException {
        System.out.println(consoleRequest);
        return bufferedReader.readLine();
    }



}
