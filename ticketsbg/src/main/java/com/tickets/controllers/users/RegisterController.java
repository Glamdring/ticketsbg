package com.tickets.controllers.users;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.model.SelectItem;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import com.tickets.controllers.BaseController;
import com.tickets.controllers.PurchaseController;
import com.tickets.controllers.valueobjects.Screen;
import com.tickets.exceptions.UserException;
import com.tickets.model.CustomerType;
import com.tickets.model.User;
import com.tickets.services.UserService;
import com.tickets.utils.SelectItemUtils;

@Controller("registerController")
@Scope("request")
public class RegisterController extends BaseController {

    private User user = new User();

    private List<SelectItem> customerTypeItems = new ArrayList<SelectItem>();

    @Autowired
    private LoggedUserHolder loggedUserHolder;

    @Autowired
    private UserService userService;

    @Autowired
    private PurchaseController purchaseController;

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

        if (purchaseController != null && purchaseController.getCurrentStep() != null) {
            return purchaseController.getCurrentStep().getScreen().getOutcome();
        }
        return Screen.HOME.getOutcome(); // TODO referer
    }

    @PostConstruct
    public void init() {
        customerTypeItems = SelectItemUtils.formSelectItems(CustomerType.class);
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

    public List<SelectItem> getcustomerTypeItems() {
        return customerTypeItems;
    }

    public void setcustomerTypeItems(List<SelectItem> customerTypeItems) {
        this.customerTypeItems = customerTypeItems;
    }
}
