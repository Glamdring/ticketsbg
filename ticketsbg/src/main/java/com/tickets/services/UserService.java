package com.tickets.services;

import java.util.List;

import com.tickets.exceptions.UserException;
import com.tickets.model.Firm;
import com.tickets.model.Role;
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
     * TODO DOC
     * @param userName
     * @return user
     */
    User findUserByUserName(String userName);

    /**
     * Checks whether the argument is a hash, or a normal password
     *
     * @param password
     * @return true if the argument is a hash
     */
    boolean isHash(String password);


    /**
     * Lists all users assigned to the specified administration
     *
     * @param administration
     * @return list of users
     */
    List<User> fetchUsers(Firm firm);


    List<Role> listNonAdminRoles();

}
