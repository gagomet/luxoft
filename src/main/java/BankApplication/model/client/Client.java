package BankApplication.model.client;

import BankApplication.exceptions.IllegalArgumentException;
import BankApplication.model.account.IAccount;
import BankApplication.model.account.impl.AbstractAccount;
import BankApplication.model.account.impl.CheckingAccount;
import BankApplication.model.account.impl.SavingAccount;
import BankApplication.type.Gender;

import java.io.Serializable;
import java.util.Collections;
import java.util.HashSet;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.Set;

/**
 * Created by Kir Kolesnikov on 14.01.2015.
 */
public class Client implements IReport, Serializable {
    private String name;
    private Set<AbstractAccount> accountsList = new HashSet<>();
    private transient AbstractAccount activeAccount;
    private float initialOverdraft;
    private Gender sex;
    private String city;
    private String email;
    private String phone;
    private static ResourceBundle bundle = ResourceBundle.getBundle("strings");
    private static ResourceBundle feedBundle = ResourceBundle.getBundle("feedfile");

    public Client(){

    }

    public Client(Gender sex) throws BankApplication.exceptions.IllegalArgumentException {
        this.sex = sex;
//        activeAccount = AccountType.SAVING.create();
//        accountsList.add(activeAccount);
//        setActiveAccount(activeAccount);
    }

    /*public Client(float initialOverdraft, Gender sex) throws IllegalArgumentException {
        this.sex = sex;
        this.initialOverdraft = initialOverdraft;
        activeAccount = AccountType.CHECKING.create();
        accountsList.add(activeAccount);
        setActiveAccount(activeAccount);
    }*/

    public AbstractAccount parseFeed(Map<String, String> feedMap) throws IllegalArgumentException {
        AbstractAccount resultAccount = null;
        if (feedMap.get(feedBundle.getString("accountType")).equalsIgnoreCase(feedBundle.getString("checkedAccount"))) {
            resultAccount = new CheckingAccount();
        } else if (feedMap.get(feedBundle.getString("accountType")).equalsIgnoreCase(feedBundle.getString("savingAccount"))) {
            resultAccount = new SavingAccount();
        }
        accountsList.add(resultAccount);
        if (resultAccount != null) {
            resultAccount.parseFeed(feedMap);
        }
        return resultAccount;
    }

    public float getInitialOverdraft() {
        return initialOverdraft;
    }

    public void setInitialOverdraft(float initialOverdraft) {
        this.initialOverdraft = initialOverdraft;
    }

    public void setActiveAccount(AbstractAccount activeAccount) {
        this.activeAccount = activeAccount;
    }

    public AbstractAccount getActiveAccount() {
        return activeAccount;
    }

    public Set<AbstractAccount> getAccountsList() {
        return Collections.unmodifiableSet(accountsList);
    }

    public void setAccountsList(Set<AbstractAccount> accountsList) {
        this.accountsList = accountsList;
    }

    public float getBalance() {
        return activeAccount.getBalance();
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    @Override
    public void printReport() {
        System.out.println(this.toString());
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append(bundle.getString("clientName"));
        builder.append(" ");
        builder.append(sex.getGenderPrefix());
        builder.append(getName());
        builder.append(" ");
        for (IAccount account : accountsList) {
            builder.append(account.toString());
            builder.append("\n");
        }
        return builder.toString();
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Client)) return false;

        Client client = (Client) o;

        if (Float.compare(client.initialOverdraft, initialOverdraft) != 0) return false;
        if (!accountsList.equals(client.accountsList)) return false;
        if (city != null ? !city.equals(client.city) : client.city != null) return false;
        if (email != null ? !email.equals(client.email) : client.email != null) return false;
        if (!name.equals(client.name)) return false;
        if (phone != null ? !phone.equals(client.phone) : client.phone != null) return false;
        if (sex != client.sex) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = name.hashCode();
        result = 31 * result + accountsList.hashCode();
        result = 31 * result + (initialOverdraft != +0.0f ? Float.floatToIntBits(initialOverdraft) : 0);
        result = 31 * result + sex.hashCode();
        result = 31 * result + (city != null ? city.hashCode() : 0);
        result = 31 * result + (email != null ? email.hashCode() : 0);
        result = 31 * result + (phone != null ? phone.hashCode() : 0);
        return result;
    }
}
