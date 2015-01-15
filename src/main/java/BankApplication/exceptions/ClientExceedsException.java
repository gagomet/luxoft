package BankApplication.exceptions;

/**
 * Created by Kir Kolesnikov on 15.01.2015.
 */
public class ClientExceedsException extends BankException {
    public ClientExceedsException(String message) {
        super(message);
    }

    public ClientExceedsException() {
    }
}
