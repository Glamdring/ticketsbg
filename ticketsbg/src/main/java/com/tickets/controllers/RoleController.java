package com.tickets.controllers;

import javax.annotation.Resource;
import javax.faces.model.ListDataModel;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import com.tickets.model.Role;
import com.tickets.services.Service;


@Controller("roleController")
@Scope("conversation.access")
public class RoleController extends BaseCRUDController<Role> {

    @Resource(name="baseService")
    private Service service;

    private Role role = new Role();

    private ListDataModel rolesModel;

    @SuppressWarnings("unchecked")
    @Override
    protected void refreshList() {
        rolesModel = new ListDataModel(service.list(Role.class));

        // End the current conversation in case the list of roles
        // is refreshed, but only if the bean has not just been constructed
        if (role != null)
            endConversation();
    }

    public Role getRole() {
        return role;
    }

    public void setRole(Role role) {
        this.role = role;
    }

    public ListDataModel getRolesModel() {
        return rolesModel;
    }

    public void setRolesModel(ListDataModel rolesModel) {
        this.rolesModel = rolesModel;
    }

    @Override
    protected Role createEntity() {
        return new Role();
    }

    @Override
    protected Role getEntity() {
        return role;
    }

    @Override
    protected void setEntity(Role entity) {
        setRole(entity);
    }

    @Override
    protected String getListScreenName() {
        return "rolesList";
    }

    @Override
    protected String getSingleScreenName() {
        return "roleScreen";
    }

    @Override
    protected ListDataModel getModel() {
        return rolesModel;
    }

    @Override
    protected Service getService() {
        return service;
    }

}
