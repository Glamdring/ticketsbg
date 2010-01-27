package com.tickets.services;

import java.util.Collection;
import java.util.List;

import org.richfaces.model.ListRowKey;

import com.tickets.model.Firm;
import com.tickets.model.Price;
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

    /**
     * Manages reordering of the stops list.
     * @param route
     * @param stops
     */
    void listReoredered(Route route, Collection<Stop> stops);

    /**
     * Gets the Price for the following path in the tree
     * @param rowKey
     * @param route
     * @return price
     */
    Price getPrice(ListRowKey rowKey, Route route);

    /**
     * Gets the price for the following stops
     * @param startStopId
     * @param endStopId
     * @param route
     * @return price
     */
    Price getPrice(int startStopId, int endStopId, Route route);

    /**
     * Lists names of stops already entered in the system,
     * for the purpose of suggestion
     *
     * @return list of names
     */
    List<String> getExistingStopNames(Firm firm);

    void saveMapAddress(String stopName, String mapAddress, Firm firm);

    String getMapUrl(String stopName, Firm firm);

    /**
     * creates or removes prices according to stop updates
     * @param route
     */
    void cascadePrices(Route route);
}
