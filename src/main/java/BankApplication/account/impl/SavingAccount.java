package BankApplication.account.impl;

/**
 * Created by Kir Kolesnikov on 14.01.2015.
 */
public class SavingAccount extends AbstractAccount {

    public SavingAccount(float startAmount) {
        super();
        setBalance(startAmount);
    }

    @Override
    public void printReport() {
//        System.out.println(bundle.getString("savingAccount") + " " + this.getBalance());
        System.out.println(super.toString());
    }


}
