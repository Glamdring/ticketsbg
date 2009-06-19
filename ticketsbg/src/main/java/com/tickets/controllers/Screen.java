package com.tickets.controllers;

public enum Screen {
    PAYMENT_SCREEN("paymentScreen"),
    SEARCH_SCREEN("searchScreen"),
    SEARCH_RESULT("searchResultScreen"),
    HOME("home"),
    PERSONAL_INFORMATION_SCREEN("personalInformationScreen"),

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
