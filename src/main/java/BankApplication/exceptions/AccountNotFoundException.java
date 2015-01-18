package BankApplication.exceptions;

/**
 * Created by Padonag on 18.01.2015.
 */
public class AccountNotFoundException extends BankException {
    public AccountNotFoundException(String message) {
        super(message);
    }

    public AccountNotFoundException() {
    }
}
