package BankApplication.service.DAO;

import BankApplication.DAO.ClientDAO;
import BankApplication.DAO.impl.ClientDAOImpl;
import BankApplication.DAO.impl.DAOFactory;
import junit.framework.TestCase;
import org.junit.Before;

public class ClientDAOImplTest extends TestCase {
    private ClientDAO testInstance;


    @Before
    public void setUp() {
        testInstance = DAOFactory.getClientDAO();
    }

    public void testFindClientByName() throws Exception {

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