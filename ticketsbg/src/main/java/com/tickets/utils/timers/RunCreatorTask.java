package com.tickets.utils.timers;

import java.util.TimerTask;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.tickets.services.RunService;

@Component("runCreatorTask")
public class RunCreatorTask extends TimerTask {

    @Autowired
    private RunService service;

    @Override
    public void run() {
        service.createRuns();
    }

}
