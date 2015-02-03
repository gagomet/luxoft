package BankApplication.service.DAO;

import BankApplication.DAO.BaseDAO;
import BankApplication.DAO.impl.BaseDAOImpl;
import BankApplication.DAO.impl.DAOFactory;
import BankApplication.model.impl.Bank;
import BankApplication.model.impl.CheckingAccount;
import BankApplication.model.impl.Client;
import BankApplication.service.impl.TestService;
import BankApplication.type.Gender;
import com.ibatis.common.jdbc.ScriptRunner;
import junit.framework.TestCase;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.URL;
import java.sql.Connection;
import java.sql.SQLException;


public class BankDAOImplTest extends TestCase {
    private Bank bank1, bank2;
    private BaseDAO testBaseDao;
    private Connection dbConnection;
    private InputStreamReader reader = null;

    @Before
    public void setUp() {
        testBaseDao = new BaseDAOImpl();
        dbConnection = testBaseDao.openConnection();
        String SQLScriptFilePath = "test.sql";
        URL url = Thread.currentThread().getContextClassLoader().getResource(SQLScriptFilePath);
        File file = new File(url.getPath());
        try {
            ScriptRunner scriptRunner = new ScriptRunner(dbConnection, false, false);
            Reader reader = new BufferedReader(new FileReader(file));
            scriptRunner.runScript(reader);
        } catch (SQLException | IOException e) {
            e.printStackTrace();
        }
    }

    @After
    public void closeUp() {
        if (dbConnection != null) {
            try {
                dbConnection.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        if (reader != null) {
            try {
                reader.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
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
        client.setCity("JizzleTown");
        client.setSex(Gender.FEMALE);
        client.setInitialOverdraft(100.0f);
        client = DAOFactory.getClientDAO().save(bank1, client);
        bank1.addClient(client);
        bank2 = DAOFactory.getBankDAO().getBankByID(1);

//        assertEquals(bank1, bank2);
        assertTrue(TestService.isEquals(bank1, bank2));
    }

}