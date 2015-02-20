package BankApplication.servlet;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Created by Kir Kolesnikov on 13.02.2015.
 */
public class SessionsAmountServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.setContentType("text/html");
        final ServletContext servletContext = req.getSession().getServletContext();
        Integer clientsConnected = (Integer) servletContext.getAttribute("clientsConnected");
        ServletOutputStream servletOutputStream = resp.getOutputStream();
        servletOutputStream.println("<h1>Clients connected: " + clientsConnected + "</h1>");
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
    }
}
