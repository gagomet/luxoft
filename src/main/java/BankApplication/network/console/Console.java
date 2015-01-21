package BankApplication.network.console;

import java.io.IOException;
import java.net.Socket;

/**
 * Created by Padonag on 15.01.2015.
 */
public interface Console {
    String consoleResponse(String consoleRequest) throws IOException;
    void sendResponse(String response);
}
