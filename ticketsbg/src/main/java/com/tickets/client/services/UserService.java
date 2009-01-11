package com.tickets.client.services;

import com.google.gwt.user.client.rpc.RemoteService;
import com.tickets.client.exceptions.UserException;
import com.tickets.client.model.User;

/**
 * Interface for handling users.
 *
 * @author Bozhidar Bozhanov
 *
 */
public interface UserService extends RemoteService {

    /**
     * Registers a new user
     *
     * @param user
     */
    void register(User user) throws UserException;


    /**
     * Logs a user in
     * @param username
     * @param password
     * @param isStaff
     *
     * @return the id of the user
     */
    int login(String username,
            char[] password,
            boolean isStaff,
            boolean passwordAlreadyHashed)
        throws UserException;

}
