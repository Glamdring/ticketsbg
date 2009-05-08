package com.tickets.services;

import java.util.Date;
import java.util.List;

import com.tickets.model.Run;

public interface SearchService {

    List<Run> search(String startStop,
            String endStop,
            Integer dayId,
            Date date,
            int fromHour,
            int toHour);
}
