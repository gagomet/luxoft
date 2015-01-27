package BankApplication.service.DAO.impl;

import BankApplication.service.DAO.BaseDAO;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.ResourceBundle;

/**
 * Created by Kir Kolesnikov on 27.01.2015.
 */
public class BaseDAOImpl implements BaseDAO {
    private ResourceBundle dbBundle = ResourceBundle.getBundle("db");

    @Override
    public Connection openConnection() {
        try {
            Connection connection = null;
            Class.forName(dbBundle.getString("driverClass")); // this is driver for H2
            connection = DriverManager.getConnection(dbBundle.getString("jdbcURL"),
                    "sa", // login
                    "" // password
            );
            return connection;
        } catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public void closeConnection(Connection connection) {
        try {
            connection.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

}
