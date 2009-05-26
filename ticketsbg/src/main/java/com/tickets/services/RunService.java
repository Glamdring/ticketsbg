package com.tickets.services;

public interface RunService<Run> extends Service<Run> {

    /**
     * Creates runs for all routes so that there are runs for the next X days,
     * where X is defined for each route
     */
    void createRuns();
}
