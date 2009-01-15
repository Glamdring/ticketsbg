package com.tickets.client.utils;

import com.extjs.gxt.ui.client.data.BeanModel;

public class EntityBeanModel extends BeanModel {

    public EntityBeanModel(Object entity) {
        setBean(entity);
    }

    public <X> X set(String property, X value) {
        return super.set(property, value);
      }
}
