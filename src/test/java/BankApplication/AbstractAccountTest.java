package BankApplication;

import BankApplication.exceptions.NotEnoughFundsException;
import BankApplication.model.impl.AbstractAccount;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

import java.util.Map;

/**
 * Created by Kir Kolesnikov on 26.01.2015.
 */
public class AbstractAccountTest {


    private class TestingAccount extends AbstractAccount {
        public TestingAccount() {
            super();
        }

        @Override
        public void parseFeed(Map<String, String> feedMap) {
            System.out.println("input map " + feedMap.toString() + " method parseFeed is working");
        }

        @Override
        public void withdraw(float amount) throws NotEnoughFundsException, IllegalArgumentException {
            System.out.println("withdraw method");
        }

        @Override
        public void printReport() {
            System.out.println("Report from " + super.toString());
        }
    }

    private TestingAccount sut;

    @Before
    public void setUp() {
        sut = new TestingAccount();
        System.out.println("@before sut setup");
    }

    @Test
    public void testSetBalanceToZeroByDefault() {
        assertEquals(0.0f, sut.getBalance(), 0);
    }

    @Test
    public void testDepositFundsMethod() throws IllegalArgumentException {
        sut.deposit(10.0f);
        assertEquals(10.0f, sut.getBalance(), 0);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testNegativeNumberDeposit_IllegalArgumentException() throws IllegalArgumentException {
        sut.deposit(-10.0f);
    }

}
