package BankApplication.DAO.impl;

import BankApplication.exceptions.ClientNotFoundException;
import BankApplication.model.Account;
import BankApplication.model.impl.Bank;
import BankApplication.model.impl.CheckingAccount;
import BankApplication.model.impl.Client;
import BankApplication.model.impl.SavingAccount;
import BankApplication.DAO.AccountDAO;
import BankApplication.DAO.ClientDAO;
import BankApplication.type.Gender;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

/**
 * Created by Kir Kolesnikov on 27.01.2015.
 */
public class ClientDAOImpl extends BaseDAOImpl implements ClientDAO {
    private static ClientDAOImpl instance;
    private static final String FIND_CLIENT_BY_NAME_STMT = "SELECT * FROM CLIENTS WHERE CLIENTS.BANK_ID=? AND CLIENTS.NAME=?";
    private static final String FIND_CLIENT_BY_ID_STMT = "SELECT * FROM CLIENTS WHERE CLIENTS.ID=?";
    private static final String GET_ALL_CLIENTS = "SELECT * FROM CLIENTS WHERE CLIENTS.BANK_ID=?";
    private static final String REMOVE_CLIENT_FROM_DB = "DELETE FROM CLIENTS WHERE ID=?";
    private static final String INSERT_CLIENT_INTO_DB = "INSERT INTO CLIENTS " +
            "(BANK_ID, NAME, OVERDRAFT, GENDER, EMAIL, CITY) VALUES (?, ?, ?, ?, ?, ?)";
    private static final String UPDATE_CLIENT_IN_DB = "UPDATE CLIENTS SET " +
            "BANK_ID=?, NAME=?, OVERDRAFT=?, GENDER=?, EMAIL=?, CITY=? WHERE ID=? ";

    private ClientDAOImpl(){}

    public static ClientDAOImpl getInstance(){
        if (instance == null) {
            return new ClientDAOImpl();
        }
        return instance;
    }

    @Override
    public Client findClientByName(Bank bank, String name) throws ClientNotFoundException {
        Client resultClient = null;
        ResultSet resultSet = null;
        try {
            setConnection(openConnection());
            PreparedStatement preparedStatement = getConnection().prepareStatement(FIND_CLIENT_BY_NAME_STMT);
            preparedStatement.setLong(1, bank.getId());
            preparedStatement.setString(2, name);
            resultSet = preparedStatement.executeQuery();
            resultClient = parseResultSetToGetOneClient(resultSet);
            List<Account> accountList = DAOFactory.getAccountDAO().getClientAccounts(resultClient.getId());
            if (accountList.size() == 1) {
                resultClient.setActiveAccount(accountList.get(0));
            }
            HashSet<Account> accountSet = new HashSet<Account>(accountList);
            resultClient.setAccountsList(accountSet);
        } catch (SQLException e) {
            handleSQLException(e);
        } finally {
            closeConnection(getConnection());
        }
        return resultClient;
    }

    @Override
    public Client findClientById(long clientId) throws ClientNotFoundException {
        ResultSet resultSet = null;
        Client resultClient = null;
        try {
            setConnection(openConnection());
            PreparedStatement preparedStatement = getConnection().prepareStatement(FIND_CLIENT_BY_ID_STMT);
            preparedStatement.setLong(1, clientId);
            resultSet = preparedStatement.executeQuery();
            resultClient = parseResultSetToGetOneClient(resultSet);
            List<Account> accountList = DAOFactory.getAccountDAO().getClientAccounts(resultClient.getId());
            if (accountList.size() == 1) {
                resultClient.setActiveAccount(accountList.get(0));
            }
            HashSet<Account> accountSet = new HashSet<Account>(accountList);
            resultClient.setAccountsList(accountSet);

        } catch (SQLException e) {
            handleSQLException(e);
        } finally {
            assert resultSet != null;
            try {
                resultSet.close();
            } catch (SQLException e) {
                handleSQLException(e);
            }
            closeConnection(getConnection());
        }
        return resultClient;
    }

    @Override
    public List<Client> getAllClients(Bank bank) {
        List<Client> resultList = new ArrayList<>();
        ResultSet resultSet = null;
        try {
            setConnection(openConnection());
            PreparedStatement preparedStatement = getConnection().prepareStatement(GET_ALL_CLIENTS);
            preparedStatement.setLong(1, bank.getId());
            resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                Client tempClient = new Client();
                tempClient.setId(resultSet.getLong("ID"));
                tempClient.setName(resultSet.getString("NAME"));
                tempClient.setBankId(resultSet.getLong("BANK_ID"));
                tempClient.setInitialOverdraft(resultSet.getFloat("OVERDRAFT"));
                if (resultSet.getInt("GENDER") == 0) {
                    tempClient.setSex(Gender.FEMALE);
                } else {
                    tempClient.setSex(Gender.MALE);
                }
                tempClient.setEmail(resultSet.getString("EMAIL"));
                tempClient.setPhone(resultSet.getString("PHONE"));
                tempClient.setCity(resultSet.getString("CITY"));
                List<Account> accountList = DAOFactory.getAccountDAO().getClientAccounts(tempClient.getId());
                HashSet<Account> accountSet = new HashSet<Account>(accountList);
                tempClient.setAccountsList(accountSet);
                resultList.add(tempClient);
            }
        } catch (SQLException e) {
            handleSQLException(e);
        } finally {
            assert resultSet != null;
            try {
                resultSet.close();
            } catch (SQLException e) {
                handleSQLException(e);
            }
            closeConnection(getConnection());
        }
        return resultList;
    }

    @Override
    public void save(Bank bank, Client client) {
        try {
            setConnection(openConnection());
            getConnection().setAutoCommit(false);
            Client currentClient = findClientById(client.getId());
            PreparedStatement preparedStatement;

            if (currentClient.getId() != 0) {
                preparedStatement = getConnection().prepareStatement(UPDATE_CLIENT_IN_DB);
                setPreparedStatementDataForClient(preparedStatement, client);
                preparedStatement.setLong(7, client.getId());
            } else {
                preparedStatement = getConnection().prepareStatement(INSERT_CLIENT_INTO_DB);
                setPreparedStatementDataForClient(preparedStatement, client);

            }
            preparedStatement.setLong(1, bank.getId());
            int changes = preparedStatement.executeUpdate();
            if (changes != 0) {
                try (ResultSet generatedKeys = preparedStatement.getGeneratedKeys()) {
                    if (generatedKeys.next()) {
                        client.setId(generatedKeys.getLong(1));
                    } else {
                        throw new SQLException("Creating user failed, no ID obtained.");
                    }
                }
            }
            getConnection().commit();
            getConnection().setAutoCommit(true);

            if (client.getInitialOverdraft() == 0.0f) {
                DAOFactory.getAccountDAO().addAccount(new SavingAccount(), client);
            } else {
                DAOFactory.getAccountDAO().addAccount(new CheckingAccount(client.getInitialOverdraft()), client);
            }
        } catch (SQLException | ClientNotFoundException e) {
            try {
                getConnection().rollback();
            } catch (SQLException e1) {
                handleSQLException(e1);
            }
            e.getMessage();
        }
    }

    @Override
    public void remove(Client client) {
        try {
            setConnection(openConnection());
            getConnection().setAutoCommit(false);
            DAOFactory.getAccountDAO().removeByClientId(client.getId());
            PreparedStatement preparedStatement = getConnection().prepareStatement(REMOVE_CLIENT_FROM_DB);
            preparedStatement.setLong(1, client.getId());
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

    private void setPreparedStatementDataForClient(PreparedStatement preparedStatement, Client client) throws SQLException {

        if (client.getName() == null) {
            preparedStatement.setNull(2, Types.VARCHAR);
        } else {
            preparedStatement.setString(2, client.getName());
        }
        if (client.getInitialOverdraft() == null) {
            preparedStatement.setNull(3, Types.REAL);
        } else {
            preparedStatement.setFloat(3, client.getInitialOverdraft());
        }
        if (client.getSex() == Gender.MALE) {
            preparedStatement.setInt(4, 1);
        } else {
            preparedStatement.setInt(4, 0);
        }
        if (client.getEmail() == null) {
            preparedStatement.setNull(5, Types.VARCHAR);
        } else {
            preparedStatement.setString(5, client.getEmail());
        }
        if (client.getCity() == null) {
            preparedStatement.setNull(6, Types.VARCHAR);
        } else {
            preparedStatement.setString(6, client.getCity());
        }

    }

    private Client parseResultSetToGetOneClient(ResultSet resultSet) throws SQLException {
        if (resultSet == null) {
            return null;
        }
        Client resultClient = new Client();
        while (resultSet.next()) {
            resultClient.setId(resultSet.getLong("ID"));
            resultClient.setName(resultSet.getString("NAME"));
            resultClient.setBankId(resultSet.getLong("BANK_ID"));
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
