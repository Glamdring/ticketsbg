package com.tickets.controllers.converters;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.ConverterException;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

@Component("timeToMinutesConverter")
@Scope("singleton")
public class TimeToMinutesConverter implements Converter {

    @Override
    public Object getAsObject(FacesContext ctx, UIComponent c, String value)
            throws ConverterException {

        if (value == null) {
            return 0;
        }

        String[] parts = value.split("\\:");
        int result = 0;

        if (parts.length == 2) {
            result = Integer.parseInt(parts[0]) * 60 + Integer.parseInt(parts[1]);
        } else {
            if (parts[0].length() == 0) {
                return 0;
            }
            result = Integer.parseInt(parts[0]);
        }

        return result;
    }

    @Override
    public String getAsString(FacesContext ctx, UIComponent c, Object value)
            throws ConverterException {

        if (value == null) {
            return "0";
        }

        int totalMinutes = (Integer) value;

        int hours = totalMinutes / 60;

        if (hours == 0) {
            return totalMinutes + "";
        }
        int minutes = totalMinutes % 60;

        return  hours + ":" + (minutes < 10 ? "0" + minutes : minutes);
    }
}
