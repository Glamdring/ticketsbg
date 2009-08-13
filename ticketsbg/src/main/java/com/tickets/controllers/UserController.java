package com.tickets.controllers;

import java.util.ArrayList;
import java.util.EnumSet;
import java.util.List;

import javax.faces.model.ListDataModel;
import javax.faces.model.SelectItem;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import com.tickets.annotations.Action;
import com.tickets.controllers.security.AccessLevel;
import com.tickets.controllers.users.LoggedUserHolder;
import com.tickets.model.Firm;
import com.tickets.model.User;
import com.tickets.services.Service;
import com.tickets.services.UserService;
import com.tickets.utils.SelectItemUtils;


@Controller("userController")
@Scope("conversation.access")
@Action(accessLevel=AccessLevel.FIRM_ADMINISTRATOR)
public class UserController extends BaseCRUDController<User> {

    @Autowired
    private UserService userService;

    @Autowired
    private LoggedUserHolder loggedUserHolder;

    private User user = new User();

    private ListDataModel usersModel;

    private List<SelectItem> accessLevelSelectItems = new ArrayList<SelectItem>();

    private List<SelectItem> firmSelectItems = new ArrayList<SelectItem>();

    @Override
    public void save() {
        //If the password has been manually changed - hash it and set it, otherwise don't modify it
        if (!userService.isHash(user.getPassword())) {
            user.setPassword(userService.saltAndHashPassword(user.getPassword()));
        }
        super.save();
    }

    @Override
    protected void refreshList() {
        usersModel = new ListDataModel(userService.list(User.class));

        EnumSet<AccessLevel> exclusions = EnumSet.of(AccessLevel.PUBLIC);
        if (loggedUserHolder.getLoggedUser() != null && loggedUserHolder.getLoggedUser().getAccessLevel().getPrivileges() < AccessLevel.ADMINISTRATOR.getPrivileges()) {
            exclusions.add(AccessLevel.ADMINISTRATOR);
        }

        accessLevelSelectItems = SelectItemUtils.formSelectItems(
                AccessLevel.class, exclusions, AccessLevel.CASHIER_DESK);

        firmSelectItems = SelectItemUtils.formSelectItems(userService.list(Firm.class));

        // End the current conversation in case the list of roles
        // is refreshed, but only if the bean has not just been constructed
        if (user != null)
            endConversation();
    }

    @Override
    protected User createEntity() {
        return new User();
    }

    @Override
    protected User getEntity() {
        return user;
    }

    @Override
    protected void setEntity(User entity) {
        setUser(entity);
    }

    @Override
    protected String getListScreenName() {
        return "usersList";
    }

    @Override
    protected String getSingleScreenName() {
        return "userScreen";
    }

    @Override
    protected ListDataModel getModel() {
        return usersModel;
    }

    @Override
    protected Service getService() {
        return userService;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public ListDataModel getUsersModel() {
        return usersModel;
    }

    public void setUsersModel(ListDataModel usersModel) {
        this.usersModel = usersModel;
    }

    public List<SelectItem> getAccessLevelSelectItems() {
        return accessLevelSelectItems;
    }

    public void setAccessLevelSelectItems(List<SelectItem> accessLevelSelectItems) {
        this.accessLevelSelectItems = accessLevelSelectItems;
    }

    public List<SelectItem> getFirmSelectItems() {
        return firmSelectItems;
    }

    public void setFirmSelectItems(List<SelectItem> firmSelectItems) {
        this.firmSelectItems = firmSelectItems;
    }

    public LoggedUserHolder getLoggedUserHolder() {
        return loggedUserHolder;
    }

    public void setLoggedUserHolder(LoggedUserHolder loggedUserHolder) {
        this.loggedUserHolder = loggedUserHolder;
    }
}
