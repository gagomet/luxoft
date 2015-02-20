package BankApplication.servlet;

import BankApplication.model.impl.Client;
import BankApplication.service.impl.ServiceFactory;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

/**
 * Created by Kir Kolesnikov on 20.02.2015.
 */
public class FindClientsServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String nameToSearch = req.getParameter("nameToSearch");
        String cityToSearch = req.getParameter("cityToSearch");
        List<Client> findedClientsList = ServiceFactory.getClientService().getClientsByNameAndCity(nameToSearch, cityToSearch);
        req.setAttribute("clientsList", findedClientsList);
        req.getRequestDispatcher("/pages/clientsFromDBView.jsp").forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
    }
}
