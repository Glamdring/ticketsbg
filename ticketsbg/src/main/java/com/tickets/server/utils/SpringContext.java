package com.tickets.server.utils;

import javax.servlet.ServletContext;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.xml.XmlBeanDefinitionReader;
import org.springframework.context.ApplicationContext;
import org.springframework.core.io.FileSystemResource;
import org.springframework.web.context.support.GenericWebApplicationContext;

public class SpringContext {

    protected static Logger log = Logger.getLogger(SpringContext.class);

    //public static XmlBeanFactory context;
    public static GenericWebApplicationContext context;

    public static void init(String path){
        try {
            context = new GenericWebApplicationContext();
            XmlBeanDefinitionReader xmlReader = new XmlBeanDefinitionReader(context);
            xmlReader.loadBeanDefinitions(new FileSystemResource(path + "spring.xml"));
            context.refresh();
        } catch (Exception ex){
            log.error(ex);
            ex.printStackTrace();
        }
    }

    public static void setServletContext(ServletContext servletContext){
        context.setServletContext(servletContext);
    }

    public static ApplicationContext getContext(){
        return context;
    }
}
