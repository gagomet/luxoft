package BankApplication.commander;

import BankApplication.exceptions.*;

/**
 * Created by Kir Kolesnikov on 15.01.2015.
 */
public interface Command {
    void execute() throws IllegalArgumentException;
    void printCommandInfo();
}
