package com.tickets.controllers;

import java.io.Serializable;

import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpSession;

import org.apache.myfaces.orchestra.conversation.Conversation;

import com.tickets.constants.Messages;

public abstract class BaseController implements Serializable {

    protected String getRequestValue(String key) {
        return FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap().get(key);
    }

    protected Object getSessionValue(String key) {
        return FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get(key);
    }

    protected void setSessionValue(String key, Object value) {
        ((HttpSession) FacesContext.getCurrentInstance().getExternalContext().getSession(false)).setAttribute(key, value);
    }

    protected void addMessage(String key, String componentId, Object... params) {
        String msg = getLocalizedMessage(key, params);
        FacesMessage facesMessage = new FacesMessage(FacesMessage.SEVERITY_INFO, msg, msg);
        FacesContext.getCurrentInstance().addMessage(componentId, facesMessage);
    }

    protected void addMessage(String key, Object... params) {
        addMessage(key, null, params);
    }

    protected void addError(String key, String componentId, Object...params) {
        String msg = getLocalizedMessage(key, params);
        FacesMessage facesMessage = new FacesMessage(FacesMessage.SEVERITY_ERROR, msg, msg);
        FacesContext.getCurrentInstance().addMessage(componentId, facesMessage);
    }

    protected void addError(String key, Object... params) {
        addError(key, null, params);
    }

    protected static String getLocalizedMessage(String key, Object...params) {
        return Messages.getString(key, params);
    }

    protected void endConversation() {
        if (Conversation.getCurrentInstance() != null) {
            Conversation.getCurrentInstance().invalidate();
        }
    }
}
