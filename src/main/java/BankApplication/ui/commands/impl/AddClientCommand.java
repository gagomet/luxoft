package BankApplication.ui.commands.impl;

import BankApplication.exceptions.IllegalArgumentException;
import BankApplication.type.Gender;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

/**
 * Created by Kir Kolesnikov on 15.01.2015.
 */
public class AddClientCommand extends AbstractCommand {
    @Override
    public void execute() throws IllegalArgumentException{
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(System.in));

        try {
            System.out.print(bundle.getString("addClientsName"));
            String clientsName = bufferedReader.readLine();
            System.out.print(bundle.getString("addClientsOverdraft"));
            Float overdraft = Float.parseFloat(bufferedReader.readLine());
            System.out.print(bundle.getString("addClientsSex"));
            Gender sex = validateClientsSex(bufferedReader.readLine());
            System.out.print(bundle.getString("addClientsSex"));
            Gender sex = validateClientsSex(bufferedReader.readLine());
            System.out.print(bundle.getString("addClientsSex"));
            Gender sex = validateClientsSex(bufferedReader.readLine());
            //TODO end this method

        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("Not valid entry :" + e.getMessage());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void printCommandInfo() {
        System.out.println("Add client to Bank System");
    }

    private Gender validateClientsSex(String input) throws IllegalArgumentException {
        if(input.equalsIgnoreCase("M")){
            return Gender.MALE;
        } else if (input.equalsIgnoreCase("F")){
            return Gender.FEMALE;
        } else {
            throw new IllegalArgumentException("Not valid gender");
        }
    }

    private boolean isName(String name){
        return name.matches("[A-Za-z ]+");
    }

    private boolean isEmail(String email) {
        return email.matches(
                "^[A-Za-z\\.-0-9]{2,}@[A-Za-z\\.-0-9]{2,}\\.[A-Za-z]{2,3}$");
    }
}
