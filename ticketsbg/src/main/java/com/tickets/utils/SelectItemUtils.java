package com.tickets.utils;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.EnumSet;
import java.util.List;

import javax.faces.model.SelectItem;

import com.tickets.constants.Messages;
import com.tickets.model.Selectable;

public class SelectItemUtils {

    public static List<SelectItem> formSelectItems(List<? extends Selectable> original, boolean addEmptyElement) {
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

    public static List<SelectItem> formSelectItems(List<? extends Selectable> original) {
        return formSelectItems(original, true);
    }

    public static void formSelectItems(Class<? extends Enum> clazz, List<SelectItem> targetList) {
        formSelectItems(clazz, targetList, null);
    }

    public static void formSelectItems(Class<? extends Enum> clazz, List<SelectItem> targetList, EnumSet<? extends Enum> exclusions) {
        try {
            Method method = clazz.getDeclaredMethod("values");
            Enum[] values = (Enum[]) method.invoke(null);
            for (Enum en : values) {
                if (exclusions == null || !exclusions.contains(en)) {
                    SelectItem si = new SelectItem(en, Messages.getString(en.toString()));
                    targetList.add(si);
                }
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
}

