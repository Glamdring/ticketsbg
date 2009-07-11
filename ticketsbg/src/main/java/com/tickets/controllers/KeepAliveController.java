package com.tickets.controllers;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

@Controller("keepAliveController")
@Scope("session")
public class KeepAliveController {

    public void poll() {
        // polling in order to keep the session alive
    }
}
