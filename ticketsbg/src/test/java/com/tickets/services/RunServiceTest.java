package com.tickets.services;

import java.util.Calendar;

import org.apache.log4j.Logger;
import org.junit.Assert;
import org.junit.Test;
import org.springframework.transaction.annotation.Transactional;

import com.tickets.model.Route;
import com.tickets.model.Run;
import com.tickets.test.BaseTest;
import com.tickets.utils.GeneralUtils;

public class RunServiceTest extends BaseTest {

    private static final Logger log = Logger.getLogger(RunServiceTest.class);

    @Test
    @Transactional
    public void createRunsTest() {
        // runService.createRuns() has been called in BaseTest @PostConstruct.
        // Here we verify if the correct number of runs have been generated

        // The run should be one every day, hence should be almost equals to
        // 'publishedRunsPeriod' the difference might vary due to current day
        // generation
        int runsDiff = getRoute().getRuns().size() - getRoute().getPublishedRunsPeriod();
        Assert.assertTrue(runsDiff >= 0 && runsDiff <= 2);

        Run run = getRoute().getRuns().get(0);
        int runHour = run.getTime().get(Calendar.HOUR_OF_DAY);
        int runMinutes = run.getTime().get(Calendar.MINUTE);

        Assert.assertEquals(getRoute().getRouteHours().get(0).getMinutes() / 60, runHour);
        Assert.assertEquals(getRoute().getRouteHours().get(0).getMinutes() % 60, runMinutes);
    }

    /**
     * This tests the creation of runs after there are already generated runs
     */
    //@Test
    @Transactional
    public void createAdditionalRunsTest() {
        log.info("--------------- starting test of creating additional runs -------------");
        Calendar now = GeneralUtils.createCalendar();
        int size = getRoute().getRuns().size();
        System.out.println("AAA: " + size);

        now.add(Calendar.DAY_OF_YEAR, 1);
        ((RunServiceImpl) getRunService()).createRuns(now);
        Route route = getRouteService().getNonLazy(getRoute().getId());
        //Assert.assertEquals(size + 1, route.getRuns().size());
        System.out.println("AAA: " + route.getRuns().size());

        now.add(Calendar.DAY_OF_YEAR, 1);
        ((RunServiceImpl) getRunService()).createRuns(now);
        route = getRouteService().getNonLazy(getRoute().getId());
        //Assert.assertEquals(size + 2, route.getRuns().size());
        System.out.println("AAA: " + route.getRuns().size());

        now.add(Calendar.DAY_OF_YEAR, 1);
        ((RunServiceImpl) getRunService()).createRuns(now);
        route = getRouteService().getNonLazy(getRoute().getId());
        System.out.println("AAA: " + route.getRuns().size());
    }
}
