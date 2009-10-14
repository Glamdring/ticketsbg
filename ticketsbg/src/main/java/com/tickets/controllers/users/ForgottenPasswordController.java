package com.tickets.controllers.users;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import com.tickets.controllers.BaseController;
import com.tickets.exceptions.UserException;
import com.tickets.services.UserService;

@Controller("forgottenPasswordController")
@Scope("request")
public class ForgottenPasswordController extends BaseController {

    @Autowired
    private UserService service;

    private String email;

    public void sendTemporaryPassword() {
        try {
            service.sendTemporaryPassword(email);
            addMessage("temporaryPasswordSent");
        } catch (UserException ex) {
            addError(ex.getMessageKey());
        }
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
