package BankApplication.service.DAO;

import BankApplication.DAO.impl.BaseDAOImpl;
import junit.framework.TestCase;
import org.junit.Before;

import java.sql.Connection;

public class BaseDAOImplTest extends TestCase {
    BaseDAOImpl testInstance;

    @Before
    public void setUp() throws Exception {
        testInstance = new BaseDAOImpl();
    }

    public void testOpenConnection() throws Exception {
        Connection connection = testInstance.openConnection();
        assertFalse(connection == null);
    }

}