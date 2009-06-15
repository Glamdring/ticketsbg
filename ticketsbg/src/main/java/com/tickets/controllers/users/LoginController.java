package com.tickets.controllers.users;

import javax.faces.component.UIInput;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import com.tickets.annotations.Action;
import com.tickets.controllers.BaseController;
import com.tickets.exceptions.UserException;
import com.tickets.model.User;
import com.tickets.services.UserService;

@Controller("loginController")
@Scope("request")
public class LoginController extends BaseController {

    private String username;
    private String password;
    private UIInput admin;

    @Autowired
    private UserService userService;

    @Autowired
    private LoggedUserHolder loggedUserHolder;

    @Action
    public String login() {
        //FacesContext.getCurrentInstance().getExternalContext().getRequest();
        System.out.println(username + ":" + password + ":" + admin);
        try {
            User loggedUser = userService.login(username,
                    password.toCharArray(),
                    false);

            loggedUserHolder.setLoggedUser(loggedUser);

            if ((Boolean) admin.getValue()) {
                return "adminPanel";
            }
            return "";
        } catch (UserException ex) {
            addError(ex.getMessageKey());
            return null;
        }
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public UIInput getAdmin() {
        return admin;
    }

    public void setAdmin(UIInput admin) {
        this.admin = admin;
    }
}
