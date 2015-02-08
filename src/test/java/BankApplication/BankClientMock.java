package BankApplication;

import BankApplication.model.impl.Bank;



import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.concurrent.Callable;

/**
 * Created by Padonag on 07.02.2015.
 */
public class BankClientMock implements Callable<Long> {
    private Bank bank;
    private static final String SERVER = "localhost";
    public BankClientMock (Bank bank){
        this.bank = bank;
    }

    @Override
    public Long call() throws Exception {
        long startExecutionTime = System.currentTimeMillis();
        Socket socket = new Socket(SERVER, 15555);
        ObjectOutputStream out = new ObjectOutputStream(socket.getOutputStream());
        out.flush();
        ObjectInputStream in = new ObjectInputStream(socket.getInputStream());

        in.readObject();
        out.writeObject("1");
        in.readObject();
        out.writeObject("Petra Petrova");
        in.readObject();
        out.writeObject("");
        in.readObject();
        out.writeObject("4");
        in.readObject();
        out.writeObject("1");
        in.readObject();
        out.writeObject("");
        in.readObject();
        out.writeObject("0");

        return System.currentTimeMillis() - startExecutionTime;
    }
}
