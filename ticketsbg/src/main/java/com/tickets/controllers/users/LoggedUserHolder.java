package com.tickets.controllers.users;

import java.io.Serializable;

import javax.faces.context.FacesContext;
import javax.servlet.http.HttpSession;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import com.tickets.controllers.Screen;
import com.tickets.model.User;

@Controller("loggedUserHolder")
@Scope("session")
public class LoggedUserHolder implements Serializable {

    private static final String LOGGED_UESR_HOLDER_KEY = "loggedUserHolder";

    private User loggedUser;

    public User getLoggedUser() {
        return loggedUser;
    }

    public void setLoggedUser(User loggedUser) {
        this.loggedUser = loggedUser;
    }

    public String logout() {
        loggedUser = null;
        return Screen.HOME.getOutcome();
    }

    /**
     * Convenient method to retreive the current used, used in classes
     * which don't 'want' to inject the holder
     *
     * @return the currently logged user; null if no user is logged
     */
    public static User getUser() {
        HttpSession session = (HttpSession) FacesContext.getCurrentInstance()
                .getExternalContext().getSession(true);

        LoggedUserHolder loggedUserHolder = (LoggedUserHolder) session
                .getAttribute(LOGGED_UESR_HOLDER_KEY);

        return loggedUserHolder.getLoggedUser();
    }
}
