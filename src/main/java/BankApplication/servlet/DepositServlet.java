package BankApplication.servlet;

import BankApplication.exceptions.ClientNotFoundException;
import BankApplication.model.impl.Bank;
import BankApplication.model.impl.Client;
import BankApplication.service.impl.ServiceFactory;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Created by Padonag on 14.02.2015.
 */
public class DepositServlet extends HttpServlet {
    //    private static final String BANK_NAME = "MYBANK";
    private static final String BANK_NAME = "MyBank";
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String clientName = (String) req.getSession().getAttribute("clientName");
        Bank bank = ServiceFactory.getBankService().getBankByName(BANK_NAME);
        Client activeClient = null;
        try {
            activeClient = ServiceFactory.getClientService().getClientByName(bank, clientName);
        } catch (ClientNotFoundException e) {
            e.printStackTrace();
        }
        Float amount = Float.parseFloat(req.getParameter("amount"));
        req.setAttribute("amount", amount);
        ServiceFactory.getAccountService().depositeFunds(activeClient.getActiveAccount(), amount);
        req.setAttribute("success", "Account was successfully refilled");
        req.getRequestDispatcher("/balance").forward(req,resp);
    }
}
