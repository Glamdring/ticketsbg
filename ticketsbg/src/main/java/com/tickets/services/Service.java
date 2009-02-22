package com.tickets.services;

import java.util.List;

public interface Service<E> {
     List list();

     List listOrdered(String orderColumn);

     E save(E entity);
}
