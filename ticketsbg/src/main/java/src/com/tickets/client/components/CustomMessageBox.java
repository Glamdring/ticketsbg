package com.tickets.client.components;

import com.extjs.gxt.ui.client.event.Listener;
import com.extjs.gxt.ui.client.event.WindowEvent;
import com.extjs.gxt.ui.client.widget.MessageBox;

public class CustomMessageBox extends MessageBox {

    public static MessageBox error(String title, String message, Listener<WindowEvent> callback) {
        MessageBox box = new MessageBox();
        box.setType(MessageBoxType.ALERT);
        box.setIcon(ERROR);
        box.setMessage(message);
        box.setTitle(title);
        box.addCallback(callback);
        box.show();
        box.setButtons(OK);
        return box;
    }

}
