package BankApplication.commander.impl;

import BankApplication.network.console.ConsoleImpl;

/**
 * Created by Kir Kolesnikov on 25.02.2015.
 */
public class ExitCommand extends AbstractCommand {

    public ExitCommand() {
    }

    @Override
    public void execute() {
        System.exit(0);
    }

    public String toString() {
        return "Exit system";
    }
}
