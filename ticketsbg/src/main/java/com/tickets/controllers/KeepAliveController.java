package com.tickets.controllers;

import java.io.Serializable;

import org.apache.log4j.Logger;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

@Controller("keepAliveController")
@Scope("session")
public class KeepAliveController implements Serializable {

    private static final Logger logger = Logger
            .getLogger(KeepAliveController.class);

    public void poll() {
        logger.info("polled");
        // polling in order to keep the session alive
    }
}
