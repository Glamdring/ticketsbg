package com.tickets.controllers;

import java.io.Serializable;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

@Controller("keepAliveController")
@Scope("session")
public class KeepAliveController implements Serializable {

    public void poll() {
        System.out.println("polled");
        // polling in order to keep the session alive
    }
}
