package BankApplication.service.impl;

import BankApplication.DAO.BankDAO;
import BankApplication.DAO.ClientDAO;
import BankApplication.DAO.impl.DAOFactory;
import BankApplication.commander.BankHolder;
import BankApplication.exceptions.ClientExceedsException;
import BankApplication.model.impl.Bank;
import BankApplication.model.impl.BankInfo;
import BankApplication.model.impl.Client;
import BankApplication.service.BankService;

/**
 * Created by Kir Kolesnikov on 29.01.2015.
 */
public class BankServiceImpl implements BankService {

    private static Bank currentBank;
    private BankHolder holder;
    private BankDAO bankDAO;
    private ClientDAO clientDAO;

    private BankServiceImpl() {
    }

    private static class LazyHolder {
        private static final BankServiceImpl INSTANCE = new BankServiceImpl();
    }

    public BankDAO getBankDAO() {
        return bankDAO;
    }

    public void setBankDAO(BankDAO bankDAO) {
        this.bankDAO = bankDAO;
    }

    public ClientDAO getClientDAO() {
        return clientDAO;
    }

    public void setClientDAO(ClientDAO clientDAO) {
        this.clientDAO = clientDAO;
    }

    public BankHolder getHolder() {
        return holder;
    }

    public void setHolder(BankHolder holder) {
        this.holder = holder;
    }

    public static BankServiceImpl getInstance() {
        return LazyHolder.INSTANCE;
    }

    @Override
    public Bank getCurrentBank() {
        return currentBank;
    }

    @Override
    public void setCurrentBank(Bank currentBank) {
        this.currentBank = currentBank;
    }

    @Override
    public void addClient(Bank bank, Client client) throws ClientExceedsException {
        clientDAO.save(bank, client);
    } //Update

    @Override
    public Bank getBankByName(String bankName) {
        return bankDAO.getBankByName(bankName);
    }

    @Override
    public void removeClient(Bank bank, Client client) {
        clientDAO.remove(client);
    } //Update

    @Override
    public BankInfo getBankInfo(Bank bank) {
        return bankDAO.getBankInfo(bank);
    }

}
