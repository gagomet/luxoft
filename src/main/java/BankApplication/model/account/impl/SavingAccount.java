package BankApplication.model.account.impl;

import java.util.Map;

/**
 * Created by Kir Kolesnikov on 14.01.2015.
 */
public class SavingAccount extends AbstractAccount {

    public SavingAccount() {
        super();
    }

    public void parseFeed(Map<String, String> feedMap){
        Float balance = Float.parseFloat(feedMap.get(feedBundle.getString("balance")));
        setBalance(balance);
    }
    @Override
    public void printReport() {
//        System.out.println(bundle.getString("savingAccount") + " " + this.getBalance());
        System.out.println(super.toString());
    }


}
