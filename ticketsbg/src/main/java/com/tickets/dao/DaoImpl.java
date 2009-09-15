package com.tickets.dao;

import java.io.Serializable;
import java.sql.Connection;
import java.util.List;

import javax.annotation.Resource;

import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;

/**
 * @author Bozhidar Bozhanov
 *
 */
//@Repository("dao")
public class DaoImpl implements Dao {

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

    public Object persist(Object o) {
        getSession().saveOrUpdate(o);
        return o;
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

    public Object attach(Object entity) {
        getSession().update(entity);
        return entity;
    }

    private Session getSession() {
        return sessionFactory.getCurrentSession();
    }

    public void flush() {
        getSession().flush();
    }

    public void clearPersistentContext() {
        getSession().clear();
    }

    public Object getDelegate() {
        return getSession();
    }

    public void commitCurrentTransaction() {
        getSession().getTransaction().commit();
        getSession().beginTransaction();
    }

    public void refresh(Object obj) {
        getSession().refresh(obj);
    }

}