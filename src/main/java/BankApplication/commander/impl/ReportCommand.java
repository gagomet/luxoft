package BankApplication.commander.impl;

import BankApplication.exceptions.*;
import BankApplication.main.BankCommander;
import BankApplication.model.impl.BankInfo;
import BankApplication.network.console.Console;

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
        BankInfo bankInfo = getBankService().getBankInfo(BankCommander.currentBank);
        console.sendResponse(bankInfo.toString());
    }

    @Override
    public String toString() {
        return "Show current Bank report";
    }
}
