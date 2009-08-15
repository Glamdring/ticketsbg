package com.tickets.controllers;

import java.io.Serializable;
import java.util.List;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import com.tickets.annotations.Action;
import com.tickets.controllers.security.AccessLevel;
import com.tickets.controllers.users.LoggedUserHolder;
import com.tickets.model.Ticket;
import com.tickets.services.TicketService;

@Controller("historyController")
@Scope("request")
@Action(accessLevel=AccessLevel.PUBLIC)
public class HistoryController
    extends BaseController
    implements Serializable {

    @Autowired
    private TicketService service;

    @Autowired
    private LoggedUserHolder userHolder;

    private List<Ticket> tickets;

    @PostConstruct
    public void init() {
        tickets = service.getTicketsByUser(userHolder.getLoggedUser());
    }

    public List<Ticket> getTickets() {
        return tickets;
    }

    public void setTickets(List<Ticket> tickets) {
        this.tickets = tickets;
    }

    public TicketService getService() {
        return service;
    }

    public void setService(TicketService service) {
        this.service = service;
    }

    public LoggedUserHolder getUserHolder() {
        return userHolder;
    }

    public void setUserHolder(LoggedUserHolder userHolder) {
        this.userHolder = userHolder;
    }
}
