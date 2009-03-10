package com.tickets.services;

import java.io.Serializable;
import java.util.List;

import org.springframework.stereotype.Service;

import com.tickets.model.Day;
import com.tickets.model.Route;
import com.tickets.model.RouteDay;
import com.tickets.model.RouteDayId;

@Service("routeService")
public class RouteServiceImpl extends BaseService<Route> implements RouteService {

    @Override
    public List list() {
        return list(Route.class);
    }

    @Override
    public List listOrdered(String orderColumn) {
        return listOrdered(Route.class, orderColumn);
    }

    @Override
    public Day getDay(Serializable id) {
        return get(Day.class, id);
    }

    @Override
    public void save(Route route, List<Integer> applicableDays) {
        route = save(route);
        for (Integer dayId : applicableDays) {
            Day day = getDay(dayId);
            RouteDay rd = new RouteDay();
            rd.setDay(day);
            rd.setRoute(route);
            RouteDayId id = new RouteDayId(route.getId(), day.getId());
            rd.setId(id);
            saveObject(rd);
        }
    }

    @Override
    public Integer[] getDaysList(Route route) {
        Integer[] days = new Integer[route.getRouteDays().size()];
        int i = 0;
        for (RouteDay rd : route.getRouteDays()) {
            days[i++] = rd.getDay().getId();
        }

        return days;
    }
}
