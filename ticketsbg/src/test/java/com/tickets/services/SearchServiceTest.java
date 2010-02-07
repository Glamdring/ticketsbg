package com.tickets.services;

import java.util.Calendar;
import java.util.List;

import junit.framework.Assert;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import com.tickets.model.Route;
import com.tickets.model.SearchResultEntry;
import com.tickets.test.BaseTest;
import com.tickets.utils.GeneralUtils;

public class SearchServiceTest extends BaseTest {


    @Autowired
    private SearchService searchService;


    @Test
    @Transactional
    public void searchTest() {

        List<SearchResultEntry> result = performSearch();

        // asserts whether all created routes are present in the search results
        Assert.assertEquals(getRequiredRoutesCount(), result.size());
    }


    @Test
    @Transactional
    public void searchInactiveFirmTest() {
        List<Route> routes = getRouteService().list(Route.class);
        routes.get(0).getFirm().setActive(false);

        List<SearchResultEntry> result = performSearch();

        // asserts whether all created routes but one
        // are present in the search results
        Assert.assertEquals(getRequiredRoutesCount() - 1, result.size());
    }

    @Test
    @Transactional
    public void searchByHourLimitTest() {
        List<Route> routes = getRouteService().list(Route.class);
        int hour = routes.get(0).getRouteHours().get(0).getMinutes() / 60;

        List<SearchResultEntry> fullResult = performSearch(hour - 4, hour + 4);
        Assert.assertEquals(getRequiredRoutesCount(), fullResult.size());

        List<SearchResultEntry> emptyResult = performSearch(hour - 6, hour - 4);
        Assert.assertEquals(0, emptyResult.size());
    }

    @Override
    protected int getRequiredRoutesCount() {
        return 10;
    }

    private List<SearchResultEntry> performSearch(int fromHour, int toHour) {
        Calendar inTwoDays = GeneralUtils.createCalendar();
        inTwoDays.add(Calendar.DAY_OF_YEAR, 2);

        List<SearchResultEntry> result = searchService.search(START_STOP,
                END_STOP, inTwoDays.getTime(), fromHour, toHour, true, null);
        return result;

    }
    private List<SearchResultEntry> performSearch() {
        return performSearch(0, 24);
    }
}
