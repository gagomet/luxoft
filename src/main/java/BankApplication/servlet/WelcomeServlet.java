package BankApplication.servlet;

import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Created by Kir Kolesnikov on 13.02.2015.
 */
public class WelcomeServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.setContentType("text/html");
        ServletOutputStream servletOutputStream = resp.getOutputStream();
        servletOutputStream.println();
        servletOutputStream.println("<h1 style=\"align:center\">ATM</h1>");
        servletOutputStream.println("<h1><a style=\"align:center\" href=\"/pages/login.html\">Login</a></h1>");
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
    }
}
