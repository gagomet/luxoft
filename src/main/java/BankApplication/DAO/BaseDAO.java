package BankApplication.DAO;

import java.sql.Connection;

/**
 * Created by Kir Kolesnikov on 27.01.2015.
 */
public interface BaseDAO {
    /**
     * Opens connection with the database
     *
     * @return
     */
    public Connection openConnection();

    /**
     * Close connection with the database
     */
    public void closeConnection(Connection connection);
}
