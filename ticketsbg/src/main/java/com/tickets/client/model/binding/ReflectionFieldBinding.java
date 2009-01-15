package com.tickets.client.model.binding;

import java.beans.BeanInfo;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;

import com.extjs.gxt.ui.client.binding.FieldBinding;
import com.extjs.gxt.ui.client.data.BaseModelData;
import com.extjs.gxt.ui.client.store.Record;
import com.extjs.gxt.ui.client.widget.form.Field;
import com.tickets.client.model.Day;
import com.tickets.client.model.Firm;
import com.tickets.client.model.Route;
import com.tickets.client.model.RouteDay;
import com.tickets.client.model.RouteHour;
import com.tickets.client.model.Run;
import com.tickets.client.model.User;
import com.tickets.client.model.UsersHistory;

public class ReflectionFieldBinding extends FieldBinding {

    public ReflectionFieldBinding(Field field, String property) {
        super(field, property);
    }

    private Object entity;

    public void setEntity(Object entity) {
        this.entity = entity;
        bind(new BaseModelData()); //dummy
    }

    public Object getEntity() {
        return entity;
    }

    @Override
    public void updateModel() {
        Object value = field.getValue();
        if (getConvertor() != null) {
          value = getConvertor().convertFieldValue(value);
        }
        //TODO
        if (getStore() != null) {
          Record r = getStore().getRecord(model);
          if (r != null) {
            r.set(property, value);
          }
        } else {
            try {
                BeanInfo info = Introspector.getBeanInfo(entity.getClass());
                for (PropertyDescriptor pd : info.getPropertyDescriptors()) {
                    if (pd.getName().equals(getProperty()))
                        pd.getWriteMethod().invoke(entity, value);
                }
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
    }

}
