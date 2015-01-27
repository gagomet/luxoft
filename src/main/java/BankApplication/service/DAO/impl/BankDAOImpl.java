package BankApplication.service.DAO.impl;

import BankApplication.model.impl.Bank;
import BankApplication.service.DAO.BankDAO;
import BankApplication.service.DAO.BaseDAO;


import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Created by Kir Kolesnikov on 27.01.2015.
 */
public class BankDAOImpl extends BaseDAOImpl implements BankDAO {
    private static final String GET_BANK_BY_NAME_STMT = "SELECT * FROM BANKS WHERE BANKS.NAME = ? ";

    @Override
    public Bank getBankByName(String name) {
        Bank resultBank = null;
        ResultSet resultSet = null;
        Connection connection = null;
        try {
            connection = openConnection();
            PreparedStatement preparedStatement = connection.prepareStatement(GET_BANK_BY_NAME_STMT);
            preparedStatement.setString(1, name);
            resultSet = preparedStatement.executeQuery();
            if (resultSet == null) {
                return null;
            }
            resultBank = new Bank();
            while (resultSet.next()) {
                resultBank.setId(resultSet.getLong("ID"));
                resultBank.setName(resultSet.getString("NAME"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            if (resultSet != null) {
                try {
                    resultSet.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
            closeConnection(connection);
        }
        return resultBank;
    }

}
