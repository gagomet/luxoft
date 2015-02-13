package BankApplication.servlet;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Created by Kir Kolesnikov on 13.02.2015.
 */
public class CheckLoggedFilter implements Filter {
    private static final Logger logger = Logger.getLogger(CheckLoggedFilter.class.getName());

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        logger.log(Level.INFO, "Using " + CheckLoggedFilter.class.getName());
    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        HttpSession session = ((HttpServletRequest) servletRequest).getSession();
        String clientName = (String) session.getAttribute("clientName");
        String path = ((HttpServletRequest) servletRequest).getRequestURI();
        HttpServletResponse httpServletResponse = (HttpServletResponse) servletResponse;
        if (path.equals("/welcome") || path.equals("/") || path.contains("/login") || clientName != null) {
            filterChain.doFilter(servletRequest, servletResponse);
        } else {
            httpServletResponse.sendRedirect("/pages/login.html");
        }
    }

    @Override
    public void destroy() {

    }
}
