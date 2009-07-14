package com.tickets.controllers;

import org.apache.myfaces.orchestra.conversation.annotations.ConversationName;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import com.tickets.model.Route;
import com.tickets.model.Run;
import com.tickets.services.RunService;

@Controller("runController")
@Scope("conversation.manual")
@ConversationName("routesAndRuns")
public class RunController extends BaseController {
    private Route route;
    private Run run;

    private int page;

    @Autowired
    private RunService<Run> service;

    public String view() {
        route = service.get(Route.class, route.getId());
        return Screen.RUNS_LIST.getOutcome();
    }

    public void delete() {
        route.getRuns().remove(run);
        service.delete(run);
    }

    public Route getRoute() {
        return route;
    }

    public void setRoute(Route route) {
        this.route = route;
    }

    public Run getRun() {
        return run;
    }

    public void setRun(Run run) {
        this.run = run;
    }

    public int getPage() {
        return page;
    }

    public void setPage(int page) {
        this.page = page;
    }

}
