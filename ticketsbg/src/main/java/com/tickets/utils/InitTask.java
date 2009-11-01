package com.tickets.utils;

import java.util.List;
import java.util.TimerTask;

import javax.annotation.Resource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.tickets.model.Day;
import com.tickets.services.Service;
import com.tickets.services.UserService;

@Component("initTask")
public class InitTask extends TimerTask {

    @Autowired
    private UserService userService;

    @Resource(name="baseService")
    private Service service;

    /**
     * Executed only once, after the spring context is initialized
     */
    @Override
    public void run() {
        userService.createInitialUser();
        fillDays();
    }

    @SuppressWarnings("unchecked")
    private void fillDays() {
        List existing = service.list(Day.class);
        if (existing.size() == 0) {
            String[] dayNames = new String[] { "mon", "tue", "wed", "thu", "fri",
                    "sat", "sun" };

            int i = 1;
            for (String dayName : dayNames) {
                Day day = new Day();
                day.setId(i++);
                day.setName(dayName);
                service.save(day);
            }
        }
    }
}
