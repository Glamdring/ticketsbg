package com.tickets.controllers;

public enum Step {

    SEARCH_RESULTS("searchResults"),
    PERSONAL_INFORMATION("personalInformationScreen"),
    PAYMENT("paymentScreen");

    private String outcome;

    private Step(String outcome) {
        this.outcome = outcome;
    }

    public String getOutcome() {
        return outcome;
    }

    public void setOutcome(String outcome) {
        this.outcome = outcome;
    }
}
