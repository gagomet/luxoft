package BankApplication;

import BankApplication.DAO.BaseDAO;
import BankApplication.DAO.impl.BaseDAOImpl;
import BankApplication.DAO.impl.DAOFactory;
import BankApplication.exceptions.ClientNotFoundException;
import BankApplication.model.impl.Bank;
import BankApplication.model.impl.Client;
import BankApplication.network.BankServerMultithread;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import java.io.IOException;
import java.io.InputStreamReader;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import static org.junit.Assert.assertEquals;

/**
 * Created by Padonag on 07.02.2015.
 */
public class BankServerThreadedTest {
    private static Connection dbConnection;
    private static InputStreamReader reader = null;
    private static final int THREADS_NUMBER = 1000;
    private static final int TEST_PORT = 15555;
    private static final int POOL_SIZE = 10;

    @BeforeClass
    public static void preSetUp() {
        BaseDAO testBaseDao = new BaseDAOImpl();
        dbConnection = testBaseDao.openConnection();
        SqlScripRunner.runSqlScript(dbConnection, "create.sql");
    }

    @Before
    public void setUp() {
        SqlScripRunner.runSqlScript(dbConnection, "stubs.sql");
    }

    @After
    public void closeUp() {
        SqlScripRunner.runSqlScript(dbConnection, "clear.sql");
    }

    @AfterClass
    public static void afterUse() {
        if (dbConnection != null) {
            try {
                dbConnection.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        if (reader != null) {
            try {
                reader.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    @Test
    public void testMultithreading() throws InterruptedException, ExecutionException {
        ExecutorService pool = Executors.newFixedThreadPool(POOL_SIZE);
        List<Future<Long>> clientsList = new ArrayList<>();
        int workedThreadsCounter = 0;
        long allTime = 0;

        BankServerMultithread server = new BankServerMultithread(TEST_PORT);
        new Thread(server).start();

        Bank bank = DAOFactory.getBankDAO().getBankByID(1);
        Client beforeClient = null;
        try {
            beforeClient = DAOFactory.getClientDAO().findClientByName(bank, "Petra Petrova");
        } catch (ClientNotFoundException e) {
            e.printStackTrace();
        }

        for (int i = 0; i < THREADS_NUMBER; i++) {
            Thread.sleep(50);
            Future<Long> future = pool.submit(new BankClientMock(bank));
            System.out.println("Waiting " + BankServerMultithread.getWaitForConnection().get());
            clientsList.add(future);
        }

        for (Future<Long> tempFuture : clientsList) {
            if (tempFuture.isDone()) {
                workedThreadsCounter++;
                long result = tempFuture.get();
                System.out.println("Thread " + workedThreadsCounter + " time = " + result);
                allTime += result;
            }
        }

        System.out.println("Average time: " + allTime/THREADS_NUMBER);

        Client afterClient = null;
        try {
            afterClient = DAOFactory.getClientDAO().findClientByName(bank, "Petra Petrova");
        } catch (ClientNotFoundException e) {
            e.printStackTrace();
        }

        assert afterClient != null;
        assert beforeClient != null;
        server.stop();
        assertEquals(beforeClient.getActiveAccount().getBalance() - THREADS_NUMBER, afterClient.getActiveAccount().getBalance(), 0.001);
    }


}
