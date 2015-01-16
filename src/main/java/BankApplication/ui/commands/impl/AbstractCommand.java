package BankApplication.ui.commands.impl;

import BankApplication.exceptions.*;
import BankApplication.exceptions.IllegalArgumentException;
import BankApplication.type.Gender;
import BankApplication.ui.commands.ICommand;
import BankApplication.ui.console.IConsole;
import BankApplication.ui.console.impl.ConsoleImpl;

import java.util.ResourceBundle;

/**
 * Created by Kir Kolesnikov on 15.01.2015.
 */
public abstract class AbstractCommand implements ICommand {
    protected IConsole console = new ConsoleImpl();
    protected ResourceBundle bundle = ResourceBundle.getBundle("strings");
    protected ResourceBundle errorsBundle = ResourceBundle.getBundle("errors");
    protected final String EMPTY_STRING = "";

    protected Gender validateClientsSex(String input) throws BankApplication.exceptions.IllegalArgumentException {
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
            if (EMPTY_STRING.equals(input)) {
                return null;
            }
            return input;
        } else {
            throw new IllegalArgumentException("Not valid name");
        }
    }

    protected String validateClientsEmail(String input) throws IllegalArgumentException {
        if (isEmail(input)) {
            if (EMPTY_STRING.equals(input)) {
                return null;
            }
            return input;
        } else {
            throw new IllegalArgumentException("Not valid email");
        }
    }

    protected String validateClientsPhone(String input) throws IllegalArgumentException {
        if (isPhone(input)) {
            if (EMPTY_STRING.equals(input)) {
                return null;
            }
            return input;
        } else {
            throw new IllegalArgumentException("Not valid phone");
        }
    }

    protected float validateFloat(String input) throws NumberFormatException {
        return Float.parseFloat(input);
    }

    protected long validateLong(String input) throws NumberFormatException {
        return Long.parseLong(input);
    }

//    protected boolean is

    protected boolean isName(String name) {
        return name.matches("[A-Za-z ]+");
    }

    protected boolean isPhone(String phone) {
        return phone.matches("[0-9]+");
    }

    protected boolean isEmail(String email) {
        return email.matches(
                "^[A-Za-z\\.-0-9]{2,}@[A-Za-z\\.-0-9]{2,}\\.[A-Za-z]{2,3}$");
    }
}
