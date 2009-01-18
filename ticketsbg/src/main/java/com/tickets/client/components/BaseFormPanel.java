package com.tickets.client.components;

import java.util.List;

import com.extjs.gxt.ui.client.binding.FieldBinding;
import com.extjs.gxt.ui.client.binding.FormBinding;
import com.extjs.gxt.ui.client.data.BeanModel;
import com.extjs.gxt.ui.client.widget.form.Field;
import com.extjs.gxt.ui.client.widget.form.FormPanel;
import com.extjs.gxt.ui.client.widget.form.TextField;
import com.extjs.gxt.ui.client.widget.form.TextField.TextFieldMessages;

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
        formBindings = new FormBinding(this);
        bind();
    }

    private void bind() {
        for (Field f : getFields()) {
            String name = f.getName();
            if (name != null) {
                FieldBinding b = new FieldBinding(f, f.getName());
                b.bind(getModel());
                formBindings.addFieldBinding(b);
            }
        }
    }
}
