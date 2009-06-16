package com.tickets.services;

import org.springframework.stereotype.Service;

import com.tickets.model.SearchResultEntry;
import com.tickets.model.Ticket;

@Service("ticketService")
public class TicketServiceImpl extends BaseService<Ticket> implements TicketService {

    @Override
    public Ticket createTicket(SearchResultEntry selectedEntry,
            SearchResultEntry selectedReturnEntry) {

        Ticket ticket = new Ticket();
        ticket.setRun(selectedEntry.getRun());
        if (selectedReturnEntry == null) {
            ticket.setTwoWay(false);
            ticket.setPrice(selectedEntry.getPrice().getPrice());
        } else {
            ticket.setTwoWay(true);
            ticket.setPrice(selectedEntry.getPrice().getTwoWayPrice());
        }
        ticket.setStartStop(selectedEntry.getPrice().getStartStop().getName());
        ticket.setEndStop(selectedEntry.getPrice().getEndStop().getName());

        save(ticket);

        return ticket;
    }

}
