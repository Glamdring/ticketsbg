package com.tickets.aop;

import org.springframework.aop.ThrowsAdvice;

import com.tickets.utils.HibernateUtil;

public class TransactionRollbacker implements ThrowsAdvice{
    public void afterThrowing(Exception ex){
        HibernateUtil.getSession().getTransaction().rollback();
    }
}
