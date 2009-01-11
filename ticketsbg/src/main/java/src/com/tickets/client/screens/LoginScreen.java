package com.tickets.client.screens;

import com.extjs.gxt.ui.client.event.ButtonEvent;
import com.extjs.gxt.ui.client.event.SelectionListener;
import com.extjs.gxt.ui.client.widget.MessageBox;
import com.extjs.gxt.ui.client.widget.button.Button;
import com.extjs.gxt.ui.client.widget.form.FormPanel;
import com.extjs.gxt.ui.client.widget.form.TextField;
import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.rpc.ServiceDefTarget;
import com.tickets.client.components.CustomMessageBox;
import com.tickets.client.constants.Messages;
import com.tickets.client.exceptions.UserException;
import com.tickets.client.services.UserService;
import com.tickets.client.services.UserServiceAsync;

public class LoginScreen extends FormPanel {

    private static final String USER_SERVLET_NAME = "userService";
    public LoginScreen() {
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

        Button loginButton = new Button(Messages.m.login());
        loginButton.addSelectionListener(new SelectionListener<ButtonEvent>() {
            @Override
            public void componentSelected(ButtonEvent ce) {

                final long start = System.currentTimeMillis();
                AsyncCallback<Boolean> callback = new AsyncCallback<Boolean>() {
                      public void onSuccess(Boolean result) {
                          MessageBox.alert("asd", result.toString(), null);
                      }

                      public void onFailure(Throwable caught) {
                          GWT.log("" + (System.currentTimeMillis() - start), null);
                          if (caught instanceof UserException) {
                              CustomMessageBox.error(Messages.m.loginFailed(),
                                    ((UserException) caught).getMessage(), null);
                          } else {
                              GWT.log("error", caught);
                          }
                      };
                };

                service.login(usernameField.getValue(),
                        passwordField.getValue().toCharArray(), true, callback);

            }
        });

        add(usernameField);
        add(passwordField);
        add(loginButton);
    }
}
