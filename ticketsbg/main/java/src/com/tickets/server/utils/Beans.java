package com.tickets.server.utils;

import org.apache.log4j.Logger;



public class Beans {

    protected static Logger log = Logger.getLogger(Beans.class);

    public static Object get(String beanName){
        try {
            return SpringContext.getContext().getBean(beanName);
        } catch (Exception ex){
            log.error(ex);
            return null;
        }
    }
}
