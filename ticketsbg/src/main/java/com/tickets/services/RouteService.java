package com.tickets.services;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import com.tickets.model.Day;
import com.tickets.model.Firm;
import com.tickets.model.Route;

public interface RouteService extends Service<Route> {

    Day getDay(Serializable id);

    void save(Route route, List<Integer> applicableDays);

    Integer[] getDaysList(Route route);

    List<Route> list(Firm firm);

    List<Route> listOrdered(Firm firm, String orderColumn);

    void addHourToRoute(Date hour, Route route);

    void removeHour(int hourId, Route route);

    /**
     * Finds a route for the specified name and firm
     *
     * @param selectedRouteName
     * @param firm
     *
     * @return the route, if found; null otherwise.
     */
    Route findRoute(String selectedRouteName, Firm firm);
}