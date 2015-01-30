package BankApplication.commander.impl;

import BankApplication.model.impl.BankInfo;
import BankApplication.network.console.Console;
import BankApplication.service.impl.BankServiceImpl;

/**
 * Created by Kir Kolesnikov on 29.01.2015.
 */
public class ReportCommand extends AbstractCommand {

    public ReportCommand() {
    }

    public ReportCommand(Console console) {
        this.console = console;
    }

    @Override
    public void execute() throws BankApplication.exceptions.IllegalArgumentException {
        BankInfo bankInfo = BankServiceImpl.getInstance().getBankInfo(BankServiceImpl.getInstance().getCurrentBank());
        console.sendResponse(bankInfo.toString());
    }

    @Override
    public String toString() {
        return "Show current Bank report";
    }
}
