package com.tickets.controllers.converters;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.ConverterException;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

@Component("enumConverter")
@Scope("singleton")
public class EnumConverter implements Converter {

    @SuppressWarnings("unchecked")
    @Override
    public Object getAsObject(FacesContext facescontext,
            UIComponent uicomponent, String s) throws ConverterException {

        //Using dollar sign, because the entry sometimes has to be in a properties file
        String[] parts = s.split("\\$");
        try {
            return Enum.valueOf((Class<Enum>) Class.forName(parts[0]), parts[1]);
        } catch (Exception ex) {
            ex.printStackTrace();
            throw new ConverterException(ex);
        }
    }

    @Override
    public String getAsString(FacesContext facescontext,
            UIComponent uicomponent, Object obj) throws ConverterException {
        if (obj == null)
            return "";

        String stringRepresentation = obj.getClass().getName() + "$" + obj.toString();

        return stringRepresentation;
    }
}
