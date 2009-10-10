package com.tickets.controllers.converters;

import java.math.BigDecimal;
import java.text.DecimalFormat;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.ConverterException;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

@Component("currencyConverter")
@Scope("singleton")
public class CurrencyConverter implements Converter {

    @Override
    public Object getAsObject(FacesContext arg0, UIComponent arg1, String value)
            throws ConverterException {
        return new BigDecimal(stripCurrencyInformation(value));
    }

    @Override
    public String getAsString(FacesContext arg0, UIComponent arg1, Object value)
            throws ConverterException {
        return addCurrencyInformation((BigDecimal) value);
    }

    //TODO : handle currencies. Currently only lev supported, and hard-coded

    private String addCurrencyInformation(BigDecimal value) {
        if (value == null) {
            value = BigDecimal.ZERO;
        }
        DecimalFormat df = new DecimalFormat();
        df.setMaximumFractionDigits(2);
        df.setMinimumFractionDigits(2);
        return df.format(value) + " лв.";
    }

    private String stripCurrencyInformation(String value) {
        return value.replace(" лв.", "");
    }
}
