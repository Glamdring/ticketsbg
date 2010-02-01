package com.tickets.services;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.faces.context.FacesContext;

import org.apache.log4j.Logger;

import com.tickets.controllers.security.AccessLevel;
import com.tickets.controllers.users.LoggedUserHolder;
import com.tickets.model.PassengerDetails;
import com.tickets.model.Route;
import com.tickets.model.Run;
import com.tickets.model.Stop;
import com.tickets.model.Ticket;
import com.tickets.model.User;

public class ServiceFunctions {

    private static final Logger log = Logger.getLogger(ServiceFunctions.class);

    // Cache used in cases when the same value is to be calculated
    // during the same request (and therefore - thread)
    private static ThreadLocal<Map<Run, Integer>> cache = new ThreadLocal<Map<Run, Integer>>();

    public static int getVacantSeats(Run run, String fromStop, String toStop) {
        return getVacantSeats(run, fromStop, toStop, true);
    }

    public static int getVacantSeats(Run run, String fromStop, String toStop,
            boolean useCache) {

        // lazily initialize the cache
        if (cache.get() == null)
            cache.set(new HashMap<Run, Integer>());

        if (useCache && cache.get().containsKey(run)) {
            Integer result = cache.get().get(run);
            return result;
        }

        int seats = run.getRoute().getSeats()
                - getUsedSeats(run, fromStop, toStop).size();

        cache.get().put(run, seats);

        return seats;
    }

    /**
     * Method for listing all used seats on a run
     *
     * The ones that are configured not to be sold online are also marked as
     * used, but only for the public part of the site.
     *
     * @param price
     * @param run
     * @return a sorted list
     */

    public static List<Integer> getUsedSeats(Run run, String fromStop,
            String toStop) {
        if (run == null || fromStop == null) {
            return Collections.<Integer> emptyList();
        }

        Route route = run.getRoute();

        List<Integer> u = new ArrayList<Integer>();

        // If this is not the admin part, mark all seats beyond the
        // configured 'sell online' seats as used
        User user = LoggedUserHolder.getUser();
        if (user == null || user.getAccessLevel() == null
                || user.getAccessLevel() == AccessLevel.PUBLIC) {
            for (int i = 1; i < run.getRoute().getSellSeatsFrom(); i++) {
                u.add(i);
            }
            for (int i = route.getSellSeatsTo() + 1; i < route.getSeats(); i++) {
                u.add(i);
            }
        }

        List<Stop> stops = route.getStops();
        Stop startStop = getStopByName(fromStop, stops);
        Stop endStop = getStopByName(toStop, stops);

        // if no end stop selected, assume final stop (TODO: sum all??)
        if (endStop == null) {
            endStop = stops.get(stops.size() - 1);
        }

        // Iterating all tickets and removing those whose stops
        // don't intersect with the current criteria
        List<Ticket> tickets = new ArrayList<Ticket>(run.getTickets().size()
                + run.getReturnTickets().size());

        tickets.addAll(run.getTickets());
        tickets.addAll(run.getReturnTickets());

        ticketCycle: for (Ticket ticket : run.getTickets()) {
            if (ticket.isTimeouted()) {
                continue;
            }
            // from (relative) first to penultimate stop
            for (int i = startStop.getIdx() - 1; i < endStop.getIdx() - 1; i++) {
                Stop tmpStop = stops.get(i);
                // If the start stop of the existing ticket is anywhere
                // in the active stops for the requested criteria
                // increase the used seats
                if (ticket.getStartStop().equals(tmpStop.getName())) {
                    for (PassengerDetails pd : ticket.getPassengerDetails()) {
                        u.add(pd.getSeat());
                    }
                    continue ticketCycle;
                }
                // If the end stop of the existing ticket is anywhere
                // in the active stops for the requested criteria
                // (excluding the first one, because thus there is no
                // intersection between them)
                if (ticket.getEndStop().equals(tmpStop.getName())
                        && i != startStop.getIdx() - 1) {
                    for (PassengerDetails pd : ticket.getPassengerDetails()) {
                        u.add(pd.getSeat());
                    }
                    continue ticketCycle;
                }
            }
        }

        Collections.sort(u);
        return u;
    }

    public static Stop getStopByName(String name, List<Stop> stops) {
        if (name == null) {
            return null;
        }

        for (Stop stop : stops) {
            if (stop.getName().startsWith(name)) {
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

    public static String concat(String string1, String string2) {
        return string1.concat(string2);
    }

    public static String formatTextNewLines(String text) {
        if (text == null) {
            return "";
        }

        text = text.replace("\r\n", "<br />");
        text = text.replace("\n", "<br />");

        return text;
    }

    private static final Map<String, String> externalResourceCache = new HashMap<String, String>();

    public static String getExternalResource(String urlString) {
        try {
            if (externalResourceCache.containsKey(urlString)) {
                return externalResourceCache.get(urlString);
            }

            URL url = new URL(urlString);
            InputStream is = url.openConnection().getInputStream();
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            int b;
            while ((b = is.read()) != -1) {
                baos.write(b);
            }
            String content = baos.toString();

            externalResourceCache.put(urlString, content);

            return content;
        } catch (Exception ex) {
            log.error("Error getting external resoruce", ex);
            return "";
        }
    }

    public static void resetExternalResourceCache() {
        externalResourceCache.clear();
    }

    private static final String ADMIN_ROOT = "/admin/";
    private static final String HELP_ROOT = "help";

    public static String getCurrentHelpPage(FacesContext ctx) {
        String viewId = ctx.getViewRoot().getViewId();

        if (viewId.contains(HELP_ROOT)) {
            return "#";
        }

        String localizedHelpRoot = HELP_ROOT + "_"
                + ctx.getViewRoot().getLocale().getLanguage() + "/";
        String currentPageName = viewId.replace(ADMIN_ROOT, "");
        return localizedHelpRoot + currentPageName;
    }
}
