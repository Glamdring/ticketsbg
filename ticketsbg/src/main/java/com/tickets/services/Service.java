package com.tickets.services;

import java.io.Serializable;
import java.util.List;

public interface Service<E> {

     E save(E entity);

     void delete(E e);

     <T> List<T> list(Class<T> clazz);

     <T> List<T> listOrdered(Class<T> clazz, String orderColumn);

     Object saveObject(Object entity);

     <T> T get(Class<T> clazz, Serializable id);
}
