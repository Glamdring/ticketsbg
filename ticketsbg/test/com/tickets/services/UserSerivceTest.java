package com.tickets.services;

import java.io.File;

import org.junit.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.test.AbstractDependencyInjectionSpringContextTests;

import com.tickets.dao.Dao;
import com.tickets.model.User;
import com.tickets.server.services.impl.UserService;
import com.tickets.utils.SpringContext;

public class UserSerivceTest extends AbstractDependencyInjectionSpringContextTests {

    @Test
    public void testSaveUser() throws Exception {

        String path = new File("").getAbsolutePath() + "/src/";
        SpringContext.init(path);
        ApplicationContext a = SpringContext.getContext();


        UserService usrv = (UserService) a.getBean("userService");
        Dao<User> dao = (Dao<User>) a.getBean("dao");
        Object o = a.getBean("dao");

        assertTrue(usrv.getDao() != null);

        User user = new User();
        user.setUsername("Bozhidar Bozhanov");
        //System.out.println(usrv.saveUser(user));
    }
}
