package BankApplication.type;

import BankApplication.account.IAccount;
import BankApplication.account.impl.AbstractAccount;
import BankApplication.account.impl.CheckingAccount;
import BankApplication.account.impl.SavingAccount;
import BankApplication.exceptions.*;
import BankApplication.exceptions.IllegalArgumentException;

/**
 * Created by Kir Kolesnikov on 14.01.2015.
 */
public enum AccountType {
    CHECKING {
        @Override
        public AbstractAccount create(float summ) throws BankApplication.exceptions.IllegalArgumentException {
            return new CheckingAccount(summ);
        }
    },
    SAVING {
        @Override
        public AbstractAccount create(float summ) {
            return new SavingAccount(summ);
        }
    };

    public abstract AbstractAccount create(float summ) throws IllegalArgumentException;


}
