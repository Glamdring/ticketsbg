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
@org.springframework.stereotype.Service("baseService")
public class BaseService<E> implements Service<E> {

    protected static Logger log = Logger.getLogger(BaseService.class);

    @Resource(name="dao")
    private Dao dao;

    public void setDao(Dao dao){
        this.dao = dao;
    }

    public Dao getDao(){
        return dao;
    }

    public E save(E e) throws RuntimeException {
        try {
            return getDao().persist(e);
        } catch (Exception ex) {
            log.error("Error saving", ex);
            return null;
        }
    }

    public <T> T get(Class<T> clazz, Serializable id) {
        T result = getDao().getById(clazz, id);
        return result;
    }

    public <T> void delete(T e) {
        getDao().delete(e);
    }

    public void delete(Class clazz, int id) {
        getDao().delete(clazz, id);
    }

    public <T> List<T> list(Class<T> clazz) {
        List<T> result = getDao().findByQuery("from " + clazz.getName(), null,
                null);
        return result;
    }

    public <T> List<T> listOrdered(Class<T> clazz, String orderColumn) {
        List<T> result = getDao().findByQuery(
                "from " + clazz.getName() + " order by " + orderColumn, null,
                null);
        return result;
    }

    public Object saveObject(Object e) throws RuntimeException {
        try {
            return getDao().persist(e);
        } catch (Exception ex) {
            log.error("Error saving", ex);
            return null;
        }
    }

    public Object attach(Object obj) {
        return getDao().attach(obj);
    }

    @SuppressWarnings("unchecked")
    @Override
    public <T> T get(Class<T> clazz, String propertyName, Serializable propertyValue) {
        List result = getDao().findByQuery("SELECT o FROM " + clazz.getName()+ " o WHERE " + propertyName + "=:" + propertyName,
                new String[] {propertyName}, new Object[] {propertyValue});
        if (result.size() > 0) {
            return (T) result.get(0);
        }

        return null;
    }
}
