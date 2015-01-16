package BankApplication.ui.commands.impl;

import BankApplication.exceptions.IllegalArgumentException;

import java.io.IOException;

/**
 * Created by Kir Kolesnikov on 15.01.2015.
 */
public class DepositCommand extends AbstractCommand {
    Long accountID;
    float amountToDeposite;

    @Override
    public void execute() {
        try {

            amountToDeposite = validateFloat(console.consoleResponse(bundle.getString("depositFunds")));

        }  catch (IOException e) {
            e.printStackTrace();
        }

    }

    @Override
    public void printCommandInfo() {
        System.out.println(bundle.getString("depositCommand"));
    }

    private void depositFunds(long accountID, float amountToDeposite) {
        try {
            if (amountToDeposite <= 0) {
                throw new IllegalArgumentException(errorsBundle.getString("depositeNegative"));
            } else {

            }
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
        }
    }
}
