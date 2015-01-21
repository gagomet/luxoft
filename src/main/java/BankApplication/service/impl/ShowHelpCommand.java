package BankApplication.service.impl;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

/**
 * Created by Padonag on 17.01.2015.
 *
 * File help.txt contains:
 * ***Manual to use Bank Commander interactive interface***

 1) Find the client from client's base of your Bank OR add a new client to it
 this makes the Client Active.
 2) Set an active account of Client by command "Set an active account"
 3) Make some operations with Active Client's account  )))
 4) Exit from the Bank Commander.
 */
public class ShowHelpCommand extends AbstractCommand {

    @Override
    public void execute() throws BankApplication.exceptions.IllegalArgumentException {
        ClassLoader classloader = Thread.currentThread().getContextClassLoader();
        InputStream inputStream = classloader.getResourceAsStream("help.txt");
        try (BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream))) {
            String currentString;
            while ((currentString = bufferedReader.readLine()) != null) {
                System.out.println(currentString);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void printCommandInfo() {
        System.out.println("Show help about system");
    }
}
