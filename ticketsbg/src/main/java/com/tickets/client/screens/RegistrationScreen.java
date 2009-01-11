package com.tickets.client.screens;

import java.util.List;

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
import com.tickets.client.services.UserService;
import com.tickets.client.services.UserServiceAsync;

public class RegistrationScreen extends BaseFormPanel {

     private static final String USER_SERVLET_NAME = "/handler/userService";
        public RegistrationScreen() {
            setLabelWidth(120);
            setLabelSeparator(":");
            final UserServiceAsync service = GWT.create(UserService.class);
            ServiceDefTarget endPoint = (ServiceDefTarget) service;
            endPoint.setServiceEntryPoint(USER_SERVLET_NAME);

            final TextField<String> usernameField = new TextField<String>();
            usernameField.setFieldLabel(Messages.m.username());
            usernameField.setAllowBlank(false);

            final TextField<String> passwordField = new TextField<String>();
            passwordField.setFieldLabel(Messages.m.password());
            passwordField.setAllowBlank(false);
            passwordField.setPassword(true);

            final TextField<String> passwordRepeatField = new TextField<String>();
            passwordRepeatField.setFieldLabel(Messages.m.repeatPassword());
            passwordRepeatField.setAllowBlank(false);
            passwordRepeatField.setPassword(true);
            passwordRepeatField.setValidateOnBlur(false);

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

            final TextField<String> nameField = new TextField<String>();
            nameField.setFieldLabel(Messages.m.username());
            nameField.setAllowBlank(false);

            Button registerButton = new Button(Messages.m.login());
            registerButton.addSelectionListener(new SelectionListener<ButtonEvent>() {
                @Override
                public void componentSelected(ButtonEvent ce) {

                    AsyncCallback<Boolean> callback = new AsyncCallback<Boolean>() {
                          public void onSuccess(Boolean result) {
                              MessageBox.alert("asd", result.toString(), null);
                          }

                          public void onFailure(Throwable caught) {
                              if (caught instanceof UserException) {
                                  CustomMessageBox.error(Messages.m.loginFailed(),
                                        ((UserException) caught).getMessage(), null);
                              } else {
                                  GWT.log("error", caught);
                              }
                          };
                    };

                    if (validateForm()) {
                        service.login(usernameField.getValue(),
                            passwordField.getValue().toCharArray(), true, false, callback);
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
        }
}
