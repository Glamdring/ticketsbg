package com.tickets.client.screens;

import com.extjs.gxt.ui.client.event.ButtonEvent;
import com.extjs.gxt.ui.client.event.SelectionListener;
import com.extjs.gxt.ui.client.widget.MessageBox;
import com.extjs.gxt.ui.client.widget.button.Button;
import com.extjs.gxt.ui.client.widget.form.TextField;
import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.rpc.ServiceDefTarget;
import com.google.gwt.user.client.ui.ClickListener;
import com.google.gwt.user.client.ui.Hyperlink;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.Widget;
import com.tickets.client.components.BaseFormPanel;
import com.tickets.client.components.CustomMessageBox;
import com.tickets.client.constants.Messages;
import com.tickets.client.exceptions.UserException;
import com.tickets.client.services.UserService;
import com.tickets.client.services.UserServiceAsync;

public class LoginScreen extends BaseFormPanel {

    private static final String USER_SERVLET_NAME = "/handler/userService";
    public LoginScreen() {
        setLabelSeparator(":");
        final UserServiceAsync service = GWT.create(UserService.class);
        ServiceDefTarget endPoint = (ServiceDefTarget) service;
        endPoint.setServiceEntryPoint(USER_SERVLET_NAME);

        final TextField<String> usernameField = new TextField<String>();
        usernameField.setFieldLabel(Messages.m.email());
        usernameField.setAllowBlank(false);

        final TextField<String> passwordField = new TextField<String>();
        passwordField.setFieldLabel(Messages.m.password());
        passwordField.setAllowBlank(false);
        passwordField.setPassword(true);

        Button loginButton = new Button(Messages.m.login());
        loginButton.addSelectionListener(new SelectionListener<ButtonEvent>() {
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

        Hyperlink registerLink = new Hyperlink();
        registerLink.setStyleName("href");
        registerLink.setText(Messages.m.register());
        registerLink.addClickListener(new ClickListener() {
            public void onClick(Widget sender) {
                RootPanel.get().clear();
                RootPanel.get().add(new RegistrationScreen());
            }
        });

        Hyperlink forgottenPasswordLink = new Hyperlink();
        forgottenPasswordLink.setStyleName("href");
        forgottenPasswordLink.setText(Messages.m.forgottenPassword());
        registerLink.addClickListener(new ClickListener() {
            public void onClick(Widget sender) {
                // TODO Auto-generated method stub

            }
        });
        add(usernameField);
        add(passwordField);
        add(loginButton);
        add(registerLink);
        add(forgottenPasswordLink);

        setCustomMessages();
        setValidateOnBlur(false);
    }
}
