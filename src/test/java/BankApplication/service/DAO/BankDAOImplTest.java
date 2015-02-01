package BankApplication.service.DAO;

import BankApplication.DAO.BankDAO;
import BankApplication.DAO.impl.DAOFactory;
import BankApplication.model.impl.Bank;
import junit.framework.TestCase;
import org.junit.Before;


public class BankDAOImplTest extends TestCase {
    private BankDAO testInstance;


    @Before
    public void setUp(){
        testInstance = DAOFactory.getBankDAO();
    }

    public void testGetBankByName() throws Exception {
        Bank newBank = testInstance.getBankByName("MyBank");
        assertFalse(newBank == null);
    }

}