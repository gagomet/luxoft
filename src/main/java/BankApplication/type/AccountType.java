package BankApplication.type;

import BankApplication.model.account.impl.AbstractAccount;
import BankApplication.model.account.impl.CheckingAccount;
import BankApplication.model.account.impl.SavingAccount;
import BankApplication.exceptions.IllegalArgumentException;

/**
 * Created by Kir Kolesnikov on 14.01.2015.
 */
public enum AccountType {
    CHECKING {
        @Override
        public AbstractAccount create() throws BankApplication.exceptions.IllegalArgumentException {
            return new CheckingAccount();
        }
    },
    SAVING {
        @Override
        public AbstractAccount create() {
            return new SavingAccount();
        }
    };

    public abstract AbstractAccount create() throws IllegalArgumentException;


}
