package com.tickets.client.utils;

import com.google.gwt.user.client.rpc.ServiceDefTarget;
import com.tickets.client.services.BaseClientServiceAsync;

public class RpcUtil {

    public static <T extends BaseClientServiceAsync> void directService(T service, String servletName) {
        ServiceDefTarget endPoint = (ServiceDefTarget) service;
        endPoint.setServiceEntryPoint(servletName);
    }
}