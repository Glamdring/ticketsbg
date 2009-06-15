package com.tickets.controllers.users;

import java.io.Serializable;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import com.tickets.model.User;

@Controller("loggedUserHolder")
@Scope("session")
public class LoggedUserHolder implements Serializable {

    private User loggedUser;

    public User getLoggedUser() {
        return loggedUser;
    }

    public void setLoggedUser(User loggedUser) {
        this.loggedUser = loggedUser;
    }
}
