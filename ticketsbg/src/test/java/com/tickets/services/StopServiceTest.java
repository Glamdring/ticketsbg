package com.tickets.services;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import junit.framework.Assert;

import org.apache.commons.lang.SerializationUtils;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.tickets.model.Price;
import com.tickets.model.Route;
import com.tickets.model.Stop;
import com.tickets.test.BaseTest;
import com.tickets.test.TestUtils;

public class StopServiceTest extends BaseTest {

    @Autowired
    private StopService stopService;

    @Test
    public void addStopToRouteTest() {
        Stop stop = new Stop();
        stop.setName(TestUtils.getRandomString(10));

        Route route = getRouteClone();
        int initialSize = route.getStops().size();
        int prices = route.getPrices().size();

        stopService.addStopToRoute(stop, route);

        int afterSize = route.getStops().size();
        int afterPrices = route.getPrices().size();

        Assert.assertEquals(initialSize + 1, afterSize);

        // after adding a stop, a new N prices are added - each with a
        // start-stop=existing stop, and an end-stop=the new stop
        Assert.assertEquals(prices + initialSize, afterPrices);

        // verifying if the idx has been set correctly
        Assert.assertTrue(stop.getIdx() == route.getStops().size());

    }

    @Test
    public void modifyExistingStopTest() {
        Route route = getRouteClone();
        Stop stop = route.getStops().get(0);

        int initialSize = route.getStops().size();
        int prices = route.getPrices().size();

        String originalName = stop.getName();

        stop.setName(TestUtils.getRandomString(5));
        stopService.addStopToRoute(stop, route);

        Assert.assertEquals(initialSize, route.getStops().size());
        Assert.assertEquals(prices, route.getPrices().size());

        stop.setName(originalName);
        stopService.addStopToRoute(stop, route);

        Assert.assertEquals(initialSize, route.getStops().size());
        Assert.assertEquals(prices, route.getPrices().size());
    }

    @Test
    public void deleteStopTest() {
        Route route = getRouteClone();

        Stop newStop = new Stop();
        newStop.setName(TestUtils.getRandomString(5));
        stopService.addStopToRoute(newStop, route);

        int initialSize = route.getStops().size();
        int prices = route.getPrices().size();

        Stop stopToDelete = route.getStops().get(1);
        stopService.delete(stopToDelete, route);

        Assert.assertEquals(initialSize - 1, route.getStops().size());
        Assert.assertEquals(prices - (initialSize - 1), route.getPrices().size());

        // now check whether prices with the existing stop were removed
        for (Price price : route.getPrices()) {
            if (price.getStartStop().equals(stopToDelete) ||
                    price.getEndStop().equals(stopToDelete)) {
                Assert.fail("There still exists a price containing the deleted stop");
            }
        }
    }

    @Test
    public void getPriceTest() {
        Price price = stopService.getPrice(START_STOP, END_STOP, getRouteClone());
        Assert.assertNotNull(price);
    }

    @Test
    public void getPriceByListRowKeyTest() {
        Route route = getRouteClone();
        Map<Integer, String> originalIndices = new HashMap<Integer, String>();
        for (Stop stop : route.getStops()) {
            originalIndices.put(stop.getIdx(), stop.getName());
        }

        Collections.reverse(route.getStops());
        stopService.listReoredered(route);

        // no index-name pair should match
        for (Stop stop : route.getStops()) {
            if (originalIndices.get(stop.getIdx()).equals(stop.getName())) {
                Assert.fail("List not properly reordered - the idx of some stops didn't change");
            }
        }
    }

    private Route getRouteClone() {
        return (Route) SerializationUtils.clone(getRoute());
    }

}
