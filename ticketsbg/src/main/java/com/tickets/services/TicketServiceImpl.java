package com.tickets.services;

import java.util.Calendar;

import org.springframework.stereotype.Service;

import com.tickets.model.Run;
import com.tickets.model.SearchResultEntry;
import com.tickets.model.Ticket;
import com.tickets.utils.GeneralUtils;

@Service("ticketService")
public class TicketServiceImpl extends BaseService<Ticket> implements TicketService {

    @Override
    public Ticket createTicket(SearchResultEntry selectedEntry,
            SearchResultEntry selectedReturnEntry) {

        //Allow only one user per run at a time, to avoid collisions
        String runIdIntern = "run" + String.valueOf(selectedEntry.getRun().getRunId()).intern();
        String returnRunIdIntern = null;
        if (selectedReturnEntry != null) {
            returnRunIdIntern = "run" + String.valueOf(selectedEntry.getRun().getRunId()).intern();
        }
        synchronized (runIdIntern) {
            if (returnRunIdIntern != null) {
                synchronized(returnRunIdIntern) {
                    return doCreateTicket(selectedEntry, selectedReturnEntry);
                }
            }

            return doCreateTicket(selectedEntry, selectedReturnEntry);
        }
    }

    private Ticket doCreateTicket(SearchResultEntry selectedEntry,
            SearchResultEntry selectedReturnEntry) {
        if (selectedEntry.getRun().getVacantSeats() > 0) {
            Ticket ticket = new Ticket();
            ticket.setRun(selectedEntry.getRun());
            if (selectedReturnEntry == null) {
                ticket.setTwoWay(false);
                ticket.setPrice(selectedEntry.getPrice().getPrice());
            } else {
                // In case there are no more seats available, quit
                // the creation process
                if (selectedReturnEntry.getRun().getVacantSeats() < 0) {
                    return null;
                }
                ticket.setTwoWay(true);
                ticket.setPrice(selectedEntry.getPrice().getTwoWayPrice());
                ticket.setReturnRun(selectedReturnEntry.getRun());
            }
            ticket.setStartStop(selectedEntry.getPrice().getStartStop().getName());
            ticket.setEndStop(selectedEntry.getPrice().getEndStop().getName());

            // Creation time set in order to remove it after a certain
            // idle time without payment
            Calendar creationTime = GeneralUtils.createEmptyCalendar();
            creationTime.setTimeInMillis(System.currentTimeMillis());
            ticket.setCreationTime(creationTime);
            ticket.setTicketCode(generateTicketCode(selectedEntry.getRun()));
            save(ticket);
            return ticket;
        }

        return null;
    }

    public String generateTicketCode(Run run) {
        String code = "" + run.getRoute().getFirm().getFirmId();
        code += run.getRoute().getId();
        code += run.getRunId();
        code += ("" + run.getTime().getTimeInMillis()).substring(6); //after the 5th digit
        code += ((int) (Math.random() * 899999 + 100000));

        if (code.length() % 2 == 1) {
            code += "" + 3;
        }

        StringBuilder sb = new StringBuilder(code);
        for (int i = 2; i < code.length() - 1 ; i += 2) {
            sb.insert(i + (i-2)/2, '-');
        }
        return sb.toString();
    }
}
