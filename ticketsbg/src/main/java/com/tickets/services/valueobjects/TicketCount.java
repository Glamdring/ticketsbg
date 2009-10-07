package com.tickets.services.valueobjects;

import java.io.Serializable;

import com.tickets.model.Discount;

/**
 * A List of ticket-counts is used to display ticket selection options
 * @author Bozhidar Bozhanov
 *
 */

public class TicketCount implements Serializable {

    private Discount discount;
    private int numberOfTickets;

    public Discount getDiscount() {
        return discount;
    }
    public void setDiscount(Discount discount) {
        this.discount = discount;
    }
    public int getNumberOfTickets() {
        return numberOfTickets;
    }
    public void setNumberOfTickets(int numberOfTickets) {
        this.numberOfTickets = numberOfTickets;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result
                + ((discount == null) ? 0 : discount.hashCode());
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
        TicketCount other = (TicketCount) obj;
        if (discount == null) {
            if (other.discount != null)
                return false;
        } else if (!discount.equals(other.discount))
            return false;
        return true;
    }
}
