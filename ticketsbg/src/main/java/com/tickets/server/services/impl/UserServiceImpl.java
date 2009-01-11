package com.tickets.server.services.impl;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Service;

import com.tickets.client.exceptions.UserException;
import com.tickets.client.model.User;
import com.tickets.client.services.UserService;

@Service("userService")
public class UserServiceImpl implements UserService {

    private static Logger log = Logger.getLogger(UserServiceImpl.class);
    @Override
    public void register(User user) {
        String email = user.getEmail();
    }

    @Override
    public boolean login(String username, char[] password, boolean isStaff) throws UserException {
        log.info(username + " : " + new String(password));
        System.out.println(username + " : " + new String(password));
        if (true) throw new UserException("asd");
        return true;
    }

    private static String EMAIL_HOST = "localhost";
    private static String FROM = "admin@ticketsbg.com";

}