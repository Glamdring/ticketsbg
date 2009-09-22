package com.tickets.services;

import java.util.Date;
import java.util.List;

import com.tickets.model.Firm;
import com.tickets.model.Route;
import com.tickets.model.SearchResultEntry;
import com.tickets.model.User;

public interface SearchService extends Service {

    /**
     * Performs a search listing all runs for the specified criteria
     *
     * @param startStop
     * @param endStop
     * @param date
     * @param fromHour
     * @param toHour
     * @param isTimeForDeparture defines whether the selected time is
     * 	for departure (true) or for arrival(false)
     * @param currentFirm
     * @return list of runs and prices
     */
    List<SearchResultEntry> search(String startStop,
            String endStop,
            Date date,
            int fromHour,
            int toHour,
            boolean isTimeForDeparture,
            Firm currentFirm);


    /**
     * Performs a search listing all runs for the specified criteria
     *
     * @param user the user performing the search
     * @param startStop
     * @param endStop
     * @param date
     * @param fromHour
     * @param toHour
     * @param isTimeForDeparture defines whether the selected time is
     * 	for departure (true) or for arrival(false)
     * @return list of runs and prices
     */
    List<SearchResultEntry> adminSearch(User user,
            String startStop,
            String endStop,
            Date date,
            int fromHour,
            int toHour,
            boolean isTimeForDeparture);

    /**
     * Lists all available stop names
     * @param user the logged user (needed in case of staff members)
     * @param firm the firm whose stops are fetched. Pass null for all
     *
     * @return list of stop names
     */
    List<String> listAllStops(User user, Firm firm);


    /**
     * Lists all available stops which are destinations from
     * the specified start stop
     *
     * @param startStopName
     * @param user the logged user (needed in case of staff members)
     * @param firm the firm whose stops are fetched. Pass null for all
     *
     * @return list of stop names
     */
    List<String> listAllEndStops(String startStopName, User user, Firm firm);


    /**
     * Lists all possible end stops for a given start stop and route
     * @param fromStop
     * @param route
     * @return the list of names
     */
    List<String> listAllEndStopsForRoute(String fromStop, Route route);
}
