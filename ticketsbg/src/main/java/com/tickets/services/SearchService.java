package com.tickets.services;

import java.util.Date;

public interface SearchService {

    void search(String startStop,
            String endStop,
            Integer dayId,
            Date fromTime,
            Date toTime);
}
