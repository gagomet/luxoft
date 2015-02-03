package BankApplication.service.impl;

import BankApplication.model.Account;
import BankApplication.model.impl.Bank;
import BankApplication.model.impl.CheckingAccount;
import BankApplication.model.impl.Client;
import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;

public class TestServiceTest  {
    Bank bank1, bank2;

    @Before
    public void initBanks() {

        Account newAccount = new CheckingAccount(10000);
        bank1 = new Bank();
        bank1.setId(1);
        bank1.setName("My Bank");
        Client client = new Client();
        client.setName("Ivan Ivanov");
        client.setCity("Kiev");
        client.addAccount(newAccount);
        client.setActiveAccount(newAccount);
        bank1.addClient(client);

        bank2 = new Bank();
        bank2.setId(2);
        bank2.setName("My Bank");
        Client client2 = new Client();
        client2.setName("Ivan Ivanov");
        client2.setCity("Kiev");
        client2.addAccount(newAccount);
        bank2.addClient(client2);
        bank2.addClient(client);
    }

    @Test
    public void testEquals() {
        assertTrue(TestService.isEquals(bank1, bank2));
    }
}
