package com.tickets.controllers.security;

public enum AccessLevel {
    ADMINISTRATOR(50),
    FIRM_ADMINISTRATOR(40),
    FIRM_COORDINATOR(30),
    CASHIER_DESK(20),
    PUBLIC(10);

    private int privileges;

    private AccessLevel(int value) {
        this.privileges = value;
    }

    public int getPrivileges() {
        return privileges;
    }

    public void setPrivileges(int privileges) {
        this.privileges = privileges;
    }
}
