package com.tickets.controllers.users;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import com.tickets.annotations.Action;
import com.tickets.controllers.BaseController;
import com.tickets.controllers.security.AccessLevel;
import com.tickets.exceptions.UserException;
import com.tickets.model.User;
import com.tickets.services.UserService;

@Controller("changePasswordController")
@Scope("request")
@Action(accessLevel = AccessLevel.PUBLIC)
public class ChangePasswordController extends BaseController {

    private String oldPassword = "";
    private String newPassword = "";
    private String newPassword2 = "";

    @Autowired
    private UserService userService;

    @Autowired
    private LoggedUserHolder loggedUserHolder;

    @Action
    public void changePassword() {
        if (!newPassword.equals(newPassword2)) {
            addError("passwords.dont.match");
        }

        try {
            User u = userService.login(loggedUserHolder.getLoggedUser()
                    .getUsername(), oldPassword.toCharArray(), false);

            // if the login process is successful - i.e.
            // a user is returned and no exception is thrown
            if (u != null) {
                userService.changePassword(loggedUserHolder.getLoggedUser(),
                        newPassword);
                addMessage("password.changed");
            }
        } catch (UserException ex) {
            addError("password.incorrect");
        }
    }

    public String getOldPassword() {
        return oldPassword;
    }

    public void setOldPassword(String oldPassword) {
        this.oldPassword = oldPassword;
    }

    public String getNewPassword() {
        return newPassword;
    }

    public void setNewPassword(String newPassword) {
        this.newPassword = newPassword;
    }

    public String getNewPassword2() {
        return newPassword2;
    }

    public void setNewPassword2(String newPassword2) {
        this.newPassword2 = newPassword2;
    }

}
