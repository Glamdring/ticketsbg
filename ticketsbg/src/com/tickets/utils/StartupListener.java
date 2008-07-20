package com.tickets.utils;


import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

import org.springframework.web.context.WebApplicationContext;

public class StartupListener implements ServletContextListener {

    @Override
    public void contextDestroyed(ServletContextEvent arg0) {
        // TODO Auto-generated method stub

    }

    @Override
    public void contextInitialized(ServletContextEvent e) {
        String path = e.getServletContext().getRealPath("/") + "WEB-INF/";
        SpringContext.init(path);
        SpringContext.setServletContext(e.getServletContext());

        e.getServletContext().setAttribute(
                WebApplicationContext.ROOT_WEB_APPLICATION_CONTEXT_ATTRIBUTE,
                SpringContext.getContext());
    }
}
