package BankApplication.model.impl;

import BankApplication.annotation.NoDB;
import BankApplication.model.Account;
import BankApplication.model.Report;
import BankApplication.service.Persistable;
import BankApplication.type.Gender;

import java.io.Serializable;
import java.util.Collections;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

/**
 * Created by Kir Kolesnikov on 14.01.2015.
 */
public class Client implements Report, Serializable, Persistable {
    @NoDB
    private long id;
    private long bankId;
    private String name;
    private Set<Account> accountsList = new TreeSet<>();
    @NoDB
    private transient Account activeAccount;
    private Float initialOverdraft;
    private Gender sex;
    private String city;
    private String email;
    private String phone;

    public Client() {
    }

    public Client(Gender sex) throws IllegalArgumentException {
        this.sex = sex;
    }

    public Account parseFeed(Map<String, String> feedMap) {
        AbstractAccount resultAccount = null;
        if (feedMap.get("accounttype").equalsIgnoreCase("c")) {
            resultAccount = new CheckingAccount();
        } else if (feedMap.get("accounttype").equalsIgnoreCase("s")) {
            resultAccount = new SavingAccount();
        }
        activeAccount = resultAccount;
        accountsList.add(resultAccount);
        if (resultAccount != null) {
            resultAccount.parseFeed(feedMap);
        }
        return resultAccount;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public long getBankId() {
        return bankId;
    }

    public void setBankId(long bankId) {
        this.bankId = bankId;
    }

    public Float getInitialOverdraft() {
        return initialOverdraft;
    }

    public void setInitialOverdraft(Float initialOverdraft) {
        this.initialOverdraft = initialOverdraft;
    }

    public void setActiveAccount(Account activeAccount) {
        this.activeAccount = activeAccount;
    }

    public Account getActiveAccount() {
        return activeAccount;
    }

    public Set<Account> getAccountsList() {
        return Collections.unmodifiableSet(accountsList);
    }

    public void setAccountsList(Set<Account> accountsList) {
        this.accountsList = accountsList;
    }

    public void addAccount(Account account) {
        accountsList.add(account);
    }

    public float getFullClientBalance() {
        float result = 0.0f;
        for (Account acc : accountsList) {
            result += acc.getBalance();
        }
        return result;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public Gender getSex() {
        return sex;
    }

    public void setSex(Gender sex) {
        this.sex = sex;
    }

    @Override
    public void printReport() {
        System.out.println(toString());
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append("Client name: ");
        if (sex != null) {
            builder.append(sex.getGenderPrefix());
        } else{
            builder.append("Smth. ");
        }
        builder.append(getName());
        builder.append(" ");
        for (Account account : accountsList) {
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

        if (bankId != client.bankId) return false;
        if (accountsList != null ? !accountsList.equals(client.accountsList) : client.accountsList != null)
            return false;
        if (city != null ? !city.equals(client.city) : client.city != null) return false;
        if (email != null ? !email.equals(client.email) : client.email != null) return false;
        if (initialOverdraft != null ? !initialOverdraft.equals(client.initialOverdraft) : client.initialOverdraft != null)
            return false;
        if (name != null ? !name.equals(client.name) : client.name != null) return false;
        if (phone != null ? !phone.equals(client.phone) : client.phone != null) return false;
        if (sex != client.sex) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = (int) (bankId ^ (bankId >>> 32));
        result = 31 * result + (name != null ? name.hashCode() : 0);
        result = 31 * result + (accountsList != null ? accountsList.hashCode() : 0);
        result = 31 * result + (initialOverdraft != null ? initialOverdraft.hashCode() : 0);
        result = 31 * result + (sex != null ? sex.hashCode() : 0);
        result = 31 * result + (city != null ? city.hashCode() : 0);
        result = 31 * result + (email != null ? email.hashCode() : 0);
        result = 31 * result + (phone != null ? phone.hashCode() : 0);
        return result;
    }
}
