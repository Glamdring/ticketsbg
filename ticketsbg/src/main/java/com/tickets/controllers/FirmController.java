package com.tickets.controllers;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;
import javax.faces.model.ListDataModel;
import javax.faces.model.SelectItem;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import com.tickets.annotations.Action;
import com.tickets.controllers.security.AccessLevel;
import com.tickets.model.Firm;
import com.tickets.services.Service;
import com.tickets.utils.SelectItemUtils;


@Controller("firmController")
@Scope("conversation.access")
@Action(accessLevel=AccessLevel.ADMINISTRATOR)
public class FirmController extends BaseCRUDController<Firm> {

    @Resource(name="baseService")
    private Service service;

    private Firm firm = new Firm();

    private ListDataModel firmsModel;

    private List<SelectItem> firmsSelectItems = new ArrayList<SelectItem>();

    @SuppressWarnings("unchecked")
    @Override
    protected void refreshList() {
        List<Firm> firms = service.list(Firm.class);
        firmsModel = new ListDataModel(firms);
        firmsSelectItems = SelectItemUtils.formSelectItems(firms);

        // End the current conversation in case the list of roles
        // is refreshed, but only if the bean has not just been constructed
        if (firm != null)
            endConversation();
    }

    @Action(accessLevel=AccessLevel.CASH_DESK)
    public Firm getFirm() {
        return firm;
    }


    public void setFirm(Firm firm) {
        this.firm = firm;
    }

    @Action(accessLevel=AccessLevel.ADMINISTRATOR)
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

    public List<SelectItem> getFirmsSelectItems() {
        return firmsSelectItems;
    }

    public void setFirmsSelectItems(List<SelectItem> firmsSelectItems) {
        this.firmsSelectItems = firmsSelectItems;
    }
}
