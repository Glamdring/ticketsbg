package com.tickets.services;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.springframework.stereotype.Service;

import com.tickets.model.Run;
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

        return result;
    }

    @SuppressWarnings("unchecked")
    @Override
    public List<String> listAllStops() {
        List<String> stops = getDao().findByNamedQuery("Stop.listAllStopNames");
        return stops;
    }

}
