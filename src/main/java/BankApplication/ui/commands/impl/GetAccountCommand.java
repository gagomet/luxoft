package BankApplication.ui.commands.impl;

import BankApplication.ui.commands.ICommand;

/**
 * Created by Kir Kolesnikov on 15.01.2015.
 */
public class GetAccountCommand implements ICommand {
    @Override
    public void execute() {

    }

    @Override
    public void printCommandInfo() {
        System.out.println("Get an accounts of the client");
    }
}
