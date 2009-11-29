package com.tickets.controllers.converters;

import java.lang.reflect.Field;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.persistence.Id;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.tickets.dao.Dao;


@Component("entityConverter")
@Scope("singleton")
public class EntityConverter implements Converter {

    @Autowired
    private Dao dao;

    @Override
    public Object getAsObject(FacesContext ctx, UIComponent component, String str) {

        try {
            String[] parts = str.split(":");
            Class<?> clazz = Class.forName(parts[0]);
            Field[] fields = clazz.getDeclaredFields();
            for (Field field : fields) {
                if (field.isAnnotationPresent(Id.class)
                    &&  (field.getType().equals(Integer.class)
                        || field.getType().getName().equals("int")
                        || field.getType().equals(Long.class)
                        || field.getType().getName().equals("long"))) {

                    Object obj = dao.getById(clazz, Integer.parseInt(parts[1]));
                    return obj;
                }
            }
            return null;
        } catch (Exception ex) {
            return null;
        }
    }

    @Override
    public String getAsString(FacesContext ctx, UIComponent component, Object obj) {
        if (obj == null) {
            return "";
        }

        try {
            Class clazz = obj.getClass();
            Field[] fields = clazz.getDeclaredFields();
            for (Field field : fields) {
                if (field.isAnnotationPresent(Id.class)) {
                    field.setAccessible(true);
                    String value = clazz.getName() + ":" + field.get(obj).toString();
                    return value;
                }
            }
            return "";
        } catch (Exception ex) {
            return "";
        }
    }

}
