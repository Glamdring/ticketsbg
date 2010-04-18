package com.tickets.utils;

import java.util.Calendar;
import java.util.Locale;
import java.util.TimeZone;

import javax.faces.context.FacesContext;
import javax.servlet.http.HttpServletRequest;

import org.apache.commons.mail.Email;
import org.apache.commons.mail.HtmlEmail;
import org.apache.commons.mail.SimpleEmail;

import com.tickets.constants.Settings;

public class GeneralUtils {

    public static Calendar getPreviousDay() {
        Calendar c = GeneralUtils.createCalendar();
        c.add(Calendar.DAY_OF_YEAR, -1);
        return c;
    }

    /**
     * Creates a new calendar, which is set to the current date/time
     *
     * @return calendar
     */
    public static Calendar createCalendar() {
        Calendar c = Calendar.getInstance(getDefaultLocale());
        c.setTimeZone(getDefaultTimeZone());
        return c;
    }

    private static Locale defaultLocale;

    public static Locale getDefaultLocale() {
        if (defaultLocale == null) {
            defaultLocale = new Locale(Settings.getValue("default.locale"));
        }
        return defaultLocale;
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

    public static Email getPreconfiguredMail(boolean html) {
        Email e = null;
        if (html) {
            e = new HtmlEmail();
        } else {
            e = new SimpleEmail();
        }
        e.setHostName(Settings.getValue("smtp.host"));
        String username = Settings.getValue("smtp.user");
        if (username.length() > 0) {
            e.setAuthentication(username, Settings.getValue("smtp.password"));
        }

        String bounceEmail = Settings.getValue("smtp.bounce.email");
        if (bounceEmail != null) {
            e.setBounceAddress(bounceEmail);
        }

        e.setTLS(true);
        e.setCharset("UTF8");

        // e.setDebug(true);

        return e;
    }

    private static ThreadLocal<Locale> currentLocale = new ThreadLocal<Locale>();

    /**
     * This convenient method makes the application dependent on the JSF
     * front-end, but in case needed a setter could easily be introduced, which
     * sets the current locale in the thread-local so that this method remains
     * framework-agnostic
     *
     * @return the current locale
     */
    public static Locale getCurrentLocale() {
        Locale locale = currentLocale.get();
        if (locale == null) {
            try {
                // TODO move this to inner-class factory to avoid implicit
                // dependecy on the jsf
                locale = FacesContext.getCurrentInstance().getViewRoot().getLocale();
            } catch (Exception ex) {
                // ignore
            }

            if (locale == null) {
                locale = GeneralUtils.getDefaultLocale();
            }
            currentLocale.set(locale);
        }

        return locale;
    }

    private static TimeZone timeZone;
    public static TimeZone getDefaultTimeZone() {
        if (timeZone == null) {
            timeZone = TimeZone.getTimeZone("Europe/Helsinki");
            if (timeZone == null) {
                timeZone = TimeZone.getTimeZone("Europe/Sofia");
                if (timeZone == null) {
                    timeZone = TimeZone.getTimeZone("GMT+2");
                }
            }
        }
        return timeZone;
    }
}