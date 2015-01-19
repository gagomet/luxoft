package BankApplication.model;

import BankApplication.exceptions.*;
import BankApplication.exceptions.IllegalArgumentException;
import BankApplication.listeners.IClientRegistrationListener;
import BankApplication.model.client.Client;
import BankApplication.type.Gender;

import java.util.*;

/**
 * Created by Kir Kolesnikov on 14.01.2015.
 */
public class Bank {
    private Set<Client> clientsList = new HashSet<Client>();
    private List<IClientRegistrationListener> listeners = new ArrayList<IClientRegistrationListener>();
    private Map<String, Client> clientsNamesTable = new TreeMap<>();
    private static ResourceBundle feedBundle = ResourceBundle.getBundle("feedfile");
    private static ResourceBundle errorsBundle = ResourceBundle.getBundle("errors");

    public Bank() {

    }

    public Bank(List<IClientRegistrationListener> listenerList) {
        this.listeners = listenerList;
        listeners.add(new IClientRegistrationListener() {
            @Override
            public void onClientAdded(Client client) {
                System.out.println("This report printed by anonymous listener");
                System.out.println(new Date(System.currentTimeMillis()));
                if (!clientsNamesTable.keySet().contains(client.getName())) {
                    clientsNamesTable.put(client.getName(), client);
                }
            }
        });
    }

    public Set<Client> getClientsList() {
        return Collections.unmodifiableSet(clientsList);
    }

    public void setClientsList(Set<Client> clientsList) {
        this.clientsList = clientsList;
    }

    public List<IClientRegistrationListener> getListeners() {
        return listeners;
    }

    public void addClient(Client client) {
        clientsList.add(client);
    }

    public void removeClient(Client client) {
        clientsList.remove(client);
    }

    public void printReport() {
        System.out.println(toString());
    }

    public Client parseFeed(Map<String, String> feedMap) throws IllegalArgumentException {
        Client resultClient = null;
        if (clientsNamesTable.get(feedMap.get("name")) == null) {
            if (feedMap.containsKey(feedBundle.getString("gender"))) {
                Gender clientsGender = getGenderFromFeed(feedMap.get(feedBundle.getString("gender")));
                resultClient = new Client(clientsGender);
                resultClient.setName(feedMap.get(feedBundle.getString("name")));
            } else {
                resultClient = clientsNamesTable.get(feedBundle.getString("name"));
                //TODO add overdrafts
            }
        }
        return resultClient;
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

    public static class PrintClientListener implements IClientRegistrationListener {

        @Override
        public void onClientAdded(Client client) {
            System.out.println("This report printed by listener PrintClientListener");
            client.printReport();
        }
    }

    public static class EmailClientListener implements IClientRegistrationListener {

        @Override
        public void onClientAdded(Client client) {
            System.out.println("This report printed by listener EmailClientListener");
            System.out.println("Email to client was sent");
        }
    }

    private Gender getGenderFromFeed(String inputString) {
        if (inputString.equalsIgnoreCase("M")) {
            return Gender.MALE;
        }
        if (inputString.equalsIgnoreCase("F")) {
            return Gender.FEMALE;
        }
        throw new FeedException(errorsBundle.getString("wrongGender"));
    }

}
