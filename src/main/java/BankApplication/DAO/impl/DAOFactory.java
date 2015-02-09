package BankApplication.DAO.impl;

import BankApplication.DAO.AccountDAO;
import BankApplication.DAO.BankDAO;
import BankApplication.DAO.ClientDAO;

import java.sql.Connection;

/**
 * Created by Padonag on 01.02.2015.
 */
public class DAOFactory {
    private static Connection connection;

    static {
        BaseDAOImpl baseDAO = new BaseDAOImpl();
        connection = baseDAO.openConnection();
    }

    public static BankDAO getBankDAO() {
        return BankDAOImpl.getInstance();
    }

    public static ClientDAO getClientDAO() {
        return ClientDAOImpl.getInstance();
    }

    public static AccountDAO getAccountDAO() {
        return AccountDAOImpl.getInstance();
    }
}
