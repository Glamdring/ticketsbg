package com.tickets.server.services.impl;

import javax.annotation.Resource;

import org.apache.log4j.Logger;

import com.google.gwt.user.server.rpc.RemoteServiceServlet;
import com.tickets.dao.Dao;

/**
 * Base class for all services. Extends RemoteServiceSerlet for handling GWT RPC
 * Other clients can also use the same services, just ignoring the fact
 * they extend a Servlet class. The alternative is having servlet wrappers,
 * which is not preferable
 *
 * @author Bozhidar Bozhanov
 *
 * @param <E> the main entity class used by this service.
 * Note that the service can use other classes as well, by supplying them
 * as arguments to the DAO
 */
public class BaseService<E> extends RemoteServiceServlet {

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
