package BankApplication;
import BankApplication.exceptions.*;
import BankApplication.exceptions.IllegalArgumentException;
import BankApplication.model.impl.SavingAccount;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Created by Kir Kolesnikov on 26.01.2015.
 */
public class SavingAccountTest {
    private SavingAccount testInstance;

    @Before
    public void setUp(){
        testInstance = new SavingAccount();
        testInstance.setBalance(100.0f);
    }

    @Test
    public void withdrawMethodTest() throws IllegalArgumentException, NotEnoughFundsException {
        testInstance.withdraw(60.0f);
        assertEquals(40.0f, testInstance.getBalance(), 0);
    }

    @Test(expected = IllegalArgumentException.class)
    public void withdrawNegativeFunds_illegalArgumentException() throws IllegalArgumentException, NotEnoughFundsException {
        testInstance.withdraw(-10.0f);
    }

    @Test (expected = NotEnoughFundsException.class)
    public void notEnoughFundsException() throws IllegalArgumentException, NotEnoughFundsException {
        testInstance.withdraw(101.0f);
    }


}
