package com.tickets.desktop.notifier;

import java.io.Serializable;
import java.text.MessageFormat;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.MissingResourceException;
import java.util.ResourceBundle;

public class Messages implements Serializable {
    private static Map<Locale, ResourceBundle> bundles = new HashMap<Locale, ResourceBundle>();
    private static final Locale DEFAULT_LOCALE = new Locale("bg");

    private static ClassLoader getCurrentClassLoader(Object defaultObject) {
        ClassLoader loader = Thread.currentThread().getContextClassLoader();
        if (loader == null) {
            loader = defaultObject.getClass().getClassLoader();
        }
        return loader;
    }

    public static String getString(String key, Object...params) {
        return getString(key, DEFAULT_LOCALE, params);
    }

    public static String getString(String key, Locale locale, Object...params) {

        if (!bundles.containsKey(locale))
            load(locale);

        String text = null;

        try {
            text = bundles.get(locale).getString(key);
        } catch (MissingResourceException e) {
            text = "?? " + key + " ??";
        }

        if (params != null) {
            MessageFormat mf = new MessageFormat(text, locale);
            text = mf.format(params);
        }
        return text;
    }

    private static void load(Locale locale) {
        bundles.put(locale, ResourceBundle.getBundle("com.tickets.desktop.notifier.messages", locale,
                getCurrentClassLoader(locale)));
    }
}
