package com.tickets.services;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.tickets.model.Route;
import com.tickets.model.Run;
import com.tickets.model.Stop;
import com.tickets.model.Ticket;

public class ServiceFunctions {

    // Cache used in cases when the same value is to be calculated
    // during the same request (and therefore - thread)
    private static ThreadLocal<Map<Run, Integer>> cache = new ThreadLocal<Map<Run, Integer>>();

    public static int getVacantSeats(Run run, String fromStop, String toStop) {
        return getVacantSeats(run, fromStop, toStop, true);
    }

    public static int getVacantSeats(Run run, String fromStop, String toStop, boolean useCache) {
        if (run == null || fromStop == null) {
            return 0;
        }

        //lazily initialize the cache
        if (cache.get() == null)
            cache.set(new HashMap<Run, Integer>());

        if (useCache && cache.get().containsKey(run)) {
            Integer result = cache.get().get(run);
            // Remove the result after getting it TODO ??
            cache.get().remove(run);
            return result;
        }

        Route route = run.getRoute();
        int seats = route.getSellSeatsTo() - route.getSellSeatsFrom() + 1;

        List<Stop> stops = run.getRoute().getStops();
        Stop startStop = getStopByName(fromStop, stops);
        Stop endStop = getStopByName(toStop, stops);

        //if no end stop selected, assume final stop (TODO: sum all??)
        if (endStop == null) {
            endStop = stops.get(stops.size() - 1);
        }

        // Iterating all tickets and removing those whose stops
        // don't intersect with the current criteria
        List<Ticket> tickets = new ArrayList<Ticket>(run.getTickets().size()
                + run.getReturnTickets().size());

        tickets.addAll(run.getTickets());
        tickets.addAll(run.getReturnTickets());

        ticketCycle:
        for (Ticket ticket : run.getTickets()) {
            if (ticket.isTimeouted()) {
                continue;
            }
            //from (relative) first to penultimate stop
            for (int i = startStop.getIdx() - 1; i < endStop.getIdx() - 1; i ++) {
                Stop tmpStop = stops.get(i);
                // If the start stop of the existing ticket is anywhere
                // in the active stops for the requested criteria
                // increase the used seats
                if (ticket.getStartStop().equals(tmpStop.getName())) {
                    seats -= ticket.getPassengersCount();
                    continue ticketCycle;
                }
                // If the end stop of the existing ticket is anywhere
                // in the active stops for the requested criteria
                // (excluding the first one, because thus there is no
                // intersection between them)
                if (ticket.getEndStop().equals(tmpStop.getName()) && i != startStop.getIdx() - 1) {
                    seats -= ticket.getPassengersCount();
                    continue ticketCycle;
                }
            }
        }

        cache.get().put(run, seats);

        return seats;
    }

    private static Stop getStopByName(String name, List<Stop> stops) {
        for (Stop stop : stops) {
            if (stop.getName().equals(name)) {
                return stop;
            }
        }
        return null;
    }

    public static int getSize(Collection collection) {
        if (collection == null) {
            return 0;
        }
        return collection.size();
    }


    public static int getSeatsCount(Collection<Ticket> tickets) {
        int sum = 0;
        for (Ticket ticket : tickets) {
            sum += ticket.getPassengersCount();
        }

        return sum;
    }

    public static void clearCache() {
        cache.set(null);
    }
}
