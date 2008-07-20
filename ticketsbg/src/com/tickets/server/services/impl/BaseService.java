package com.tickets.server.services.impl;

import javax.annotation.Resource;

import org.apache.log4j.Logger;

import com.tickets.dao.Dao;

public class BaseService<E> {

    protected static Logger log = Logger.getLogger(BaseService.class);

    Dao<E> dao;

    @Resource(name="dao")
    public void setDao(Dao<E> dao){
        this.dao = dao;
    }

    public Dao<E> getDao(){
        return dao;
    }
}
