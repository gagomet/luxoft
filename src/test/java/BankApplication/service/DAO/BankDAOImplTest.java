package BankApplication.service.DAO;

import BankApplication.DAO.impl.BaseDAOImpl;
import BankApplication.DAO.impl.DAOFactory;
import BankApplication.model.impl.Bank;
import BankApplication.model.impl.CheckingAccount;
import BankApplication.model.impl.Client;
import BankApplication.service.impl.TestService;
import junit.framework.TestCase;
import org.h2.jdbcx.JdbcConnectionPool;
import org.junit.Before;
import org.junit.Test;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;


public class BankDAOImplTest extends TestCase {
     private Bank bank1, bank2;

    @Before
    public void setUp() {
        DataSource dataSource = JdbcConnectionPool.create("jdbc:h2:mem:test;DB_CLOSE_DELAY=-1", "user", "password");
        try {
            Connection conn = dataSource.getConnection();

        } catch (SQLException e) {
            e.printStackTrace();
        }

    }

    @Test
    public void testGetBankByName() throws Exception {
        Bank newBank = DAOFactory.getBankDAO().getBankByName("MyBank");
        assertFalse(newBank == null);
    }

    @Test
    public void testInsert() {
        bank1 = DAOFactory.getBankDAO().getBankByID(1);
        Client client = new Client();
        client.setName("Petr Petrov");
        client.setCity("Jizzle");
        client.setInitialOverdraft(100.0f);
        client.addAccount(new CheckingAccount());
        bank1.addClient(client);
        DAOFactory.getBankDAO().saveChangesToBank(bank1);
        bank2 = DAOFactory.getBankDAO().getBankByID(1);
        assertTrue(TestService.isEquals(bank1, bank2));
    }

}