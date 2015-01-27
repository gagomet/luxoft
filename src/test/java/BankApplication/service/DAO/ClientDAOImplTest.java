package BankApplication.service.DAO;

import BankApplication.model.impl.Bank;
import BankApplication.model.impl.Client;
import BankApplication.service.DAO.impl.BankDAOImpl;
import BankApplication.service.DAO.impl.ClientDAOImpl;
import junit.framework.TestCase;
import org.junit.Before;

public class ClientDAOImplTest extends TestCase {
    private ClientDAOImpl testInstance;
    private BankDAO bankDAO;
    private Bank dummyBank;

    @Before
    public void setUp() {
        testInstance = new ClientDAOImpl();
        bankDAO = new BankDAOImpl();
        dummyBank = bankDAO.getBankByName("MYBANK");
    }

    public void testFindClientByName() throws Exception {
        Client newbie = testInstance.findClientByName(dummyBank, "Ivan Ivanov");
        System.out.println(newbie.toString());
        assertFalse(newbie == null);
        assertFalse(newbie.getAccountsList().size() == 0);
    }

    public void testFindClientById() throws Exception {

    }

    public void testGetAllClients() throws Exception {

    }

    public void testSave() throws Exception {

    }

    public void testRemove() throws Exception {

    }
}