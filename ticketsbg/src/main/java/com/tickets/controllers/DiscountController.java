package com.tickets.controllers;

import javax.faces.model.ListDataModel;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import com.tickets.annotations.Action;
import com.tickets.controllers.security.AccessLevel;
import com.tickets.controllers.users.LoggedUserHolder;
import com.tickets.model.GenericDiscount;
import com.tickets.services.DiscountService;
import com.tickets.services.Service;


@Controller("discountController")
@Scope("conversation.access")
@Action(accessLevel=AccessLevel.FIRM_ADMINISTRATOR)
public class DiscountController extends BaseCRUDController<GenericDiscount> {

    @Autowired
    private DiscountService service;

    @Autowired
    private LoggedUserHolder loggedUserHolder;

    private GenericDiscount discount = new GenericDiscount();

    private ListDataModel discountsModel;

    @Override
    public void save() {
        if (discount.getFirm() == null) {
            discount.setFirm(loggedUserHolder.getLoggedUser().getFirm());
        }
        super.save();
    }

    @Override
    protected void refreshList() {
        discountsModel = new ListDataModel(
                service.getGenericDiscounts(loggedUserHolder.getLoggedUser().getFirm()));

        if (discount != null)
            endConversation();
    }

    @Override
    protected GenericDiscount createEntity() {
        return new GenericDiscount();
    }

    @Override
    protected GenericDiscount getEntity() {
        return discount;
    }

    @Override
    protected void setEntity(GenericDiscount entity) {
        setDiscount(entity);
    }

    @Override
    protected String getListScreenName() {
        return "discountsList";
    }

    @Override
    protected String getSingleScreenName() {
        return "discountScreen";
    }

    @Override
    protected ListDataModel getModel() {
        return discountsModel;
    }

    @Override
    protected Service getService() {
        return service;
    }

    public GenericDiscount getDiscount() {
        return discount;
    }

    public void setDiscount(GenericDiscount discount) {
        this.discount = discount;
    }

    public ListDataModel getDiscountsModel() {
        return discountsModel;
    }

    public void setDiscountsModel(ListDataModel discountsModel) {
        this.discountsModel = discountsModel;
    }

    public LoggedUserHolder getLoggedUserHolder() {
        return loggedUserHolder;
    }

    public void setLoggedUserHolder(LoggedUserHolder loggedUserHolder) {
        this.loggedUserHolder = loggedUserHolder;
    }
}
