package com.tickets.services;

import java.util.List;

import com.tickets.model.Discount;
import com.tickets.model.SearchResultEntry;
import com.tickets.model.Ticket;
import com.tickets.model.User;

public interface TicketService extends Service<Ticket> {

    Ticket createTicket(SearchResultEntry selectedEntry,
            SearchResultEntry selectedReturnEntry, int seat, int returnSeat);

    Ticket createTicket(SearchResultEntry selectedEntry,
            SearchResultEntry selectedReturnEntry, int seat, int returnSeat, Discount discount);

    /**
     * Finalizes the purchase
     * @param tickets
     * @param the logged user
     *
     */
    void finalizePurchase(List<Ticket> tickets, User user);

    /**
     * Gets the number of the first vacant seat
     * Used when the user hasn't chosen a seat
     *
     * @param selectedEntry
     * @return the seat number
     */
    int getFirstVacantSeat(SearchResultEntry selectedEntry);

    /**
     * Lists all tickets purchased by the specified user
     * @param user
     * @return the list
     */

    List<Ticket> getTicketsByUser(User user);
}
