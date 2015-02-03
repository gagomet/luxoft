package BankApplication;

import com.ibatis.common.jdbc.ScriptRunner;

import java.io.*;
import java.net.URL;
import java.sql.Connection;
import java.sql.SQLException;

/**
 * Created by Kir Kolesnikov on 03.02.2015.
 */
public class SqlScripRunner {
    public static void runSqlScript(Connection connection, String scriptNameWithExtension){
        URL url = Thread.currentThread().getContextClassLoader().getResource(scriptNameWithExtension);
        File file = new File(url.getPath());
        try {
            ScriptRunner scriptRunner = new ScriptRunner(connection, false, false);
            Reader reader = new BufferedReader(new FileReader(file));
            scriptRunner.runScript(reader);
        } catch (SQLException | IOException e) {
            e.printStackTrace();
        }
    }
}
