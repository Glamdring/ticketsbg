package com.tickets.server.services.impl;

import org.springframework.stereotype.Service;

import com.tickets.model.User;

@Service("userService")
public class UserService extends BaseService<User>{

    public User saveUser(User user){
        return getDao().persist(user);
    }
}