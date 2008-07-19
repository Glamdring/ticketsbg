package com.tickets.aop;

import java.lang.reflect.Method;

import org.springframework.aop.support.StaticMethodMatcherPointcut;

public class TransactionPointcut extends StaticMethodMatcherPointcut {

    public boolean matches(Method method, Class clazz) {
        if (clazz.getPackage().getName().equals("com.tickets.services"))
            return true;
        else
            return false;
    }


}
