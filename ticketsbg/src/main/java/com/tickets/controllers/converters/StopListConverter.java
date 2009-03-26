package com.tickets.controllers.converters;

import javax.annotation.Resource;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.tickets.controllers.RouteController;
import com.tickets.model.Stop;

@Component("stopListConverter")
@Scope("request")
public class StopListConverter implements Converter {

    @Resource
    private RouteController routeController;

    public Object getAsObject(FacesContext ctx, UIComponent c, String str) {
        try {
            return routeController.getRoute().getStops().get(Integer.parseInt(str) - 1);
        } catch (Exception ex) {
            ex.printStackTrace();
            return null;
        }
    }
    public String getAsString(FacesContext ctx, UIComponent c, Object obj) {
        if (obj == null) return null;
        return ((Stop) obj).getIdx() + "";
    }
}
