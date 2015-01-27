package BankApplication.service.DAO;

import BankApplication.model.impl.Bank;

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
}

