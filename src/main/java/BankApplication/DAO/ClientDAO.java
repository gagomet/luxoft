package BankApplication.DAO;

import BankApplication.exceptions.ClientNotFoundException;
import BankApplication.model.impl.Bank;
import BankApplication.model.impl.Client;

import java.util.List;

/**
 * Created by Kir Kolesnikov on 27.01.2015.
 */
public interface ClientDAO {
    /**
     * Return client by its name, initialize client accounts.
     * @param bank
     * @param name
     * @return
     */
    Client findClientByName(Bank bank, String name) throws ClientNotFoundException;

    /**
     * Return client by its name, initialize client accounts.
     * @param clientId
     * @param
     * @return
     */
    Client findClientById(long clientId) throws ClientNotFoundException;

    /**
     * Returns the list of all clients of the Bank
     * and their accounts
     * @param bank
     * @return
     */
    List<Client> getAllClients(Bank bank);

    /**
     * Method should insert new Client (if id==null)
     * or update client in database (if id!=null)
     * @param client
     */
    Client save(Bank bank, Client client);

    /**
     * Method removes client from Database
     * @param client
     */
    void remove(Client client);
}
