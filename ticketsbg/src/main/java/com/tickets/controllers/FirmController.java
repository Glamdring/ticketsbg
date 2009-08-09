package com.tickets.controllers;

import javax.annotation.Resource;
import javax.faces.model.ListDataModel;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import com.tickets.annotations.Action;
import com.tickets.controllers.security.AccessLevel;
import com.tickets.model.Firm;
import com.tickets.services.Service;


@Controller("firmController")
@Scope("conversation.access")
@Action(accessLevel=AccessLevel.ADMINISTRATOR)
public class FirmController extends BaseCRUDController<Firm> {

    @Resource(name="baseService")
    private Service service;

    private Firm firm = new Firm();

    private ListDataModel firmsModel;

    @SuppressWarnings("unchecked")
    @Override
    protected void refreshList() {
        firmsModel = new ListDataModel(service.list(Firm.class));

        // End the current conversation in case the list of roles
        // is refreshed, but only if the bean has not just been constructed
        if (firm != null)
            endConversation();
    }

    public Firm getFirm() {
        return firm;
    }


    public void setFirm(Firm firm) {
        this.firm = firm;
    }


    public ListDataModel getFirmsModel() {
        return firmsModel;
    }

    public void setFirmsModel(ListDataModel firmsModel) {
        this.firmsModel = firmsModel;
    }

    @Override
    protected Firm createEntity() {
        return new Firm();
    }

    @Override
    protected Firm getEntity() {
        return firm;
    }

    @Override
    protected void setEntity(Firm entity) {
        setFirm(entity);
    }

    @Override
    protected String getListScreenName() {
        return "firmsList";
    }

    @Override
    protected String getSingleScreenName() {
        return "firmScreen";
    }

    @Override
    protected ListDataModel getModel() {
        return firmsModel;
    }

    @Override
    protected Service getService() {
        return service;
    }

}
