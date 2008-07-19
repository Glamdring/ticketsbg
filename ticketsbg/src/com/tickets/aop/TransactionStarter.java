package com.tickets.aop;

import java.lang.reflect.Method;

import org.springframework.aop.MethodBeforeAdvice;

import com.tickets.utils.HibernateUtil;

public class TransactionStarter implements MethodBeforeAdvice {

    public void before(Method method, Object[] args, Object target) throws Throwable {
        System.out.println("Transaction started");
        HibernateUtil.getSession().beginTransaction();
    }

}
