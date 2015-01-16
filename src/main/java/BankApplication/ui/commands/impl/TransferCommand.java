package BankApplication.ui.commands.impl;

import BankApplication.ui.commands.ICommand;

/**
 * Created by Kir Kolesnikov on 15.01.2015.
 */
public class TransferCommand extends AbstractCommand{
    @Override
    public void execute() {

    }

    @Override
    public void printCommandInfo() {
        System.out.println(bundle.getString("transferCommand"));
    }
}
