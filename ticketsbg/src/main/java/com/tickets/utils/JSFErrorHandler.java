package com.tickets.utils;

import java.io.IOException;
import java.util.Locale;

import javax.faces.application.FacesMessage;
import javax.faces.application.ViewExpiredException;
import javax.faces.context.FacesContext;

import com.tickets.constants.Messages;

public final class JSFErrorHandler {

    public static void handleException(FacesContext facesContext, Exception ex) throws IOException {
        String msg = "";
        Locale locale = Messages.DEFAULT_LOCALE;
        try {
            locale = facesContext.getViewRoot().getLocale();
        } catch (Exception e) {
            // ignore
        }
        if (ex instanceof ViewExpiredException) {
            msg = Messages.getString("viewExpired", locale);
        } else {
            msg = Messages.getString("unexpectedError", locale);
        }

        FacesMessage facesMessage = new FacesMessage(FacesMessage.SEVERITY_ERROR, msg, msg);
        FacesContext.getCurrentInstance().addMessage(null, facesMessage);

        facesContext.getExternalContext().dispatch("/index.jsp");
    }
}
