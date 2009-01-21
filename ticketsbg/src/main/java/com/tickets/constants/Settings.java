package com.tickets.constants;

import java.io.InputStream;
import java.util.Properties;


public class Settings {

    private static Properties settings;

    public static String getValue(String key) {
        if (settings == null)
            load();

        try {
            String setting = settings.getProperty(key);
            if (setting == null)
                setting = key;

            return setting;
        } catch (Exception ex) {
            return key;
        }
    }

    private static void load() {
        try {
            InputStream is = Messages.class
                    .getResourceAsStream("/settings.properties");
            settings = new Properties();
            settings.load(is);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
}
