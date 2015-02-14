package BankApplication.servlet;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Created by Kir Kolesnikov on 13.02.2015.
 */
public class LoginServlet extends HttpServlet {
    private static final Logger logger = Logger.getLogger(LoginServlet.class.getName());

    @Override
    protected void doPost(final HttpServletRequest req, final HttpServletResponse resp) throws ServletException, IOException {

        final String clientName = req.getParameter("client");
        if (clientName == null) {
            logger.log(Level.INFO, "Client not found");
            req.setAttribute("error", "Client not determine");
            req.getRequestDispatcher("/pages/error.jsp").forward(req,resp);
        }
        req.getSession().setAttribute("clientName", clientName);
        logger.log(Level.INFO, "Client " + clientName + " logged into ATM");
        resp.sendRedirect("/pages/menu.html");
    }

    @Override
    protected void doGet(final HttpServletRequest req, final HttpServletResponse resp) throws ServletException, IOException {
    }
}
