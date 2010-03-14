package com.tickets.services;

import java.io.Serializable;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.hibernate.Hibernate;
import org.springframework.stereotype.Service;

import com.tickets.model.Day;
import com.tickets.model.Firm;
import com.tickets.model.Route;
import com.tickets.model.RouteDay;
import com.tickets.model.RouteHour;

@Service("routeService")
public class RouteServiceImpl extends BaseService<Route> implements RouteService {

    public List<Route> list(Firm firm) {
        return getDao().findByQuery("SELECT r FROM Route r WHERE r.firm=:firm",
                new String[] { "firm" }, new Object[] { firm });
    }

    public List<Route> listOrdered(Firm firm, String orderColumn) {
        return getDao().findByQuery(
                "SELECT r FROM Route r WHERE r.firm=:firm ORDER BY "
                        + orderColumn,
                new String[] { "firm" },
                new Object[] { firm });
    }

    @Override
    public Day getDay(Serializable id) {
        return get(Day.class, id);
    }

    @Override
    public void save(Route route, List<Integer> applicableDays) {
        List<RouteDay> routeDays = route.getRouteDays();

        Integer[] daysArray = applicableDays.toArray(new Integer[applicableDays.size()]);
        for (int i = 0; i < routeDays.size(); i ++) {
            if (Arrays.binarySearch(daysArray, routeDays.get(i).getDay().getId()) < 0) {
                routeDays.remove(i);
                i--;
            }
        }

        for (Integer dayId : applicableDays) {
            if (!containsDay(routeDays, dayId)) {
                Day day = getDay(dayId);
                RouteDay rd = new RouteDay();
                rd.setDay(day);
                //rd.setRoute(route);
                //routeDays.add(rd);
                route.addRouteDay(rd);
            }
        }

        save(route);
    }

    private boolean containsDay(List<RouteDay> list, Integer dayId) {
        for (RouteDay rd : list) {
            if (rd.getDay().getId() == dayId.intValue()) {
                return true;
            }
        }
        return false;
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

    @Override
    public void addHourToRoute(Date hour, Route route) {
        if (hour == null) {
            return;
        }

        RouteHour rh = new RouteHour();
        Calendar c = Calendar.getInstance();
        c.setTime(hour);
        rh.setMinutes(c.get(Calendar.HOUR_OF_DAY) * 60 + c.get(Calendar.MINUTE));
        route.addHour(rh);
    }

    @Override
    public void removeHour(int hourId, Route route) {
        if (hourId <= 0)
            return;

        List<RouteHour> list = route.getRouteHours();

        for (int i = 0; i < list.size(); i ++) {
            if (list.get(i).getId() == hourId) {
                list.remove(i);
                break;
            }
        }
    }

    @Override
    public Route findRoute(String selectedRouteName, Firm firm) {
        List result = getDao().findByNamedQuery("Route.findByNameAndFirm",
                new String[] { "name", "firm" },
                new Object[] { selectedRouteName, firm });
        if (result != null && result.size() > 0) {
            return (Route) result.get(0);
        }

        return null;
    }

    @Override
    public Route getNonLazy(int routeId) {
        Route route = get(Route.class, routeId);
        Hibernate.initialize(route.getRuns());
        return route;
    }

}
