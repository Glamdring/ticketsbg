package com.tickets.controllers;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import javax.faces.context.FacesContext;
import javax.faces.event.PhaseEvent;
import javax.faces.event.PhaseId;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.springframework.web.context.request.ServletRequestAttributes;

import com.tickets.controllers.valueobjects.Screen;
import com.tickets.controllers.valueobjects.Step;
import com.tickets.model.Customer;
import com.tickets.model.Order;
import com.tickets.model.PaymentMethod;
import com.tickets.model.Ticket;
import com.tickets.model.User;
import com.tickets.services.PaymentService;
import com.tickets.services.TicketService;

@Controller("purchaseController")
@Scope("session")
public class PurchaseController extends BaseController {

    private List<Ticket> tickets = new ArrayList<Ticket>();
    private Order order;

    private Step currentStep;

    /** Holding the current ticket when operation with the cart table */
    private Ticket currentTicket;

    @Autowired
    private transient TicketService ticketService;

    @Autowired
    private transient PaymentService paymentService;


    public void setPaymentMethod(PaymentMethod paymentMethod) {
        for (Ticket ticket : tickets) {
            ticket.setPaymentMethod(paymentMethod);
            ticket.setPaymentInProcess(true);
            ticketService.save(ticket);
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
        HttpSession session = ((HttpServletRequest) FacesContext
                .getCurrentInstance().getExternalContext().getRequest())
                .getSession();


        String prefix = ServletRequestAttributes.DESTRUCTION_CALLBACK_NAME_PREFIX;
        session.removeAttribute(prefix + "searchController");
        session.removeAttribute(prefix + "paymentController");
        session.removeAttribute(prefix + "purchaseController");
        session.removeAttribute(prefix + "personalInformationController");
    }

    /**
     * Method is set as phase listener for
     * payment success and cancellation pages
     *
     * @param evt
     */
    public void afterPhase(PhaseEvent evt) {
        if (evt.getPhaseId() == PhaseId.RENDER_RESPONSE) {
            clearPurchase();
        }
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

    public Order getOrder() {
        if (order == null) {
            order = new Order();
            order.setTickets(tickets);
        }
        return order;
    }

    public void setOrder(Order order) {
        this.order = order;
    }

}
