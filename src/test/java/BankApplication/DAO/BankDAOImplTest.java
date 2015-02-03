package BankApplication.DAO;

import BankApplication.DAO.impl.BaseDAOImpl;
import BankApplication.DAO.impl.DAOFactory;
import BankApplication.SqlScripRunner;
import BankApplication.model.impl.Bank;
import BankApplication.model.impl.Client;
import BankApplication.service.impl.TestService;
import BankApplication.type.Gender;
import org.junit.*;

import java.io.IOException;
import java.io.InputStreamReader;
import java.sql.Connection;
import java.sql.SQLException;
import static org.junit.Assert.*;


public class BankDAOImplTest {
    private Bank bank1, bank2;
    private static BaseDAO testBaseDao;
    private static Connection dbConnection;
    private static InputStreamReader reader = null;

    @BeforeClass
    public static void preSetUp(){
        testBaseDao = new BaseDAOImpl();
        dbConnection = testBaseDao.openConnection();
        SqlScripRunner.runSqlScript(dbConnection, "create.sql");
    }

    @Before
    public void setUp() {
        SqlScripRunner.runSqlScript(dbConnection, "stubs.sql");
    }

    @After
    public void closeUp() {
        SqlScripRunner.runSqlScript(dbConnection, "clear.sql");
    }

    @AfterClass
    public static void afterUse(){
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
    public void getBankByNameTest() throws Exception {
        Bank newBank = DAOFactory.getBankDAO().getBankByName("MyBank");
        assertFalse(newBank == null);
    }

    @Test
    public void insertTest() {
        bank1 = DAOFactory.getBankDAO().getBankByID(1);
        Client client = new Client();
        client.setName("Petr Petrov");
        client.setCity("JizzleTown");
        client.setSex(Gender.FEMALE);
        client.setInitialOverdraft(100.0f);
        client = DAOFactory.getClientDAO().save(bank1, client);
        bank1.addClient(client);
        bank2 = DAOFactory.getBankDAO().getBankByID(1);

        assertTrue(TestService.isEquals(bank1, bank2));

    }

    @Test
    public void getBankByIDTest(){
        Bank bank = DAOFactory.getBankDAO().getBankByID(1);
        assertFalse(bank == null);
    }

    @Test
    public void saveChangesToBank(){
        Bank bank = DAOFactory.getBankDAO().getBankByID(1);
        Client client = new Client();
        client.setName("Newbie");
        client.setInitialOverdraft(0.0f);
        client.setSex(Gender.MALE);
        bank.addClient(client);
        Bank changed = DAOFactory.getBankDAO().saveChangesToBank(bank);

        assertEquals(bank, changed);
    }



}