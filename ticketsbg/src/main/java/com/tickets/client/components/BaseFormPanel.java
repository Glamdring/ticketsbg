package com.tickets.client.components;

import java.util.List;

import com.extjs.gxt.ui.client.widget.form.Field;
import com.extjs.gxt.ui.client.widget.form.FormPanel;
import com.extjs.gxt.ui.client.widget.form.TextField;
import com.extjs.gxt.ui.client.widget.form.TextField.TextFieldMessages;
import com.tickets.client.constants.Messages;

public class BaseFormPanel extends FormPanel {

    protected void setCustomMessages() {
        TextFieldMessages msgs = new TextField().new TextFieldMessages();

        List<Field> fields = getFields();
        for (Field field : fields) {
            field.setMessages(msgs);
        }
    }

    protected boolean validateForm() {
        List<Field> fields = getFields();
        boolean valid = true;
        for (Field field : fields) {
            if (field.validate() == false)
                valid = false;
        }
        return valid;
    }

    protected void setValidateOnBlur(boolean validateOnBlur) {
        List<Field> fields = getFields();
        for (Field field : fields) {
            field.setValidateOnBlur(validateOnBlur);
        }
    }

}
