package com.tickets.client.screens;

import com.extjs.gxt.ui.client.event.BaseEvent;
import com.extjs.gxt.ui.client.event.ComponentEvent;
import com.extjs.gxt.ui.client.event.SelectionListener;
import com.extjs.gxt.ui.client.widget.menu.Menu;
import com.extjs.gxt.ui.client.widget.menu.MenuItem;
import com.google.gwt.user.client.ui.AbsolutePanel;
import com.tickets.client.constants.Messages;

public class MainScreen extends AbsolutePanel {

    public MainScreen() {

        Menu mainMenu = new Menu();
        MenuItem coursesItem = new MenuItem(Messages.m.routes());
        coursesItem.addSelectionListener(new SelectionListener<ComponentEvent>() {

            @Override
            public void componentSelected(ComponentEvent ce) {
                // TODO Auto-generated method stub

            }
        });

        add(mainMenu);
    }
}
