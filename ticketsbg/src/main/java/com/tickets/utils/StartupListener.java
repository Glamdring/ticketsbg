package com.tickets.utils;


import java.util.Locale;
import java.util.TimeZone;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

import org.apache.log4j.Logger;

public class StartupListener implements ServletContextListener {

    private static final Logger logger = Logger.getLogger(StartupListener.class);

    @Override
    public void contextDestroyed(ServletContextEvent evt) {

    }

    @Override
    public void contextInitialized(ServletContextEvent e) {
        TimeZone.setDefault(GeneralUtils.getDefaultTimeZone());
        logger.info("TimeZone set to: " + TimeZone.getDefault());

        Locale.setDefault(GeneralUtils.getDefaultLocale());
    }
}