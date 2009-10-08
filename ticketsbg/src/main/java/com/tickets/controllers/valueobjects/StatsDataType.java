package com.tickets.controllers.valueobjects;

public enum StatsDataType {
    TICKETS_COUNT,
    PASSENGERS_COUNT,
    MONEY;

    public String getKey() {
        return this.toString();
    }
}
