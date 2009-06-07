package com.tickets.services;

import java.util.Calendar;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import org.springframework.stereotype.Service;

import com.tickets.model.Run;
import com.tickets.model.RunPriceHolder;
import com.tickets.model.Stop;
import com.tickets.utils.GeneralUtils;

@Service("searchService")
public class SearchServiceImpl extends BaseService implements SearchService {

    @SuppressWarnings("unchecked")
    @Override
    public List<RunPriceHolder> search(String fromStop, String toStop, Date date,
            int fromHour, int toHour, boolean isTimeForDeparture) {

        Calendar fromTime = GeneralUtils.createEmptyCalendar();
        fromTime.setTime(date);
        fromTime.add(Calendar.HOUR_OF_DAY, fromHour);

        Calendar toTime = GeneralUtils.createEmptyCalendar();
        toTime.setTime(date);
        toTime.add(Calendar.HOUR_OF_DAY, toHour);
        // toTime.roll(Calendar.MINUTE, 1);

        List<RunPriceHolder> result = getDao().findByNamedQuery("Run.search",
                new String[] { "fromStop", "toStop"},
                new Object[] { fromStop, toStop });


        // Complexity n^2 in the worst case, but generally n (because of the
        // limited number of stops)
        // This better go in the query, but such date + minutes calculations
        // are hardly possible with JPAQL
        for (Iterator<RunPriceHolder> it = result.iterator(); it.hasNext();) {
            RunPriceHolder rph = it.next();
            Run run = rph.getRun();

            Stop fromStopObj = findStop(run.getRoute().getStops(), fromStop);
            Stop toStopObj = findStop(run.getRoute().getStops(), toStop);

            int minutesFromStartToBeSought = 0;
            if (isTimeForDeparture) {
                minutesFromStartToBeSought = fromStopObj.getTimeToDeparture();
            } else {
                minutesFromStartToBeSought = toStopObj.getTimeToArrival();
            }

            // Cloning in order to avoid update queries, as the run object
            // is still associated with the session
            Calendar targetTime = (Calendar) run.getTime().clone();
            targetTime.add(Calendar.MINUTE, minutesFromStartToBeSought);

            //Filtering if the timing doesn't fit the selected
            if (fromTime.compareTo(targetTime) > 0
                    || toTime.compareTo(targetTime) < 0) {
                it.remove();
            }
        }

        return result;
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
    public List<String> listAllStops() {
        List<String> stops = getDao().findByNamedQuery("Stop.listAllStopNames");
        return stops;
    }

}
