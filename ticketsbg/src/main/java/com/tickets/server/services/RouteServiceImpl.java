package com.tickets.server.services;

import java.util.List;

import org.springframework.stereotype.Service;

import com.tickets.client.services.RouteService;
import com.tickets.model.Route;

@Service("routeService")
public class RouteServiceImpl extends BaseService<Route> implements RouteService {

    @Override
    public List list() {
        return list(Route.class);
    }

    @Override
    public List listOrdered(String orderColumn) {
        return listOrdered(Route.class, orderColumn);
    }

}
