package com.tickets.client.services;

import java.util.List;

import com.google.gwt.user.client.rpc.RemoteService;

public interface BaseClientService<E> {

     List list();

     List listOrdered(String orderColumn);

     E save(E entity);
}
