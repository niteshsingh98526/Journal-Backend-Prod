package com.journal.journalApp.filter;

import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.logging.Logger;

@Component
@Order(value = 1)
public class AuthorizationFilter implements Filter {
    private static final Logger LOGGER = Logger.getLogger(AuthorizationFilter.class.getName());

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {
        LOGGER.info("[AuthorizationFilter] - doFilter method");
        LOGGER.info("Local Port : " + request.getLocalPort());
        LOGGER.info("Server Name : " + request.getServerName());

        HttpServletRequest httpServletRequest = (HttpServletRequest) request;
        LOGGER.info("Method Name : " + httpServletRequest.getMethod());
        LOGGER.info("Request URI : " + httpServletRequest.getRequestURI());
        LOGGER.info("Servlet Path : " + httpServletRequest.getServletPath());
        chain.doFilter(request, response);

    }
}
