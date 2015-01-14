package BankApplication.bank.listeners;

import BankApplication.client.Client;

/**
 * Created by Kir Kolesnikov on 14.01.2015.
 */
public interface IClientRegistrationListener {
    void onClientAdded(Client client);
}
