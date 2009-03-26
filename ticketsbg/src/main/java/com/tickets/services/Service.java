package com.tickets.services;

import java.io.Serializable;
import java.util.List;

public interface Service<E> {

     void save(E entity);

     void delete(E e);

     <T> List<T> list(Class<T> clazz);

     <T> List<T> listOrdered(Class<T> clazz, String orderColumn);

     void saveObject(Object entity);

     <T> T get(Class<T> clazz, Serializable id);
}
