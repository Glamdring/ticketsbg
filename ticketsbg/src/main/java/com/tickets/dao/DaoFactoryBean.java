package com.tickets.dao;

import org.springframework.beans.factory.SmartFactoryBean;

import com.tickets.utils.SpringContext;

//@Component("daoFactory")
public class DaoFactoryBean implements SmartFactoryBean {

    @Override
    public boolean isEagerInit() {
        return true;
    }

    @Override
    public boolean isPrototype() {
        return true;
    }

    @Override
    public Object getObject() throws Exception {
        return SpringContext.getContext().getBean("dao");
    }

    @Override
    public Class getObjectType() {
        return Dao.class;
    }

    @Override
    public boolean isSingleton() {
        return false;
    }
}
