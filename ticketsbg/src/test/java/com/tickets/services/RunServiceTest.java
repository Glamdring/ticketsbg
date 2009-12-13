package com.tickets.services;

import java.util.Calendar;

import org.junit.Assert;
import org.junit.Test;
import org.springframework.transaction.annotation.Transactional;

import com.tickets.model.Run;
import com.tickets.test.BaseTest;

public class RunServiceTest extends BaseTest {

    @Test
    @Transactional
    public void createRunsTest() {
        // runService.createRuns() has been called in BaseTest @PostConstruct.
        // Here we verify if the correct number of runs have been generated

        // The run should be one every day, hence should be almost equals to
        // 'publishedRunsPeriod' the difference might vary due to current day
        // generation
        int runsDiff = route.getRuns().size() - route.getPublishedRunsPeriod();
        Assert.assertTrue(runsDiff >= 0 && runsDiff <= 2);

        Run run = route.getRuns().get(0);
        int runHour = run.getTime().get(Calendar.HOUR_OF_DAY);
        int runMinutes = run.getTime().get(Calendar.MINUTE);

        Assert.assertEquals(route.getRouteHours().get(0).getMinutes() / 60, runHour);
        Assert.assertEquals(route.getRouteHours().get(0).getMinutes() % 60, runMinutes);
    }
}
