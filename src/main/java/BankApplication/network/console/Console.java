package BankApplication.network.console;

import java.io.IOException;

/**
 * Created by Padonag on 15.01.2015.
 */
public interface Console {
    void consoleResponse(String consoleRequest) throws IOException;

    void sendResponse(String response);

    String getMessageFromClient() throws IOException, ClassNotFoundException;
}
