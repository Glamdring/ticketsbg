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
                time = run.getTime();
            }
            createRunsForRoute(route, time);
        }
    }

    public void createRunsForRoute(Route route, Calendar time) {
        int day = time.get(Calendar.DAY_OF_YEAR);
        int dayOfWeek = time.get(Calendar.DAY_OF_WEEK);
        // the current day, starting from Monday
        int currentDay = dayOfWeek > 1 ? dayOfWeek - 1 : 7;

        CircularLinkedList<RouteDay> routeDays =
            new CircularLinkedList<RouteDay>(route.getRouteDays());


        // Identifying the day from which to start generating
        ListNode<RouteDay> dayNode = null;
        for (int i = 0; i < routeDays.size(); i ++) {
            // The first week-day of the route which is after the current day
            int diff = routeDays.get(i).getValue().getDay().getId() - currentDay;
            if (diff > 0) {
                dayNode = routeDays.get(i);
                break;
            }
        }
        if (dayNode == null)
            return; // no day found

        Calendar now = GeneralUtils.getEmptyCalendar();
        now.setTimeInMillis(System.currentTimeMillis());

        int daysToGenerate = route.getPublishedRunsPeriod() -
            (day - now.get(Calendar.DAY_OF_YEAR));

        //TODO new year?
        now.add(Calendar.DAY_OF_YEAR, day - now.get(Calendar.DAY_OF_YEAR));
        for (int i = 0; i < daysToGenerate; i ++) {
            for (RouteHour rh : route.getRouteHours()) {
                Run run = new Run();
                Calendar runTime = (Calendar) now.clone();
                runTime.set(Calendar.HOUR_OF_DAY, rh.getMinutes() / 60);
                runTime.set(Calendar.MINUTE, rh.getMinutes() % 60);
                runTime.set(Calendar.SECOND, 0);
                run.setTime(runTime);
                route.addRun(run);
            }
            int tmpDay = dayNode.getValue().getDay().getId();
            now.add(Calendar.DAY_OF_YEAR, tmpDay - currentDay);
            dayNode = dayNode.getNext();
        }
    }
}