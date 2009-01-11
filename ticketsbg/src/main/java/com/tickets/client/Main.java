package com.tickets.client;

import java.util.Locale;

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.user.client.Command;
import com.google.gwt.user.client.ui.AbsolutePanel;
import com.google.gwt.user.client.ui.Panel;
import com.google.gwt.user.client.ui.RootPanel;
import com.tickets.client.components.MainPanel;
import com.tickets.client.screens.CoursesScreen;
import com.tickets.client.screens.LoginScreen;

/**
 * Entry point classes define <code>onModuleLoad()</code>.
 */
public class Main implements EntryPoint {

    /**
     * This is the entry point method.
     */
    private MainPanel mainPanel;

    public void onModuleLoad() {

        //mainPanel = new MainPanel();

        RootPanel.get().add(new LoginScreen());


    }

    public AbsolutePanel getMainPanel() {
        return mainPanel;
    }

    class CoursesCommand implements Command {
        public void execute() {
            Panel p = getMainPanel();
            CoursesScreen screen = new CoursesScreen();
            p.add(screen);
        }
    }
}
