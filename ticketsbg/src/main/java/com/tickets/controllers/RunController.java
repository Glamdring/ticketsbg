package com.tickets.controllers;

import org.apache.myfaces.orchestra.conversation.annotations.ConversationName;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import com.tickets.annotations.Action;
import com.tickets.controllers.security.AccessLevel;
import com.tickets.controllers.valueobjects.Screen;
import com.tickets.model.Route;
import com.tickets.model.Run;
import com.tickets.services.RunService;
import com.tickets.utils.GeneralUtils;

@Controller("runController")
@Scope("conversation.manual")
@ConversationName("routesAndRuns")
@Action(accessLevel=AccessLevel.FIRM_COORDINATOR)
public class RunController extends BaseController {
    private Route route;
    private Run run;

    private int page;

    @Autowired
    private RunService<Run> service;

    @Action
    public String view() {
        route = service.get(Route.class, route.getId());
        return Screen.RUNS_LIST.getOutcome();
    }

    @Action
    public void delete() {
        route.getRuns().remove(run);
        service.delete(run);
    }

    @Action
    public void newRun() {
        run = new Run();
        run.setTime(GeneralUtils.createEmptyCalendar());
    }

    @Action
    public void addRun() {
        // saving the route, which will cascade the newly added runs
        route.addRun(run);
        service.saveObject(route);
    }

    @Action(accessLevel=AccessLevel.CASH_DESK)
    public void setSeatsExceeded() {
        run.setSeatsExceeded(true);
        service.save(run);
    }

    @Action(accessLevel=AccessLevel.CASH_DESK)
    public void undoSeatsExceeded() {
        run.setSeatsExceeded(false);
        service.save(run);
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
