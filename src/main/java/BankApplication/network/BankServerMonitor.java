package BankApplication.network;

/**
 * Created by Padonag on 05.02.2015.
 */
public class BankServerMonitor implements Runnable {


    @Override
    public void run() {
        while (true) {
            try {
                System.out.println("Now waiting to connect: " + BankServerMultithread.getWaitForConnection().toString());
                Thread.sleep(10000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
