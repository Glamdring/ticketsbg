package com.tickets.services;

import java.util.Calendar;
import java.util.List;

import org.springframework.stereotype.Service;

import com.tickets.model.Route;
import com.tickets.model.RouteDay;
import com.tickets.model.RouteHour;
import com.tickets.model.Run;
import com.tickets.utils.CircularLinkedList;
import com.tickets.utils.GeneralUtils;
import com.tickets.utils.ListNode;

@Service("runService")
public class RunServiceImpl extends BaseService<Run> implements RunService<Run> {

    @SuppressWarnings("unchecked")
    @Override
    public void createRuns() {

        List<List> runsAndRoutes = getDao().findByNamedQuery("Run.getLastRuns");

        for (List result : runsAndRoutes) {
            Route route = (Route) result.get(0);
            Run run = (Run) result.get(1);
            Calendar time;
            if (run == null) {
                time = GeneralUtils.getPreviousDay();
            } else {
                time = Calendar.getInstance(GeneralUtils.getLocale());
                time.setTimeInMillis(run.getTime().getTimeInMillis());
            }
            //If the single run is already created
            if (run != null && route.isSingleRun()) {
                continue;
            }

            if (route.isSingleRun()) {
                createSingleRunForRoute(route);
            } else {
                //TODO optimize skipping seasonal
                createRunsForRoute(route, time);
            }
        }
    }

    private void createSingleRunForRoute(Route route) {
        //Only if no other runs exist for this route
        if (route.getRuns().size() == 0) {
            Run run = new Run();
            run.setTime(route.getSingleRunDateTime());
            //automatically saving because attached to session
            route.addRun(run);
        }
    }


    public void createRunsForRoute(Route route, Calendar time) {
        int day = time.get(Calendar.DAY_OF_YEAR);
        int dayOfWeek = time.get(Calendar.DAY_OF_WEEK);
        // the current day, starting from Monday
        int currentDay = dayOfWeek; // > 1 ? dayOfWeek - 1 : 7; if locale is EN
        CircularLinkedList<RouteDay> routeDays =
            new CircularLinkedList<RouteDay>(route.getRouteDays());


        // Identifying the day from which to start generating
        ListNode<RouteDay> dayNode = null;
        int routeDaysCount = routeDays.size();
        for (int i = 0; i < routeDaysCount; i ++) {
            // The first week-day of the route which is after the current day
            int diff = routeDays.get(i).getValue().getDay().getId() - currentDay;
            if (diff > 0 || i == routeDaysCount - 1) {
                dayNode = routeDays.get(i);
                break;
            }
        }
        if (dayNode == null)
            return; // no day found

        Calendar now = GeneralUtils.createEmptyCalendar();

        int daysToGenerate = route.getPublishedRunsPeriod() -
            (day - now.get(Calendar.DAY_OF_YEAR));

        //TODO new year?
        //Starting either from the last already generated day + 1, or from today (=yesterday + 1)
        now.add(Calendar.DAY_OF_YEAR, day - now.get(Calendar.DAY_OF_YEAR) + 1);
        while (daysToGenerate > 0) {
            int tmpDay = dayNode.getValue().getDay().getId();
            // 'scrolling' to the next date where a run is needed
            // then setting the current day for the next iteration to be
            // the day for which a run has been added on this iteration
            int daysToScroll = tmpDay >= currentDay ? tmpDay - currentDay : 7 - currentDay + tmpDay;
            currentDay = tmpDay;
            now.add(Calendar.DAY_OF_YEAR, daysToScroll);
            dayNode = dayNode.getNext();
            daysToGenerate -= daysToScroll;

            // Check is runs should be generated in case
            // the route is marked as seasonal
            if (route.isSeasonal()
                    && (now.compareTo(route.getSeasonStart()) <= 0
                    || now.compareTo(route.getSeasonEnd()) >= 0)) {
                continue;
            }

            for (RouteHour rh : route.getRouteHours()) {
                Run run = new Run();
                Calendar runTime = (Calendar) now.clone();
                runTime.set(Calendar.HOUR_OF_DAY, rh.getMinutes() / 60);
                runTime.set(Calendar.MINUTE, rh.getMinutes() % 60);
                runTime.set(Calendar.SECOND, 0);
                run.setTime(runTime);
                //Saving automatically, because attached to session
                route.addRun(run);
            }
        }
    }
}