package BankApplication.servlet;

import BankApplication.service.ClientService;
import BankApplication.service.impl.AccountServiceImpl;
import BankApplication.service.impl.BankServiceImpl;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

/**
 * Created by Kir Kolesnikov on 26.02.2015.
 */
public class ApplicationListener implements ServletContextListener {
    @Override
    public void contextInitialized(ServletContextEvent servletContextEvent) {
        ApplicationContext applicationContext = new ClassPathXmlApplicationContext("application-context.xml");
        ServletContext context = servletContextEvent.getServletContext();

        context.setAttribute("bankService", applicationContext.getBean(BankServiceImpl.class));
        context.setAttribute("clientService", applicationContext.getBean(ClientService.class));
        context.setAttribute("accountService", applicationContext.getBean(AccountServiceImpl.class));

    }

    @Override
    public void contextDestroyed(ServletContextEvent servletContextEvent) {

    }
}
