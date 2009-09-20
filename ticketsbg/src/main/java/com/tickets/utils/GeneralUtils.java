package com.tickets.utils;

import java.util.Calendar;
import java.util.Locale;

import javax.faces.context.FacesContext;
import javax.servlet.http.HttpServletRequest;

public class GeneralUtils {

    public static Calendar getPreviousDay() {
        Calendar c = Calendar.getInstance(getLocale());
        c.roll(Calendar.DAY_OF_MONTH, false);
        return c;
    }

    /**
     * Creates a new calendar, which is set to the current date/time
     *
     * @return calendar
     */
    public static Calendar createEmptyCalendar() {
        return Calendar.getInstance(getLocale());
    }

    public static Locale getLocale() {
        return new Locale("bg");
    }

    public static String getSubdomain() {
        HttpServletRequest request = (HttpServletRequest) FacesContext
                .getCurrentInstance().getExternalContext().getRequest();
        return getSubdomain(request);
    }

    public static String getSubdomain(HttpServletRequest request) {

        String serverName = request.getServerName();

        String subdomain = null;
        // Get only in case a subdomain is typed (i.e. '.' is not
        // only contained once)
        if (serverName.lastIndexOf(".") != serverName.indexOf(".")) {
            subdomain = serverName.substring(0, serverName.indexOf("."));
            subdomain = subdomain.replace(".", "");
        }

        return subdomain;
    }
}
