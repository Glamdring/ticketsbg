package com.tickets.controllers;

import java.util.ArrayList;
import java.util.List;

import org.apache.myfaces.orchestra.conversation.annotations.ConversationName;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import com.tickets.model.Customer;
import com.tickets.model.PaymentMethod;
import com.tickets.model.Ticket;
import com.tickets.model.User;

@Controller("purchaseController")
@Scope("conversation.manual")
@ConversationName("purchaseConversation")
public class PurchaseController extends BaseController {

    private List<Ticket> tickets = new ArrayList<Ticket>();
    private Step currentStep;

    public Step getCurrentStep() {
        return currentStep;
    }
    public void setCurrentStep(Step currentStep) {
        this.currentStep = currentStep;
    }
    public List<Ticket> getTickets() {
        return tickets;
    }
    public void setTickets(List<Ticket> tickets) {
        this.tickets = tickets;
    }

    public void addTicket(Ticket ticket) {
        tickets.add(ticket);
    }

    public void setPaymentMethod(PaymentMethod paymentMethod) {
        for (Ticket ticket : tickets) {
            ticket.setPaymentMethod(paymentMethod);
        }
    }
    public void setUser(User user) {
        for (Ticket ticket : tickets) {
            ticket.setUser(user);
        }

    }
    public void setCustomerInformation(Customer customer) {
        for (Ticket ticket : tickets) {
            ticket.setCustomerInformation(customer);
        }
    }
}
