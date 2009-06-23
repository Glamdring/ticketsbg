package com.tickets.utils;

import java.util.Calendar;
import java.util.Locale;

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
}
