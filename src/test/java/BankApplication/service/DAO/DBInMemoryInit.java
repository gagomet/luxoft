package BankApplication.service.DAO;

import com.ibatis.common.jdbc.ScriptRunner;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;
import java.net.URL;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * Created by Padonag on 27.01.2015.
 */
public class DBInMemoryInit {
    private static final String DB_DRIVER = "org.h2.Driver";
    private static final String DB_CONNECTION = "jdbc:h2:mem:test;DB_CLOSE_DELAY=-1";
    private static final String DB_USER = "";
    private static final String DB_PASSWORD = "";


    private static Connection getDBConnection() {
        Connection dbConnection = null;
        try {
            Class.forName(DB_DRIVER);
        } catch (ClassNotFoundException e) {
            System.out.println(e.getMessage());
        }
        try {
            dbConnection = DriverManager.getConnection(DB_CONNECTION, DB_USER, DB_PASSWORD);
            return dbConnection;
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return dbConnection;
    }


    public static Connection wakeTestDbFromScript(){
        Connection connection = getDBConnection();
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
        return connection;
    }

}
