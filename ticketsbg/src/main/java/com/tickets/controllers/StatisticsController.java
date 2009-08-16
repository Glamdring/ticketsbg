package com.tickets.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import com.tickets.services.StatisticsService;

@Controller
@Scope("singleton")
public class StatisticsController extends BaseController {

    @Autowired
    private StatisticsService statsService;


    public int getCompaniesCount() {
        return statsService.getCompaniesCount();
    }

    public int getDestinations() {
        return statsService.getDestinationsCount();
    }

}
