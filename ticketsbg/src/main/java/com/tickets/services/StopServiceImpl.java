package com.tickets.services;

import java.util.Collections;
import java.util.List;

import org.richfaces.model.ListRowKey;
import org.springframework.stereotype.Service;

import com.tickets.model.Firm;
import com.tickets.model.Price;
import com.tickets.model.Route;
import com.tickets.model.Stop;
import com.tickets.model.StopMap;

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

        cascadePrices(route);
        return stop;
    }

    @Override
    public void delete(Stop stop, Route route) {
        route.getStops().remove(stop);

        int i = 1;
        for (Stop s : route.getStops()) {
            s.setIdx(i++);
        }
        cascadePrices(route);
    }

    private void cascadePrices(Route route) {
        List<Price> prices = route.getPrices();
        List<Stop> stops = route.getStops();
        // to size - 1, because no sub-route can start from the last stop
        for (int i = 0; i < stops.size() - 1; i++) {
            // from 1, because the first stop cannot be an end-stop
            for (int j = i + 1; j < stops.size(); j++) {
                Price price = new Price();
                price.setStartStop(stops.get(i));
                price.setEndStop(stops.get(j));
                if (!containsPrice(prices, price)) {
                    route.addPrice(price);
                }
            }
        }

        /*
         * Remove unneeded prices (~= DELETE_ORPHAN) Remove if: - the last stop
         * is starting stop - if the first stop is an end stop - if either start
         * or end stop is not found in the stops list (deleted)
         */

        for (int i = 0; i < prices.size(); i++) {
            Price price = prices.get(i);
            boolean shouldRemove = false;
            if (price.getStartStop().getStopId() == stops.get(stops.size() - 1)
                    .getStopId()) {
                shouldRemove = true;
            }
            if (price.getEndStop().getStopId() == stops.get(0).getStopId()) {
                shouldRemove = true;
            }
            if (!containsStop(stops, price.getStartStop())
                    || !containsStop(stops, price.getEndStop())) {
                shouldRemove = true;
            }

            if (shouldRemove) {
                prices.remove(i);
                i--;
            }
        }
    }

    private boolean containsStop(List<Stop> stops, Stop stop) {
        for (Stop s : stops) {
            if (s.getStopId() == stop.getStopId()) {
                return true;
            }
        }
        return false;
    }

    private boolean containsPrice(List<Price> prices, Price price) {
        for (Price p : prices) {
            if (p.getStartStop().getStopId() == price.getStartStop()
                    .getStopId()
                    && p.getEndStop().getStopId() == price.getEndStop()
                            .getStopId()) {
                return true;
            }
        }
        return false;
    }

    @Override
    public void listReoredered(Route route) {
        if (route.getStops() != null) {
            for (int i = 0, max = route.getStops().size(); i < max; i++) {
                route.getStops().get(i).setIdx(i + 1);
            }
        }
        // Reordering the list itself, not only changing the idx's
        Collections.sort(route.getStops());

        cascadePrices(route);
    }

    @Override
    public Price getPrice(ListRowKey keys, Route route) {
        int startStopId = Integer.parseInt(((String) keys.get(0)).replace(
                "start", ""));
        int endStopId = Integer.parseInt(((String) keys.get(1)).replace("end",
                ""));

        return getPrice(startStopId, endStopId, route);
    }

    @Override
    public Price getPrice(int startStopId, int endStopId, Route route) {
        Stop startStop = getStop(startStopId, route.getStops());
        Stop endStop = getStop(endStopId, route.getStops());

        Price searchedPrice = null;

        for (Price price : route.getPrices()) {
            if (endStop.getStopId() == price.getEndStop().getStopId()
                    && startStop.getStopId() == price.getStartStop()
                            .getStopId()) {
                searchedPrice = price;
                break;
            }

        }

        return searchedPrice;
    }

    private Stop getStop(int stopId, List<Stop> stops) {
        for (Stop stop : stops) {
            if (stop.getStopId() == stopId) {
                return stop;
            }
        }
        return null;
    }

    @SuppressWarnings("unchecked")
    @Override
    public List<String> getExistingStopNames(Firm firm) {
        List<String> result = getDao()
            .findByQuery(
                "SELECT stop.name FROM Stop stop WHERE stop.route.firm=:firm GROUP BY stop.name ORDER BY stop.name",
                new String[] { "firm" }, new Object[] { firm });
        return result;
    }

    @Override
    public void saveMapAddress(String stopName, String mapAddress, Firm firm) {
        StopMap sm = getStopMap(stopName, firm);
        sm.setStopName(stopName);
        sm.setMapUrl(mapAddress);
        sm.setFirm(firm);
        getDao().persist(sm);
    }

    @Override
    public String getMapUrl(String stopName, Firm firm) {
        return getStopMap(stopName, firm).getMapUrl();
    }

    private StopMap getStopMap(String stopName, Firm firm) {
        List list = getDao().findByNamedQuery("StopMap.findByStopName",
                new String[] { "stopName", "firm" },
                new Object[] { stopName, firm });

        if (list != null && list.size() > 0) {
            return (StopMap) list.get(0);
        }

        return new StopMap();
    }
}