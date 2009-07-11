package com.tickets.controllers;

public enum Step {

    SEARCH_RESULTS(Screen.SEARCH_RESULTS),
    PAYMENT(Screen.PAYMENT_SCREEN);

    private Screen screen;

    private Step(Screen screen) {
        this.screen = screen;
    }

    public Screen getScreen() {
        return screen;
    }

    public void setScreen(Screen screen) {
        this.screen = screen;
    }
}