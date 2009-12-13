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
        routeService.addHourToRoute(cal.getTime(), route);

        Assert.assertEquals(1, route.getRouteHours().size());
        Assert.assertEquals(14, route.getRouteHours().get(0).getMinutes() / 60);
        Assert.assertEquals(30, route.getRouteHours().get(0).getMinutes() % 60);
    }

    @Test
    @Transactional
    public void findRouteTest() {
        Route result = routeService.findRoute(route.getName(), route.getFirm());
        Assert.assertEquals(route.getId(), result.getId());
    }

    @Test
    @Transactional
    public void listByFirmTest() {
        List<Route> routes = routeService.list(route.getFirm());

        // all routes count should be equal to routes for this firm
        Assert.assertEquals(routeService.list(Route.class).size(), routes.size());
        Assert.assertEquals(route.getId(), routes.get(0).getId());

        routes = routeService.listOrdered(route.getFirm(), "id");
        Assert.assertEquals(routeService.list(Route.class).size(), routes.size());
        Assert.assertEquals(route.getId(), routes.get(0).getId());
    }

}
