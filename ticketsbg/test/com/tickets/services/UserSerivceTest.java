package com.tickets.services;

import org.junit.Test;
import org.springframework.beans.factory.xml.XmlBeanFactory;
import org.springframework.core.io.FileSystemResource;
import org.springframework.test.AbstractDependencyInjectionSpringContextTests;

import com.tickets.dao.GenericDao;
import com.tickets.model.User;
import com.tickets.server.services.impl.UserService;

public class UserSerivceTest extends AbstractDependencyInjectionSpringContextTests {

    @Test
    public void testSaveUser() throws Exception {

        XmlBeanFactory a = new XmlBeanFactory(new FileSystemResource("src/spring.xml"));

        UserService usrv = (UserService) a.getBean("userService");
        GenericDao<User> dao = (GenericDao<User>) a.getBean("dao");
        Object o = a.getBean("dao");

        //System.out.println(o.getClass());
        assertTrue(usrv.getDao() != null);

        User user = new User();
        user.setUsername("Bozhidar Bozhanov");
        System.out.println(usrv.saveUser(user));
    }
}
