package com.tickets.controllers.converters;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.ConverterException;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.tickets.controllers.valueobjects.Seat;

@Component("seatConverter")
@Scope("singleton")
public class SeatConverter implements Converter {

    @Override
    public Object getAsObject(FacesContext facescontext,
            UIComponent uicomponent, String s) throws ConverterException {
        String[] parts = s.split(":");
        Seat seat = new Seat(Integer.parseInt(parts[0]));
        seat.setVacant(Boolean.parseBoolean(parts[1]));
        seat.setId(Integer.parseInt(parts[2]));
        return seat;
    }

    @Override
    public String getAsString(FacesContext facescontext,
            UIComponent uicomponent, Object obj) throws ConverterException {
        Seat seat = (Seat) obj;
        String result = seat.getNumber() + ":" + seat.isVacant() + ":" + seat.getId();
        return result;
    }

}
