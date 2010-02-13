package com.tickets.services;

import java.util.Calendar;
import java.util.List;

import junit.framework.Assert;

import org.junit.Test;
import org.springframework.transaction.annotation.Transactional;

import com.tickets.model.Route;
import com.tickets.test.BaseTest;
import com.tickets.utils.GeneralUtils;

public class RouteServiceTest extends BaseTest {

    @Test
    public void addHourToRouteTest() {
        Route route = new Route();
        Calendar cal = GeneralUtils.createCalendar();
        cal.set(Calendar.HOUR_OF_DAY, 14);
        cal.set(Calendar.MINUTE, 30);
        getRouteService().addHourToRoute(cal.getTime(), route);

        Assert.assertEquals(1, route.getRouteHours().size());
        Assert.assertEquals(14, route.getRouteHours().get(0).getMinutes() / 60);
        Assert.assertEquals(30, route.getRouteHours().get(0).getMinutes() % 60);
    }

    @Test
    @Transactional
    public void findRouteTest() {
        Route result = getRouteService().findRoute(getRoute().getName(), getRoute().getFirm());
        Assert.assertEquals(getRoute().getId(), result.getId());
    }

    @Test
    @Transactional
    public void listByFirmTest() {
        List<Route> routes = getRouteService().list(getRoute().getFirm());

        // all routes count should be equal to routes for this firm
        Assert.assertEquals(getRouteService().list(Route.class).size(), routes.size());
        Assert.assertEquals(getRoute().getId(), routes.get(0).getId());

        routes = getRouteService().listOrdered(getRoute().getFirm(), "id");
        Assert.assertEquals(getRouteService().list(Route.class).size(), routes.size());
        Assert.assertEquals(getRoute().getId(), routes.get(0).getId());
    }

}
