package BankApplication.servlet;

import BankApplication.exceptions.AccountNotFoundException;
import BankApplication.exceptions.ClientNotFoundException;
import BankApplication.model.impl.Bank;
import BankApplication.model.impl.Client;
import BankApplication.service.AccountService;
import BankApplication.service.BankService;
import BankApplication.service.ClientService;
import BankApplication.service.impl.ServiceFactory;
import BankApplication.type.Gender;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Created by Padonag on 22.02.2015.
 */
public class EditClientServlet extends HttpServlet {
        private static final String BANK_NAME = "MYBANK";
//    private static final String BANK_NAME = "MyBank";

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        ServletContext context = getServletContext();
        BankService bankService = (BankService)context.getAttribute("bankService");
        ClientService clientService = (ClientService)context.getAttribute("clientService");
        AccountService accountService = (AccountService)context.getAttribute("accountService");
        Bank bank = bankService.getBankByName(BANK_NAME);
        Client editableClient;
        long clientsId = Long.parseLong(req.getParameter("clientsID"));

        if (clientsId == 0) {
            editableClient = new Client();
        } else {
            try {
                editableClient = clientService.getClientById(clientsId);
            } catch (ClientNotFoundException e) {
                req.setAttribute("error", "Client not found in DB");
                req.getRequestDispatcher("/pages/error.jsp").forward(req, resp);
                return;
            }
        }
        editableClient.setName(req.getParameter("clientsName"));
        editableClient.setCity(req.getParameter("clientsCity"));
        if (req.getParameter("gender").equals("MALE")) {
            editableClient.setSex(Gender.MALE);
        } else {
            editableClient.setSex(Gender.FEMALE);
        }
        editableClient.setEmail(req.getParameter("clientsEmail"));
        long accountId = 0;
        try {
            accountId = Long.parseLong(req.getParameter("clientsActiveAccountId"));
            editableClient.setActiveAccount(accountService.getAccountById(editableClient, accountId));
        } catch (AccountNotFoundException e) {
            req.setAttribute("error", "Acount with id " + accountId + " not found in DB");
            req.getRequestDispatcher("/pages/error.jsp").forward(req, resp);
            return;
        }
        editableClient.getActiveAccount().setBalance(Float.parseFloat(req.getParameter("clientsBalance")));

        Client editedClient = clientService.saveClient(bank, editableClient);

        req.setAttribute("client", editedClient);
        req.getRequestDispatcher("/pages/editClient.jsp").forward(req, resp);

    }
}
