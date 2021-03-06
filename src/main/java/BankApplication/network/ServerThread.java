package BankApplication.network;

import BankApplication.DAO.impl.DAOFactory;
import BankApplication.commander.Command;
import BankApplication.commander.CommandsManager;
import BankApplication.commander.impl.AddClientCommand;
import BankApplication.commander.impl.DepositCommand;
import BankApplication.commander.impl.FindClientCommand;
import BankApplication.commander.impl.GetAccountCommand;
import BankApplication.commander.impl.RemoveClientCommand;
import BankApplication.commander.impl.ReportCommand;
import BankApplication.commander.impl.ShowHelpCommand;
import BankApplication.commander.impl.TransferCommand;
import BankApplication.commander.impl.WithdrawCommand;
import BankApplication.model.impl.Bank;
import BankApplication.model.impl.Client;
import BankApplication.network.console.Console;
import BankApplication.network.console.MultithreadServerConsoleImpl;
import BankApplication.service.impl.ServiceFactory;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.Iterator;
import java.util.Map;
import java.util.TreeMap;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Created by Kir Kolesnikov on 30.01.2015.
 */
public class ServerThread implements Runnable, CommandsManager {
    private Socket clientSocket = null;
    private ObjectOutputStream out;
    private ObjectInputStream in;
    private String message;
    private Console console = new MultithreadServerConsoleImpl(this);
    private Client currentClient;
    long beginTime;

    private static final Logger logger = Logger.getLogger(ServerThread.class.getName());

    private Map<String, Command> commandsMap = new TreeMap<>();
    private static final String FEED_FILES_FOLDER = "c:\\!toBankApplication\\";
//    private static String bankName = "MyBank"; /*home*/
        static String bankName = "MYBANK";  /*work*/

    public ServerThread(Socket socket) {
        this.clientSocket = socket;
    }


    public void initialize() {

        //init commands with remote console
        commandsMap.put("1", new FindClientCommand(console, this));
        commandsMap.put("2", new AddClientCommand(console, this));
        commandsMap.put("3", new RemoveClientCommand(console, this));
        commandsMap.put("4", new WithdrawCommand(console, this));
        commandsMap.put("5", new DepositCommand(console, this));
        commandsMap.put("6", new GetAccountCommand(console, this));
        commandsMap.put("7", new TransferCommand(console, this));
        commandsMap.put("8", new ReportCommand(console, this));
        commandsMap.put("9", new ShowHelpCommand(console, this));

    }

    @Override
    public void run() {
        logger.log(Level.INFO, "User connected to server");
        beginTime = System.currentTimeMillis();
        BankServerMultithread.getWaitForConnection().decrementAndGet();
        initialize();
        Bank bank = DAOFactory.getBankDAO().getBankByName(bankName);
        ServiceFactory.getBankService().setCurrentBank(bank);
        try {
            System.out.println("ServerThread connected");
            System.out.println("Connection received from " + clientSocket.getInetAddress().getHostName());
            out = new ObjectOutputStream(clientSocket.getOutputStream());
            out.flush();
            in = new ObjectInputStream(clientSocket.getInputStream());
            console.consoleResponse(composeUserMenu());
            do {

                message = console.getMessageFromClient();
                if (commandsMap.containsKey(message)) {
                    Command cmd = commandsMap.get(message);
                    cmd.execute();
                    console.consoleResponse(composeUserMenu());
                } else if (message.equals("0")) {
                    sendMessage("0");
                    logger.log(Level.INFO, "User disconnected, session time: " + (System.currentTimeMillis() - beginTime));
                }
                System.out.println("Client> " + message);
            } while (!message.equals("0"));

        } catch (IOException | IllegalArgumentException | ClassNotFoundException e) {
            e.printStackTrace();
        } finally {
            try {
                in.close();
                out.close();
                clientSocket.close();
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

    void sendMessage(final String msg) {
        try {
            out.writeObject(msg);
            out.flush();
            System.out.println("server>" + msg);
        } catch (IOException ioException) {
            ioException.printStackTrace();
        }
    }

    private String composeUserMenu() {

        StringBuilder builder = new StringBuilder();
        Iterator iterator = commandsMap.entrySet().iterator();
        builder.append(System.getProperty("line.separator"));
        builder.append("Active client now is: ");
        if (currentClient == null) {
            builder.append("N/A");
        } else {
            builder.append(currentClient.toString());
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
        builder.append("0      -->    exit");
        return builder.toString();
    }


    @Override
    public Client getCurrentClient() {
        return currentClient;
    }

    @Override
    public void setCurrentClient(Client client) {
        currentClient = client;
    }

    public Console getConsole() {
        return console;
    }
}
