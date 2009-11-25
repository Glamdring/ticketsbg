package com.tickets.utils;

import java.util.Calendar;
import java.util.Locale;

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
}
