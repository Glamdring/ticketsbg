package com.tickets.controllers.security;

public enum AccessLevel {
    ADMINISTRATOR(5),
    FIRM_ADMINISTRATOR(4),
    FIRM_COORDINATOR(3),
    CASHIER_DESK(2),
    PUBLIC(1);

    private int value;

    private AccessLevel(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }

    public void setValue(int value) {
        this.value = value;
    }

}
