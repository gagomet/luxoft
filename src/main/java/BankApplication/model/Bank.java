package BankApplication.model;

import BankApplication.listeners.IClientRegistrationListener;
import BankApplication.model.client.Client;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.ResourceBundle;

/**
 * Created by Kir Kolesnikov on 14.01.2015.
 */
public class Bank {
    private List<Client> clientsList = new ArrayList<Client>();
    private List<IClientRegistrationListener> listeners = new ArrayList<IClientRegistrationListener>();

    public Bank(){

    }

    public Bank (List<IClientRegistrationListener> listenerList){
        this.listeners=listenerList;
//        this.listeners.add(new PrintClientListener());
//        this.listeners.add(new EmailClientListener());
        listeners.add(new IClientRegistrationListener() {
            @Override
            public void onClientAdded(Client client) {
                System.out.println("This report printed by anonymous listener");
                System.out.println(new Date(System.currentTimeMillis()));
            }
        });
    }

    public List<Client> getClientsList() {
        return clientsList;
    }

    public void setClientsList(List<Client> clientsList) {
        this.clientsList = clientsList;
    }

    public List<IClientRegistrationListener> getListeners() {
        return listeners;
    }

    public void printReport() {
//        for (Client client : clientsList) {
//            client.printReport();
//        }
        System.out.println(toString());
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        for (Client client : clientsList) {
            builder.append(client.toString());
            builder.append("\n");
        }
        return builder.toString();
    }

    public static class PrintClientListener implements IClientRegistrationListener{

        @Override
        public void onClientAdded(Client client) {
            System.out.println("This report printed by listener PrintClientListener");
            client.printReport();
        }
    }

   public static class EmailClientListener implements IClientRegistrationListener{

        @Override
        public void onClientAdded(Client client) {
            System.out.println("This report printed by listener EmailClientListener");
            System.out.println("Email to client was sent");
        }
    }
}
