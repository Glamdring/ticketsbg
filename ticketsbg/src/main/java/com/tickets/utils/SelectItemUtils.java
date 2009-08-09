package com.tickets.utils;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.EnumSet;
import java.util.List;

import javax.faces.model.SelectItem;

import com.tickets.constants.Messages;
import com.tickets.model.Selectable;

public class SelectItemUtils {

    public static List<SelectItem> formSelectItems(
            List<? extends Selectable> original, boolean addEmptyElement) {
        int size = addEmptyElement ? original.size() + 1 : original.size();

        List<SelectItem> list = new ArrayList<SelectItem>(size);

        if (addEmptyElement) {
            list.add(new SelectItem(null, ""));
        }

        for (Selectable sl : original) {
            list.add(new SelectItem(sl, sl.getLabel()));
        }

        return list;
    }


    public static List<SelectItem> formSelectItems(
            List<? extends Selectable> original) {
        return formSelectItems(original, true);
    }

    public static List<SelectItem> formSelectItems(Class<? extends Enum> clazz) {
        return formSelectItems(clazz, null, null);
    }

    public static List<SelectItem> formSelectItems(Class<? extends Enum> clazz,
            Enum defaultValue) {
        return formSelectItems(clazz, null, defaultValue);
    }

    public static List<SelectItem> formSelectItems(Class<? extends Enum> clazz,
            EnumSet<? extends Enum> exclusions) {
        return formSelectItems(clazz, exclusions, null);
    }

    public static List<SelectItem> formSelectItems(Class<? extends Enum> clazz,
            EnumSet<? extends Enum> exclusions, Enum defaultValue) {
        try {
            Method method = clazz.getDeclaredMethod("values");
            Enum[] values = (Enum[]) method.invoke(null);
            List<SelectItem> targetList = new ArrayList<SelectItem>(
                    values.length);

            if (defaultValue != null) {
                targetList.add(new SelectItem(defaultValue, Messages
                        .getString(defaultValue.toString())));
            }
            for (Enum en : values) {
                // if this is the default value, skip, because it's alread added
                if (en == defaultValue) {
                    continue;
                }
                if (exclusions == null || !exclusions.contains(en)) {
                    SelectItem si = new SelectItem(en, Messages.getString(en
                            .toString()));
                    targetList.add(si);
                }
            }
            return targetList;
        } catch (Exception ex) {
            ex.printStackTrace();
            return new ArrayList<SelectItem>();
        }
    }
}
