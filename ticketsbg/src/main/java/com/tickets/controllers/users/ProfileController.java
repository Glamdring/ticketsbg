package com.tickets.controllers.users;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.faces.model.SelectItem;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import com.tickets.annotations.Action;
import com.tickets.controllers.BaseController;
import com.tickets.controllers.security.AccessLevel;
import com.tickets.model.CustomerType;
import com.tickets.services.UserService;
import com.tickets.utils.SelectItemUtils;


@Controller("profileController")
@Scope("conversation.access")
@Action(accessLevel=AccessLevel.FIRM_ADMINISTRATOR)
public class ProfileController extends BaseController {

    @Autowired
    private UserService userService;

    @Autowired
    private LoggedUserHolder loggedUserHolder;

    private List<SelectItem> customerTypeItems = new ArrayList<SelectItem>();

    public void save() {
        userService.save(loggedUserHolder.getLoggedUser());
        addMessage("profile.update.successful");
    }

    @PostConstruct
    public void init() {
        for (Iterator<FacesMessage> it = FacesContext.getCurrentInstance().getMessages(); it.hasNext();) {
            System.out.println(it.next().getSummary());
        }
        customerTypeItems = SelectItemUtils.formSelectItems(CustomerType.class);
    }

    public List<SelectItem> getCustomerTypeItems() {
        return customerTypeItems;
    }

    public void setCustomerTypeItems(List<SelectItem> customerTypeItems) {
        this.customerTypeItems = customerTypeItems;
    }
}
