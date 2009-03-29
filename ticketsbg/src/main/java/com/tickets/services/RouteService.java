package com.tickets.services;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import com.tickets.model.Day;
import com.tickets.model.Route;

public interface RouteService extends Service<Route> {

    Day getDay(Serializable id);

    void save(Route route, List<Integer> applicableDays);

    Integer[] getDaysList(Route route);

    List list();

    List listOrdered(String orderColumn);

    void addHourToRoute(Date hour, Route route);

    void removeHour(int hourId, Route route);
}