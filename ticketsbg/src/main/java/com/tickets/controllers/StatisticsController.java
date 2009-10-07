package com.tickets.controllers;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
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
import com.tickets.controllers.valueobjects.PurchaseMeansType;
import com.tickets.controllers.valueobjects.StatsDataType;
import com.tickets.model.Firm;
import com.tickets.model.Route;
import com.tickets.model.Ticket;
import com.tickets.model.User;
import com.tickets.model.stats.StatsHolder;
import com.tickets.services.RouteService;
import com.tickets.services.StatisticsService;
import com.tickets.utils.SelectItemUtils;

@Controller("statisticsController")
@Scope("conversation.access")
@Action(accessLevel=AccessLevel.FIRM_COORDINATOR)
public class StatisticsController extends BaseController {

    @Autowired
    private StatisticsService statsService;

    @Autowired
    private RouteService routeService;

    private String selectedRouteName;
    private int selectedPeriod = Calendar.DAY_OF_MONTH;
    private int selectedTimeType = StatisticsService.BY_RUN_TIME;
    private StatsDataType selectedDataType;
    private PurchaseMeansType selectedPurchaseMeansType = PurchaseMeansType.BOTH;

    private Date fromDate;
    private Date toDate;

    private List<SelectItem> periodItems;
    private List<SelectItem> timeTypeItems;
    private List<SelectItem> dataTypeItems;
    private List<SelectItem> purchaseMeansTypeItems;
    private List<String> routeNames;

    private int page;
    private BigDecimal totalPrice = BigDecimal.ZERO;

    @PostConstruct
    public void init() {
        // Don't initialize in case a not in the admin panel
        User user = LoggedUserHolder.getUser();
        if (user != null && user.isStaff()) {
            periodItems = new ArrayList<SelectItem>();
            periodItems.add(new SelectItem(Calendar.DAY_OF_MONTH, Messages.getString("statsDayOfMonth")));
            periodItems.add(new SelectItem(Calendar.MONTH, Messages.getString("statsMonth")));
            periodItems.add(new SelectItem(Calendar.DAY_OF_WEEK, Messages.getString("statsDayOfWeek")));

            timeTypeItems = new ArrayList<SelectItem>();
            timeTypeItems.add(new SelectItem(StatisticsService.BY_RUN_TIME, Messages.getString("statsByRunTime")));
            timeTypeItems.add(new SelectItem(StatisticsService.BY_PURCHASE_TIME, Messages.getString("statsByPurchaseTime")));

            dataTypeItems = SelectItemUtils.formSelectItems(StatsDataType.class);

            purchaseMeansTypeItems = SelectItemUtils.formSelectItems(
                    PurchaseMeansType.class, PurchaseMeansType.BOTH);
        }
    }

    @Action(accessLevel=AccessLevel.PUBLIC)
    public int getCompaniesCount() {
        return statsService.getCompaniesCount();
    }

    @Action(accessLevel=AccessLevel.PUBLIC)
    public int getDestinations() {
        return statsService.getDestinationsCount();
    }

    public List<StatsHolder> getStatistics() {
        Firm firm = LoggedUserHolder.getUser().getFirm();
        Route route = routeService.findRoute(selectedRouteName, firm);

        return statsService.getStatistics(route, selectedPeriod,
                selectedTimeType, firm,
                fromDate, toDate, selectedDataType, selectedPurchaseMeansType);
    }

    public List<Ticket> getTickets() {
        Firm firm = LoggedUserHolder.getUser().getFirm();
        Route route = routeService.findRoute(selectedRouteName, firm);

        List<Ticket> tickets = statsService.getTickets(route, selectedPeriod,
                selectedTimeType, firm,
                fromDate, toDate, selectedDataType, selectedPurchaseMeansType);

        totalPrice = statsService.getTotalPrice(tickets);

        return tickets;
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

    public Date getFromDate() {
        return fromDate;
    }

    public void setFromDate(Date fromDate) {
        this.fromDate = fromDate;
    }

    public Date getToDate() {
        return toDate;
    }

    public void setToDate(Date toDate) {
        this.toDate = toDate;
    }

    public StatsDataType getSelectedDataType() {
        return selectedDataType;
    }

    public void setSelectedDataType(StatsDataType selectedDataType) {
        this.selectedDataType = selectedDataType;
    }

    public List<SelectItem> getDataTypeItems() {
        return dataTypeItems;
    }

    public void setDataTypeItems(List<SelectItem> dataTypeItems) {
        this.dataTypeItems = dataTypeItems;
    }

    public PurchaseMeansType getSelectedPurchaseMeansType() {
        return selectedPurchaseMeansType;
    }

    public void setSelectedPurchaseMeansType(
            PurchaseMeansType selectedPurchaseMeansType) {
        this.selectedPurchaseMeansType = selectedPurchaseMeansType;
    }

    public List<SelectItem> getPurchaseMeansTypeItems() {
        return purchaseMeansTypeItems;
    }

    public void setPurchaseMeansTypeItems(List<SelectItem> purchaseMeansTypeItems) {
        this.purchaseMeansTypeItems = purchaseMeansTypeItems;
    }

    public int getPage() {
        return page;
    }

    public void setPage(int page) {
        this.page = page;
    }

    public BigDecimal getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(BigDecimal totalPrice) {
        this.totalPrice = totalPrice;
    }
}
