package com.tickets.dao;

import java.io.Serializable;
import java.sql.Connection;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Repository;

@Repository("dao")
@Scope("singleton")
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
        throw new UnsupportedOperationException(
                "Evict is not supported by EntityManager");
    }

    public int executeQuery(String query, String[] names, Object[] args) {
        if (names == null)
            names = new String[] {};

        if (args == null)
            args = new Object[] {};

        Query q = entityManager.createQuery(query);
        for (int i = 0; i < names.length; i++) {
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
        for (int i = 0; i < names.length; i++) {
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
        for (int i = 0; i < names.length; i++) {
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
        throw new UnsupportedOperationException(
                "Connection not available via EntityManager");
    }

    public Object persist(Object e) {
        // if e is already in the persistence context (session), no action is
        // taken, except for cascades
        // if e is detached, a copy (e') is returned, which is attached
        // (managed)
        // if e is transient (new instance), it is saved and a persistent (and
        // managed) copy is returned

        e = entityManager.merge(e);

        return e;
    }

    public Object saveObject(Object o) {
        o = entityManager.merge(o);
        return o;
    }

    public List findByQuery(org.hibernate.Query query) {
        return query.list();
    }

    @Override
    public Object attach(Object entity) {
        return entityManager.merge(entity);
    }

    @Override
    public void flush() {
        entityManager.flush();
    }

    @Override
    public void clearPersistentContext() {
        entityManager.clear();
    }
}
