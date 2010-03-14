package com.tickets.utils;


import java.util.Locale;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

public class StartupListener implements ServletContextListener {

    @Override
    public void contextDestroyed(ServletContextEvent evt) {

    }

    @Override
    public void contextInitialized(ServletContextEvent e) {
        Locale.setDefault(GeneralUtils.getDefaultLocale());
    }
}