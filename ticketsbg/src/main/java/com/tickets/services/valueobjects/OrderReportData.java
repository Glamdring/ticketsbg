package com.tickets.services.valueobjects;

import java.io.Serializable;
import java.util.Collection;

public class OrderReportData implements Serializable {

    private String title;
    private Collection<TicketInfo> tickets;

    public String getTitle() {
        return title;
    }
    public void setTitle(String title) {
        this.title = title;
    }
    public Collection<TicketInfo> getTickets() {
        return tickets;
    }
    public void setTickets(Collection<TicketInfo> tickets) {
        this.tickets = tickets;
    }
}