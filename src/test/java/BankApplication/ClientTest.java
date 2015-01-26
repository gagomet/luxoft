package BankApplication;

import BankApplication.model.Account;
import BankApplication.model.impl.CheckingAccount;
import BankApplication.model.impl.Client;
import BankApplication.model.impl.SavingAccount;
import org.junit.Before;
import org.junit.Test;

import java.util.HashSet;
import java.util.Set;
import static org.junit.Assert.*;

public class ClientTest  {
    private Client testInstance;

    @Before
    public void setUp() throws Exception {
        testInstance = new Client();
        Set<Account> accounts = new HashSet<>();
        CheckingAccount checking = new CheckingAccount();
        checking.setOverdraft(100.0f);
        checking.deposit(50.0f);
        checking.withdraw(100.0f);
        SavingAccount saving = new SavingAccount();
        saving.deposit(70.0f);
        accounts.add(checking);
        accounts.add(saving);
        testInstance.setAccountsList(accounts);
    }

    @Test
    public void testFullClientBalance(){
        assertEquals(20.0f, testInstance.getFullClientBalance(), 0);
    }

}