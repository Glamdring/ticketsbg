package com.tickets.dao;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.tickets.dao.interfaces.Dao;
import com.tickets.services.UserService;

@Repository("dao")
public class GenericDao<E> extends Dao<E> {

    private UserService srv;

    @Autowired
    public void setSrv(UserService srv){
        System.out.println("doodoo");
    }

    @PostConstruct
    public void initialize() {
      System.out.println("haha");
    }

    public void delete(E e) {
        // TODO Auto-generated method stub

    }

    public void delete(int id) {
        // TODO Auto-generated method stub

    }

    public <T> E get(T key) {
        // TODO Auto-generated method stub
        return null;
    }

    public E getByExample(E e) {
        // TODO Auto-generated method stub
        return null;
    }

    public E getById(int id) {
        // TODO Auto-generated method stub
        return null;
    }

    public <T> T save(E e) {
        return (T) getSession().save(e);
    }

    public void update(E e) {
        getSession().update(e);
    }

    public void saveOrUpdate(E e) {
        getSession().saveOrUpdate(e);
    }

    public <T> void deleteByKey(T key) {
        // TODO Auto-generated method stub

    }
}
