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
import com.tickets.model.Agent;
import com.tickets.model.Firm;
import com.tickets.model.User;
import com.tickets.services.AgentService;
import com.tickets.services.OfficeService;
import com.tickets.services.Service;
import com.tickets.services.UserService;
import com.tickets.utils.SelectItemUtils;
import com.tickets.utils.ValidationBypassingEventListener;


@Controller("userController")
@Scope("conversation.access")
@Action(accessLevel=AccessLevel.FIRM_ADMINISTRATOR)
public class UserController extends BaseCRUDController<User> {

    @Autowired
    private UserService userService;

    @Autowired
    private AgentService agentService;

    @Autowired
    private OfficeService officeService;

    @Autowired
    private LoggedUserHolder loggedUserHolder;

    private User user = new User();

    private ListDataModel usersModel;

    private ListDataModel agentsUsersModel;

    private List<SelectItem> accessLevelSelectItems = new ArrayList<SelectItem>();

    private List<SelectItem> firmSelectItems = new ArrayList<SelectItem>();

    private List<SelectItem> agents = new ArrayList<SelectItem>();

    private List<SelectItem> offices = new ArrayList<SelectItem>();

    @Override
    public void save() {
        //If the password has been manually changed - hash it and set it, otherwise don't modify it
        if (!userService.isHash(user.getPassword())) {
            user.setPassword(userService.saltAndHashPassword(user.getPassword()));
        }

        User loggedUser = loggedUserHolder.getLoggedUser();
        if (loggedUser.getFirm() != null) {
            user.setFirm(loggedUser.getFirm());
        }
        ValidationBypassingEventListener.turnValidationOff();
        try {
            super.save();
        } finally {
            ValidationBypassingEventListener.turnValidationOn();
        }
    }

    @Override
    protected void refreshList() {

        User loggedUser = loggedUserHolder.getLoggedUser();

        usersModel = new ListDataModel(userService.fetchUsers(loggedUser.getFirm()));

        agentsUsersModel = new ListDataModel(userService.fetchAgentsUsers(loggedUser.getFirm()));


        EnumSet<AccessLevel> exclusions = EnumSet.of(AccessLevel.PUBLIC, AccessLevel.PUBLIC_LOGGED);
        if (loggedUser.getAccessLevel().getPrivileges() < AccessLevel.ADMINISTRATOR.getPrivileges()) {
            agents = SelectItemUtils.formSelectItems(agentService.getAgents(loggedUser.getFirm()));
            exclusions.add(AccessLevel.ADMINISTRATOR);
        } else {
            // if administrator, agents = all possible
            agents = SelectItemUtils.formSelectItems(userService.list(Agent.class));
        }

        accessLevelSelectItems = SelectItemUtils.formSelectItems(
                AccessLevel.class, exclusions, AccessLevel.CASH_DESK);

        firmSelectItems = SelectItemUtils.formSelectItems(userService.list(Firm.class));

        offices = SelectItemUtils.formSelectItems(officeService.getOffices(loggedUser.getFirm()));

        // End the current conversation in case the list of roles
        // is refreshed, but only if the bean has not just been constructed
        if (user != null)
            endConversation();
    }

    @Action
    public void deleteAgentUser() {
        userService.delete((User) (getAgentsUsersModel().getRowData()));
        refreshList();
    }


    @Override
    protected User createEntity() {
        User user = new User();
        user.setStaff(true);
        return user;
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

    public List<SelectItem> getAgents() {
        return agents;
    }

    public void setAgents(List<SelectItem> agents) {
        this.agents = agents;
    }

    public ListDataModel getAgentsUsersModel() {
        return agentsUsersModel;
    }

    public void setAgentsUsersModel(ListDataModel agentsUsersModel) {
        this.agentsUsersModel = agentsUsersModel;
    }

    public List<SelectItem> getOffices() {
        return offices;
    }

    public void setOffices(List<SelectItem> offices) {
        this.offices = offices;
    }
}
