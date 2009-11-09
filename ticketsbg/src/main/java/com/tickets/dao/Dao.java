package com.tickets.dao;

import java.io.Serializable;
import java.sql.Connection;
import java.util.List;

import org.hibernate.Query;

public interface Dao {

    /**
     * Deletes an object identified by the specified Id
     *
     * @param <T>
     * @param clazz
     * @param id
     */
    <T extends Serializable> void delete(Class clazz, T id);

    /**
     * Deletes the specified object
     * @param e
     */
    void delete(Object obj);

    /**
     * Loads the object from the persistent store
     *
     * @param <T>
     * @param clazz
     * @param id
     * @return the entity, if found. Null otherwise.
     */
    <T> T getById(Class<T> clazz, Serializable id);

    /**
     * Makes the entity persistent.
     * If it is transient, saves it.
     * If it is detatched, attaches it.
     *
     * @param entity
     * @return the persistent entity
     */
    <T> T persist(T e);

    /**
     * Removes the specified object from the session
     * @param o
     */
    void evict(Object o);

    /**
     * Lists the result of the specified query
     *
     * @param query
     * @param names
     * @param args
     * @return the result list
     */
    <T> List<T> findByQuery(String query, String[] names, Object[] args);

    /**
     * Lists the result of the specified named query
     *
     * @param name
     * @param names
     * @param args
     * @return the result list
     */
    <T> List<T> findByNamedQuery(String name, String[] names, Object[] args);

    int executeQuery(final String query, final String[] names,
            final Object[] args);

    /**
     * Retrieves the underlying database connection
     * @return connection
     */
    Connection getConnection();

    /**
     * Convenient method for calling no-param queries
     *
     * @param query
     * @return result list
     */
    <T> List<T> findByQuery(String query);

    /**
     * Convenient method for calling no-param named queries
     *
     * @param query
     * @return result list
     */
    <T> List<T> findByNamedQuery(String query);

    /**
     * Executes the specified query. Use only in case of specific Query-object
     * manipulations.
     * @param query
     * @return the result list
     */
    <T> List<T> findByQuery(Query query);

    /**
     * Re-attaches the entity to the session
     * @param entity
     */
    <T> T attach(T entity);

    /**
     * Flushes the underlying persistent context
     */
    void flush();

    /**
     * Clears the underlying persistent context
     */
    void clearPersistentContext();

    /**
     * Returns the native implementation delegate
     * @return the delegate
     */
    Object getDelegate();

    /**
     * Commits the current transaction, and starts a new one
     */
    void commitCurrentTransaction();

    /**
     * Refresh the state of the given object
     * @param obj
     */
    void refresh(Object obj);

    /**
     * Executes an update/delete named query
     * @param name
     */
    int executeNamedQuery(String name);
}
