package BankApplication.ui.commands.impl;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

/**
 * Created by Padonag on 17.01.2015.
 */
public class ShowHelpCommand extends AbstractCommand {

    @Override
    public void execute() throws BankApplication.exceptions.IllegalArgumentException {
        ClassLoader classloader = Thread.currentThread().getContextClassLoader();
        InputStream inputStream = classloader.getResourceAsStream("help.txt");
        try(BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream))){
            String currentString;
            while((currentString = bufferedReader.readLine())!=null){
                System.out.println(currentString);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void printCommandInfo() {
        System.out.println(bundle.getString("help"));
    }
}
