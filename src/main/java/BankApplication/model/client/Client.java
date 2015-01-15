package BankApplication.model.client;

import BankApplication.account.IAccount;
import BankApplication.account.impl.AbstractAccount;
import BankApplication.type.AccountType;
import BankApplication.type.Gender;

import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

/**
 * Created by Kir Kolesnikov on 14.01.2015.
 */
public class Client implements IReport {
    private String name;
    private List<AbstractAccount> accountsList = new ArrayList<AbstractAccount>();
    private AbstractAccount activeAccount;
    private float initialOverdraft;
    private Gender sex;
    private static ResourceBundle bundle = ResourceBundle.getBundle("strings");

    public Client(Gender sex) {
        this.sex = sex;
        activeAccount = AccountType.SAVING.create(0); // magic numbers ((( TODO fix the magic numbers
        accountsList.add(activeAccount);
        setActiveAccount(activeAccount);
    }

    public Client(float initialOverdraft, Gender sex) {
        this.sex = sex;
        this.initialOverdraft = initialOverdraft;
        activeAccount = AccountType.CHECKING.create(initialOverdraft);
        accountsList.add(activeAccount);
        setActiveAccount(activeAccount);
    }

    public void setActiveAccount(AbstractAccount activeAccount) {
        this.activeAccount = activeAccount;
    }

    public IAccount getActiveAccount() {
        return activeAccount;
    }

    public List<AbstractAccount> getAccountsList() {
        return accountsList;
    }

    public void setAccountsList(List<AbstractAccount> accountsList) {
        this.accountsList = accountsList;
    }

    public float getBalance() {
        return activeAccount.getBalance();
    }

    @Override
    public void printReport() {
        System.out.println(bundle.getString("clientName") + this.sex.getGenderPrefix() + this.name);
        for (IAccount account : accountsList) {
            account.printReport();
        }

    }

    public void setName(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Client client = (Client) o;

        if (Float.compare(client.initialOverdraft, initialOverdraft) != 0) return false;
        if (!accountsList.equals(client.accountsList)) return false;
        if (!name.equals(client.name)) return false;
        if (sex != client.sex) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = name.hashCode();
        result = 31 * result + accountsList.hashCode();
        result = 31 * result + (initialOverdraft != +0.0f ? Float.floatToIntBits(initialOverdraft) : 0);
        result = 31 * result + sex.hashCode();
        return result;
    }
}
