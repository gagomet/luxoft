package BankApplication.DAO.impl;

import BankApplication.DAO.BankDAO;
import BankApplication.exceptions.DAOException;
import BankApplication.model.impl.Bank;
import BankApplication.model.impl.BankInfo;
import BankApplication.model.impl.Client;
import BankApplication.service.impl.BankReport;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Created by Kir Kolesnikov on 27.01.2015.
 */
public class BankDAOImpl extends BaseDAOImpl implements BankDAO {
    private static BankDAOImpl instance;
    private static final String GET_BANK_BY_ID_STMT = "SELECT * FROM BANKS WHERE BANKS.ID = ? ";
    private static final String GET_BANK_BY_NAME_STMT = "SELECT * FROM BANKS WHERE BANKS.NAME = ? ";
    private static final String GET_NUMBER_OF_CLIENTS_STMT = "SELECT COUNT(ID) FROM CLIENTS";
    private static final String GET_TOTAL_FUNDS_STMT = "SELECT SUM(BALANCE) FROM ACCOUNTS";
    private static final String INSERT_NEW_BANK = "INSERT INTO BANKS(NAME) VALUES (?)";
    private static final String UPDATE_BANK_STMT = "UPDATE BANKS SET BANKS.NAME=? WHERE BANKS.ID=?";

    private BankDAOImpl() {

    }

    public static BankDAOImpl getInstance() {
        if (instance == null) {
            return new BankDAOImpl();
        }
        return instance;
    }

    @Override
    public void createNewBankInDB(Bank bank) {
        setConnection(openConnection());
        try {
            getConnection().setAutoCommit(false);

            PreparedStatement preparedStatement = getConnection().prepareStatement(INSERT_NEW_BANK);
            preparedStatement.setString(1, bank.getName());
            preparedStatement.executeUpdate();
            Set<Client> newBankClients = bank.getClientSet();
            for (Client client : newBankClients) {
                DAOFactory.getClientDAO().save(bank, client);
            }
            getConnection().commit();
            getConnection().setAutoCommit(false);
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

    public Bank getBankByID(long id) { //greed getting of Bank instance
        Bank result = new Bank();
        ResultSet resultSet;
        setConnection(openConnection());
        try {
            PreparedStatement preparedStatement = getConnection().prepareStatement(GET_BANK_BY_ID_STMT);
            preparedStatement.setLong(1, id);
            resultSet = preparedStatement.executeQuery();
            if (resultSet != null) {
                while (resultSet.next()) {
                    result.setId(resultSet.getLong("ID"));
                    result.setName(resultSet.getString("NAME"));
                }
                List<Client> clientList = DAOFactory.getClientDAO().getAllClients(result);
                result.setClientSet(new HashSet<Client>(clientList));
            } else {
                throw new DAOException("Can't find bank with id " + id);
            }

        } catch (SQLException e) {
            handleSQLException(e);
        } finally {
            closeConnection(getConnection());
        }
        return result;
    }

    public void saveChangesToBank(Bank changedBank){ //full greed update of existing Bank
        setConnection(openConnection());
        try {
            getConnection().setAutoCommit(false);
            PreparedStatement preparedStatement = getConnection().prepareStatement(UPDATE_BANK_STMT);
            preparedStatement.setString(1, changedBank.getName());
            int changes = preparedStatement.executeUpdate();
            if (changes != 0) {
                try (ResultSet generatedKeys = preparedStatement.getGeneratedKeys()) {
                    if (generatedKeys.next()) {
                        changedBank.setId(generatedKeys.getLong(1));
                    } else {
                        throw new SQLException("Updating Bank failed, no ID obtained.");
                    }
                }
                List<Client> clientList = DAOFactory.getClientDAO().getAllClients(changedBank);
                for(Client tempClient : clientList){
                    DAOFactory.getClientDAO().save(changedBank, tempClient);
                }
            } else {
                throw new DAOException("Can't update the Bank");
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
        }
    }

    @Override
    public Bank getBankByName(String name) { //lazy getting of Bank instance
        Bank resultBank = null;
        ResultSet resultSet = null;
        try {
            setConnection(openConnection());
            PreparedStatement preparedStatement = getConnection().prepareStatement(GET_BANK_BY_NAME_STMT);
            preparedStatement.setString(1, name);
            resultSet = preparedStatement.executeQuery();
            if (resultSet == null) {
                throw new DAOException("Unable to get Bank from DB");
            } else {
                resultBank = new Bank();
                while (resultSet.next()) {
                    resultBank.setId(resultSet.getLong("ID"));
                    resultBank.setName(resultSet.getString("NAME"));
                }
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
            closeConnection(getConnection());
        }
        return resultBank;
    }

    @Override
    public BankInfo getBankInfo(Bank bank) {
        BankInfo result = new BankInfo();
        int count = 0;
        float summ = 0f;
        try {
            setConnection(openConnection());
            Statement statement = getConnection().createStatement();
            ResultSet resultSet = statement.executeQuery(GET_NUMBER_OF_CLIENTS_STMT);
            while (resultSet.next()) {
                count = resultSet.getInt(1);
            }
            result.setTotalClients(count);
            resultSet = statement.executeQuery(GET_TOTAL_FUNDS_STMT);
            while (resultSet.next()) {
                summ = resultSet.getFloat(1);
            }
            result.setTotalCredit(summ);
            List<Client> clientList = DAOFactory.getClientDAO().getAllClients(bank);
            result.setClientsByCities(BankReport.getClientsFromListToMapByCity(clientList));
        } catch (SQLException e) {
            handleSQLException(e);
        } finally {
            closeConnection(getConnection());
        }
        return result;
    }

}
