package BankApplication.servlet;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpSessionEvent;
import javax.servlet.http.HttpSessionListener;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Created by Kir Kolesnikov on 13.02.2015.
 */
public class SessionListener implements HttpSessionListener {
    private static final Logger logger = Logger.getLogger(SessionListener.class.getName());

    @Override
    public void sessionCreated(HttpSessionEvent httpSessionEvent) {
        final ServletContext servletContext = httpSessionEvent.getSession().getServletContext();
        synchronized (SessionListener.class) {
            Integer clientsConnected = (Integer) servletContext.getAttribute("clientsConnected");
            if (clientsConnected == null) {
                clientsConnected = 1;
            } else {
                clientsConnected++;
            }
            servletContext.setAttribute("clientsConnected", clientsConnected);
        }
    }

    @Override
    public void sessionDestroyed(HttpSessionEvent httpSessionEvent) {
        final ServletContext servletContext = httpSessionEvent.getSession().getServletContext();
        synchronized (SessionListener.class) {
            Integer clientsConnected = (Integer) servletContext.getAttribute("clientsConnected");
            clientsConnected--;
            servletContext.setAttribute("clientsConnected", clientsConnected);
        }
    }
}
