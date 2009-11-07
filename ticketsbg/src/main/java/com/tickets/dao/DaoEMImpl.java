package com.tickets.dao;

import java.io.Serializable;
import java.sql.Connection;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import org.hibernate.Session;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Repository;

import com.tickets.controllers.security.ActionLogger;
import com.tickets.controllers.security.DatabaseOperationType;

@Repository("dao")
@Scope("singleton")
public class DaoEMImpl implements Dao {

    @PersistenceContext
    private EntityManager entityManager;

    @SuppressWarnings("unchecked")
    public <T extends Serializable> void delete(Class clazz, T id) {
        delete(getById(clazz, id));

    }

    public void delete(Object object) {
        ActionLogger.logAction(this, object, DatabaseOperationType.SAVE);
        object = entityManager.merge(object);
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

    @SuppressWarnings("unchecked")
    public <T> List<T> findByNamedQuery(String name, String[] names, Object[] args) {
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

    public <T> List<T> findByNamedQuery(String query) {
        return findByNamedQuery(query, null, null);
    }

    @SuppressWarnings("unchecked")
    public <T> List<T> findByQuery(String query, String[] names, Object[] args) {
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

    public <T> List<T> findByQuery(String query) {
        return findByQuery(query, null, null);
    }

    public <T> T getById(Class<T> clazz, Serializable id) {
        T e = entityManager.find(clazz, id);
        ActionLogger.logAction(this, e, DatabaseOperationType.RETRIEVE);
        return e;
    }

    public Connection getConnection() {
        throw new UnsupportedOperationException(
                "Connection not available via EntityManager");
    }

    public <T> T persist(T e) {
        // if e is already in the persistence context (session), no action is
        // taken, except for cascades
        // if e is detached, a copy (e') is returned, which is attached
        // (managed)
        // if e is transient (new instance), it is saved and a persistent (and
        // managed) copy is returned
        e = entityManager.merge(e);

        ActionLogger.logAction(this, e, DatabaseOperationType.SAVE);
        return e;
    }

    @SuppressWarnings("unchecked")
    public <T> List<T> findByQuery(org.hibernate.Query query) {
        return query.list();
    }

    public Object attach(Object entity) {
        return entityManager.merge(entity);
    }

    public void flush() {
        entityManager.flush();
    }

    public void clearPersistentContext() {
        entityManager.clear();
    }

    public Object getDelegate() {
        return entityManager.getDelegate();
    }

    public void commitCurrentTransaction() {
        ((Session) getDelegate()).getTransaction().commit();
        ((Session) getDelegate()).beginTransaction();
    }

    @Override
    public void refresh(Object obj) {
        entityManager.refresh(obj);
    }

    @Override
    public int executeNamedQuery(String name) {
        return entityManager.createNamedQuery(name).executeUpdate();
    }
}
