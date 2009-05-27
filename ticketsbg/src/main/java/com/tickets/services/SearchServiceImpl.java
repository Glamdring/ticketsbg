package com.tickets.services;

import java.util.Calendar;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import org.springframework.stereotype.Service;

import com.tickets.model.Run;
import com.tickets.model.Stop;
import com.tickets.utils.GeneralUtils;

@Service("searchService")
public class SearchServiceImpl extends BaseService implements SearchService {


    @SuppressWarnings("unchecked")
    @Override
    public List<Run> search(String fromStop, String toStop, Date date,
            int fromHour, int toHour, boolean isTimeForDeparture) {

        Calendar fromTime = GeneralUtils.createEmptyCalendar();
        fromTime.setTime(date);
        fromTime.add(Calendar.HOUR_OF_DAY, fromHour);

        Calendar toTime = GeneralUtils.createEmptyCalendar();
        toTime.setTime(date);
        toTime.add(Calendar.HOUR_OF_DAY, toHour);
        //toTime.roll(Calendar.MINUTE, 1);

        List<Run> result = getDao().findByNamedQuery("Run.search",
                new String[] { "fromStop", "toStop", "fromTime", "toTime" },
                new Object[] { fromStop, toStop, fromTime, toTime });

        //Complexity n^2 in the worst case, but generally n (because of the limited number of stops)
        for (Iterator<Run> it = result.iterator(); it.hasNext();) {
            Run run = it.next();
            int fromStopIdx = stopIndex(run.getRoute().getStops(), fromStop);
            int toStopIdx = stopIndex(run.getRoute().getStops(), toStop);
            if (fromStopIdx == -1 || toStopIdx == -1 || fromStopIdx > toStopIdx) {
                it.remove();
            }
        }

        return result;
    }

    private int stopIndex(List<Stop> stops, String stopName) {
        if (stopName == null)
            return -1;

        for (Stop stop : stops) {
            if (stopName.equals(stop.getName())) {
                return stop.getIdx();
            }
        }
        return -1;
    }

    @SuppressWarnings("unchecked")
    @Override
    public List<String> listAllStops() {
        List<String> stops = getDao().findByNamedQuery("Stop.listAllStopNames");
        return stops;
    }

}
