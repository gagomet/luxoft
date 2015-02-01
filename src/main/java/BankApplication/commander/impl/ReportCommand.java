package BankApplication.commander.impl;

import BankApplication.model.impl.BankInfo;
import BankApplication.network.console.Console;
import BankApplication.service.impl.BankServiceImpl;
import BankApplication.service.impl.ServiceFactory;

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
        BankInfo bankInfo = ServiceFactory.getBankService().getBankInfo(ServiceFactory.getBankService().getCurrentBank());
        console.sendResponse(bankInfo.toString());
    }

    @Override
    public String toString() {
        return "Show current Bank report";
    }
}
