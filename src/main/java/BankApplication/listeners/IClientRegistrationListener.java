package BankApplication.listeners;

import BankApplication.BankApplication;
import BankApplication.model.client.Client;

/**
 * Created by Kir Kolesnikov on 14.01.2015.
 */
public interface IClientRegistrationListener {
    void onClientAdded(Client client);
}
