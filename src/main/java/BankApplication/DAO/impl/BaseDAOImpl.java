package BankApplication.DAO.impl;

import BankApplication.DAO.BaseDAO;
import BankApplication.exceptions.DAOException;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.ResourceBundle;
import java.util.logging.FileHandler;
import java.util.logging.Handler;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Created by Kir Kolesnikov on 27.01.2015.
 */
public class BaseDAOImpl implements BaseDAO {
    private ResourceBundle dbBundle = ResourceBundle.getBundle("db");
    private static Connection connection;

    private static final Logger logger = Logger.getLogger(BaseDAOImpl.class.getName());

    public static Connection getConnection() {
        if (connection != null) {
            return connection;
        }
        logger.log(Level.SEVERE,"Connection is null" );
        throw new DAOException("Connection is null");
    }

    public static void setConnection(Connection connection) {
        BaseDAOImpl.connection = connection;
    }

    public BaseDAOImpl() {
    }

    @Override
    public Connection openConnection() {
        try {
            Connection connection;
            Class.forName(dbBundle.getString("driverClass")); // this is driver for H2
            connection = DriverManager.getConnection(dbBundle.getString("jdbcURL"),
                    "sa", // login
                    "" // password
            );
            return connection;
        } catch (ClassNotFoundException | SQLException e) {
            logger.log(Level.SEVERE, e.getMessage());
        }
        throw new DAOException("Can't reach the connection to DB");
    }

    @Override
    public void closeConnection(Connection connection) {
        try {
            connection.close();
        } catch (SQLException e) {
            handleSQLException(e);
        }
    }

    protected void handleSQLException(SQLException e) {
        if (e.getNextException() != null) {
            logger.log(Level.SEVERE, e.getMessage());
            SQLException nextException = e.getNextException();
            handleSQLException(nextException);
        } else {
            System.out.println(e.getSQLState() + "   " + e.getMessage());
        }
    }

}
