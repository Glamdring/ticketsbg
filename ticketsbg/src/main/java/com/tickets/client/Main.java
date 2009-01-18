package com.tickets.client;

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.user.client.ui.RootPanel;
import com.tickets.client.screens.LoginScreen;

/**
 * Entry point classes define <code>onModuleLoad()</code>.
 */
public class Main implements EntryPoint {

    /**
     * This is the entry point method.
     */
    public void onModuleLoad() {

        //mainPanel = new MainPanel();

        RootPanel.get().add(new LoginScreen());

    }

}
