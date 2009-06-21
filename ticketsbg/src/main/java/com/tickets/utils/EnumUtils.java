package com.tickets.utils;

import java.lang.reflect.Method;
import java.util.List;

import javax.faces.model.SelectItem;

import com.tickets.constants.Messages;

public class EnumUtils {

    public static void formSelectItems(Class<? extends Enum> clazz, List<SelectItem> targetList) {
        try {
            Method method = clazz.getDeclaredMethod("values");
            Enum[] values = (Enum[]) method.invoke(null);
            for (Enum en : values) {
                SelectItem si = new SelectItem(en, Messages.getString(en.toString()));
                targetList.add(si);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
}
