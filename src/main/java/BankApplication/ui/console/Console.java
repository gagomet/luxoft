package BankApplication.ui.console;

import java.io.BufferedReader;
import java.io.IOException;

/**
 * Created by Padonag on 15.01.2015.
 */
public interface Console {
    String consoleResponse(String consoleRequest) throws IOException;
    String getCurrentRequest();
    String getCurrentResponse();
}
