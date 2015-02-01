package BankApplication.service.impl;

import BankApplication.DAO.impl.DAOFactory;
import BankApplication.exceptions.ClientExceedsException;
import BankApplication.model.impl.Bank;
import BankApplication.model.impl.BankInfo;
import BankApplication.model.impl.Client;
import BankApplication.service.BankService;

/**
 * Created by Kir Kolesnikov on 29.01.2015.
 */
public class BankServiceImpl implements BankService {
    private static BankServiceImpl instance;
    private static Bank currentBank;

    private BankServiceImpl(){
    }

    public static BankServiceImpl getInstance() {
        if (instance == null) {
            instance = new BankServiceImpl();
        }
        return instance;
    }

    public Bank getCurrentBank(){
        return currentBank;
    }


    public void setCurrentBank(Bank currentBank) {
        this.currentBank = currentBank;
    }

    @Override
    public void addClient(Bank bank, Client client) throws ClientExceedsException {
        DAOFactory.getClientDAO().save(bank, client);
    } //Update

    @Override
    public void removeClient(Bank bank, Client client) {
        DAOFactory.getClientDAO().remove(client);
    } //Update

    @Override
    public BankInfo getBankInfo(Bank bank) {
        return DAOFactory.getBankDAO().getBankInfo(bank);
    }

}
