package BankApplication.exceptions;

/**
 * Created by Padonag on 17.01.2015.
 */
public class ClientNotFoundException extends BankException {
    public ClientNotFoundException(String message) {
        super(message);
    }

    public ClientNotFoundException() {
    }
}
