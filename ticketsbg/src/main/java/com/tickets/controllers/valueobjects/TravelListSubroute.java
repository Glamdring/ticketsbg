package com.tickets.controllers.valueobjects;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;


public class TravelListSubroute {

    private String caption;
    private List<TicketDisplayInfo> tickets = new LinkedList<TicketDisplayInfo>();

    public String getCaption() {
        return caption;
    }
    public void setCaption(String caption) {
        this.caption = caption;
    }
    public List<TicketDisplayInfo> getTickets() {
        return tickets;
    }
    public void setTickets(List<TicketDisplayInfo> tickets) {
        this.tickets = tickets;
    }

    public void order() {
        Collections.sort(tickets);
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((caption == null) ? 0 : caption.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        TravelListSubroute other = (TravelListSubroute) obj;
        if (caption == null) {
            if (other.caption != null)
                return false;
        } else if (!caption.equals(other.caption))
            return false;
        return true;
    }
}
