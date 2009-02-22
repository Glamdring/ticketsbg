package com.tickets.controllers.converters;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.ConverterException;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

@Component("booleanConverter")
@Scope("singleton")
public class BooleanConverter implements Converter {

    @Override
    public Object getAsObject(FacesContext ctx, UIComponent c, String value)
            throws ConverterException {
        return Boolean.getBoolean(value);
    }

    @Override
    public String getAsString(FacesContext ctx, UIComponent c, Object value)
            throws ConverterException {

        if (value == null)
            return "false";

        return value.toString();
    }
}
