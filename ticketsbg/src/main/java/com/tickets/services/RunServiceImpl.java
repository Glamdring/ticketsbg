package com.tickets.services;

import java.util.Calendar;
import java.util.List;

import org.apache.log4j.Logger;
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

    private static final Logger logger = Logger.getLogger(RunServiceImpl.class);

    @Override
    public void createRuns() {
        createRuns(GeneralUtils.createCalendar());
    }

    /**
     * This method is used in cases someone (a unit test) wants to specify a
     * value of "now" different from the real current time
     *
     * @param now
     */
    public void createRuns(Calendar now) {
        List<List> runsAndRoutes = getDao().findByNamedQuery("Run.getLastRuns");

        logger.debug("Found for run creation: " + runsAndRoutes.size());

        for (List result : runsAndRoutes) {
            Route route = (Route) result.get(0);
            Run run = (Run) result.get(1);
            Calendar time;
            if (run == null) {
                time = GeneralUtils.getPreviousDay();
            } else {
                time = (Calendar) run.getTime().clone();
            }
            //If the single run is already created, skip
            if (run != null && route.isSingleRun()) {
                continue;
            }

            if (route.isSingleRun()) {
                createSingleRunForRoute(route);
            } else {
                //TODO optimize skipping seasonal
                createRunsForRoute(route, time, (Calendar) now.clone());
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


    public void createRunsForRoute(Route route, Calendar time, Calendar now) {
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
        if (dayNode == null) {
            return; // no day found
        }

        int diff = day - now.get(Calendar.DAY_OF_YEAR);
        // check whether the 'now' or 'day' (whichever is greater) isn't in the
        // next year recalculate the difference in days
        if (diff > 300) {
            diff = now.get(Calendar.DAY_OF_YEAR) + (getDaysInYear(time) - day);
        } else if (diff < -300) {
            diff = day + (getDaysInYear(time) - now.get(Calendar.DAY_OF_YEAR));
        }

        int daysToGenerate = route.getPublishedRunsPeriod() - diff + 1; // +1 to guarantee that each day a new day is generated

        logger.debug("Generating runs for " + daysToGenerate + " days, for route " + route.getId());

        // Starting either from the last already generated day , or from yesterday,
        // so that after the first "scrolling" the correct day is retrieved
        now.add(Calendar.DAY_OF_YEAR, diff);
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

    private static int getDaysInYear(Calendar time) {
        int year = time.get(Calendar.YEAR);
        if (year % 4 == 0 && (year % 100 != 0 || year % 400 == 0)) {
            return 366;
        }
        return 365;
    }

    public static void main(String[] args) {
        Calendar time = GeneralUtils.getPreviousDay();
        Calendar now = GeneralUtils.createCalendar();

        int day = time.get(Calendar.DAY_OF_YEAR);
        int diff = day - now.get(Calendar.DAY_OF_YEAR);

        logger.debug("Difference is: " + diff);
        // check whether the 'now' or 'day' (whichever is greater) isn't in the
        // next year recalculate the difference in days
        if (diff > 300) {
            diff = now.get(Calendar.DAY_OF_YEAR) + (getDaysInYear(time) - day);
        } else if (diff < -300) {
            diff = day + (getDaysInYear(time) - now.get(Calendar.DAY_OF_YEAR));
        }
    }
}