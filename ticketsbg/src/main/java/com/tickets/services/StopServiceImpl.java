package com.tickets.services;

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
            if (containsStop(route.getStops(), stop)) {
                throw new IllegalArgumentException();
            }
            stop.setIdx(route.getStops().size() + 1);
            route.addStop(stop);
        } else {
            route.getStops().set(route.getStops().indexOf(stop), stop);
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

    public void cascadePrices(Route route) {
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
         * Remove unneeded prices (~= DELETE_ORPHAN) Remove if:
         * - the last stop is starting stop
         * - the first stop is an end stop
         * - if either start or end stop is not found in the stops list (deleted)
         */

        for (int i = 0; i < prices.size(); i++) {
            Price price = prices.get(i);
            boolean shouldRemove = false;
            if (price.getStartStop().equals(stops.get(stops.size() - 1))) {
                shouldRemove = true;
            }
            if (price.getEndStop().equals(stops.get(0))) {
                shouldRemove = true;
            }
            if (!containsStop(stops, price.getStartStop())
                    || !containsStop(stops, price.getEndStop())) {
                shouldRemove = true;
            }

            if (shouldRemove) {
                // setting to null so that hibernate can safely cascade delete
                // orphan without tying to persist anything
                price.setStartStop(null);
                price.setEndStop(null);
                prices.remove(i);
                i--;
            }
        }
    }

    private boolean containsStop(List<Stop> stops, Stop stop) {
        for (Stop s : stops) {
            if (s.equals(stop)) {
                return true;
            }
        }
        return false;
    }

    private boolean containsPrice(List<Price> prices, Price price) {
        for (Price p : prices) {
            if (p.getStartStop().equals(price.getStartStop())
                    && p.getEndStop().equals(price.getEndStop())) {
                return true;
            }
        }
        return false;
    }

    @Override
    public void listReoredered(Route route) {

        List<Stop> stops = route.getStops();

        if (stops == null || stops.size() == 0) {
            return;
        }

        for (int i = 0, max = stops.size(); i < max; i++) {
            stops.get(i).setIdx(i + 1);
        }

        cascadePrices(route);
    }

    @Override
    public Price getPrice(ListRowKey keys, Route route) {
        String startStopName = ((String) keys.get(0)).replace("start", "");
        String endStopName = ((String) keys.get(1)).replace("end", "");

        return getPrice(startStopName, endStopName, route);
    }

    @Override
    public Price getPrice(String startStopName, String endStopName, Route route) {
        Stop startStop = getStop(startStopName, route.getStops());
        Stop endStop = getStop(endStopName, route.getStops());

        if (startStop == null || endStop == null) {
            return null;
        }
        Price searchedPrice = null;

        for (Price price : route.getPrices()) {
            if (endStop.equals(price.getEndStop())
                    && startStop.equals(price.getStartStop())) {
                searchedPrice = price;
                break;
            }

        }

        return searchedPrice;
    }

    private Stop getStop(String stopName, List<Stop> stops) {
        for (Stop stop : stops) {
            if (stop.getName().equals(stopName)) {
                return stop;
            }
        }
        return null;
    }

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