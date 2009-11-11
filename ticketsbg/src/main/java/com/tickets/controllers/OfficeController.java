package com.tickets.controllers;

import java.util.List;

import javax.faces.model.ListDataModel;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import com.tickets.annotations.Action;
import com.tickets.controllers.security.AccessLevel;
import com.tickets.controllers.users.LoggedUserHolder;
import com.tickets.model.Office;
import com.tickets.services.OfficeService;
import com.tickets.services.Service;


@Controller("officeController")
@Scope("conversation.access")
@Action(accessLevel=AccessLevel.FIRM_COORDINATOR)
public class OfficeController extends BaseCRUDController<Office> {

    @Autowired
    private OfficeService service;

    private Office office = new Office();

    private ListDataModel officesModel;

    @Override
    public void save() {
        office.setFirm(LoggedUserHolder.getUser().getFirm());
        super.save();
    }

    @Override
    protected void refreshList() {
        List<Office> offices = service.getOffices(LoggedUserHolder.getUser().getFirm());
        officesModel = new ListDataModel(offices);

        // End the current conversation in case the list of roles
        // is refreshed, but only if the bean has not just been constructed
        if (office != null)
            endConversation();
    }

    @Action(accessLevel=AccessLevel.FIRM_COORDINATOR)
    public Office getOffice() {
        return office;
    }


    public void setOffice(Office office) {
        this.office = office;
    }

    @Action(accessLevel=AccessLevel.FIRM_COORDINATOR)
    public ListDataModel getOfficesModel() {
        return officesModel;
    }

    public void setOfficesModel(ListDataModel officesModel) {
        this.officesModel = officesModel;
    }

    @Override
    protected Office createEntity() {
        return new Office();
    }

    @Override
    protected Office getEntity() {
        return office;
    }

    @Override
    protected void setEntity(Office entity) {
        setOffice(entity);
    }

    @Override
    protected String getListScreenName() {
        return "officesList";
    }

    @Override
    protected String getSingleScreenName() {
        return null;
    }

    @Override
    protected ListDataModel getModel() {
        return officesModel;
    }

    @Override
    protected Service getService() {
        return service;
    }
}
