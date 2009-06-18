package com.tickets.controllers.converters;

import java.lang.reflect.Field;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.ConverterException;
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

    @SuppressWarnings("unchecked")
    @Override
    public Object getAsObject(FacesContext ctx, UIComponent component, String str)
            throws ConverterException {

        try {
            String[] parts = str.split(":");
            Class clazz = Class.forName(parts[0]);
            Field[] fields = clazz.getDeclaredFields();
            for (Field field : fields) {
                if (field.isAnnotationPresent(Id.class)) {
                    if (field.getType().equals(Integer.class) || field.getType().getName().equals("int")
                            || field.getType().equals(Long.class) || field.getType().getName().equals("long")) {
                        return dao.getById(clazz, Integer.parseInt(parts[1]));
                    }
                }
            }
            return null;
        } catch (Exception ex) {
            ex.printStackTrace();
            return null;
        }
    }

    @Override
    public String getAsString(FacesContext ctx, UIComponent component, Object obj)
            throws ConverterException {
        try {
            Class clazz = obj.getClass();
            Field[] fields = clazz.getDeclaredFields();
            for (Field field : fields) {
                if (field.isAnnotationPresent(Id.class)) {
                    field.setAccessible(true);
                    return clazz.getName() + ":" + field.get(obj).toString();
                }
            }
            return "";
        } catch (Exception ex) {
            ex.printStackTrace();
            return "";
        }
    }

}
