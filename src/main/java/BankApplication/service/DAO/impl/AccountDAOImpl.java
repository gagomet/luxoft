package BankApplication.service.DAO.impl;

import BankApplication.model.Account;
import BankApplication.model.impl.CheckingAccount;
import BankApplication.model.impl.Client;
import BankApplication.model.impl.SavingAccount;
import BankApplication.service.DAO.AccountDAO;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Kir Kolesnikov on 27.01.2015.
 */
public class AccountDAOImpl extends BaseDAOImpl implements AccountDAO {
    public static final String GET_CLIENTS_ACCOUNTS_STMT = "SELECT * FROM ACCOUNTS WHERE CLIENT_ID=?";
    public static final String REMOVE_ALL_ACCOUNTS_BY_ID = "DELETE FROM ACCOUNTS WHERE CLIENT_ID=?";
    public static final String SAVE_NEW_ACCOUNT_TO_DB_STMT = "INSERT INTO ACCOUNTS " +
            "(CLIENT_ID, OVERDRAFT, BALANCE) VALUES (?, ?, ?)";


    @Override
    public void save(Account account, Client client) {
        Connection connection = null;
        try {
            connection = openConnection();
            connection.setAutoCommit(false);
            PreparedStatement preparedStatement = connection.prepareStatement(SAVE_NEW_ACCOUNT_TO_DB_STMT);
            preparedStatement.setLong(1, client.getId());
            if (account instanceof CheckingAccount) {
                CheckingAccount checkingAccount = (CheckingAccount) account;
                preparedStatement.setFloat(2, checkingAccount.getOverdraft());
            } else {
                preparedStatement.setNull(2, Types.BIGINT);
            }
            preparedStatement.setFloat(3, account.getBalance());
            preparedStatement.executeUpdate();
            connection.commit();
            connection.setAutoCommit(true);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


    @Override
    public void removeByClientId(long id) {
        Connection connection = null;
        try {
            connection = openConnection();
            connection.setAutoCommit(false);
            PreparedStatement preparedStatement = connection.prepareStatement(REMOVE_ALL_ACCOUNTS_BY_ID);
            preparedStatement.setLong(1, id);
            preparedStatement.executeUpdate();
            connection.commit();
            connection.setAutoCommit(true);
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            closeConnection(connection);
        }
    }

    @Override
    public List<Account> getClientAccounts(long id) {
        List<Account> resultList = new ArrayList<>();
        Connection connection = null;
        try {
            connection = openConnection();
            PreparedStatement preparedStatement = connection.prepareStatement(GET_CLIENTS_ACCOUNTS_STMT);
            preparedStatement.setLong(1, id);
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet == null) {
                return null;
            }
            while (resultSet.next()) {
                Account tempAccount;
                if (resultSet.getFloat("OVERDRAFT") == 0.0f) {
                    tempAccount = new SavingAccount();
                } else {
                    tempAccount = new CheckingAccount(resultSet.getFloat("OVERDRAFT"));
                }
                tempAccount.setBalance(resultSet.getFloat("BALANCE"));
                resultList.add(tempAccount);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            closeConnection(connection);
        }
        return resultList;
    }
}
