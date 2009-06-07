package com.tickets.dao;

import java.io.Serializable;
import java.sql.Connection;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

//@Repository
public class DaoEMImpl implements Dao {

    @PersistenceContext
    private EntityManager entityManager;


    @SuppressWarnings("unchecked")

    public <T extends Serializable> void delete(Class clazz, T id) {
        entityManager.remove(getById(clazz, id));

    }

    public void delete(Object object) {
        entityManager.remove(object);

    }

    public void evict(Object o) {
        throw new UnsupportedOperationException("Evict is not supported by EntityManager");
    }


    public int executeQuery(String query, String[] names, Object[] args) {
        if (names == null)
            names = new String[] {};

        if (args == null)
            args = new Object[] {};

        Query q = entityManager.createQuery(query);
        for (int i = 0; i < names.length; i ++) {
            q.setParameter(names[i], args[i]);
        }
        return q.executeUpdate();
    }


    public List findByNamedQuery(String name, String[] names, Object[] args) {
        if (names == null)
            names = new String[] {};

        if (args == null)
            args = new Object[] {};

         Query q = entityManager.createNamedQuery(name);
         for (int i = 0; i < names.length; i ++) {
             q.setParameter(names[i], args[i]);
         }

         return q.getResultList();
    }


    public List findByNamedQuery(String query) {
        return findByNamedQuery(query, null, null);
    }


    public List findByQuery(String query, String[] names, Object[] args) {
         if (names == null)
             names = new String[] {};

         if (args == null)
             args = new Object[] {};

         Query q = entityManager.createQuery(query);
         for (int i = 0; i < names.length; i ++) {
             q.setParameter(names[i], args[i]);
         }

         return q.getResultList();
    }


    public List findByQuery(String query) {
        return findByQuery(query, null, null);
    }


    public <T> T getById(Class<T> clazz, Serializable id) {
        return entityManager.find(clazz, id);
    }


    public Connection getConnection() {
       throw new UnsupportedOperationException("Connection not available via EntityManager");
    }


    public void persist(Object e) {
        //TODO make work
        if (entityManager.contains(e))
            e = entityManager.merge(e);
        else
            entityManager.persist(e);
    }


    public Object saveObject(Object o) {
        o = entityManager.merge(o);
        entityManager.persist(o);
        return o;
    }

    public List findByQuery(org.hibernate.Query query) {
        return query.list();
    }

    @Override
    public void attach(Object entity) {
        entityManager.persist(entity);
    }
}
