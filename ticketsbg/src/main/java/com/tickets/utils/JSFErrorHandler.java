package com.tickets.utils;

import java.io.IOException;
import java.util.Locale;

import javax.faces.application.FacesMessage;
import javax.faces.application.ViewExpiredException;
import javax.faces.context.FacesContext;

import com.tickets.constants.Messages;

public final class JSFErrorHandler {

    public static void handleThrowable(FacesContext facesContext, Throwable t) throws IOException {
        String msg = "";
        String msgKey = "";
        Locale locale = Messages.DEFAULT_LOCALE;
        try {
            locale = facesContext.getViewRoot().getLocale();
        } catch (Exception e) {
            // ignore
        }
        if (t instanceof ViewExpiredException) {
            msgKey = "viewExpired";
        } else {
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

    public static void handleException(FacesContext facesContext, Exception ex) throws IOException {
        JSFErrorHandler.handleThrowable(facesContext, ex);
    }
}
