package com.tickets.controllers.valueobjects;

public enum Screen {
    PAYMENT_SCREEN("paymentScreen"),
    SEARCH_SCREEN("searchScreen"),
    SEARCH_RESULTS("searchResults"),
    HOME("home"),
    REGISTRATION_SCREEN("registrationScreen"),
    LOGIN_SCREEN("loginScreen"),

    ROUTE_SCREEN("routeScreen"),
    ROUTES_LIST("routesList"),
    ADMIN_HOME("adminPanel"),
    ADMIN_LOGIN_SCREEN("adminLoginScreen"),
    RUNS_LIST("runsList"),
    ADMIN_SEARCH_RESULTS("adminSearchResults"),

    UNAUTHORIZED("unauthorized")
    ;


    private String outcome;

    private Screen(String outcome) {
        this.outcome = outcome;
    }

    public String getOutcome() {
        return outcome;
    }

    public void setOutcome(String outcome) {
        this.outcome = outcome;
    }
}
