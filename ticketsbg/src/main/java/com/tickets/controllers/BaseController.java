package com.tickets.controllers;

import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpSession;

import com.tickets.constants.Messages;

public abstract class BaseController {

    protected String getRequestValue(String key) {
        return FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap().get(key);
    }

    protected Object getSessionValue(String key) {
        return FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get(key);
    }

    protected void setSessionValue(String key, Object value) {
        ((HttpSession) FacesContext.getCurrentInstance().getExternalContext().getSession(false)).setAttribute(key, value);
    }

    protected void addMessage(String key, Object...params) {
        String msg = getLocalizedMessage(key, params);
        FacesMessage facesMessage = new FacesMessage(FacesMessage.SEVERITY_INFO, msg, msg);
        FacesContext.getCurrentInstance().addMessage(null, facesMessage);
    }

    protected void addError(String key, Object...params) {
        String msg = getLocalizedMessage(key, params);
        FacesMessage facesMessage = new FacesMessage(FacesMessage.SEVERITY_ERROR, msg, msg);
        FacesContext.getCurrentInstance().addMessage(null, facesMessage);
    }

    protected String getLocalizedMessage(String key, Object...params) {
        return Messages.getString(key, params);
    }
}
