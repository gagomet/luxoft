package BankApplication.DAO;

import BankApplication.DAO.BaseDAO;
import BankApplication.DAO.impl.BaseDAOImpl;
import BankApplication.DAO.impl.DAOFactory;
import BankApplication.SqlScripRunner;
import BankApplication.model.Account;
import BankApplication.model.impl.CheckingAccount;
import BankApplication.model.impl.Client;
import BankApplication.service.impl.TestService;
import BankApplication.type.Gender;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import java.io.IOException;
import java.io.InputStreamReader;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class AccountDAOImplTest {
    private static BaseDAO testBaseDao;
    private static Connection dbConnection;
    private static InputStreamReader reader = null;

    @BeforeClass
    public static void preSetUp() {
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
    public void testSave() throws Exception {
        Account account = new CheckingAccount(1000);
        account.setId(1);
        account.setBalance(999);
        account.setClientId(1);

        Client client = new Client();
        client.setBankId(1);
        client.setName("Ivan Ivanov");
        client.setInitialOverdraft(1000.0f);
        client.setSex(Gender.MALE);
        client.setEmail("ivan@server.mail");
        client.setPhone("+3801234567");
        client.setCity("Hatsapetovka");
        Account account1 = new CheckingAccount(client.getInitialOverdraft());
        account1.setBalance(-100f);
        account1.setClientId(1);
        Account account2 = new CheckingAccount(client.getInitialOverdraft());
        account2.setBalance(2222f);
        account2.setClientId(1);
        client.addAccount(account2);
        client.addAccount(account1);

        DAOFactory.getAccountDAO().save(account, client);
        Client modifiedClient = DAOFactory.getClientDAO().findClientById(1);
        assertTrue(TestService.isEquals(client, modifiedClient));
    }

    @Test
    public void testRemoveByClientId() throws Exception {
        Client client = new Client();
        client.setBankId(1);
        client.setName("Ivan Ivanov");
        client.setInitialOverdraft(1000.0f);
        client.setSex(Gender.MALE);
        client.setEmail("ivan@server.mail");
        client.setPhone("+3801234567");
        client.setCity("Hatsapetovka");

        DAOFactory.getAccountDAO().removeByClientId(1);

        Client modifiedClient = DAOFactory.getClientDAO().findClientById(1);

        assertTrue(TestService.isEquals(client, modifiedClient));


    }
    @Test
    public void testGetClientAccounts() throws Exception {
        Client client = new Client();
        client.setBankId(1);
        client.setName("Ivan Ivanov");
        client.setInitialOverdraft(1000.0f);
        client.setSex(Gender.MALE);
        client.setEmail("ivan@server.mail");
        client.setPhone("+3801234567");
        client.setCity("Hatsapetovka");
        Account account1 = new CheckingAccount(client.getInitialOverdraft());
        account1.setBalance(-100f);
        account1.setClientId(1);
        Account account2 = new CheckingAccount(client.getInitialOverdraft());
        account2.setBalance(2222f);
        account2.setClientId(1);
        client.addAccount(account2);
        client.addAccount(account1);

        List<Account> stationaryList = new ArrayList<>(client.getAccountsList());

        List<Account> accountsFromDB = DAOFactory.getAccountDAO().getClientAccounts(1);

        assertEquals(stationaryList, accountsFromDB);
    }
    @Test
    public void testAddAccount() throws Exception {

    }
    @Test
    public void testTransferFunds() throws Exception {

    }
}