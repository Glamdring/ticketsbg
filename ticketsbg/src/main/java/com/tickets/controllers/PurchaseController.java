package com.tickets.controllers;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import org.apache.myfaces.orchestra.conversation.annotations.ConversationName;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import com.tickets.constants.Constants;
import com.tickets.constants.Settings;
import com.tickets.controllers.valueobjects.Screen;
import com.tickets.controllers.valueobjects.Step;
import com.tickets.model.Customer;
import com.tickets.model.PaymentMethod;
import com.tickets.model.Ticket;
import com.tickets.model.User;
import com.tickets.services.TicketService;

@Controller("purchaseController")
@Scope("conversation.manual")
@ConversationName("purchaseConversation")
public class PurchaseController extends BaseController {

    private List<Ticket> tickets = new ArrayList<Ticket>();
    private Step currentStep;

    @Autowired
    private TicketService service;

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
            ticket.setPaymentInProcess(true);
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

    public void finalizePurchase(User user) {
        service.finalizePurchase(tickets, user);
        setCurrentStep(null);
        setTickets(new ArrayList<Ticket>());
    }

    public long getTimeRemaining() {
        Calendar min = null;
        for (Ticket ticket : tickets) {
            if (min == null) {
                min = ticket.getCreationTime();
            } else if (min.compareTo(ticket.getCreationTime()) < 0){
                min = ticket.getCreationTime();
            }
        }

        if (min == null) {
            return 0;
        }

        long timeoutPeriod = Integer.parseInt(Settings
                .getValue("ticket.timeout"))
                * Constants.ONE_MINUTE;

        long result = timeoutPeriod - (System.currentTimeMillis() - min.getTimeInMillis());

        if (result > timeoutPeriod || result < 0) {
            return 0;
        }

        return result;
    }

    public String timeoutPurchase() {
        for (Ticket ticket : tickets) {
            ticket.setTimeouted(true);
            service.save(ticket);
        }

        tickets.clear();

        return Screen.SEARCH_SCREEN.getOutcome();
    }

    public BigDecimal getTotalPrice() {
        BigDecimal sum = BigDecimal.ZERO;
        for (Ticket ticket : tickets) {
            if (!ticket.isAltered()) {
                sum = sum.add(ticket.getTotalPrice());
            } else {
                sum = sum.add(ticket.getAlterationPriceDifference());
            }
        }

        return sum;
    }
}
