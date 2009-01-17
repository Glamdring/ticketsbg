package com.tickets.client.screens;

import com.allen_sauer.gwt.log.client.Log;
import com.extjs.gxt.ui.client.event.ButtonEvent;
import com.extjs.gxt.ui.client.event.SelectionListener;
import com.extjs.gxt.ui.client.widget.MessageBox;
import com.extjs.gxt.ui.client.widget.button.Button;
import com.extjs.gxt.ui.client.widget.form.Field;
import com.extjs.gxt.ui.client.widget.form.TextField;
import com.extjs.gxt.ui.client.widget.form.Validator;
import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.rpc.ServiceDefTarget;
import com.tickets.client.components.BaseFormPanel;
import com.tickets.client.components.CustomMessageBox;
import com.tickets.client.constants.Messages;
import com.tickets.client.exceptions.UserException;
import com.tickets.client.model.User;
import com.tickets.client.services.UserService;
import com.tickets.client.services.UserServiceAsync;

public class RegistrationScreen extends BaseFormPanel<User> {

    private static final String USER_SERVLET_NAME = "/handler/userService";

    User user = new User();

    public RegistrationScreen() {
        setLabelWidth(120);
        setLabelSeparator(":");
        final UserServiceAsync service = GWT.create(UserService.class);
        ServiceDefTarget endPoint = (ServiceDefTarget) service;
        endPoint.setServiceEntryPoint(USER_SERVLET_NAME);

        final TextField<String> usernameField = new TextField<String>();
        usernameField.setFieldLabel(Messages.m.username());
        usernameField.setAllowBlank(false);
        usernameField.setName("username");

        final TextField<String> passwordField = new TextField<String>();
        passwordField.setFieldLabel(Messages.m.password());
        passwordField.setAllowBlank(false);
        passwordField.setPassword(true);
        passwordField.setName("password");

        final TextField<String> passwordRepeatField = new TextField<String>();
        passwordRepeatField.setFieldLabel(Messages.m.repeatPassword());
        passwordRepeatField.setAllowBlank(false);
        passwordRepeatField.setPassword(true);
        passwordRepeatField.setValidateOnBlur(false);
        passwordRepeatField.setName("repeatPassword");

        final TextField<String> emailField = new TextField<String>();
        emailField.setFieldLabel(Messages.m.email());
        emailField.setAllowBlank(false);
        emailField.setValidator(new Validator() {
            public String validate(Field field, String value) {
                if (value.indexOf("@") == -1)
                    return Messages.m.emailInvalid();

                return null;
            }
        });
        emailField.setName("email");

        final TextField<String> nameField = new TextField<String>();
        nameField.setFieldLabel(Messages.m.name());
        nameField.setAllowBlank(false);
        nameField.setName("name");

        Button registerButton = new Button(Messages.m.register());
        registerButton.addSelectionListener(new SelectionListener<ButtonEvent>() {
            @Override
            public void componentSelected(ButtonEvent ce) {

                AsyncCallback<Void> callback = new AsyncCallback<Void>() {
                    public void onSuccess(Void result) {
                        MessageBox.alert(Messages.m.successfulRegistration(),
                                Messages.m.successfulRegistration(), null);
                    }

                    public void onFailure(Throwable caught) {
                        if (caught instanceof UserException) {
                            UserException ue = ((UserException) caught);
                            String message = "";
                            if (ue.equals(UserException.EMAIL_PROBLEM))
                                message = Messages.m.emailProblem();

                            CustomMessageBox.error(Messages.m.registrationFailed(),
                                    message, null);
                        } else {
                            Log.error("error", caught);
                        }
                    };
                };

                if (isValid()) {
                    service.register(user, callback);
                }
            }
        });

        add(usernameField);
        add(passwordField);
        add(passwordRepeatField);
        add(emailField);
        add(nameField);
        add(registerButton);

        setCustomMessages();
        setValidateOnBlur(false);
        bindForm();
    }

    @Override
    protected User getEntity() {
        return user;
    }
}
