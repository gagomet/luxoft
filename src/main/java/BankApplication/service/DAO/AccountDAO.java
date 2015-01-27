package BankApplication.service.DAO;

import BankApplication.model.Account;
import BankApplication.model.impl.Client;

import java.util.List;

/**
 * Created by Kir Kolesnikov on 27.01.2015.
 */
public interface AccountDAO {
    /**
     * Save Account in database
     * @param client
     */
    public void save(Account account, Client client);

    /**
     * Remove all accounts of client
     * @param id Id of the client
     */
    public void removeByClientId(long id);

    /**
     * Get all accounts of the client
     * @param id Id of the client
     */
    public List<Account> getClientAccounts(long id);
}
