package com.tickets.dao;

import java.io.Serializable;
import java.sql.Connection;
import java.util.List;

public interface Dao<E> {

    <T extends Serializable> void delete(Class clazz, T id);

    <T> T getById(Class<T> clazz, Serializable id);

    E save(E e);

    E merge(E e);

    void delete(E e);

    List findByQuery(String query, String[] names, Object[] args);

    List findByNamedQuery(String name, String[] names, Object[] args);

    Object saveObject(Object o);

    Object mergeIfNotContained(Object o);

    void evict(Object o);

    void deleteObject(Object object);

    int executeQuery(final String query, final String[] names,
            final Object[] args);

    Connection getConnection();

    /**
     * Convenient method for calling no-param queries
     *
     * @param query
     * @return result list
     */
    List findByQuery(String query);

    /**
     * Convenient method for calling no-param named queries
     *
     * @param query
     * @return result list
     */
    List findByNamedQuery(String query);
}
