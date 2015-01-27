package BankApplication.service.DAO;

import BankApplication.model.impl.Bank;
import BankApplication.service.DAO.impl.BankDAOImpl;
import com.ibatis.common.jdbc.ScriptRunner;
import junit.framework.TestCase;
import org.h2.jdbcx.JdbcConnectionPool;
import org.junit.Before;

import javax.sql.DataSource;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;
import java.net.URL;
import java.sql.Connection;
import java.sql.SQLException;


public class BankDAOImplTest extends TestCase {
    private BankDAOImpl testInstance;
    private Connection connection;
    private static final String DB_URL = "jdbc:h2:mem:test;DB_CLOSE_DELAY=-1";
    private static final String DB_USER = "";
    private static final String DB_PASSWORD = "";

    @Before
    public void setUp(){
        testInstance = new BankDAOImpl();
        DataSource dataSource = JdbcConnectionPool.create(DB_URL, DB_USER, DB_PASSWORD);
        try {
            connection = dataSource.getConnection();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        String SQLScriptFilePath = "test.sql";
        URL url = Thread.currentThread().getContextClassLoader().getResource(SQLScriptFilePath);
        File file = new File(url.getPath());

        try {
            ScriptRunner scriptRunner = new ScriptRunner(connection, false, false);
            Reader reader = new BufferedReader(new FileReader(file));
            scriptRunner.runScript(reader);
        } catch (SQLException | IOException e) {
            e.printStackTrace();
        }
    }

    public void testGetBankByName() throws Exception {
        Bank newBank = testInstance.getBankByName("MYBANK");
        assertFalse(newBank == null);
    }

}