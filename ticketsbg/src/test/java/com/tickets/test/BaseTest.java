package com.tickets.test;

import java.math.BigDecimal;
import java.util.Calendar;
import java.util.List;

import org.junit.Before;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.transaction.TransactionConfiguration;

import com.tickets.dao.Dao;
import com.tickets.model.Day;
import com.tickets.model.Firm;
import com.tickets.model.Price;
import com.tickets.model.Route;
import com.tickets.model.RouteDay;
import com.tickets.model.RouteHour;
import com.tickets.model.Stop;
import com.tickets.services.RouteService;
import com.tickets.services.RunService;
import com.tickets.utils.GeneralUtils;
import com.tickets.utils.InitTask;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations="classpath:/spring.xml")
@TransactionConfiguration(transactionManager="jpaTransactionManager", defaultRollback=false)
public abstract class BaseTest {

    protected static final BigDecimal ONE_WAY_PRICE = BigDecimal.ONE;
    protected static final BigDecimal RETURN_PRICE = ONE_WAY_PRICE.add(ONE_WAY_PRICE);
    private static final String END_STOP = "endStop";
    private static final String START_STOP = "startStop";

    @Autowired
    private RouteService routeService;

    @Autowired
    private RunService runService;

    @Autowired
    private InitTask initTask;

    @Autowired
    private Dao dao;

    private Route route;

    private Route returnRoute;

    @Before
    public void init() {
        initTask.run();

        List<Route> routes = routeService.list(Route.class);
        for (Route r : routes) {
            routeService.delete(r);
        }

        Route route = new Route();
        Firm firm = new Firm();
        firm.setName("Firm1");
        firm.setFirmKey("firm1");

        firm = (Firm) routeService.saveObject(firm);
        firm.setHoursBeforeTravelAlterationAllowed(2);
        route.setFirm(firm);

        Stop startStop = new Stop();
        startStop.setName(START_STOP);
        startStop.setIdx(1);
        startStop.setTimeToArrival(0);
        startStop.setTimeToDeparture(0);
        route.addStop(startStop);

        Stop endStop = new Stop();
        endStop.setName(END_STOP);
        endStop.setIdx(2);
        endStop.setTimeToArrival(200);
        endStop.setTimeToDeparture(0);
        route.addStop(endStop);

        Price price = new Price();
        price.setStartStop(startStop);
        price.setEndStop(endStop);
        price.setPrice(ONE_WAY_PRICE);
        price.setTwoWayPrice(RETURN_PRICE);

        route.addPrice(price);

        RouteHour hour = new RouteHour();
        hour.setMinutes((GeneralUtils.createCalendar().get(
            Calendar.HOUR_OF_DAY) - 3) * 60); // the current hour
        // -3, in order to fix HSQDLDB TimeZone issue

        route.addHour(hour);

        for (int i = 1; i <= 7; i++ ) {
            RouteDay rd = new RouteDay();
            rd.setDay(routeService.get(Day.class, i));
            route.addRouteDay(rd);
        }

        route.setName(START_STOP + "-" + END_STOP);
        route.setSellSeatsFrom(1);
        route.setSellSeatsTo(51);

        this.route = routeService.save(route);

        route.getStops().get(0).setIdx(2);
        route.getStops().get(1).setIdx(1);
        route.getStops().get(1).setTimeToArrival(0);
        route.getStops().get(0).setTimeToArrival(200);
        route.setName(END_STOP + "-" + START_STOP);

        this.returnRoute = routeService.save(route);

        runService.createRuns(); //this is tested elsewhere
    }

    public RouteService getRouteService() {
        return routeService;
    }

    public void setRouteService(RouteService routeService) {
        this.routeService = routeService;
    }

    public RunService getRunService() {
        return runService;
    }

    public void setRunService(RunService runService) {
        this.runService = runService;
    }

    public InitTask getInitTask() {
        return initTask;
    }

    public void setInitTask(InitTask initTask) {
        this.initTask = initTask;
    }

    public Dao getDao() {
        return dao;
    }

    public void setDao(Dao dao) {
        this.dao = dao;
    }

    public Route getRoute() {
        return route;
    }

    public void setRoute(Route route) {
        this.route = route;
    }

    public Route getReturnRoute() {
        return returnRoute;
    }

    public void setReturnRoute(Route returnRoute) {
        this.returnRoute = returnRoute;
    }
}
