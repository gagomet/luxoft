package BankApplication.DAO;

import BankApplication.DAO.impl.BaseDAOImpl;
import BankApplication.DAO.impl.DAOFactory;
import BankApplication.SqlScripRunner;
import BankApplication.model.Account;
import BankApplication.model.impl.Bank;
import BankApplication.model.impl.CheckingAccount;
import BankApplication.model.impl.Client;
import BankApplication.service.impl.TestService;
import BankApplication.type.Gender;
import org.junit.*;

import java.io.IOException;
import java.io.InputStreamReader;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;

public class ClientDAOImplTest {

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
    public void testFindClientByName() throws Exception {
        Bank bank = DAOFactory.getBankDAO().getBankByID(1);
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

        Client clientFromDb = DAOFactory.getClientDAO().findClientByName(bank, "Ivan Ivanov" );
//        assertEquals(client, clientFromDb);
        assertTrue(TestService.isEquals(client, clientFromDb));
    }
    @Test
    public void testFindClientById() throws Exception {
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

        Client clientFromDb = DAOFactory.getClientDAO().findClientById(1);
        assertTrue(TestService.isEquals(client, clientFromDb));
    }
    @Test
    public void testGetAllClients() throws Exception {
        Bank bank = DAOFactory.getBankDAO().getBankByID(1);
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

        Client client2 = new Client();
        client2.setBankId(1);
        client2.setName("Petra Petrova");
        client2.setInitialOverdraft(1500.0f);
        client2.setSex(Gender.FEMALE);
        client2.setEmail("petrova@girl.here");
        client2.setCity("Odessa");
        Account account3 = new CheckingAccount(client.getInitialOverdraft());
        account3.setBalance(2450f);
        account3.setClientId(2);
        client2.addAccount(account3);

        List<Client> allClients = new ArrayList<>();
        allClients.add(client);
        allClients.add(client2);

        List<Client> clientsFromDb = DAOFactory.getClientDAO().getAllClients(bank);
        assertTrue(TestService.isEquals(allClients, clientsFromDb));
    }
    @Test
    public void testSave() throws Exception {
        Bank bank = DAOFactory.getBankDAO().getBankByID(1);
        Client client = new Client();
        client.setBankId(1);
        client.setName("Newbie");
        client.setInitialOverdraft(1100f);
        client.setSex(Gender.MALE);
        client.setEmail("newb@server.mail");
        client.setPhone("+3801234567");
        client.setCity("Hatsapetovka");

        client = DAOFactory.getClientDAO().save(bank, client);

        Client freshClientFromDB = DAOFactory.getClientDAO().findClientByName(bank, "Newbie");

        assertTrue(TestService.isEquals(client, freshClientFromDB));
    }
    @Test
    public void testRemove() throws Exception {
        Bank bank = DAOFactory.getBankDAO().getBankByID(1);
        Client client2 = new Client();
        client2.setBankId(1);
        client2.setId(2);
        client2.setName("Petra Petrova");
        client2.setInitialOverdraft(1500.0f);
        client2.setSex(Gender.FEMALE);
        client2.setEmail("petrova@girl.here");
        client2.setCity("Odessa");
        Account account3 = new CheckingAccount(client2.getInitialOverdraft());
        account3.setBalance(2450f);
        account3.setClientId(2);
        account3.setId(2);
        client2.addAccount(account3);

        DAOFactory.getClientDAO().remove(client2);
        Bank modifiedBank = DAOFactory.getBankDAO().getBankByID(1);

        assertNotEquals(bank.getClientSet().size(), modifiedBank.getClientSet().size());


    }
}