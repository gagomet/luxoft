package BankApplication.commander;

import BankApplication.model.impl.Bank;

/**
 * Created by Kir Kolesnikov on 26.02.2015.
 */
public class BankHolder {
    private Bank bank;

    public BankHolder() {
    }

    public BankHolder(Bank bank) {
        this.bank = bank;
    }

    public Bank getBank() {
        return bank;
    }

    public void setBank(Bank bank) {
        this.bank = bank;
    }
}
