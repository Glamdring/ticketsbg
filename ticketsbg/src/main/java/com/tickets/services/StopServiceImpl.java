package com.tickets.services;

import org.springframework.stereotype.Service;

import com.tickets.model.Route;
import com.tickets.model.Stop;

@Service("stopService")
public class StopServiceImpl extends BaseService<Stop> implements StopService {


    public Stop addStopToRoute(Stop stop, Route route) {

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
