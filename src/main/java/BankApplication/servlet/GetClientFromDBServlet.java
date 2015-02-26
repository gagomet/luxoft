package BankApplication.servlet;

import BankApplication.exceptions.ClientNotFoundException;
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
 * Created by Kir Kolesnikov on 20.02.2015.
 */
public class GetClientFromDBServlet extends HttpServlet {
    private static final String BANK_NAME = "MYBANK";
//    private static final String BANK_NAME = "MyBank";

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String clientName = (String) req.getSession().getAttribute("clientName");
        ServletContext context = getServletContext();
        BankService bankService = (BankService)context.getAttribute("bankService");
        ClientService clientService = (ClientService)context.getAttribute("clientService");
        Bank bank = bankService.getBankByName(BANK_NAME);
        Client client = null;
        try {
            client = clientService.getClientByName(bank, clientName);
        } catch (ClientNotFoundException e) {
            req.setAttribute("error", "Client not found in DB");
            req.getRequestDispatcher("/pages/error.jsp").forward(req,resp);
            return;
        }
        req.setAttribute("clientFromDb", client);
        //TODO change jsp view to another
        req.getRequestDispatcher("/pages/checkBalance.jsp").forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
    }
}
