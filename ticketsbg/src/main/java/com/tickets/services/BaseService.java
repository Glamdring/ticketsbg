package com.tickets.services;

import java.io.Serializable;
import java.util.List;

import javax.annotation.Resource;

import org.apache.log4j.Logger;

import com.tickets.dao.Dao;

/**
 * Base class for all services.
 *
 * @author Bozhidar Bozhanov
 *
 * @param <E> the main entity class used by this service.
 * Note that the service can use other classes as well, by supplying them
 * as arguments to the DAO
 */
public abstract class BaseService<E> implements Service<E> {

    protected static Logger log = Logger.getLogger(BaseService.class);

    @Resource(name="daoFactory")
    private Dao<E> dao;

    public void setDao(Dao<E> dao){
        this.dao = dao;
    }

    public Dao<E> getDao(){
        return dao;
    }

    public E save(E e) throws RuntimeException {
        try {
            return getDao().save(e);
        } catch (Exception ex) {
            log.error("Error saving", ex);
            return e;
        }
    }

    public <T> T get(Class<T> clazz, Serializable id) {
        T result = getDao().getById(clazz, id);
        if (result == null)
            try {
                result = clazz.newInstance();
            } catch (Exception ex) {
            }

        return result;
    }

    public void delete(E e) {
        getDao().delete(e);
    }

    public void delete(Class clazz, int id) {
        getDao().delete(clazz, id);
    }

    @SuppressWarnings("unchecked")
    public <T> List<T> list(Class<T> clazz) {
        List<T> result = getDao().findByQuery("from " + clazz.getName(), null,
                null);
        return result;
    }

    @SuppressWarnings("unchecked")
    public <T> List<T> listOrdered(Class<T> clazz, String orderColumn) {
        List<T> result = getDao().findByQuery(
                "from " + clazz.getName() + " order by " + orderColumn, null,
                null);
        return result;
    }

    public Object saveObject(Object e) throws RuntimeException {
        try {
            return getDao().saveObject(e);
        } catch (Exception ex) {
            log.error("Error saving", ex);
            return e;
        }
    }
}
