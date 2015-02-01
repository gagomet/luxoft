package BankApplication.DAO.impl;

import BankApplication.DAO.AccountDAO;
import BankApplication.DAO.BankDAO;
import BankApplication.DAO.ClientDAO;

/**
 * Created by Padonag on 01.02.2015.
 */
public class DAOFactory {

    public static BankDAO getBankDAO(){
        return BankDAOImpl.getInstance();
    }

    public static ClientDAO getClientDAO(){
        return ClientDAOImpl.getInstance();
    }

    public static AccountDAO getAccountDAO(){
        return AccountDAOImpl.getInstance();
    }
}
