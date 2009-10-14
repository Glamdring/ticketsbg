package com.tickets.controllers.users;

import javax.faces.component.UIInput;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import com.tickets.annotations.Action;
import com.tickets.controllers.BaseController;
import com.tickets.controllers.PurchaseController;
import com.tickets.controllers.RouteController;
import com.tickets.controllers.security.AccessLevel;
import com.tickets.controllers.valueobjects.Screen;
import com.tickets.exceptions.UserException;
import com.tickets.model.User;
import com.tickets.services.UserService;

@Controller("loginController")
@Scope("request")
@Action(accessLevel=AccessLevel.PUBLIC)
public class LoginController extends BaseController {

    private String username;
    private String password;
    private UIInput admin;

    @Autowired
    private UserService userService;

    @Autowired
    private LoggedUserHolder loggedUserHolder;

    @Autowired
    private RouteController routeController;

    @Autowired
    private PurchaseController purchaseController;

    @Action
    public String login() {
        try {
            User loggedUser = userService.login(username,
                    password.toCharArray(),
                    false);

            loggedUserHolder.setLoggedUser(loggedUser);

            if (loggedUser.isChangePasswordAfterLogin()) {
                addMessage("changePasswordAfterTempLogin");
                return Screen.PROFILE_SCREEN.getOutcome();
            }

            if (Boolean.parseBoolean(admin.getValue().toString())) {
                return "adminPanel";
            } else if (purchaseController != null && purchaseController.getCurrentStep() != null){
                return purchaseController.getCurrentStep().getScreen().getOutcome();
            }

            if (loggedUser.isStaff()) {
                routeController.init();
            }

            return Screen.HOME.getOutcome(); //TODO referer, or at least the active step
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
