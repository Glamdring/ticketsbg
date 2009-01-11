package com.tickets.server.services;

import javax.annotation.Resource;

import org.apache.log4j.Logger;

import com.tickets.server.dao.Dao;

/**
 * Base class for all services.
 *
 * @author Bozhidar Bozhanov
 *
 * @param <E> the main entity class used by this service.
 * Note that the service can use other classes as well, by supplying them
 * as arguments to the DAO
 */
public class BaseService<E> {

    protected static Logger log = Logger.getLogger(BaseService.class);

    @Resource(name="dao")
    private Dao<E> dao;

    public void setDao(Dao<E> dao){
        this.dao = dao;
    }

    public Dao<E> getDao(){
        return dao;
    }
}
