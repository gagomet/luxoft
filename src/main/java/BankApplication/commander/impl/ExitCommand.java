package BankApplication.commander.impl;

/**
 * Created by Kir Kolesnikov on 25.02.2015.
 */
public class ExitCommand extends AbstractCommand {
    @Override
    public void execute() {
        System.exit(0);
    }

    public void printCommandInfo() {
        System.out.println("Exit");
    }
}
