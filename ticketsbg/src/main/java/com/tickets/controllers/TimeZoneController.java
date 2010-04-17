package com.tickets.controllers;

import java.io.Serializable;
import java.util.TimeZone;

import javax.annotation.PostConstruct;

import org.apache.log4j.Logger;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

@Controller("timeZoneController")
@Scope("singleton")
public class TimeZoneController implements Serializable {

    private static final Logger logger = Logger.getLogger(TimeZoneController.class);

    private TimeZone timeZone;

    @PostConstruct
    public void init() {
        // Important!!!!
        // the following command muts be run on linux to set the default TZ:
        // dpkg-reconfigure tzdata

        timeZone = TimeZone.getTimeZone("Europe/Helsinki");
        if (timeZone == null) {
            timeZone = TimeZone.getTimeZone("Europe/Sofia");
            if (timeZone == null) {
                timeZone = TimeZone.getTimeZone("GMT+2");
            }
        }
        logger.info("Initialized application with timeZone=" + timeZone);
    }

    public TimeZone getTimeZone() {
        return timeZone;
    }

    public void setTimeZone(TimeZone timeZone) {
        this.timeZone = timeZone;
    }
}
