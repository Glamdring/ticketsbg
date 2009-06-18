package com.tickets.controllers.users;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.model.SelectItem;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import com.tickets.controllers.BaseController;
import com.tickets.exceptions.UserException;
import com.tickets.model.User;
import com.tickets.model.UserType;
import com.tickets.services.UserService;

@Controller("registerController")
@Scope("request")
public class RegisterController extends BaseController {

    private User user = new User();

    private List<SelectItem> userTypeItems = new ArrayList<SelectItem>();

    @Autowired
    private LoggedUserHolder loggedUserHolder;

    @Autowired
    private UserService userService;

    public String register() {

        if (!validate())
            return null;

        try {
            user = userService.register(user);
            loggedUserHolder.setLoggedUser(user);
        } catch (UserException uex) {
            addError("emailProblem", "email");
            return null;
        }

        return "home";
        //TODO or return to current step;
    }

    @PostConstruct
    public void init() {
        UserType[] userTypes = UserType.values();
        for (UserType userType : userTypes) {
            SelectItem si = new SelectItem(userType, getLocalizedMessage(userType.toString()));
            userTypeItems.add(si);
        }
    }

    private boolean validate() {
        boolean valid = true;

        if (!user.getPassword().equals(user.getRepeatPassword())) {
            addError("passwordMustMatch", "repeatPassword");
            valid = false;
        }
        if (!user.isAgreedToTerms()) {
            addError("mustAgreeToTerms", "agreedToTerms");
            valid = false;
        }

        return valid;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public List<SelectItem> getUserTypeItems() {
        return userTypeItems;
    }

    public void setUserTypeItems(List<SelectItem> userTypeItems) {
        this.userTypeItems = userTypeItems;
    }
}
