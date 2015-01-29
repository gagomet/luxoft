package BankApplication.service.impl;

import BankApplication.DAO.BankDAO;
import BankApplication.DAO.ClientDAO;
import BankApplication.DAO.impl.BankDAOImpl;
import BankApplication.DAO.impl.ClientDAOImpl;
import BankApplication.exceptions.ClientExceedsException;
import BankApplication.model.impl.Bank;
import BankApplication.model.impl.BankInfo;
import BankApplication.model.impl.Client;
import BankApplication.service.BankService;

/**
 * Created by Kir Kolesnikov on 29.01.2015.
 */
public class BankServiceImpl implements BankService {
    private BankDAO bankDAO = new BankDAOImpl();
    private ClientDAO clientDAO = new ClientDAOImpl();

    @Override
    public void addClient(Bank bank, Client client) throws ClientExceedsException {
        clientDAO.save(bank, client);
    }

    @Override
    public void removeClient(Bank bank, Client client) {
        clientDAO.remove(client);
    }

    @Override
    public BankInfo getBankInfo(Bank bank) {
        return bankDAO.getBankInfo(bank);
    }
}
