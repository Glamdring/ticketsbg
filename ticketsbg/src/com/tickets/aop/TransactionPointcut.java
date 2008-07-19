package com.tickets.aop;

import java.lang.reflect.Method;

import org.springframework.aop.support.StaticMethodMatcherPointcut;

public class TransactionPointcut extends StaticMethodMatcherPointcut {

    @SuppressWarnings("unchecked")
    public boolean matches(Method method, Class clazz) {
        if (clazz.getPackage().getName().equals("com.tickets.services"))
            return true;

        return false;
    }


}
