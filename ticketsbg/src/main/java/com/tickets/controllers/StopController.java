package com.tickets.controllers;

import java.io.Serializable;

import javax.annotation.Resource;
import javax.faces.event.ValueChangeEvent;

import org.richfaces.component.html.HtmlOrderingList;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import com.tickets.annotations.Action;
import com.tickets.model.Stop;
import com.tickets.services.StopService;

@Controller("stopController")
@Scope("conversation.manual")
public class StopController extends BaseController implements Serializable {

    private Stop stop;
    private HtmlOrderingList stopsTable;

    @Resource
    private RouteController routeController;

    @Autowired
    private StopService service;

    @Action
    public String addStop() {
        stop = new Stop();
        return "stopScreen";
    }

    @Action
    public String edit() {
        stop = (Stop) stopsTable.getRowData();

        return "stopScreen";
    }

    @Action
    public String save() {
        service.addStopToRoute(stop, routeController.getRoute());

        return "routeScreen";
    }

    @Action
    public String delete() {
        routeController.getRoute().getStops()
            .remove(stopsTable.getRowData());

        return "routeScreen";
    }

    @SuppressWarnings("unused")
    public void listReordered(ValueChangeEvent evt) {
        System.out.println("LIST REORDERED");
        if (routeController.getRoute().getStops() != null) {
            for (int i = 0, max = routeController.getRoute().getStops().size(); i < max; i ++) {
                routeController.getRoute().getStops().get(i).setIdx(i + 1);
            }
        }
    }

    public Stop getStop() {
        return stop;
    }

    public void setStop(Stop stop) {
        this.stop = stop;
    }

    public HtmlOrderingList getStopsTable() {
        return stopsTable;
    }

    public void setStopsTable(HtmlOrderingList stopsTable) {
        this.stopsTable = stopsTable;
    }
}
