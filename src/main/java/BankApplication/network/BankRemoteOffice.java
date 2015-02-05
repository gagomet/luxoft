package BankApplication.network;


import BankApplication.DAO.impl.DAOFactory;
import BankApplication.commander.impl.*;
import BankApplication.model.impl.Bank;
import BankApplication.network.console.Console;
import BankApplication.network.console.RemoteConsoleImpl;
import BankApplication.commander.Command;
import BankApplication.service.impl.ClientServiceImpl;
import BankApplication.service.impl.ServiceFactory;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Iterator;
import java.util.Map;
import java.util.TreeMap;

/**
 * Created by Kir Kolesnikov on 20.01.2015.
 */
public class BankRemoteOffice {
    private ServerSocket providerSocket;
    private Socket clientSocket = null;
    private ObjectOutputStream out;
    private ObjectInputStream in;
    private String message;
    Console console = new RemoteConsoleImpl(this);

    private Map<String, Command> commandsMap = new TreeMap<>();
    private static final String FEED_FILES_FOLDER = "c:\\!toBankApplication\\";
//    private static String bankName = "MyBank";
        static String bankName = "MYBANK";

    public void initialize() {

        //init commands with remote console
        commandsMap.put("1", new FindClientCommand(console));
        commandsMap.put("2", new AddClientCommand(console));
        commandsMap.put("3", new RemoveClientCommand(console));
        commandsMap.put("4", new WithdrawCommand(console));
        commandsMap.put("5", new DepositCommand(console));
        commandsMap.put("6", new GetAccountCommand(console));
        commandsMap.put("7", new TransferCommand(console));
        commandsMap.put("8", new ReportCommand(console));
        commandsMap.put("9", new ShowHelpCommand(console));

    }

    void run() {
        try {
            providerSocket = new ServerSocket(15000, 10);
            System.out.println("Waiting for clientSocket");
            clientSocket = providerSocket.accept();
            System.out.println("Connection received from " + clientSocket.getInetAddress().getHostName());
            out = new ObjectOutputStream(clientSocket.getOutputStream());
            out.flush();
            in = new ObjectInputStream(clientSocket.getInputStream());
            do {
                message = console.consoleResponse(composeUserMenu());
                if (commandsMap.containsKey(message)) {
                    Command cmd = commandsMap.get(message);
                    cmd.execute();
                }  else if (message.equals("0")) {
                    sendMessage("0");
                }
                System.out.println("Client> " + message);


            } while (!message.equals("exit"));

        } catch (IOException | IllegalArgumentException e) {
            e.printStackTrace();
        } finally {
            try {
                in.close();
                out.close();
                providerSocket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public ObjectOutputStream getOut() {
        return out;
    }

    public ObjectInputStream getIn() {
        return in;
    }

    public String receiveMessage() {
        String result = null;
        try {
            result = (String) in.readObject();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            System.err.println("Data received in unknown format");
        }
        return result;
    }

    void sendMessage(final String msg) {
        try {
            out.writeObject(msg);
            out.flush();
            System.out.println("server>" + msg);
        } catch (IOException ioException) {
            ioException.printStackTrace();
        }
    }

    public static void main(String[] args) {
        BankRemoteOffice remoteOffice = new BankRemoteOffice();
        Bank bank = DAOFactory.getBankDAO().getBankByName(bankName) ;
        ServiceFactory.getBankService().setCurrentBank(bank);
        remoteOffice.initialize();
        remoteOffice.run();
    }

    private String composeUserMenu() {

        StringBuilder builder = new StringBuilder();
        Iterator iterator = commandsMap.entrySet().iterator();
        builder.append(System.getProperty("line.separator"));
        builder.append("Active client now is: ");
        if (ClientServiceImpl.getInstance().getCurrentClient() == null) {
            builder.append("N/A");
        } else {
            builder.append(ClientServiceImpl.getInstance().getCurrentClient().toString());
        }
        builder.append(System.getProperty("line.separator"));
        while (iterator.hasNext()) {
            Map.Entry<String, Command> entry = (Map.Entry<String, Command>) iterator.next();
            builder.append(entry.getKey());
            builder.append("      -->    ");
            builder.append(entry.getValue().toString());
            builder.append(System.getProperty("line.separator"));
        }
        builder.append(System.getProperty("line.separator"));
        builder.append("exit      -->    Exit");
        return builder.toString();
    }
}
