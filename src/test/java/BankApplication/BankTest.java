package BankApplication;

import BankApplication.model.impl.Bank;
import BankApplication.model.impl.Client;
import BankApplication.type.Gender;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Created by Kir Kolesnikov on 26.01.2015.
 */
public class BankTest {
    private Bank testInstance;

    @Before
    public void setUp(){
        testInstance = new Bank();
    }

    @Test
    public void testAddingNewClient() {
        int clientsNumber = testInstance.getClientsList().size();
        Client newbie = new Client();
        newbie.setInitialOverdraft(0f);
        newbie.setName("Newbie");
        newbie.setSex(Gender.MALE);
        testInstance.addClient(newbie);
        assertTrue(testInstance.getClientsList().size() == clientsNumber + 1);
    }

    @Test
    public void testRemovingClient() {
        Client toDeath = new Client();
        toDeath.setName("Newbie");
        toDeath.setSex(Gender.MALE);
        toDeath.setInitialOverdraft(0f);
        testInstance.addClient(toDeath);
        int numberOfClients = testInstance.getClientsList().size();
        testInstance.removeClient(toDeath);
        assertTrue(testInstance.getClientsList().size() == numberOfClients - 1);
    }

}
