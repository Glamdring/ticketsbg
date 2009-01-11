package com.tickets.client.services;

import com.google.gwt.user.client.rpc.AsyncCallback;
import com.tickets.client.model.User;

/**
 * Interface for handling users.
 *
 * @author Bozhidar Bozhanov
 *
 */
public interface UserServiceAsync {

    /**
     * Registers a new user
     *
     * @param user
     */
    void register(User user, AsyncCallback<Void> callback);


    /**
     * Logs a user in
     * @param username
     * @param password
     * @param isStaff
     */
    void login(String username,
            char[] password,
            boolean isStaff,
            AsyncCallback<Boolean> callback);

}
