package com.tickets.services;

import com.tickets.model.Route;
import com.tickets.model.Stop;

public interface StopService extends Service<Stop> {

    /**
     * Adds the stop to the route, but does not persist it.
     * Persisting is done only when the route itself is persisted
     *
     * @param stop
     * @param route
     * @return the modified stop (with the set idx)
     */
    Stop addStopToRoute(Stop stop, Route route);

    /**
     * Removes a stop and sets the order of the remaining ones
     *
     * @param stop
     * @param route
     */
    void delete(Stop stop, Route route);
}
