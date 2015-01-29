package BankApplication.DAO.impl;

import BankApplication.DAO.ClientDAO;
import BankApplication.exceptions.DAOException;
import BankApplication.model.impl.Bank;
import BankApplication.model.impl.BankInfo;
import BankApplication.DAO.BankDAO;
import BankApplication.model.impl.Client;
import BankApplication.service.impl.BankReport;


import java.sql.*;
import java.util.HashSet;
import java.util.List;

/**
 * Created by Kir Kolesnikov on 27.01.2015.
 */
public class BankDAOImpl extends BaseDAOImpl implements BankDAO {
    private static final String GET_BANK_BY_NAME_STMT = "SELECT * FROM BANKS WHERE BANKS.NAME = ? ";
    public static final String GET_NUMBER_OF_CLIENTS_STMT = "SELECT COUNT(ID) FROM CLIENTS";
    public static final String GET_TOTAL_FUNDS_STMT = "SELECT SUM(BALANCE) FROM ACCOUNTS";
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
                throw new DAOException("Unable to get Bank from DB");
            }
            resultBank = new Bank();
            while (resultSet.next()) {
                resultBank.setId(resultSet.getLong("ID"));
                resultBank.setName(resultSet.getString("NAME"));
            }
        } catch (SQLException e) {
            handleSQLException(e);
        } finally {
            if (resultSet != null) {
                try {
                    resultSet.close();
                } catch (SQLException e) {
                    handleSQLException(e);
                }
            }
            closeConnection(connection);
        }
        return resultBank;
    }


    //TODO add command to use this method
    @Override
    public BankInfo getBankInfo(Bank bank) {
       BankInfo result = new BankInfo();
        Connection connection = null;
        int count = 0;
        float summ = 0f;
        try{
            connection = openConnection();
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(GET_NUMBER_OF_CLIENTS_STMT);
            while(resultSet.next()){
                count = resultSet.getInt(1);
            }
            result.setTotalAccounts(count);
            resultSet = statement.executeQuery(GET_TOTAL_FUNDS_STMT);
            while (resultSet.next()){
                summ = resultSet.getFloat(1);
            }
            result.setTotalCredit(summ);
            ClientDAO clientDAO = new ClientDAOImpl();
            List<Client> clientList = clientDAO.getAllClients(bank);
            result.setClientsByCities(BankReport.getClientsFromListToMapByCity(clientList));
        } catch (SQLException e) {
            handleSQLException(e);
        }
        return result;
    }

}
