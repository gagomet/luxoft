package BankApplication.exceptions;

/**
 * Created by Kir Kolesnikov on 15.01.2015.
 */
public class BankException extends Exception {
    private String message;

    public BankException() {

    }

    public BankException(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
