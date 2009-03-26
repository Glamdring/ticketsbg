package com.tickets.dao;

import java.io.Serializable;
import java.sql.Connection;
import java.util.List;

import javax.annotation.Resource;

import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.stereotype.Repository;

/**
 * @author Bozhidar Bozhanov
 *
 * @param <E>
 */
@Repository("dao")
public class DaoImpl<E> implements Dao<E> {

    @Resource(name="sessionFactory")
    private SessionFactory sessionFactory;

    @SuppressWarnings("unchecked")
    public <T extends Serializable> void delete(Class clazz, T id) {
        getSession().delete(getById(clazz, id));
    }

    @SuppressWarnings("unchecked")
    public <T> T getById(Class<T> clazz, Serializable id) {
        return (T) getSession().get(clazz, id);
    }

    public void persist(Object o) {
        getSession().saveOrUpdate(o);
    }

    public List findByQuery(String query, String[] names, Object[] args) {
        if (names == null)
            names = new String[] {};

        if (args == null)
            args = new Object[] {};

        Query q = getSession().createQuery(query);
        for (int i = 0; i < names.length; i ++) {
            q.setParameter(names[i], args[i]);
        }

        return q.list();
    }

    public List findByNamedQuery(String name, String[] names, Object[] args) {
        if (names == null)
            names = new String[] {};

        if (args == null)
            args = new Object[] {};

         Query q = getSession().getNamedQuery(name);
         for (int i = 0; i < names.length; i ++) {
             q.setParameter(names[i], args[i]);
         }

         return q.list();
    }

    public void evict(Object o) {
        getSession().evict(o);
    }

    public void delete(Object object) {
        getSession().delete(object);
    }

    public int executeQuery(String query, String[] names, Object[] args) {
        if (names == null)
            names = new String[] {};

        if (args == null)
            args = new Object[] {};

        Query q = getSession().getNamedQuery(query);
        for (int i = 0; i < names.length; i ++) {
            q.setParameter(names[i], args[i]);
        }

        return q.executeUpdate();
    }

    @SuppressWarnings("deprecation")
    public Connection getConnection() {
        return getSession().connection();
    }

    public List findByQuery(String query) {
        return findByQuery(query, null, null);
    }

    public List findByNamedQuery(String query) {
        return findByNamedQuery(query, null, null);
    }

    public List findByQuery(Query query) {
        return query.list();
    }

    public void attach(Object entity) {
        getSession().update(entity);
    }

    private Session getSession() {
        return sessionFactory.getCurrentSession();
    }

}