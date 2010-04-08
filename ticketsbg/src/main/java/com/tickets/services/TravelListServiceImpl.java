package com.tickets.services;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;

import com.tickets.constants.Messages;
import com.tickets.controllers.valueobjects.TicketDisplayInfo;
import com.tickets.model.PassengerDetails;
import com.tickets.model.PaymentMethod;
import com.tickets.model.Price;
import com.tickets.model.Run;
import com.tickets.model.Ticket;
import com.tickets.services.valueobjects.TravelListSubroute;

@SuppressWarnings("unchecked")
@Service("travelListService")
public class TravelListServiceImpl extends BaseService implements TravelListService {

    @Override
    public List<TravelListSubroute> getTravelList(Run run, boolean onlineOnly) {
        List<TravelListSubroute> travelList = new ArrayList<TravelListSubroute>(run.getRoute()
                .getPrices().size());
        for (Ticket ticket : run.getTickets()) {
            if (ticket.isCommitted()) {
                insertTicketDataIntoTravelList(run, travelList, ticket, false, onlineOnly);
            }
        }

        for (Ticket ticket : run.getReturnTickets()) {
            if (ticket.isCommitted()) {
                insertTicketDataIntoTravelList(run, travelList, ticket, true, onlineOnly);
            }
        }

        for (TravelListSubroute tls : travelList) {
            tls.order();
        }

        return travelList;
    }


    private void insertTicketDataIntoTravelList(Run run,
            List<TravelListSubroute> travelList, Ticket ticket,
            boolean isReturn, boolean onlineOnly) {

        if (onlineOnly && ticket.getPaymentMethod() == PaymentMethod.CASH_DESK) {
            return;
        }

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
            if (ticket.getCustomerInformation() != null) {
                tdi.setCustomerName(ticket.getCustomerInformation().getName());
            } else {
                tdi.setCustomerName(Messages.getString("noCustomerName"));
            }

            tmpTLS.getTickets().add(tdi);

            if (idx > -1) {
                travelList.set(idx, tmpTLS);
            } else {
                travelList.add(tmpTLS);
            }
        }
    }


    @Override
    public void sendSMS(String phoneNumber, Run run) {
        List<TravelListSubroute> travelList = getTravelList(run, true);
        //TODO send SMS
    }

}
