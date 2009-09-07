package com.tickets.services;

import java.util.Collections;
import java.util.List;

import org.springframework.stereotype.Service;

import com.tickets.constants.Constants;
import com.tickets.constants.Settings;
import com.tickets.controllers.handlers.SeatHandler;
import com.tickets.model.Discount;
import com.tickets.model.PaymentMethod;
import com.tickets.model.Route;
import com.tickets.model.Run;
import com.tickets.model.SearchResultEntry;
import com.tickets.model.Ticket;
import com.tickets.model.User;
import com.tickets.utils.GeneralUtils;

@Service("ticketService")
public class TicketServiceImpl extends BaseService<Ticket> implements
        TicketService {

    @Override
    public Ticket createTicket(SearchResultEntry selectedEntry,
            SearchResultEntry selectedReturnEntry, int seat, int returnSeat,
            Discount discount) {

        // Allow only one user per run at a time, to avoid collisions
        String runIdIntern = "run"
                + String.valueOf(selectedEntry.getRun().getRunId()).intern();
        String returnRunIdIntern = null;
        if (selectedReturnEntry != null) {
            returnRunIdIntern = "run"
                    + String.valueOf(selectedEntry.getRun().getRunId())
                            .intern();
        }
        synchronized (runIdIntern) {
            if (returnRunIdIntern != null) {
                synchronized (returnRunIdIntern) {
                    return doCreateTicket(selectedEntry, selectedReturnEntry,
                            seat, returnSeat, discount);
                }
            }

            return doCreateTicket(selectedEntry, selectedReturnEntry, seat,
                    returnSeat, discount);
        }
    }

    @Override
    public Ticket createTicket(SearchResultEntry selectedEntry,
            SearchResultEntry selectedReturnEntry, int seat, int returnSeat) {

        return createTicket(selectedEntry, selectedReturnEntry, seat,
                returnSeat, null);
    }

    private Ticket doCreateTicket(SearchResultEntry selectedEntry,
            SearchResultEntry selectedReturnEntry, int seat, int returnSeat,
            Discount discount) {
        if (ServiceFunctions.getVacantSeats(selectedEntry.getRun(),
                selectedEntry.getPrice().getStartStop().getName(),
                selectedEntry.getPrice().getEndStop().getName()) > 0) {

            Ticket ticket = new Ticket();
            ticket.setSeat(seat);
            ticket.setReturnSeat(returnSeat);
            ticket.setRun(selectedEntry.getRun());
            ticket.setDiscount(discount);

            if (selectedReturnEntry == null) {
                ticket.setTwoWay(false);
                ticket.setPrice(selectedEntry.getPrice().getPrice());
            } else {
                // In case there are no more seats available, quit
                // the creation process
                if (ServiceFunctions.getVacantSeats(selectedReturnEntry
                        .getRun(), selectedReturnEntry.getPrice()
                        .getStartStop().getName(), selectedReturnEntry
                        .getPrice().getEndStop().getName()) < 0) {
                    return null;
                }

                ticket.setTwoWay(true);
                ticket.setPrice(selectedEntry.getPrice().getTwoWayPrice());
                ticket.setReturnRun(selectedReturnEntry.getRun());
            }
            ticket.setStartStop(selectedEntry.getPrice().getStartStop()
                    .getName());
            ticket.setEndStop(selectedEntry.getPrice().getEndStop().getName());

            // Creation time set in order to remove it after a certain
            // idle time without payment
            ticket.setCreationTime(GeneralUtils.createEmptyCalendar());
            ticket.setTicketCode(generateTicketCode(selectedEntry.getRun()));

            ticket = save(ticket);

            // updating the collections, so that the results
            // are 'visible' immediately
            // no risk of double-saving, because the ticket object
            // is already persistent
            selectedEntry.getRun().getTickets().add(ticket);
            if (selectedReturnEntry != null) {
                selectedReturnEntry.getRun().getReturnTickets().add(ticket);
            }

            return ticket;
        }

        return null;
    }

    public String generateTicketCode(Run run) {
        String code = "";
        try {
            code += run.getRoute().getFirm().getFirmId();
        } catch (Exception ex) {
            // Ignore - only occurs in test environment
        }
        code += run.getRunId();
        code += ("" + run.getTime().getTimeInMillis()).substring(8); // after
        // the
        // 7th
        // digit
        code += ((int) (Math.random() * 8999 + 100000));

        if (code.length() % 2 == 1) {
            code += "" + 3;
        }

        StringBuilder sb = new StringBuilder(code);
        for (int i = 2; i < code.length() - 1; i += 2) {
            sb.insert(i + (i - 2) / 2, '-');
        }
        return sb.toString();
    }

    @Override
    public void finalizePurchase(List<Ticket> tickets, User user) {
        for (Ticket ticket : tickets) {
            ticket.setCommitted(true);
            if (ticket.getPaymentMethod() == null) {
                ticket.setPaymentMethod(PaymentMethod.CASH_DESK);
            }
            if (ticket.getUser() == null) {
                ticket.setUser(user);
            }
            ticket = save(ticket);
        }
    }

    @Override
    public int getFirstVacantSeat(SearchResultEntry entry) {
        List<Integer> usedSeats = SeatHandler.getUsedSeats(entry.getPrice(),
                entry.getRun());

        Route route = entry.getRun().getRoute();
        for (int i = route.getSellSeatsFrom(); i < route.getSellSeatsTo(); i++) {
            if (Collections.binarySearch(usedSeats, i) < 0) {
                return i;
            }
        }

        throw new IllegalStateException("All seats have been taken!");
    }

    @SuppressWarnings("unchecked")
    @Override
    public List<Ticket> getTicketsByUser(User user) {
        return getDao().findByNamedQuery("Ticket.findByUser",
                new String[] { "user" }, new Object[] { user });
    }

    @SuppressWarnings("unchecked")
    @Override
    public void clearUnusedTickets() {
        List<Ticket> unconfirmed = getDao().findByNamedQuery(
                "Ticket.findUnconfirmed");

        long timeoutPeriod = Integer.parseInt(Settings
                .getValue("ticket.timeout"))
                * Constants.ONE_MINUTE;

        long inProcessTimeoutPeriod = timeoutPeriod * 3 / 2;
        for (Ticket ticket : unconfirmed) {
            long diff = System.currentTimeMillis()
                    - ticket.getCreationTime().getTimeInMillis();

            if (diff > (ticket.isPaymentInProcess()
                    ? inProcessTimeoutPeriod
                    : timeoutPeriod)) {
                ticket.setTimeouted(true);
            }
        }
    }
}
