package BankApplication.model;

import BankApplication.model.Client;

/**
 * Created by Kir Kolesnikov on 14.01.2015.
 */
public interface ClientRegistrationListener {
    void onClientAdded(Client client);
}
