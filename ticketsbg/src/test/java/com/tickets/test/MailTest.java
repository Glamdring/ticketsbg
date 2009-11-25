package com.tickets.test;

import java.io.FileOutputStream;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import org.junit.Test;

import com.tickets.model.Customer;
import com.tickets.model.Firm;
import com.tickets.model.Route;
import com.tickets.model.Run;
import com.tickets.model.Ticket;
import com.tickets.services.TicketServiceImpl;
import com.tickets.services.valueobjects.TicketInfo;

public class MailTest {

    @Test
    public void emailTest() {
        TicketServiceImpl tsi = new TicketServiceImpl();

        Run run = new Run();
        Route route = new Route();
        Firm firm = new Firm();
        run.setRoute(route);
        route.setFirm(firm);

        List<Ticket> list = new ArrayList<Ticket>();
        Ticket ticket = new Ticket();
        ticket.setTicketCode("12-12-32-12-32");
        ticket.setStartStop("София");
        ticket.setEndStop("Варна");
        ticket.setDepartureTime(Calendar.getInstance());
        ticket.setRun(run);
        ticket.setTotalPrice(BigDecimal.valueOf(50));
        Customer c = new Customer();
        c.setEmail("glamd@abv.bg");
        c.setName("Божидар Пламенов Божанов");
        ticket.setCustomerInformation(c);
        list.add(ticket);

        List<TicketInfo> infos = tsi.getTicketInfo(list);
        infos.get(0).setSeats("1,2,3");
        infos.get(0).setFirmName("Витоша Експрес");
        infos.add(infos.get(0));
        byte[] fl = tsi.createPurchasePdf(infos);

        FileOutputStream fos;
        try {
            fos = new FileOutputStream("c:/out.pdf");
            fos.write(fl);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}
