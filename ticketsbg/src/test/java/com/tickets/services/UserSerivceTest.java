package com.tickets.services;

import java.io.File;

import org.junit.Test;
import org.springframework.context.ApplicationContext;

import com.tickets.model.User;
import com.tickets.utils.SpringContext;

public class UserSerivceTest {

    @Test
    @SuppressWarnings({ "unused" })
    public void testSaveUser() throws Exception {

        String path = new File("").getAbsolutePath() + "/src/main/resources/";
        SpringContext.init(path);
        ApplicationContext a = SpringContext.getContext();


        UserService usrv = (UserService) a.getBean("userService");

        User user = new User();
        user.setUsername("Bozhidar Bozhanov");
        //System.out.println(usrv.saveUser(user));
    }
}
