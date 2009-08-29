package com.tickets.controllers;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.model.SelectItem;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import com.tickets.annotations.Action;
import com.tickets.constants.Messages;
import com.tickets.controllers.security.AccessLevel;
import com.tickets.controllers.users.LoggedUserHolder;
import com.tickets.model.Firm;
import com.tickets.model.Route;
import com.tickets.model.stats.SoldTickets;
import com.tickets.services.RouteService;
import com.tickets.services.StatisticsService;

@Controller("statisticsController")
@Scope("singleton")
@Action(accessLevel=AccessLevel.FIRM_COORDINATOR)
public class StatisticsController extends BaseController {

    @Autowired
    private StatisticsService statsService;

    @Autowired
    private RouteService routeService;

    private String selectedRouteName;

    private int selectedPeriod = Calendar.DAY_OF_MONTH;

    private int selectedTimeType = StatisticsService.BY_RUN_TIME;

    private List<SelectItem> periodItems;
    private List<SelectItem> timeTypeItems;
    private List<String> routeNames;

    @PostConstruct
    public void init() {
        periodItems = new ArrayList<SelectItem>();
        periodItems.add(new SelectItem(Calendar.DAY_OF_MONTH, Messages.getString("statsDayOfMonth")));
        periodItems.add(new SelectItem(Calendar.MONTH, Messages.getString("statsMonth")));
        periodItems.add(new SelectItem(Calendar.DAY_OF_WEEK, Messages.getString("statsDayOfWeek")));

        timeTypeItems = new ArrayList<SelectItem>();
        timeTypeItems.add(new SelectItem(StatisticsService.BY_RUN_TIME, Messages.getString("statsByRunTime")));
        timeTypeItems.add(new SelectItem(StatisticsService.BY_PURCHASE_TIME, Messages.getString("statsByPurchaseTime")));
    }

    @Action(accessLevel=AccessLevel.PUBLIC)
    public int getCompaniesCount() {
        return statsService.getCompaniesCount();
    }

    @Action(accessLevel=AccessLevel.PUBLIC)
    public int getDestinations() {
        return statsService.getDestinationsCount();
    }

    public List<SoldTickets> getSoldTickets() {
        Firm firm = LoggedUserHolder.getUser().getFirm();
        Route route = routeService.findRoute(selectedRouteName, firm);

        return statsService.getSoldTickets(route, selectedPeriod,
                selectedTimeType, firm);
    }

    public String getSelectedRouteName() {
        return selectedRouteName;
    }

    public void setSelectedRouteName(String selectedRouteName) {
        this.selectedRouteName = selectedRouteName;
    }

    public int getSelectedPeriod() {
        return selectedPeriod;
    }

    public void setSelectedPeriod(int selectedPeriod) {
        this.selectedPeriod = selectedPeriod;
    }

    public List<SelectItem> getPeriodItems() {
        return periodItems;
    }

    public void setPeriodItems(List<SelectItem> periodItems) {
        this.periodItems = periodItems;
    }

    @SuppressWarnings("unchecked")
    public List<String> getRouteNames() {
        if (routeNames == null) {
            List<Route> routes = routeService.list(LoggedUserHolder.getUser().getFirm());
            routeNames = new ArrayList<String>(routes.size());
            for (Route route : routes) {
                routeNames.add(route.getName());
            }
        }
        return routeNames;
    }

    public void setRouteNames(List<String> routeNames) {
        this.routeNames = routeNames;
    }

    public List<SelectItem> getTimeTypeItems() {
        return timeTypeItems;
    }

    public void setTimeTypeItems(List<SelectItem> timeTypeItems) {
        this.timeTypeItems = timeTypeItems;
    }

    public int getSelectedTimeType() {
        return selectedTimeType;
    }

    public void setSelectedTimeType(int selectedTimeType) {
        this.selectedTimeType = selectedTimeType;
    }
}
