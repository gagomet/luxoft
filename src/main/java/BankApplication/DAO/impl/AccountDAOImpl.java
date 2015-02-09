package BankApplication.DAO.impl;

import BankApplication.DAO.AccountDAO;
import BankApplication.model.Account;
import BankApplication.model.impl.CheckingAccount;
import BankApplication.model.impl.Client;
import BankApplication.model.impl.SavingAccount;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Kir Kolesnikov on 27.01.2015.
 */
public class AccountDAOImpl extends BaseDAOImpl implements AccountDAO {
    private static final String GET_CLIENTS_ACCOUNTS_STMT = "SELECT * FROM ACCOUNTS WHERE CLIENT_ID=?";
    private static final String GET_ACCOUNT_BY_ID_STMT = "SELECT * FROM ACCOUNTS WHERE ID=?";
    private static final String REMOVE_ALL_ACCOUNTS_BY_ID = "DELETE FROM ACCOUNTS WHERE CLIENT_ID=?";
    private static final String SAVE_NEW_ACCOUNT_TO_DB_STMT = "INSERT INTO ACCOUNTS " +
            "(CLIENT_ID, OVERDRAFT, BALANCE) VALUES (?, ?, ?)";
    private static final String UPDATE_ACCOUNT_IN_DB_STMT = "UPDATE ACCOUNTS " +
            "SET BALANCE=? WHERE ID=?";

    private AccountDAOImpl() {
    }

    private static class LazyHolder {
        private static final AccountDAOImpl INSTANCE = new AccountDAOImpl();
    }

    public static AccountDAOImpl getInstance() {
        return LazyHolder.INSTANCE;
    }


    @Override
    public synchronized void save(Account account, Client client) {
        try {

            setConnection(openConnection());
            getConnection().setAutoCommit(false);
            PreparedStatement preparedStatement = getConnection().prepareStatement(UPDATE_ACCOUNT_IN_DB_STMT);
            preparedStatement.setFloat(1, account.getBalance());
            preparedStatement.setLong(2, account.getId());
            preparedStatement.executeUpdate();
            getConnection().commit();
            getConnection().setAutoCommit(true);

        } catch (SQLException e) {
            try {
                getConnection().rollback();
            } catch (SQLException e1) {
                handleSQLException(e1);
            }
            handleSQLException(e);
        }
    }

    @Override
    public void removeByClientId(long id) {
        try {
            setConnection(openConnection());
            getConnection().setAutoCommit(false);
            PreparedStatement preparedStatement = getConnection().prepareStatement(REMOVE_ALL_ACCOUNTS_BY_ID);
            preparedStatement.setLong(1, id);
            preparedStatement.executeUpdate();
            getConnection().commit();
            getConnection().setAutoCommit(true);
        } catch (SQLException e) {
            try {
                getConnection().rollback();
            } catch (SQLException e1) {
                handleSQLException(e1);
            }
            handleSQLException(e);
        } finally {
            closeConnection(getConnection());
        }
    }

    @Override
    public List<Account> getClientAccounts(long id) {
        List<Account> resultList = new ArrayList<>();
        try {
            setConnection(openConnection());
            PreparedStatement preparedStatement = getConnection().prepareStatement(GET_CLIENTS_ACCOUNTS_STMT);
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
            handleSQLException(e);
        } finally {
            closeConnection(getConnection());
        }
        return resultList;
    }

    @Override
    public Account addAccount(Account account, Client client) {
        try {
            setConnection(openConnection());
            getConnection().setAutoCommit(false);
            PreparedStatement preparedStatement = getConnection().prepareStatement(SAVE_NEW_ACCOUNT_TO_DB_STMT);
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
            getConnection().commit();
            getConnection().setAutoCommit(true);
        } catch (SQLException e) {
            try {
                getConnection().rollback();
            } catch (SQLException e1) {
                handleSQLException(e1);
            }
            handleSQLException(e);
        } finally {
            closeConnection(getConnection());
        }
        return account;
    }

    public void transferFunds(Account sender, Account recipient, float amount) {
        try {
            setConnection(openConnection());
            getConnection().setAutoCommit(false);
            PreparedStatement preparedStatement = getConnection().prepareStatement(UPDATE_ACCOUNT_IN_DB_STMT);
            preparedStatement.setFloat(1, sender.getBalance() - amount);
            preparedStatement.setLong(2, sender.getId());
            preparedStatement.executeUpdate();
            preparedStatement.setFloat(1, recipient.getBalance() + amount);
            preparedStatement.setLong(2, recipient.getId());
            preparedStatement.executeUpdate();
            getConnection().commit();
            getConnection().setAutoCommit(true);

        } catch (SQLException e) {
            try {
                getConnection().rollback();
            } catch (SQLException e1) {
                handleSQLException(e1);
            }
            handleSQLException(e);
        } finally {
            closeConnection(getConnection());
        }
    }

    @Override
    public Account getAccountById(long id) {
        Account result = null;
        try {
            setConnection(openConnection());
            PreparedStatement preparedStatement = getConnection().prepareStatement(GET_ACCOUNT_BY_ID_STMT);
            preparedStatement.setLong(1, id);
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet == null) {
                return null;
            }
            while (resultSet.next()) {
                if (resultSet.getFloat("OVERDRAFT") == 0.0f) {
                    result = new SavingAccount();
                } else {
                    result = new CheckingAccount(resultSet.getFloat("OVERDRAFT"));
                }
                result.setId(resultSet.getLong("ID"));
                result.setClientId(resultSet.getLong("CLIENT_ID"));
                result.setBalance(resultSet.getFloat("BALANCE"));
            }
        } catch (SQLException e) {
            handleSQLException(e);
        }
        return result;
    }

}
