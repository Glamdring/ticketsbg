package com.tickets.services;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tickets.constants.Constants;
import com.tickets.constants.Settings;
import com.tickets.controllers.handlers.SeatHandler;
import com.tickets.exceptions.TicketAlterationException;
import com.tickets.exceptions.TicketCreationException;
import com.tickets.model.Discount;
import com.tickets.model.PassengerDetails;
import com.tickets.model.PaymentMethod;
import com.tickets.model.Route;
import com.tickets.model.Run;
import com.tickets.model.SearchResultEntry;
import com.tickets.model.Ticket;
import com.tickets.model.User;
import com.tickets.services.valueobjects.Seat;
import com.tickets.services.valueobjects.TicketCount;
import com.tickets.services.valueobjects.TicketCountsHolder;
import com.tickets.utils.GeneralUtils;

@Service("ticketService")
public class TicketServiceImpl extends BaseService<Ticket> implements
        TicketService {

    @Autowired
    private DiscountService discountService;

    public Ticket createTicket(SearchResultEntry selectedEntry,
            SearchResultEntry selectedReturnEntry, TicketCountsHolder ticketCountsHolder,
            List<Seat> selectedSeats, List<Seat> selectedReturnSeats) throws TicketCreationException {

        // Allow only one user per run at a time, to avoid collisions
        String runIdIntern = ("run"
                + String.valueOf(selectedEntry.getRun().getRunId())).intern();
        String returnRunIdIntern = null;

        if (selectedReturnEntry != null) {
            returnRunIdIntern = ("run"
                    + String.valueOf(selectedEntry.getRun().getRunId()))
                            .intern();
        }

        synchronized (runIdIntern) {
            if (returnRunIdIntern != null) {
                synchronized (returnRunIdIntern) {
                    return doCreateTicket(selectedEntry, selectedReturnEntry,
                            ticketCountsHolder, selectedSeats, selectedReturnSeats);
                }
            }

            return doCreateTicket(selectedEntry, selectedReturnEntry,
                    ticketCountsHolder, selectedSeats, selectedReturnSeats);
        }
    }

    private Ticket doCreateTicket(SearchResultEntry selectedEntry,
            SearchResultEntry selectedReturnEntry, TicketCountsHolder ticketCountsHolder,
            List<Seat> selectedSeats, List<Seat> selectedReturnSeats) throws TicketCreationException {

        Ticket ticket = new Ticket();
        ticket = fillTicketData(ticket, selectedEntry, selectedReturnEntry,
                ticketCountsHolder, selectedSeats, selectedReturnSeats);

        return ticket;
    }

    private Ticket fillTicketData(Ticket ticket, SearchResultEntry selectedEntry,
            SearchResultEntry selectedReturnEntry, TicketCountsHolder ticketCountsHolder,
            List<Seat> selectedSeats, List<Seat> selectedReturnSeats) throws TicketCreationException {

        // Validate selected ticket counts
        if (ticketCountsHolder.getTotalCount() == 0) {
            throw new TicketCreationException("mustChooseNonZeroTicketCounts");
        }

        int totalRequestedTickets = ticketCountsHolder.getTotalCount();

        Run run = selectedEntry.getRun();
        //reload the run
        getDao().refresh(run);

        boolean enoughSeats = ServiceFunctions.getVacantSeats(run,
                selectedEntry.getPrice().getStartStop().getName(),
                selectedEntry.getPrice().getEndStop().getName(), false) >= totalRequestedTickets;


        // defaults to true (in case no return is chosen)
        boolean enoughReturnSeats = true;

        Run returnRun = null;
        if (selectedReturnEntry != null) {
            returnRun = selectedReturnEntry.getRun();
            getDao().refresh(returnRun);
            enoughReturnSeats = ServiceFunctions.getVacantSeats(returnRun, selectedReturnEntry.getPrice()
                .getStartStop().getName(), selectedReturnEntry
                .getPrice().getEndStop().getName(), false) >= totalRequestedTickets;
        }

        if (enoughSeats && enoughReturnSeats) {

            ticket.setRun(selectedEntry.getRun());

            if (selectedReturnEntry == null) {
                ticket.setTwoWay(false);
            } else {
                ticket.setTwoWay(true);
                ticket.setReturnRun(selectedReturnEntry.getRun());
            }

            ticket.setStartStop(selectedEntry.getPrice().getStartStop()
                    .getName());
            ticket.setEndStop(selectedEntry.getPrice().getEndStop().getName());

            // Creation time set in order to remove it after a certain
            // idle time without payment
            ticket.setCreationTime(GeneralUtils.createEmptyCalendar());
            ticket.setTicketCode(generateTicketCode(selectedEntry.getRun()));

            int counter = 0;
            for (counter = 0; counter < ticketCountsHolder.getRegularTicketsCount(); counter++) {

                Seats seats = getSeatNumbers(selectedEntry,
                        selectedReturnEntry, selectedSeats,
                        selectedReturnSeats, counter);

                PassengerDetails deatils = createPassengerDetails(selectedEntry,
                        selectedReturnEntry, seats.getSeat(), seats.getReturnSeat());
                ticket.addPassengerDetails(deatils);
            }

            for (TicketCount tc : ticketCountsHolder.getTicketCounts()) {
                for (int i = 0; i < tc.getNumberOfTickets(); i++) {
                    counter ++;
                    Seats seats = getSeatNumbers(selectedEntry,
                            selectedReturnEntry, selectedSeats,
                            selectedReturnSeats, counter);
                    PassengerDetails details = createPassengerDetails(
                            selectedEntry, selectedReturnEntry,
                            seats.getSeat(), seats.getReturnSeat(),
                                tc.getDiscount());

                    ticket.addPassengerDetails(details);
                }
            }


            BigDecimal totalPrice = BigDecimal.ZERO;
            for (PassengerDetails pd : ticket.getPassengerDetails()) {
                totalPrice = totalPrice.add(pd.getPrice());
            }

            ticket.setTotalPrice(totalPrice);
            ticket.setPassengersCount(ticket.getPassengerDetails().size());

            // Collections with tickets should be updated only in case
            // the current ticket is just created (and not an existing one
            // that is being altered)
            boolean shouldUpdateCollections = ticket.getId() == 0;

            // Persist
            ticket = save(ticket);

            // updating the collections, so that the results
            // are 'visible' immediately
            // no risk of double-saving, because the ticket object
            // is already persistent
            if (shouldUpdateCollections) {
                selectedEntry.getRun().getTickets().add(ticket);
                if (selectedReturnEntry != null) {
                    selectedReturnEntry.getRun().getReturnTickets().add(ticket);
                }
            }

            // Flush here, so that data is flushed while in synchronized block
            getDao().flush();
        } else {
            // If the ticket isn't created - i.e. another user has just
            // confirmed a ticket, leaving less seats than requested,
            // re-do the search and display an error message
            if (!enoughSeats) {
                throw new TicketCreationException("remainingSeatsLessThanRequested", true);
            }

            if (!enoughReturnSeats) {
                throw new TicketCreationException("remainingReturnSeatsLessThanRequested", true);
            }
        }

        return ticket;
    }

    private PassengerDetails createPassengerDetails(SearchResultEntry selectedEntry,
            SearchResultEntry selectedReturnEntry, int seat, int returnSeat) {

        return createPassengerDetails(selectedEntry, selectedReturnEntry, seat,
                returnSeat, null);
    }

    private PassengerDetails createPassengerDetails(SearchResultEntry selectedEntry,
            SearchResultEntry selectedReturnEntry, int seat, int returnSeat,
            Discount discount) {


        PassengerDetails details = new PassengerDetails();

        details.setSeat(seat);
        details.setDiscount(discount);

        if (selectedReturnEntry == null) {
            details.setPrice(selectedEntry.getPrice().getPrice());
        } else {
            details.setPrice(selectedEntry.getPrice().getTwoWayPrice());
            details.setReturnSeat(returnSeat);
        }

        if (discount != null) {
            BigDecimal price = details.getPrice();
            price = discountService.calculatePrice(price, discount);
            details.setPrice(price);
        }

        return details;

    }

    private Seats getSeatNumbers(SearchResultEntry selectedEntry,
            SearchResultEntry selectedReturnEntry, List<Seat> selectedSeats,
            List<Seat> selectedReturnSeats, int currentCounter) {

        int seat = -1;
        int returnSeat = -1;
        if (selectedSeats.size() > currentCounter) {
            seat = selectedSeats.get(currentCounter).getNumber();
        }
        if (selectedReturnSeats != null && selectedReturnSeats.size() > currentCounter) {
            returnSeat = selectedReturnSeats.get(currentCounter).getNumber();
        }

        if (seat == -1) {
            seat = getFirstVacantSeat(selectedEntry);
        }
        if (returnSeat == -1) {
            seat = getFirstVacantSeat(selectedReturnEntry);
        }

        Seats seats = new Seats();
        seats.setSeat(seat);
        seats.setReturnSeat(returnSeat);

        return seats;
    }

    String generateTicketCode(Run run) {
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

    /**
     * Gets the number of the first vacant seat
     * Used when the user hasn't chosen a seat
     *
     * @param selectedEntry
     * @return the seat number
     */
    private int getFirstVacantSeat(SearchResultEntry entry) {
        List<Integer> usedSeats = SeatHandler.getUsedSeats(entry.getPrice(),
                entry.getRun());

        Route route = entry.getRun().getRoute();
        for (int i = route.getSellSeatsFrom(); i <= route.getSellSeatsTo(); i++) {
            if (Collections.binarySearch(usedSeats, i) < 0) {
                return i;
            }
        }

        return -1;
    }

    //------------------ End of Ticket creation methods -------------------


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

    @SuppressWarnings("unchecked")
    @Override
    public List<Ticket> getTicketsByUser(User user) {
        return getDao().findByNamedQuery("Ticket.findByUser",
                new String[] { "user" }, new Object[] { user });
    }

    @SuppressWarnings("unchecked")
    @Override
    public void timeoutUnusedTickets() {
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

        System.gc();
    }


    @Override
    public void clearTimeoutedTickets() {
        getDao().executeNamedQuery("Ticket.deleteTimeouted");
    }



    @Override
    public Ticket findTicket(String ticketCode, String email) {
        List result = getDao().findByNamedQuery("Ticket.findByCodeAndEmail",
                new String[] { "ticketCode", "email" },
                new Object[] { ticketCode, email });

        if (result.size() > 0) {
            Ticket ticket = (Ticket) result.get(0);
            int hoursBeforeTravelAlterationAllowed = ticket.getRun().getRoute().getFirm().getHoursBeforeTravelAlterationAllowed();

            // Check whether the ticket can be altered
            if (ticket.getRun().getTime().getTimeInMillis()
                    - GeneralUtils.createEmptyCalendar().getTimeInMillis()
                    >= hoursBeforeTravelAlterationAllowed * Constants.ONE_HOUR ) {

                if (!ticket.isAltered()) {
                    return ticket;
                }
                throw new TicketAlterationException();
            }
            throw new TicketAlterationException(hoursBeforeTravelAlterationAllowed);
        }

        return null;
    }

    @Override
    public void alterTicket(Ticket ticket, SearchResultEntry selectedEntry,
            SearchResultEntry selectedReturnEntry,
            TicketCountsHolder ticketCountsHolder, List<Seat> selectedSeats,
            List<Seat> selectedReturnSeats) throws TicketCreationException {

        ticket.setAltered(true);

        BigDecimal originalPrice = ticket.getTotalPrice();
        fillTicketData(ticket, selectedEntry, selectedReturnEntry, ticketCountsHolder, selectedSeats, selectedReturnSeats);
        BigDecimal newPrice = ticket.getTotalPrice();

        ticket.setAlterationPriceDifference(newPrice.subtract(originalPrice));
    }
}

class Seats {
    private int seat;
    private int returnSeat;
    public int getSeat() {
        return seat;
    }
    public void setSeat(int seat) {
        this.seat = seat;
    }
    public int getReturnSeat() {
        return returnSeat;
    }
    public void setReturnSeat(int returnSeat) {
        this.returnSeat = returnSeat;
    }
}
