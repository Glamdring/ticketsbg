package com.tickets.utils.timers;

import java.util.TimerTask;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.tickets.services.StatisticsService;

@Component("statisticsTask")
public class StatisticsTask extends TimerTask {

    @Autowired
    private StatisticsService service;

    @Override
    public void run() {
        service.refreshStatistics();
    }

}
