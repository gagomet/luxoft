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


    @Before
    public void setUp(){
        testInstance = new BankDAOImpl();
    }

    public void testGetBankByName() throws Exception {
        Bank newBank = testInstance.getBankByName("MYBANK");
        assertFalse(newBank == null);
    }

}