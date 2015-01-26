package BankApplication.service;

import BankApplication.TestsInitializer;
import BankApplication.exceptions.ClientNotFoundException;
import BankApplication.model.Account;
import BankApplication.model.impl.Bank;
import BankApplication.model.impl.CheckingAccount;
import BankApplication.model.impl.Client;
import BankApplication.service.impl.BankServiceImpl;
import BankApplication.type.Gender;
import junit.framework.TestCase;
import org.junit.Before;
import org.junit.Test;

import java.util.Set;

public class BankServiceImplTest extends TestCase {
    BankService testInstanceBankService;
    Bank dummyBank;
    Client dummyClient;

    @Before
    public void setUp(){
        testInstanceBankService = new BankServiceImpl();
        dummyBank = TestsInitializer.getDummyBankInstance();
        try {
            dummyClient = testInstanceBankService.getClientByName(dummyBank, "Beggar");
        } catch (ClientNotFoundException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testAddClient() throws Exception {
        Set<Client> clients = dummyBank.getClientsList();
        Client newbie = new Client();
        newbie.setName("Newbie");
        newbie.setSex(Gender.FEMALE);
        testInstanceBankService.addClient(dummyBank, newbie);
        assertEquals(dummyBank.getClientsList(), clients);
    }

    @Test
    public void testRemoveClient() throws Exception {
        Set<Client> clients = dummyBank.getClientsList();
        testInstanceBankService.removeClient(dummyBank, dummyClient );
        assertEquals(dummyBank.getClientsList(), clients);
    }

    public void testAddAccount() throws Exception {
        Client client = testInstanceBankService.getClientByName(dummyBank, "Beggar");
        Set<Account> accountsBefore = client.getAccountsList();
        CheckingAccount account = new CheckingAccount();
        account.setOverdraft(100.0f);
        account.deposit(20.0f);
        testInstanceBankService.addAccount(client, account);
        assertEquals(accountsBefore, client.getAccountsList());

    }

    /*public void testSetActiveAccount() throws Exception {

    }

    public void testDepositeFunds() throws Exception {

    }

    public void testWithdrawFunds() throws Exception {

    }

    public void testGetClientByName() throws Exception {

    }

    public void testGetAccountById() throws Exception {

    }

    public void testSaveClient() throws Exception {

    }

    public void testLoadClient() throws Exception {

    }*/
}