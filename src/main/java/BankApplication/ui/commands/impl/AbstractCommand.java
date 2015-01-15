package BankApplication.ui.commands.impl;

import BankApplication.ui.commands.ICommand;

import java.util.ResourceBundle;

/**
 * Created by Kir Kolesnikov on 15.01.2015.
 */
public abstract class AbstractCommand implements ICommand {
    protected ResourceBundle bundle = ResourceBundle.getBundle("strings");
}
