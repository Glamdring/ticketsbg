package com.tickets.controllers;

import java.util.Locale;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import com.tickets.utils.GeneralUtils;

@Controller("localeController")
@Scope("session")
public class LocaleController extends BaseController {

    private Locale locale = GeneralUtils.getDefaultLocale();

    private String selectedLanguage;

    public void changeLocale() {
        setLocale(new Locale(selectedLanguage));
    }

    public Locale getLocale() {
        return locale;
    }

    public void setLocale(Locale locale) {
        this.locale = locale;
    }

    public String getSelectedLanguage() {
        return selectedLanguage;
    }

    public void setSelectedLanguage(String selectedLanguage) {
        this.selectedLanguage = selectedLanguage;
    }

    public String getLocaleWithCountry() {
        if (locale.getLanguage().equals("bg")) {
            return "bg_BG";
        }

        if (locale.getLanguage().equals("en")) {
            return "en_GB";
        }

        return null;
    }
}
