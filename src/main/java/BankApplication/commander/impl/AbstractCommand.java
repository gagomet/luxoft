package BankApplication.commander.impl;

import BankApplication.exceptions.IllegalArgumentException;
import BankApplication.network.console.ConsoleImpl;
import BankApplication.type.Gender;
import BankApplication.commander.Command;
import BankApplication.network.console.Console;

import java.util.ResourceBundle;

/**
 * Created by Kir Kolesnikov on 15.01.2015.
 */
public abstract class AbstractCommand implements Command {
    protected Console console;
    protected ResourceBundle errorsBundle = ResourceBundle.getBundle("errors");
    private final String EMPTY_STRING = "";

    public void printCommandInfo(){
        System.out.println(toString());
    }

    public AbstractCommand() {
        this.console = new ConsoleImpl();
    }

    protected Gender validateClientsSex(String input) throws IllegalArgumentException {
        if (input.equalsIgnoreCase("M")) {
            return Gender.MALE;
        } else if (input.equalsIgnoreCase("F")) {
            return Gender.FEMALE;
        } else {
            throw new IllegalArgumentException("Not valid gender");
        }
    }

    protected String validateClientsName(String input) throws IllegalArgumentException {
        if (isName(input)) {
            return input;
        } else {
            throw new IllegalArgumentException("Not valid name");
        }
    }

    protected String validateClientsCity(String input) throws IllegalArgumentException {
        if (isName(input)) {
            return input;
        } else if (EMPTY_STRING.equals(input)) {
            return null;
        } else {
            throw new IllegalArgumentException("Not valid city");
        }
    }

    protected String validateClientsEmail(String input) throws IllegalArgumentException {
        if (isEmail(input)) {
            return input;
        } else if (EMPTY_STRING.equals(input)) {
            return null;
        } else {
            throw new IllegalArgumentException("Not valid email");
        }
    }


    protected String validateClientsPhone(String input) throws IllegalArgumentException {
        if (isPhone(input)) {
            return input;
        } else if (EMPTY_STRING.equals(input)) {
            return null;
        } else {
            throw new IllegalArgumentException("Not valid phone");
        }
    }

    protected Float validateFunds(String input) throws IllegalArgumentException {
        if (isFunds(input)) {
            Float result = Float.parseFloat(input);
            if (result > 0) {
                return result;
            } else {
                throw new IllegalArgumentException("Not valid funds");
            }
        } else if (EMPTY_STRING.equals(input)) {
            return null;
        } else {
            throw new IllegalArgumentException("Not valid funds");
        }
    }

    protected Long validateId(String input) throws IllegalArgumentException {
        if (isId(input)) {
            Long result = Long.parseLong(input);
            if (result > 0) {
                return result;
            } else {
                throw new IllegalArgumentException("Not valid funds");
            }
        } else {
            throw new IllegalArgumentException("Not valid funds");
        }
    }


    protected boolean isId(String id) {
        return id.matches("[0-9]");
    }

    protected boolean isName(String name) {
        return name.matches("[A-Za-z ]+");
    }

    protected boolean isPhone(String phone) {
        return phone.matches("[+0-9]{4,15}");
    }

    protected boolean isEmail(String email) {
        return email.matches(
                "^[A-Za-z\\.-0-9]{2,}@[A-Za-z\\.-0-9]{2,}\\.[A-Za-z]{2,3}$");
    }

    protected boolean isFunds(String funds) {
        return funds.matches("^[0-9]{1,7}([,.][0-9]{1,2})?$");
    }
}
