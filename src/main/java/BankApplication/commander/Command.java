package BankApplication.commander;

/**
 * Created by Kir Kolesnikov on 15.01.2015.
 */
public interface Command {
    void execute();

    void printCommandInfo();
}
