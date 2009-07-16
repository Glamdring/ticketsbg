package com.tickets.services;

import java.util.Date;
import java.util.List;

import com.tickets.model.SearchResultEntry;
import com.tickets.model.User;

public interface SearchService {

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
     * @return list of runs and prices
     */
    List<SearchResultEntry> search(String startStop,
            String endStop,
            Date date,
            int fromHour,
            int toHour,
            boolean isTimeForDeparture);


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
     *
     * @return list of stop names
     */
    List<String> listAllStops(User user);


    /**
     * Lists all available stops which are destinations from
     * the specified start stop
     *
     * @param startStopName
     * @param user the logged user (needed in case of staff members)
     * @return list of stop names
     */
    List<String> listAllEndStops(String startStopName, User user);
}
