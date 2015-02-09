package BankApplication.exceptions;

/**
 * Created by Kir Kolesnikov on 29.01.2015.
 */
public class DAOException extends RuntimeException {
    public DAOException(String message) {
        super(message);
    }
}
