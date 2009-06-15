package com.tickets.services;

import com.tickets.exceptions.UserException;
import com.tickets.model.User;

/**
 * Interface for handling users.
 *
 * @author Bozhidar Bozhanov
 *
 */
public interface UserService extends Service<User> {

    /**
     * Registers a new user
     *
     * @param user
     * @return user
     */
    User register(User user) throws UserException;


    /**
     * Logs a user in
     * @param username
     * @param password
     * @param isStaff
     *
     * @return the logged user
     */
    User login(String username,
            char[] password,
            boolean passwordAlreadyHashed)
        throws UserException;

}
