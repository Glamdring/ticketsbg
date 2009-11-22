package com.tickets.services;

import java.util.List;

import com.tickets.exceptions.UserException;
import com.tickets.model.Firm;
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


    /**
     * First salts and then hashes the provided password
     *
     * @param password
     * @return the salted and hashed password
     */
    String saltAndHashPassword(String password);

    /**
     * Checks whether the argument is a hash, or a normal password
     *
     * @param password
     * @return true if the argument is a hash
     */
    boolean isHash(String password);


    /**
     * Lists all internal users assigned to the specified firm
     *
     * @param firm
     * @return list of users
     */
    List<User> fetchUsers(Firm firm);


    /**
     * Fetches all users that are external to the firm (those
     * assigned to agents)
     *
     * @param firm
     * @return list of useres
     */
    List<User> fetchAgentsUsers(Firm firm);


    /**
     * Change the password of the specified user
     * @param loggedUserHolder
     * @param newPassword
     */
    void changePassword(User user, String newPassword);


    /**
     * Activates user
     *
     * @param code
     * @return user
     * @throws UserException
     */
    User activateUserWithCode(String code) throws UserException;


    /**
     * Sends a mail with tempPassword
     * @param email
     * @throws UserException
     */
    void sendTemporaryPassword(String email) throws UserException;

    /**
     * Checks whether a username is taken
     * @param username
     * @return true if the username is taken, false otherwise
     */
    boolean usernameExists(String username);


    /**
     * Checks whether a email is taken
     * @param email
     * @return true if the email is taken, false otherwise
     */
    boolean emailExists(String email);


    /**
     * Creates an initial administrator user
     */
    void createInitialUser();

}
