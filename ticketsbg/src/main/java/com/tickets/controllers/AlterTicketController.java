package com.tickets.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import com.tickets.annotations.Action;
import com.tickets.controllers.security.AccessLevel;
import com.tickets.exceptions.TicketAlterationException;
import com.tickets.model.Ticket;
import com.tickets.services.TicketService;


@Controller("alterTicketController")
@Scope("conversation.access")
@Action(accessLevel=AccessLevel.PUBLIC)
public class AlterTicketController extends BaseController {

    @Autowired
    private TicketService service;

    @Autowired
    private SearchController searchController;

    private String ticketCode;

    private String email;

    private Ticket ticket;

    public void proceed() {

    }

    public void identifyTicket() {
        try {
            ticket = service.findTicket(ticketCode, email);
        } catch (TicketAlterationException ex) {
            if (ex.isAlreadyAltered()) {
                addError("secondTicketAlterationNotAllowed");
            } else {
                addError("ticketAlterationNotAllowed", ex.getHoursBeforeTravelAlterationAllowed());
            }
            return;
        }

        if (ticket == null) {
            addError("noTicketFound");
            return;
        }

        searchController.setDate(ticket.getRun().getTime().getTime());
        if (!ticket.isTwoWay() && ticket.getReturnRun() != null) {
            searchController.setReturnDate(ticket.getReturnRun().getTime().getTime());
            searchController.setTravelType("twoWay");
        } else {
            searchController.setTravelType("oneWay");
        }

        searchController.setFromStop(ticket.getStartStop());
        searchController.setToStop(ticket.getEndStop());
    }

    public String getTicketCode() {
        return ticketCode;
    }

    public void setTicketCode(String ticketCode) {
        this.ticketCode = ticketCode;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Ticket getTicket() {
        return ticket;
    }

    public void setTicket(Ticket ticket) {
        this.ticket = ticket;
    }
}
