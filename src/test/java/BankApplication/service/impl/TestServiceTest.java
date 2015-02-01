package BankApplication.service.impl;

import BankApplication.model.impl.Bank;
import BankApplication.model.impl.CheckingAccount;
import BankApplication.model.impl.Client;
import junit.framework.TestCase;
import org.junit.Before;

public class TestServiceTest extends TestCase {
Client client, client2;
    @Before
    public void  setUp(){
        Client client = new Client ();
        client.setName("Ivan Ivanov");
        client.setCity("Kiev");
        client.setActiveAccount(new CheckingAccount(1000));
        // add some fields from Client
        // marked as @NoDB, with different values
        // for client and client2

        Client client2 = new Client ();
        client2.setName("Ivan Ivanov");
        client2.setCity("Kiev");
        client2.setActiveAccount(new CheckingAccount(100000));
        // add some fields from Client
        // marked as @NoDB, with different values
        // for client and client2
    }

    public void testIsEquals() throws Exception {
        Client client = new Client ();
        client.setName("Ivan Ivanov");
        client.setCity("Kiev");
        client.setActiveAccount(new CheckingAccount(1000));
        // add some fields from Client
        // marked as @NoDB, with different values
        // for client and client2

        Client client2 = new Client ();
        client2.setName("Ivan Ivanov");
        client2.setCity("Kiev");
        client2.setActiveAccount(new CheckingAccount(100000));
        // add some fields from Client
        // marked as @NoDB, with different values
        // for client and client2
    }
}