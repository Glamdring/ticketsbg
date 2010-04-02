package com.tickets.services;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import org.springframework.stereotype.Service;

import com.tickets.model.Discount;
import com.tickets.model.Firm;
import com.tickets.model.Route;
import com.tickets.model.Run;
import com.tickets.model.SearchResultEntry;
import com.tickets.model.Stop;
import com.tickets.model.User;
import com.tickets.utils.GeneralUtils;

@Service("searchService")
public class SearchServiceImpl extends BaseService implements SearchService {

    @Override
    public List<SearchResultEntry> search(String fromStop, String toStop,
            Date date, int fromHour, int toHour, boolean isTimeForDeparture,
            Firm currentFirm) {


        List<SearchResultEntry> result;
        if (currentFirm == null) {
            result = getDao().findByNamedQuery("Run.search",
                    new String[] { "fromStop", "toStop", "runDate" },
                    new Object[] { fromStop + "%", toStop + "%", date});
        } else {
            result = getDao().findByNamedQuery("Run.searchByFirm",
                    new String[] { "fromStop", "toStop", "firm", "runDate" },
                    new Object[] { fromStop + "%", toStop + "%", currentFirm, date});
        }

        filterSearchResults(fromStop, toStop, date, fromHour, toHour,
                isTimeForDeparture, result);

        sortResult(result, isTimeForDeparture);
        return result;
    }

    private void sortResult(List<SearchResultEntry> result,
            boolean isTimeForDeparture) {

        Comparator<SearchResultEntry> comparator = null;

        if (isTimeForDeparture) {
            comparator = new DepartureComparator();
        } else {
            comparator = new ArrivalComparator();
        }

        Collections.sort(result, comparator);
    }

    private class DepartureComparator implements Comparator<SearchResultEntry> {
        @Override
        public int compare(SearchResultEntry o1, SearchResultEntry o2) {
            int timing = o1.getDepartureTime().compareTo(o2.getDepartureTime());
            if (timing != 0) {
                return timing;
            }

            return o1.getPrice().getPrice().compareTo(o2.getPrice().getPrice());
        }
    }
    private class ArrivalComparator implements Comparator<SearchResultEntry> {
        @Override
        public int compare(SearchResultEntry o1, SearchResultEntry o2) {
            int timing = o1.getArrivalTime().compareTo(o2.getArrivalTime());
            if (timing != 0) {
                return timing;
            }

            return o1.getPrice().getPrice().compareTo(o2.getPrice().getPrice());
        }
    }

    @Override
    public List<SearchResultEntry> adminSearch(User user, String startStop,
            String endStop, Date date, int fromHour, int toHour,
            boolean isTimeForDeparture) {

        List<SearchResultEntry> result = null;

        if (endStop == null || endStop.length() == 0) {
            result = getDao().findByNamedQuery("Run.adminSearchNoEndStop",
                    new String[] { "fromStop", "user", "runDate" },
                    new Object[] { startStop + "%", user, date });
        } else {
            result = getDao().findByNamedQuery("Run.adminSearch",
                    new String[] { "fromStop", "toStop", "user", "runDate" },
                    new Object[] { startStop + "%", endStop + "%", user, date });
        }

        filterSearchResults(startStop, endStop, date, fromHour, toHour,
                isTimeForDeparture, result);

        return result;

    }

    private void filterSearchResults(String fromStop, String toStop, Date date,
            int fromHour, int toHour, boolean isTimeForDeparture,
            List<SearchResultEntry> result) {

        Set<Integer> usedRuns = new HashSet<Integer>();

        Calendar fromTime = GeneralUtils.createCalendar();
        fromTime.setTime(date);
        fromTime.set(Calendar.HOUR_OF_DAY, fromHour);

        Calendar toTime = GeneralUtils.createCalendar();
        toTime.setTime(date);
        toTime.set(Calendar.HOUR_OF_DAY, toHour);
        // toTime.roll(Calendar.MINUTE, 1);

        // Complexity n^2 in the worst case, but generally n (because of the
        // limited number of stops)
        // This better go in the query, but such date + minutes calculations
        // are hardly possible with JPAQL
        for (Iterator<SearchResultEntry> it = result.iterator(); it.hasNext();) {
            SearchResultEntry entry = it.next();
            Run run = entry.getRun();

            // If the run is about to launch in less then the set hours,
            // remove the current entry
            Calendar salesLimit = (Calendar) run.getTime().clone();

            int minutes = run.getRoute().getStopSalesBeforeRunTime() % 60;
            int hours = run.getRoute().getStopSalesBeforeRunTime() / 60;

            salesLimit.add(Calendar.HOUR, -1 * hours);
            salesLimit.add(Calendar.MINUTE, -1 * minutes);

            if (GeneralUtils.createCalendar().compareTo(salesLimit) > -1) {
                it.remove();
                continue;
            }

            Stop fromStopObj = ServiceFunctions.getStopByName(fromStop, run.getRoute().getStops());
            Stop toStopObj = ServiceFunctions.getStopByName(toStop, run.getRoute().getStops());

            // if no target stop chosen, assume the final stop of the route;
            if (toStopObj == null) {
                toStopObj = run.getRoute().getStops().get(
                        run.getRoute().getStops().size() - 1);
            }

            // Cloning in order to avoid update queries, as the run object
            // is still associated with the session
            Calendar departureTime = (Calendar) run.getTime().clone();
            departureTime
                    .add(Calendar.MINUTE, fromStopObj.getTimeToDeparture());

            Calendar arrivalTime = (Calendar) run.getTime().clone();
            arrivalTime.add(Calendar.MINUTE, toStopObj.getTimeToArrival());

            Calendar targetTime = null;

            if (isTimeForDeparture) {
                targetTime = departureTime;
            } else {
                targetTime = arrivalTime;
            }

            // Filtering if the timing doesn't fit the selected
            if (fromTime.compareTo(targetTime) > 0
                    || toTime.compareTo(targetTime) < 0) {

                it.remove();
                continue;
            }

            // Filtering the ones with no seats remaining
            if (ServiceFunctions.getVacantSeats(run, fromStop, toStop, false) <= 0) {
                it.remove();
                continue;
            }

            // disallow a run to be included twice
            if (!usedRuns.add(run.getRunId())) {
                it.remove();
                continue;
            }

            entry.setArrivalTime(arrivalTime);
            entry.setDepartureTime(departureTime);
        }
    }

    @Override
    public List<String> listAllStops(User user, Firm firm) {
        List<String> stops = null;
        if (user != null && user.isStaff()) {
            stops = getDao().findByNamedQuery("Stop.listAllStopNamesForUser",
                    new String[] { "user" }, new Object[] { user });
        } else if (firm != null){
            stops = getDao().findByNamedQuery("Stop.listAllStopNamesForFirm",
                    new String[] {"firm"},
                    new Object[] {firm});
        } else {
            stops = getDao().findByNamedQuery("Stop.listAllStopNames");
        }

        addCommonStopNames(stops);
        return stops;
    }

    private static final String CITY_DELIMITER = " - ";

    /**
     * Adds stop names that consist of the common parts of existing stop names.
     * For example for "Sofia - Central" and "Sofia - Serdika" the name "Sofia"
     * should be added
     *
     * @param stops
     */
    private void addCommonStopNames(List<String> stops) {
        Set<String> additions = new HashSet<String>();
        String currentCommonPart = "";
        for (String name : stops) {
            int idx = name.indexOf(CITY_DELIMITER);
            if (idx != -1) {
                // changing currentCommonPart only if this is the first of the group
                // otherwise, if it is the second or more, add it to the set;
                // the set doesn't allow duplicates
                if (currentCommonPart.length() > 0
                        && name.startsWith(currentCommonPart)
                        && !stops.contains(currentCommonPart)) {// this line might be inefficient
                    additions.add(currentCommonPart);
                } else {
                    currentCommonPart = name.substring(0, idx).trim();
                }
            }
        }

        // this could be made more efficient by adding the name at the correct
        // point in the above loop (and by mirroring the List to a LinkedList),
        // but it is not justified for the small number
        if (additions.size() > 0) {
            stops.addAll(additions);
            Collections.sort(stops);
        }
    }

    @Override
    public List<String> listAllEndStops(String startStopName, User user, Firm firm) {
        List<String> stops = null;
        if (user != null && user.isStaff()) {
            stops = getDao().findByNamedQuery(
                    "Stop.listAllEndStopNamesForUser",
                    new String[] { "startStopName", "user" },
                    new Object[] { startStopName + "%", user });
        } else if (firm != null) {
            stops = getDao().findByNamedQuery("Stop.listAllEndStopNamesForFirm",
                    new String[] { "startStopName", "firm"},
                    new Object[] { startStopName + "%", firm });
        } else {
            stops = getDao().findByNamedQuery("Stop.listAllEndStopNames",
                    new String[] { "startStopName" },
                    new Object[] { startStopName + "%"});
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
            if (stop.getName().startsWith(fromStop)) {
                insertionStarted = true;
            }
        }
        return names;
    }

    @Override
    public List<Discount> getApplicableDiscounts(
            SearchResultEntry selectedEntry, String startStop, String endStop) {

        List<Discount> discounts = selectedEntry.getRun().getRoute().getDiscounts();

        Calendar departureTime = selectedEntry.getDepartureTime();
        int today = GeneralUtils.createCalendar().get(Calendar.DAY_OF_YEAR);

        for (Iterator<Discount> it = discounts.iterator(); it.hasNext();) {
            Discount discount = it.next();
            if (!(discount.getStartStop().startsWith(startStop)
                    && discount.getEndStop().startsWith(endStop))) {
                it.remove();
                continue;
            }

            // remove if :
            // 1.
            // 1.1. the day of departure is today
            // 1.2. the discount is not marked as "currentDayOnly"
            // 1.3. there is a discount with the same start and end stop marked
            //      as "currentDayOnly"
            //
            // 2. if the discount is for the current day and the departure time
            //    is not today
            if (!discount.isCurrentDayOnly()
                    && departureTime.get(Calendar.DAY_OF_YEAR) == today) {
                if (currentDayDiscountExists(discounts, startStop, endStop)) {
                    it.remove();
                }
            } else  if (discount.isCurrentDayOnly()) {
                it.remove();
            }
        }

        return discounts;
    }

    private boolean currentDayDiscountExists(List<Discount> discounts, String startStop, String endStop) {
        for (Discount discount : discounts) {
            if (discount.isCurrentDayOnly()
                    && discount.getStartStop().equals(startStop)
                    && discount.getEndStop().equals(endStop)) {
                return true;
            }
        }

        return false;
    }
}
