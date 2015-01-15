package BankApplication.ui.commands.impl;

import BankApplication.ui.commands.ICommand;

/**
 * Created by Kir Kolesnikov on 15.01.2015.
 */
public class DepositCommand implements ICommand {
    @Override
    public void execute() {

    }

    @Override
    public void printCommandInfo() {
        System.out.println("Deposit funds to account");
    }
}
