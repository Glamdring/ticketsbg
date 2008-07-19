package com.tickets.dao.interfaces;

import org.hibernate.Session;

import com.tickets.utils.HibernateUtil;

public abstract class Dao<E> {

    private Session session;

    public abstract E getById(int id);
    public abstract E getByExample(E e);
    public abstract <T> E get(T key);
    public abstract void delete(E e);
    public abstract void delete(int id);
    public abstract <T> void deleteByKey(T key);
    public abstract <T> T save(E e);
    public abstract void update(E e);
    public abstract void saveOrUpdate(E e);

    public Session getSession(){
        if (session == null || !session.isOpen())
            session = HibernateUtil.openSession();

        return session;
    }
}
