package BankApplication.exceptions;

/**
 * Created by Kir Kolesnikov on 19.01.2015.
 */
public class FeedException extends RuntimeException {
    public FeedException(String message) {
        super(message);
    }
}
