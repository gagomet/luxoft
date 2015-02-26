package BankApplication.servlet;

import BankApplication.exceptions.ClientNotFoundException;
import BankApplication.exceptions.NotEnoughFundsException;
import BankApplication.model.impl.Bank;
import BankApplication.model.impl.Client;
import BankApplication.service.AccountService;
import BankApplication.service.BankService;
import BankApplication.service.ClientService;
import BankApplication.service.impl.ServiceFactory;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Created by Padonag on 14.02.2015.
 */
public class WithdrawServlet extends HttpServlet {
    private static final String BANK_NAME = "MYBANK";

    //    private static final String BANK_NAME = "MyBank";
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String clientName = (String) req.getSession().getAttribute("clientName");
        ServletContext context = getServletContext();
        BankService bankService = (BankService) context.getAttribute("bankService");
        ClientService clientService = (ClientService) context.getAttribute("clientService");
        AccountService accountService = (AccountService) context.getAttribute("accountService");
        Bank bank = bankService.getBankByName(BANK_NAME);
        Client activeClient = null;
        try {
            activeClient = clientService.getClientByName(bank, clientName);
        } catch (ClientNotFoundException e) {
            e.printStackTrace();
        }
        Float amount = Float.parseFloat(req.getParameter("amount"));
        req.setAttribute("amount", amount);
        try {
            accountService.withdrawFunds(activeClient.getActiveAccount(), amount);
        } catch (NotEnoughFundsException e) {
            req.setAttribute("error", "Not enough funds!");
            req.setAttribute("clientFunds", activeClient.getActiveAccount().getBalance());
            req.getRequestDispatcher("/pages/error.jsp").forward(req, resp);
        }
        req.setAttribute("success", "Account was successfully reduced");
        req.getRequestDispatcher("/balance").forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
    }
}
