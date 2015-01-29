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
    Bank getBankByName(String name);

    /**
     * Should fill the BankInfo
     */
    BankInfo getBankInfo(Bank bank);



}

