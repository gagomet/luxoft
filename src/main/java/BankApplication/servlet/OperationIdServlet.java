package BankApplication.servlet;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Created by Padonag on 14.02.2015.
 */
public class OperationIdServlet extends HttpServlet {
    private static final String DEPOSIT_ID = "deposit";
    private static final String WITHDRAW_ID = "withdraw";

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String test = (String) req.getParameter("operationId");
        ServletContext context= getServletContext();
        if (test.equals(DEPOSIT_ID)){
            RequestDispatcher rd= context.getRequestDispatcher("/deposit");
            rd.forward(req, resp);
        } else if(test.equals(WITHDRAW_ID)){
            RequestDispatcher rd= context.getRequestDispatcher("/withdraw");
            rd.forward(req, resp);
        } else {
            req.setAttribute("error", "Unknown operation!");
            req.getRequestDispatcher("/pages/error.jsp").forward(req,resp);
        }
    }
}
