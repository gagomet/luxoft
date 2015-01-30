package BankApplication.commander.impl;

import BankApplication.commander.impl.AbstractCommand;
import BankApplication.network.console.Console;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

/**
 * Created by Padonag on 17.01.2015.
 * <p/>
 * File help.txt contains:
 * ***Manual to use Bank Commander interactive interface***
 * <p/>
 * 1) Find the client from client's base of your Bank OR add a new client to it
 * this makes the Client Active.
 * 2) Set an active account of Client by command "Set an active account"
 * 3) Make some operations with Active Client's account  )))
 * 4) Exit from the Bank Commander.
 */
public class ShowHelpCommand extends AbstractCommand {

    public ShowHelpCommand() {
    }

    public ShowHelpCommand(Console console) {
        this.console = console;
    }

    @Override
    public void execute() throws BankApplication.exceptions.IllegalArgumentException {
        ClassLoader classloader = Thread.currentThread().getContextClassLoader();
        InputStream inputStream = classloader.getResourceAsStream("help.txt");
        StringBuilder builder = new StringBuilder();
        try (BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream))) {
            while ((bufferedReader.readLine()) != null) {
                builder.append(bufferedReader.readLine());
            }
        } catch (IOException e) {
            e.printStackTrace();
            console.sendResponse(e.getMessage());
        }
        console.sendResponse(builder.toString());
    }

    @Override
    public String toString() {
        return "Show help about system";
    }
}
