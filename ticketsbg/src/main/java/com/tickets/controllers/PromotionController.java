package com.tickets.controllers;

import java.util.List;

import javax.annotation.Resource;
import javax.faces.model.ListDataModel;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import com.tickets.annotations.Action;
import com.tickets.controllers.security.AccessLevel;
import com.tickets.controllers.users.LoggedUserHolder;
import com.tickets.model.Promotion;
import com.tickets.services.Service;


@Controller("promotionController")
@Scope("conversation.access")
@Action(accessLevel=AccessLevel.ADMINISTRATOR)
public class PromotionController extends BaseCRUDController<Promotion> {

    @Resource(name="baseService")
    private Service service;

    private Promotion promotion = new Promotion();

    private ListDataModel promotionsModel;

    @Override
    public void save() {
        promotion.setFirm(LoggedUserHolder.getUser().getFirm());
        super.save();
    }

    @SuppressWarnings("unchecked")
    @Override
    protected void refreshList() {
        List<Promotion> promotions = service.list(Promotion.class);
        promotionsModel = new ListDataModel(promotions);

        // End the current conversation in case the list of roles
        // is refreshed, but only if the bean has not just been constructed
        if (promotion != null)
            endConversation();
    }

    @Action(accessLevel=AccessLevel.FIRM_ADMINISTRATOR)
    public Promotion getPromotion() {
        return promotion;
    }


    public void setPromotion(Promotion promotion) {
        this.promotion = promotion;
    }

    @Action(accessLevel=AccessLevel.ADMINISTRATOR)
    public ListDataModel getPromotionsModel() {
        return promotionsModel;
    }

    public void setPromotionsModel(ListDataModel promotionsModel) {
        this.promotionsModel = promotionsModel;
    }

    @Override
    protected Promotion createEntity() {
        return new Promotion();
    }

    @Override
    protected Promotion getEntity() {
        return promotion;
    }

    @Override
    protected void setEntity(Promotion entity) {
        setPromotion(entity);
    }

    @Override
    protected String getListScreenName() {
        return "promotionsList";
    }

    @Override
    protected String getSingleScreenName() {
        return "promotionScreen";
    }

    @Override
    protected ListDataModel getModel() {
        return promotionsModel;
    }

    @Override
    protected Service getService() {
        return service;
    }
}
