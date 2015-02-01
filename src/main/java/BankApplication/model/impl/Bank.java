package BankApplication.model.impl;

import BankApplication.annotation.NoDB;
import BankApplication.exceptions.FeedException;
import BankApplication.exceptions.IllegalArgumentException;
import BankApplication.model.ClientRegistrationListener;
import BankApplication.type.Gender;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.Set;
import java.util.TreeMap;

/**
 * Created by Kir Kolesnikov on 14.01.2015.
 */
public class Bank {
    private Long id;
    private String name;
    @NoDB
    private Set<Client> clientSet = new HashSet<Client>();
    @NoDB
    private List<ClientRegistrationListener> listeners = new ArrayList<ClientRegistrationListener>();
    @NoDB
    private Map<String, Client> clientsNamesTable = new TreeMap<>();
    @NoDB
    private static ResourceBundle errorsBundle = ResourceBundle.getBundle("errors");

    public Bank() {

    }

    public Bank(List<ClientRegistrationListener> listenerList) {
        this.listeners = listenerList;
        listeners.add(new ClientRegistrationListener() {
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

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Set<Client> getClientSet() {
        return Collections.unmodifiableSet(clientSet);
    }

    public void setClientSet(Set<Client> clientSet) {
        this.clientSet = clientSet;
    }

    public List<ClientRegistrationListener> getListeners() {
        return listeners;
    }

    //TODO remove to service
    public void addClient(Client client) {
        clientSet.add(client);
    }

    public void removeClient(Client client) {
        clientSet.remove(client);
    }

    public void printReport() {
        System.out.println(toString());
    }

    public Client parseFeed(Map<String, String> feedMap) throws IllegalArgumentException {
        Client resultClient = null;
        if (clientsNamesTable.get(feedMap.get("name")) == null) {
            if (feedMap.containsKey("gender")) {
                Gender clientsGender = getGenderFromFeed(feedMap.get("gender"));
                resultClient = new Client();
                resultClient.setName(feedMap.get("gender"));
                resultClient.setName(feedMap.get("name"));
                resultClient.setInitialOverdraft(Float.parseFloat(feedMap.get("overdraft")));
            } else {
                resultClient = clientsNamesTable.get("name");
            }
        }
        if (resultClient != null) {
            resultClient.parseFeed(feedMap);
        }
        return resultClient;
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        for (Client client : clientSet) {
            builder.append(client.toString());
            builder.append("\n");
        }
        return builder.toString();
    }

    public static class PrintClientListener implements ClientRegistrationListener {

        @Override
        public void onClientAdded(Client client) {
            System.out.println("This report printed by listener PrintClientListener");
            client.printReport();
        }
    }

    public static class EmailClientListener implements ClientRegistrationListener {

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
