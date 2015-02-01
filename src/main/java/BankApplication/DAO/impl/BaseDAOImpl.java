package BankApplication.DAO.impl;

import BankApplication.DAO.BaseDAO;
import BankApplication.exceptions.DAOException;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.ResourceBundle;

/**
 * Created by Kir Kolesnikov on 27.01.2015.
 */
public class BaseDAOImpl implements BaseDAO {
    private ResourceBundle dbBundle = ResourceBundle.getBundle("db");
    private static Connection connection;

    public static Connection getConnection() {
        return connection;
    }

    public static void setConnection(Connection connection) {
        BaseDAOImpl.connection = connection;
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
            System.out.println(e.getMessage());
            System.out.println(e.getMessage());
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

    protected void handleSQLException(SQLException e){
        if(e.getNextException() != null){
            SQLException nextException = e.getNextException();
            handleSQLException(nextException);
        } else {
            System.out.println(e.getSQLState() + e.getMessage());
        }
    }

}
