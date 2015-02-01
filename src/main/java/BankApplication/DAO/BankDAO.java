package BankApplication.DAO;

import BankApplication.model.impl.Bank;
import BankApplication.model.impl.BankInfo;

/**
 * Created by Kir Kolesnikov on 27.01.2015.
 */
public interface BankDAO {
    /**
     * Finds Bank by its name.
     * Do not load the list of the clients.
     * @param name
     * @return
     */
    public Bank getBankByName(String name);

    /**
     * Should fill the BankInfo
     */
    public BankInfo getBankInfo(Bank bank);

    public void createNewBankInDB(Bank bank);

    public void saveChangesToBank(Bank changedBank); //full greed update of existing Bank

    public Bank getBankByID(long id); //greed getting of Bank instance

}

