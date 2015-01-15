package BankApplication.exceptions;

/**
 * Created by Kir Kolesnikov on 15.01.2015.
 */
public class IllegalArgumentException extends BankException {
    public IllegalArgumentException() {
    }

    public IllegalArgumentException(String message) {
        super(message);
    }
}
