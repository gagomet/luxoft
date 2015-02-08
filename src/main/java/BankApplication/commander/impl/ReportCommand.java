package BankApplication.commander.impl;

import BankApplication.commander.CommandsManager;
import BankApplication.model.impl.BankInfo;
import BankApplication.network.console.Console;
import BankApplication.service.impl.BankServiceImpl;
import BankApplication.service.impl.ServiceFactory;

import java.io.IOException;

/**
 * Created by Kir Kolesnikov on 29.01.2015.
 */
public class ReportCommand extends AbstractCommand {

    public ReportCommand() {
    }

    public ReportCommand(Console console, CommandsManager manager) {
        this.console = console;
        setManager(manager);
    }

    @Override
    public void execute()  {
        BankInfo bankInfo = ServiceFactory.getBankService().getBankInfo(ServiceFactory.getBankService().getCurrentBank());
            console.sendResponse(bankInfo.toString());
    }

    @Override
    public String toString() {
        return "Show current Bank report";
    }
}
