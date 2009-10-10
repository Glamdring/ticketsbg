package com.tickets.controllers;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import org.apache.myfaces.orchestra.conversation.annotations.ConversationName;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import com.tickets.controllers.valueobjects.Screen;
import com.tickets.controllers.valueobjects.Step;
import com.tickets.model.Customer;
import com.tickets.model.PaymentMethod;
import com.tickets.model.Ticket;
import com.tickets.model.User;
import com.tickets.services.PaymentService;
import com.tickets.services.TicketService;

@Controller("purchaseController")
@Scope("conversation.manual")
@ConversationName("purchaseConversation")
public class PurchaseController extends BaseController {

    private List<Ticket> tickets = new ArrayList<Ticket>();
    private Step currentStep;

    /** Holding the current ticket when operation with the cart table */
    private Ticket currentTicket;

    @Autowired
    private TicketService ticketService;

    @Autowired
    private PaymentService paymentService;


    public void setPaymentMethod(PaymentMethod paymentMethod) {
        for (Ticket ticket : tickets) {
            ticket.setPaymentMethod(paymentMethod);
            ticket.setPaymentInProcess(true);
        }
    }

    public void setUser(User user) {
        for (Ticket ticket : tickets) {
            ticket.setUser(user);
            // Not persisting here, because setCustomerInformation
            // is always called after this method
        }

    }

    public void setCustomerInformation(Customer customer) {
        for (Ticket ticket : tickets) {
            ticket.setCustomerInformation(customer);
            ticketService.save(ticket);
        }
    }

    public void finalizePurchase(User user) {
        ticketService.finalizePurchase(tickets, user);
        clearPurchase();
    }

    public void clearPurchase() {
        setCurrentStep(null);
        setTickets(new ArrayList<Ticket>());
    }

    public long getTimeRemaining() {
        return ticketService.getTimeRemaining(tickets);
    }

    public String timeoutPurchase() {
        for (Ticket ticket : tickets) {
            ticket.setTimeouted(true);
            ticketService.save(ticket);
        }

        tickets.clear();

        return Screen.SEARCH_SCREEN.getOutcome();
    }

    public BigDecimal getTotalPrice() {
        return paymentService.getTotalPrice(tickets);
    }

    public void removeTicket() {
        tickets.remove(currentTicket);
    }

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

    public Ticket getCurrentTicket() {
        return currentTicket;
    }
    public void setCurrentTicket(Ticket currentTicket) {
        this.currentTicket = currentTicket;
    }
}
