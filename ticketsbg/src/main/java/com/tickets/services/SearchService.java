package com.tickets.services;

import java.util.Date;
import java.util.List;

import com.tickets.model.SearchResultEntry;

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
     * Lists all available stop names
     *
     * @return list of stop names
     */
    List<String> listAllStops();
}
