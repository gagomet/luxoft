package BankApplication.service.DAO.impl;

import BankApplication.exceptions.ClientNotFoundException;
import BankApplication.model.Account;
import BankApplication.model.impl.Bank;
import BankApplication.model.impl.Client;
import BankApplication.service.DAO.AccountDAO;
import BankApplication.service.DAO.ClientDAO;
import BankApplication.type.Gender;


import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Created by Kir Kolesnikov on 27.01.2015.
 */
public class ClientDAOImpl extends BaseDAOImpl implements ClientDAO {
    public static final String FIND_CLIENT_BY_NAME_STMT = "SELECT * FROM CLIENTS WHERE CLIENTS.BANK_ID=? AND CLIENTS.NAME=?";
    public static final String FIND_CLIENT_BY_ID_STMT = "SELECT * FROM CLIENTS WHERE CLIENTS.ID=?";

    @Override
    public Client findClientByName(Bank bank, String name) throws ClientNotFoundException {
        Client resultClient = null;
        Connection connection = null;
        ResultSet resultSet = null;
        try {
            connection = openConnection();
            PreparedStatement preparedStatement = connection.prepareStatement(FIND_CLIENT_BY_NAME_STMT);
            preparedStatement.setLong(1, bank.getId());
            preparedStatement.setString(2, name);
            resultSet = preparedStatement.executeQuery();
            resultClient = parseResultSetToGetOneClient(resultSet);
            AccountDAO accountDAO = new AccountDAOImpl();
            List<Account> accountList = accountDAO.getClientAccounts(resultClient.getId());
            HashSet<Account> accountSet = new HashSet<Account>(accountList);
            resultClient.setAccountsList(accountSet);
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            assert resultSet != null;
            try {
                resultSet.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
            closeConnection(connection);
        }
        return resultClient;
    }

    @Override
    public Client findClientById(int clientId) throws ClientNotFoundException {
        Connection connection = null;
        ResultSet resultSet = null;
        Client resultClient = null;
        try {
            connection = openConnection();
            PreparedStatement preparedStatement = connection.prepareStatement(FIND_CLIENT_BY_ID_STMT);
            preparedStatement.setLong(1, clientId);
            resultSet = preparedStatement.executeQuery();
            resultClient = parseResultSetToGetOneClient(resultSet);
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            assert resultSet != null;
            try {
                resultSet.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
            closeConnection(connection);
        }
        return resultClient;
    }

    @Override
    public List<Client> getAllClients(Bank bank) {
        return null;
    }

    @Override
    public void save(Client client) {

    }

    @Override
    public void remove(Client client) {

    }

    private Client parseResultSetToGetOneClient(ResultSet resultSet) throws SQLException {
        if (resultSet == null) {
            return null;
        }
        Client resultClient = new Client();
        while (resultSet.next()) {
            resultClient.setId(resultSet.getLong("ID"));
            resultClient.setName(resultSet.getString("NAME"));
            resultClient.setInitialOverdraft(resultSet.getFloat("OVERDRAFT"));
            if (resultSet.getInt("GENDER") == 0) {
                resultClient.setSex(Gender.FEMALE);
            } else {
                resultClient.setSex(Gender.MALE);
            }
            resultClient.setEmail(resultSet.getString("EMAIL"));
            resultClient.setPhone(resultSet.getString("PHONE"));
            resultClient.setCity(resultSet.getString("CITY"));
        }
        return resultClient;
    }
}
