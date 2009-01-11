package com.tickets.dao;

import java.io.Serializable;

import org.hibernate.Query;
import org.springframework.context.annotation.Scope;
import org.springframework.orm.hibernate3.support.HibernateDaoSupport;
import org.springframework.stereotype.Repository;

@Repository("dao")
@Scope("prototype")
public class Dao<E> extends HibernateDaoSupport {

    public void delete(E e) {
        getSession().delete(e);
    }

    @SuppressWarnings("unchecked")
    public <T extends Serializable> void delete(Class clazz, T id) {
        getSession().delete(getById(clazz, id));
    }

    @SuppressWarnings("unchecked")
    public E getById(Class clazz, Serializable id) {
        return (E) getSession().get(clazz, id);
    }

    @SuppressWarnings("unchecked")
    public E persist(E e) {
        getSession().persist(e);
        return e;
    }

    @SuppressWarnings("unchecked")
    public E merge(E e) {
        e = (E) getSession().merge(e);
        return e;
    }
    public Query createQuery(String query){
        return getSession().createQuery(query);
    }

    public Query getNamedQuery(String name){
        return getSession().getNamedQuery(name);
    }
}
