package com.tickets.client.components;

import java.util.List;

import com.extjs.gxt.ui.client.binding.FormBinding;
import com.extjs.gxt.ui.client.data.BeanModel;
import com.extjs.gxt.ui.client.data.ModelData;
import com.extjs.gxt.ui.client.widget.form.Field;
import com.extjs.gxt.ui.client.widget.form.FormPanel;
import com.extjs.gxt.ui.client.widget.form.TextField;
import com.extjs.gxt.ui.client.widget.form.TextField.TextFieldMessages;
import com.tickets.client.model.binding.ReflectionFieldBinding;
import com.tickets.client.utils.EntityBeanModel;

public class BaseFormPanel<E> extends FormPanel {

    private FormBinding formBindings;

    protected void setCustomMessages() {
        TextFieldMessages msgs = new TextField().new TextFieldMessages();

        List<Field> fields = getFields();
        for (Field field : fields) {
            field.setMessages(msgs);
        }
    }

    protected void setValidateOnBlur(boolean validateOnBlur) {
        List<Field> fields = getFields();
        for (Field field : fields) {
            field.setValidateOnBlur(validateOnBlur);
        }
    }

    protected void bindForm() {
        if (getEntity() == null)
            return;

        formBindings = new FormBinding(this);
        bind();
    }

    private void bind() {
        for (Field f : getFields()) {
            String name = f.getName();
            if (name != null) {
                ReflectionFieldBinding b = new ReflectionFieldBinding(f, f.getName());
                b.setEntity(getEntity());
                formBindings.addFieldBinding(b);
            }
        }
    }

    protected E getEntity() {
        return null;
    }

    protected BeanModel getBindingModel() {
        BeanModel model = new EntityBeanModel(getEntity());
        return model;
    }
}
