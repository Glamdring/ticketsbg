package com.tickets.controllers.security;

public enum AccessLevel {
    ADMINISTRATOR(60),
    FIRM_ADMINISTRATOR(50),
    FIRM_COORDINATOR(40),
    CASHIER_DESK(30),
    PUBLIC_LOGGED(20),
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
