package com.tickets.client.constants;

import com.google.gwt.core.client.GWT;
import com.google.gwt.i18n.client.Constants;

public interface Messages extends Constants {
    public static Messages m = (Messages) GWT.create(Messages.class);

    String tickets();
    String sale();
    String check();

    String timeTable();
    String view();
    String courses();
}
