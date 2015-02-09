package BankApplication.commander;

import BankApplication.model.impl.Client;

/**
 * Created by Kir Kolesnikov on 06.02.2015.
 */
public interface CommandsManager {
    public Client getCurrentClient();

    public void setCurrentClient(Client client);
}
