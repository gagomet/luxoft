package BankApplication.account.impl;

import java.util.ResourceBundle;

/**
 * Created by Kir Kolesnikov on 14.01.2015.
 */
public class SavingAccount extends AbstractAccount {
    private static ResourceBundle bundle = ResourceBundle.getBundle("strings");
    //TODO add constructor with overdraft

    public SavingAccount(float startAmount){
        super();
        setBalance(startAmount);
    }

    @Override
    public void printReport() {
        System.out.println(bundle.getString("savingAccount") + " " + this.getBalance());
    }
}
