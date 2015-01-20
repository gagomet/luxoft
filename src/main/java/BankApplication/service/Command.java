package BankApplication.service;

import BankApplication.exceptions.*;

/**
 * Created by Kir Kolesnikov on 15.01.2015.
 */
public interface Command {
    void execute() throws BankApplication.exceptions.IllegalArgumentException;

    void printCommandInfo();
}
