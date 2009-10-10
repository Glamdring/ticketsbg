package com.tickets.services;

import java.util.List;

import com.tickets.exceptions.TicketCreationException;
import com.tickets.model.SearchResultEntry;
import com.tickets.model.Ticket;
import com.tickets.model.User;
import com.tickets.services.valueobjects.Seat;
import com.tickets.services.valueobjects.TicketCountsHolder;

public interface TicketService extends Service<Ticket> {

    /**
     * Creates a ticket for the supplied arguments
     *
     * @param selectedEntry
     * @param selectedReturnEntry
     * @param ticketCountsHolder
     * @param selectedSeats
     * @param selectedReturnSeats
     * @return the created and persisted ticket
     * @throws TicketCreationException
     */
    Ticket createTicket(SearchResultEntry selectedEntry,
            SearchResultEntry selectedReturnEntry,
            TicketCountsHolder ticketCountsHolder,
            List<Seat> selectedSeats,
            List<Seat> selectedReturnSeats) throws TicketCreationException;


    /**
     * Search for the appropriate ticket
     * @param ticketCode
     * @param email
     * @return the found ticket
     */
    Ticket findTicket(String ticketCode, String email);

    /**
     * Alters the supplied ticket
     *
     * @param selectedEntry
     * @param selectedReturnEntry
     * @param ticketCountsHolder
     * @param selectedSeats
     * @param selectedReturnSeats
     * @throws TicketCreationException
     */
    void alterTicket(Ticket ticket, SearchResultEntry selectedEntry,
            SearchResultEntry selectedReturnEntry,
            TicketCountsHolder ticketCountsHolder, List<Seat> selectedSeats,
            List<Seat> selectedReturnSeats) throws TicketCreationException ;

    /**
     * Finalizes the purchase
     * @param tickets
     * @param the logged user
     *
     */
    void finalizePurchase(List<Ticket> tickets, User user);


    /**
     * Finalizes the purchase
     * @param tickets
     *
     */
    void finalizePurchase(List<Ticket> tickets);

    /**
     * Lists all tickets purchased by the specified user
     * @param user
     * @return the list
     */

    List<Ticket> getTicketsByUser(User user);

    /**
     * Timeouts unused tickets. The timeout is defined in a setting,
     * in minutes. All tickets that have timeouted and don't have
     * completed transactions are set as timeouted=true, and then the
     * user is notified of the timeout by the UI
     */
    void timeoutUnusedTickets();


    /**
     * Deletes all tickets that have been set as timeouted=true.
     */
    void clearTimeoutedTickets();


    /**
     * Calculates the remaining time before the tickets timeout
     * @param tickets
     * @return time remaining
     */
    long getTimeRemaining(List<Ticket> tickets);
}
