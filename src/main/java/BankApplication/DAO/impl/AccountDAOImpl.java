package BankApplication.DAO.impl;

import BankApplication.model.Account;
import BankApplication.model.impl.CheckingAccount;
import BankApplication.model.impl.Client;
import BankApplication.model.impl.SavingAccount;
import BankApplication.DAO.AccountDAO;

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
    public static final String UPDATE_ACCOUNT_IN_DB_STMT = "UPDATE ACCOUNTS " +
            "SET BALANCE=? WHERE ID=?";

    public AccountDAOImpl() {
    }

    @Override
    public void save(Account account, Client client) {
        Connection connection = null;
        try {
            connection = openConnection();
            connection.setAutoCommit(false);
            PreparedStatement preparedStatement = connection.prepareStatement(UPDATE_ACCOUNT_IN_DB_STMT);
            preparedStatement.setFloat(1, account.getBalance());
            preparedStatement.setLong(2, account.getId());
            preparedStatement.executeUpdate();
            connection.commit();
            connection.setAutoCommit(true);
        } catch (SQLException e) {
            try {
                connection.rollback();
            } catch (SQLException e1) {
                handleSQLException(e1);
            }
            handleSQLException(e);
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
            try {
                connection.rollback();
            } catch (SQLException e1) {
                handleSQLException(e1);
            }
            handleSQLException(e);
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
                tempAccount.setId(resultSet.getLong("ID"));
                tempAccount.setClientId(resultSet.getLong("CLIENT_ID"));
                tempAccount.setBalance(resultSet.getFloat("BALANCE"));
                resultList.add(tempAccount);
            }
        } catch (SQLException e) {
            try {
                connection.rollback();
            } catch (SQLException e1) {
                handleSQLException(e1);
            }
            handleSQLException(e);
        } finally {
            closeConnection(connection);
        }
        return resultList;
    }

    @Override
    public void addAccount(Account account, Client client) {
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
                preparedStatement.setNull(2, Types.REAL);
            }
            preparedStatement.setFloat(3, account.getBalance());
            int changes = preparedStatement.executeUpdate();
            if (changes != 0) {
                try (ResultSet generatedKeys = preparedStatement.getGeneratedKeys()) {
                    if (generatedKeys.next()) {
                        account.setId(generatedKeys.getLong(1));
                    } else {
                        throw new SQLException("Creating account failed, no ID obtained.");
                    }
                }
            }
            connection.commit();
            connection.setAutoCommit(true);
        } catch (SQLException e) {
            try {
                connection.rollback();
            } catch (SQLException e1) {
                handleSQLException(e1);
            }
            handleSQLException(e);
        } finally {
            closeConnection(connection);
        }
    }

    public void transferFunds(Account sender, Account recipient, float amount) {
        Connection connection = null;
        try {
            connection = openConnection();
            connection.setAutoCommit(false);
            PreparedStatement preparedStatement = connection.prepareStatement(UPDATE_ACCOUNT_IN_DB_STMT);
            preparedStatement.setFloat(1, sender.getBalance() - amount);
            preparedStatement.setLong(2, sender.getId());
            preparedStatement.executeUpdate();
            preparedStatement.setFloat(1, recipient.getBalance() + amount);
            preparedStatement.setLong(2, recipient.getId());
            preparedStatement.executeUpdate();
            connection.commit();
            connection.setAutoCommit(true);

        } catch (SQLException e) {
            try {
                connection.rollback();
            } catch (SQLException e1) {
                handleSQLException(e1);
            }
            handleSQLException(e);
        }
    }

}
