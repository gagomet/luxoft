package BankApplication.service.DAO;

import BankApplication.model.impl.Bank;
import BankApplication.DAO.impl.BankDAOImpl;
import junit.framework.TestCase;
import org.junit.Before;


public class BankDAOImplTest extends TestCase {
    private BankDAOImpl testInstance;


    @Before
    public void setUp(){
        testInstance = new BankDAOImpl();
    }

    public void testGetBankByName() throws Exception {
        Bank newBank = testInstance.getBankByName("MYBANK");
        assertFalse(newBank == null);
    }

}