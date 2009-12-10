package com.tickets.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import com.tickets.annotations.Action;
import com.tickets.controllers.security.AccessLevel;
import com.tickets.services.StatisticsService;

@Controller("publicStatsController")
@Scope("request")
public class PublicStatisticsController extends BaseController {

    @Autowired
    private StatisticsService statsService;

    @Action(accessLevel=AccessLevel.PUBLIC)
    public int getCompaniesCount() {
        return statsService.getCompaniesCount();
    }

    @Action(accessLevel=AccessLevel.PUBLIC)
    public int getDestinations() {
        return statsService.getDestinationsCount();
    }
}
