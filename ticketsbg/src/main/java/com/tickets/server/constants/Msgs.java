package com.tickets.server.constants;

import java.io.InputStream;
import java.text.MessageFormat;
import java.util.Properties;

import com.tickets.client.constants.Messages;

public class Msgs {
    private static Properties messages;

    public static String getString(String key, Object... args) {
        if (messages == null)
            load();

        try {
            String msg = messages.getProperty(key);
            if (msg == null)
                msg = key;

            MessageFormat format = new MessageFormat(msg);

            return format.format(args);
        } catch (Exception ex) {
            return key;
        }
    }

    private static void load() {
        try {
            InputStream is = Messages.class
                    .getResourceAsStream("/msgs.properties");
            messages = new Properties();
            messages.load(is);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
}
