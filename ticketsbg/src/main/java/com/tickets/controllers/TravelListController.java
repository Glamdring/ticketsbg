package com.tickets.controllers;

import java.util.ArrayList;
import java.util.List;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import com.tickets.annotations.Action;
import com.tickets.controllers.security.AccessLevel;
import com.tickets.controllers.valueobjects.TicketDisplayInfo;
import com.tickets.controllers.valueobjects.TravelListSubroute;
import com.tickets.model.PassengerDetails;
import com.tickets.model.Price;
import com.tickets.model.Run;
import com.tickets.model.Ticket;

@Controller("travelListController")
@Scope("request")
@Action(accessLevel=AccessLevel.CASH_DESK)
public class TravelListController extends BaseController {
    private Run run;
    private List<TravelListSubroute> travelList = new ArrayList<TravelListSubroute>();

    public String openTravelList() {
        travelList = new ArrayList<TravelListSubroute>(run.getRoute()
                .getPrices().size());
        for (Ticket ticket : run.getTickets()) {
            insertTicketDataIntoTravelList(ticket, false);
        }

        for (Ticket ticket : run.getReturnTickets()) {
            insertTicketDataIntoTravelList(ticket, true);
        }

        for (TravelListSubroute tls : travelList) {
            tls.order();
        }

        return "travelList";
    }

    private void insertTicketDataIntoTravelList(Ticket ticket, boolean isReturn) {
        List<Price> prices = run.getRoute().getPrices();
        for (Price price : prices) {

            if (!(price.getStartStop().getName().equals(ticket.getStartStop())
                    && price.getEndStop().getName().equals(ticket.getEndStop()))) {
                continue;
            }

            String caption = price.getStartStop().getName() + " - "
                    + price.getEndStop().getName();

            TravelListSubroute tmpTLS = new TravelListSubroute();
            tmpTLS.setCaption(caption);

            int idx = travelList.indexOf(tmpTLS);
            if (idx > -1) {
                tmpTLS = travelList.get(idx);
            }

            TicketDisplayInfo tdi = new TicketDisplayInfo();
            tdi.setTicketCode(ticket.getTicketCode());
            String numbers = "";
            String delimiter = "";

            for (PassengerDetails pd : ticket.getPassengerDetails()) {
                if (isReturn) {
                    numbers += delimiter + pd.getReturnSeat();
                } else {
                    numbers += delimiter + pd.getSeat();
                }
                delimiter = ",";
            }

            tdi.setSeatNumbers(numbers);

            tmpTLS.getTickets().add(tdi);

            if (idx > -1) {
                travelList.set(idx, tmpTLS);
            } else {
                travelList.add(tmpTLS);
            }
        }
    }

    public Run getRun() {
        return run;
    }

    public void setRun(Run run) {
        this.run = run;
    }

    public List<TravelListSubroute> getTravelList() {
        return travelList;
    }

    public void setTravelList(List<TravelListSubroute> travelList) {
        this.travelList = travelList;
    }
}
