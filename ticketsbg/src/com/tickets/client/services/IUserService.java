package com.tickets.client.services;

import com.tickets.client.model.User;

public interface IUserService {

    /**
     * Registers a new user
     *
     * @param user
     */
    public void register(User user);

}
