package com.tickets.model;

import java.io.Serializable;

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

}
