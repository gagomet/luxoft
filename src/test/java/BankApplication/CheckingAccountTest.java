package BankApplication;

import BankApplication.exceptions.*;
import BankApplication.exceptions.IllegalArgumentException;
import BankApplication.model.impl.CheckingAccount;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Created by Kir Kolesnikov on 26.01.2015.
 */
public class CheckingAccountTest {
    private CheckingAccount testInstance;

    @Before
    public void setUp() throws IllegalArgumentException {
        testInstance = new CheckingAccount();
        testInstance.setOverdraft(50.0f);
        testInstance.deposit(100.0f);
    }



    @Test
    public void withdrawMethodTest() throws NotEnoughFundsException, IllegalArgumentException {
        testInstance.withdraw(60.0f);
        assertEquals(40.0f, testInstance.getBalance(), 0);
        testInstance.withdraw(0.0f);
        assertEquals(40.0f, testInstance.getBalance(), 0);
    }

    @Test(expected = OverdraftLimitExceedException.class)
    public void overdraftLimitExceedException() throws NotEnoughFundsException, IllegalArgumentException {
        testInstance.withdraw(200.0f);
    }

    @Test(expected = IllegalArgumentException.class)
    public void withdrawNegativeFunds_illegalArgumentException() throws IllegalArgumentException, NotEnoughFundsException {
        testInstance.withdraw(-10.0f);
    }
}
