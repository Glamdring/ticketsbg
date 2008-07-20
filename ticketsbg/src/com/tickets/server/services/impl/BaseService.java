package com.tickets.server.services.impl;

import javax.annotation.Resource;

import com.tickets.dao.Dao;

public class BaseService<E> {
    Dao<E> dao;

    @Resource(name="dao")
    public void setDao(Dao<E> dao){
        this.dao = dao;
    }

    public Dao<E> getDao(){
        return dao;
    }
}
