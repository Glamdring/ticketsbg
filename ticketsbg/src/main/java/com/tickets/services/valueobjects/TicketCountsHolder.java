package com.tickets.services.valueobjects;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;


public class TicketCountsHolder implements Serializable {

    // defaults to 1 for user-friendliness, as this is the most common case
    private int regularTicketsCount = 1;
    private List<TicketCount> ticketCounts = new ArrayList<TicketCount>();


    public int getRegularTicketsCount() {
        return regularTicketsCount;
    }


    public void setRegularTicketsCount(int regularTicketsCount) {
        this.regularTicketsCount = regularTicketsCount;
    }


    public List<TicketCount> getTicketCounts() {
        return ticketCounts;
    }


    public void setTicketCounts(List<TicketCount> ticketCounts) {
        this.ticketCounts = ticketCounts;
    }

    public int getTotalCount() {
        int total = regularTicketsCount;
        for (TicketCount tc : ticketCounts) {
            total += tc.getNumberOfTickets();
        }
        return total;
    }
}
