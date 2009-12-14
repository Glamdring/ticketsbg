package com.tickets.utils;

import java.util.Locale;

import javax.faces.application.FacesMessage;
import javax.faces.application.ViewExpiredException;
import javax.faces.context.FacesContext;

import org.apache.log4j.Logger;

import com.tickets.constants.Messages;

public final class JSFErrorHandler {

    private static final Logger logger = Logger.getLogger(JSFErrorHandler.class);

    public static void handleThrowable(FacesContext facesContext, Throwable t) {
        String msg = "";
        String msgKey = "";
        Locale locale = GeneralUtils.getDefaultLocale();
        try {
            locale = facesContext.getViewRoot().getLocale();
        } catch (Exception e) {
            // ignore
        }
        if (t instanceof ViewExpiredException) {
            msgKey = "viewExpired";
        } else {
            logger.error("Exception caught", t);
            msgKey = "unexpectedError";
        }

        msg = Messages.getString(msgKey, locale);

        FacesMessage facesMessage = new FacesMessage(FacesMessage.SEVERITY_ERROR, msg, msg);
        FacesContext.getCurrentInstance().addMessage(null, facesMessage);

        try {
            facesContext.getExternalContext().redirect("index.jsp?errorKey=" + msgKey);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void handleException(FacesContext facesContext, Exception ex) {
        JSFErrorHandler.handleThrowable(facesContext, ex);
    }
}
