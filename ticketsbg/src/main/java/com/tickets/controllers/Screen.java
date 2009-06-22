package com.tickets.controllers;

public enum Screen {
    PAYMENT_SCREEN("paymentScreen"),
    SEARCH_SCREEN("searchScreen"),
    SEARCH_RESULTS("searchResults"),
    HOME("home"),
    PERSONAL_INFORMATION_SCREEN("personalInformationScreen"),
    REGISTRATION_SCREEN("registrationScreen"),

    ROUTE_SCREEN("routeScreen"),
    ROUTES_LIST("routesList")
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
