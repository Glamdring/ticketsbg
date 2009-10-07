package com.tickets.utils.timers;

import java.util.TimerTask;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.tickets.services.TicketService;

@Component("unusedTicketsTimeoutingTask")
public class UnusedTicketsTimeoutingTask extends TimerTask {

    @Autowired
    private TicketService service;

    @Override
    public void run() {
        service.timeoutUnusedTickets();
    }

}
