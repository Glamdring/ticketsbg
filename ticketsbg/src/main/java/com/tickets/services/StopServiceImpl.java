package com.tickets.services;

import org.springframework.stereotype.Service;

import com.tickets.model.Route;
import com.tickets.model.Stop;

@Service("stopService")
public class StopServiceImpl extends BaseService<Stop> implements StopService {


    public Stop addStopToRoute(Stop stop, Route route) {


//        List<Stop> existingStops = getDao().findByNamedQuery("Stop.findStopsByRoute",
//                new String[]{"route"}, new Object[]{route});
//
//        stop.setRoute(route);
//        stop.setIdx(existingStops.size());

        if (stop.getIdx() == 0) {
            if (route.getStops() != null) {
                stop.setIdx(route.getStops().size() + 1);
            } else {
                stop.setIdx(1);
            }
            route.addStop(stop);
        } else {
            route.getStops().set(stop.getIdx() - 1, stop);
        }

        return stop;
    }
}
