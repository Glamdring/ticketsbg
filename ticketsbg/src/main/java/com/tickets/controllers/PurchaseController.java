package com.tickets.controllers;

import org.apache.myfaces.orchestra.conversation.annotations.ConversationName;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import com.tickets.model.Ticket;

@Controller("purchaseController")
@Scope("conversation.manual")
@ConversationName("purchaseConversation")
public class PurchaseController extends BaseController {

    private Ticket ticket;
    private Step currentStep;

    public Ticket getTicket() {
        return ticket;
    }
    public void setTicket(Ticket ticket) {
        this.ticket = ticket;
    }
    public Step getCurrentStep() {
        return currentStep;
    }
    public void setCurrentStep(Step currentStep) {
        this.currentStep = currentStep;
    }
}
