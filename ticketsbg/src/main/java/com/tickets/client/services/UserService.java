package com.tickets.client.services;

import com.tickets.exceptions.UserException;
import com.tickets.model.User;

/**
 * Interface for handling users.
 *
 * @author Bozhidar Bozhanov
 *
 */
public interface UserService extends BaseClientService<User> {

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
