package com.tickets.services;

import java.util.Calendar;

import org.junit.Assert;
import org.junit.Test;

import com.tickets.model.Firm;
import com.tickets.model.Route;
import com.tickets.model.Run;

public class TicketServiceTest {

    @Test
    public void testTicketCodeGeneration() {
        Run run = new Run();
        run.setRunId(2412);
        Calendar cal = Calendar.getInstance();
        cal.setTimeInMillis(System.currentTimeMillis());
        run.setTime(cal);
        Route route = new Route();
        route.setId(402);
        run.setRoute(route);
        Firm firm = new Firm();
        firm.setFirmId(17);
        route.setFirm(firm);
        TicketServiceImpl impl = new TicketServiceImpl();
        try {
            String code = impl.generateTicketCode(run);
            System.out.println(code);
            Assert.assertNotNull(code);
        } catch (Exception ex) {
            ex.printStackTrace();
            Assert.fail();
        }
    }
}
