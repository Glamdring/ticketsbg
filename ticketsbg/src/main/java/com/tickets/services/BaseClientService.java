package com.tickets.services;

import java.util.List;

public interface BaseClientService<E> {

     List list();

     List listOrdered(String orderColumn);

     E save(E entity);
}
