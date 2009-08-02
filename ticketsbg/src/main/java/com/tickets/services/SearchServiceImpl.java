package com.tickets.services;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import org.springframework.stereotype.Service;

import com.tickets.model.Route;
import com.tickets.model.Run;
import com.tickets.model.SearchResultEntry;
import com.tickets.model.Stop;
import com.tickets.model.User;
import com.tickets.utils.GeneralUtils;

@Service("searchService")
public class SearchServiceImpl extends BaseService implements SearchService {

    @SuppressWarnings("unchecked")
    @Override
    public List<SearchResultEntry> search(String fromStop, String toStop, Date date,
            int fromHour, int toHour, boolean isTimeForDeparture) {

        List<SearchResultEntry> result = getDao().findByNamedQuery("Run.search",
                new String[] { "fromStop", "toStop"},
                new Object[] { fromStop, toStop });

        filterSearchResults(fromStop, toStop, date, fromHour, toHour, isTimeForDeparture,
                result);

        return result;
    }

    @SuppressWarnings("unchecked")
    @Override
    public List<SearchResultEntry> adminSearch(User user, String startStop,
            String endStop, Date date, int fromHour, int toHour,
            boolean isTimeForDeparture) {

        List<SearchResultEntry> result = null;

        if (endStop == null || endStop.length() == 0) {
            result = getDao().findByNamedQuery("Run.adminSearchNoEndStop",
                    new String[] { "fromStop", "user"},
                    new Object[] { startStop, user});
        } else {
            result = getDao().findByNamedQuery("Run.adminSearch",
                    new String[] { "fromStop", "toStop", "user"},
                    new Object[] { startStop, endStop, user});
        }

        filterSearchResults(startStop, endStop, date, fromHour, toHour,
                isTimeForDeparture, result);

        return result;

    }

    private void filterSearchResults(String fromStop, String toStop, Date date,
            int fromHour, int toHour, boolean isTimeForDeparture,
            List<SearchResultEntry> result) {
        Calendar fromTime = GeneralUtils.createEmptyCalendar();
        fromTime.setTime(date);
        fromTime.add(Calendar.HOUR_OF_DAY, fromHour);

        Calendar toTime = GeneralUtils.createEmptyCalendar();
        toTime.setTime(date);
        toTime.add(Calendar.HOUR_OF_DAY, toHour);
        // toTime.roll(Calendar.MINUTE, 1);


        // Complexity n^2 in the worst case, but generally n (because of the
        // limited number of stops)
        // This better go in the query, but such date + minutes calculations
        // are hardly possible with JPAQL
        for (Iterator<SearchResultEntry> it = result.iterator(); it.hasNext();) {
            SearchResultEntry entry = it.next();
            Run run = entry.getRun();

            Stop fromStopObj = findStop(run.getRoute().getStops(), fromStop);
            Stop toStopObj = findStop(run.getRoute().getStops(), toStop);

            // if no target stop chosen, assume the final stop of the route;
            if (toStopObj == null) {
                toStopObj = run.getRoute().getStops().get(run.getRoute().getStops().size() - 1);
            }

            // Cloning in order to avoid update queries, as the run object
            // is still associated with the session
            Calendar departureTime = (Calendar) run.getTime().clone();
            departureTime.add(Calendar.MINUTE, fromStopObj.getTimeToDeparture());

            Calendar arrivalTime = (Calendar) run.getTime().clone();
            arrivalTime.add(Calendar.MINUTE, toStopObj.getTimeToArrival());


            Calendar targetTime = null;

            if (isTimeForDeparture) {
                targetTime = departureTime;
            } else {
                targetTime = arrivalTime;
            }

            //Filtering if the timing doesn't fit the selected
            if (fromTime.compareTo(targetTime) > 0
                    || toTime.compareTo(targetTime) < 0) {

                it.remove();
                continue;
            }

            //Filtering the ones with no seats remaining
            if (ServiceFunctions.getVacantSeats(run, fromStop, toStop) == 0) {
                it.remove();
                continue;
            }

            entry.setArrivalTime(arrivalTime);
            entry.setDepartureTime(departureTime);
        }
    }

    private Stop findStop(List<Stop> stops, String stopName) {
        if (stopName == null)
            return null;

        for (Stop stop : stops) {
            if (stopName.equals(stop.getName())) {
                return stop;
            }
        }
        return null;
    }

    @SuppressWarnings("unchecked")
    @Override
    public List<String> listAllStops(User user) {
        List<String> stops = null;
        if (user != null && user.isStaff()) {
            stops = getDao().findByNamedQuery("Stop.listAllStopNamesForUser",
                    new String[]{"user"}, new Object[]{user});
        } else {
            stops = getDao().findByNamedQuery("Stop.listAllStopNames");
        }

        return stops;
    }

    @SuppressWarnings("unchecked")
    @Override
    public List<String> listAllEndStops(String startStopName, User user) {
        List<String> stops = null;
        if (user != null && user.isStaff()) {
            stops = getDao().findByNamedQuery("Stop.listAllEndStopNamesForUser",
                    new String[]{"startStopName", "user"}, new Object[]{startStopName, user});
        } else {
            stops = getDao().findByNamedQuery("Stop.listAllEndStopNames",
                new String[]{"startStopName"}, new Object[]{startStopName});
        }

        return stops;
    }

    @Override
    public List<String> listAllEndStopsForRoute(String fromStop, Route route) {
        List<String> names = new ArrayList<String>(route.getStops().size());
        boolean insertionStarted = false;
        for (Stop stop : route.getStops()) {
            if (insertionStarted) {
                names.add(stop.getName());
            }
            if (stop.getName().equals(fromStop)) {
                insertionStarted = true;
            }
        }
        return names;
    }
}
