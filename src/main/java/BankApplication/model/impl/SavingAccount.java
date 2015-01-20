package BankApplication.model.impl;

import java.util.Map;

/**
 * Created by Kir Kolesnikov on 14.01.2015.
 */
public class SavingAccount extends AbstractAccount {

    public SavingAccount() {
        super();
    }

    public void parseFeed(Map<String, String> feedMap){
        Float balance = Float.parseFloat(feedMap.get("balance"));
        setBalance(balance);
    }
    @Override
    public void printReport() {
        System.out.println(super.toString());
    }


}
