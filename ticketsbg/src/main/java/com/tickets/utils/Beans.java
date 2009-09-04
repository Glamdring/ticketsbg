package com.tickets.utils;

import javax.faces.context.FacesContext;

import org.apache.log4j.Logger;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.jsf.FacesContextUtils;

public class Beans {

    protected static Logger log = Logger.getLogger(Beans.class);

    public static Object get(String beanName) {
        try {
            WebApplicationContext context = FacesContextUtils
                    .getRequiredWebApplicationContext(FacesContext
                            .getCurrentInstance());
            if (context == null) {
                return SpringContext.getContext().getBean(beanName);
            }

            return context.getBean(beanName);
        } catch (Exception ex) {
            log.error(ex);
            return null;
        }
    }
}
