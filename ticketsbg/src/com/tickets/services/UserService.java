package com.tickets.services;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.tickets.dao.GenericDao;
import com.tickets.dao.interfaces.Dao;
import com.tickets.model.User;

@Service("userService")
public class UserService {

    Dao<User> dao;

    @Resource(name="dao")
    public void setData(GenericDao<User> dao){
        this.dao = dao;
    }

    public Dao<User> getDao(){
        return dao;
    }

    public User saveUser(User user){
        return getDao().save(user);
    }
}