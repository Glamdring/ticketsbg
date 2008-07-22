package com.tickets.client.services;

import com.google.gwt.user.client.rpc.RemoteService;
import com.tickets.client.model.User;

/**
 * Interface for handling users.
 *
 * @author Bozhidar Bozhanov
 *
 */
public interface IUserService extends RemoteService {

    /**
     * Registers a new user
     *
     * @param user
     */
    public void register(User user);

}
