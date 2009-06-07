package com.tickets.controllers;

import java.util.TimeZone;

import javax.annotation.PostConstruct;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

@Controller("timeZoneController")
@Scope("session")
public class TimeZoneController {

    private TimeZone timeZone;

    @PostConstruct
    public void init() {
        timeZone = TimeZone.getTimeZone("Europe/Helsinki");
    }

    public TimeZone getTimeZone() {
        return timeZone;
    }

    public void setTimeZone(TimeZone timeZone) {
        this.timeZone = timeZone;
    }
}
