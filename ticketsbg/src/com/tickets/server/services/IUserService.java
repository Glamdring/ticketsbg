package com.tickets.server.services;

import com.tickets.model.User;

public interface IUserService {

    /**
     * Registers a new user
     *
     * @param user
     */
    public void register(User user);

}
