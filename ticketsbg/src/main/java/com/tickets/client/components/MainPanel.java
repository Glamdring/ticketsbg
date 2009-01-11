package com.tickets.client.components;

import com.google.gwt.user.client.ui.AbsolutePanel;
import com.google.gwt.user.client.ui.Widget;

public class MainPanel extends AbsolutePanel {

    @Override
    public void add(Widget w) {
        clear();
        super.add(w);
    }
}
