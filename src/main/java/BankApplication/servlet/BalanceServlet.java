package BankApplication.servlet;

import BankApplication.DAO.BaseDAO;
import BankApplication.DAO.impl.BaseDAOImpl;
import BankApplication.DAO.impl.DAOFactory;
import BankApplication.exceptions.ClientNotFoundException;
import BankApplication.model.impl.Bank;
import BankApplication.model.impl.Client;
import BankApplication.service.impl.ServiceFactory;

import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Created by Kir Kolesnikov on 13.02.2015.
 */
public class BalanceServlet extends HttpServlet {
    private static final String BANK_NAME = "MYBANK";
//    private static final String BANK_NAME = "MyBank";

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String clientName = (String) req.getSession().getAttribute("clientName");
        Bank bank = DAOFactory.getBankDAO().getBankByName(BANK_NAME);
        ServiceFactory.getBankService().setCurrentBank(bank);
        Client client = null;
        try {
            client = ServiceFactory.getClientService().getClientByName(ServiceFactory.getBankService().getCurrentBank(), clientName);
        } catch (ClientNotFoundException e) {
            e.printStackTrace();
        }
        ServletOutputStream servletOutputStream = resp.getOutputStream();
        servletOutputStream.println("<h3> " + client.toString() + "</h3>");
    }
}
