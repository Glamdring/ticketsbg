package com.tickets.aop;

import java.lang.reflect.Method;
import org.springframework.aop.AfterReturningAdvice;

import com.tickets.utils.HibernateUtil;


public class TransactionCommitter implements AfterReturningAdvice {

    public void afterReturning(Object returnValue, Method method, Object[] args, Object target) throws Throwable {
        HibernateUtil.getSession().getTransaction().commit();
    }

}
