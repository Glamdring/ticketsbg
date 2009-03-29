package com.tickets.controllers;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;
import javax.faces.event.ValueChangeEvent;
import javax.faces.model.ListDataModel;

import org.apache.myfaces.orchestra.conversation.Conversation;
import org.richfaces.component.html.HtmlOrderingList;
import org.richfaces.component.html.HtmlTree;
import org.richfaces.event.NodeSelectedEvent;
import org.richfaces.model.ListRowKey;
import org.richfaces.model.TreeNode;
import org.richfaces.model.TreeNodeImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import com.tickets.annotations.Action;
import com.tickets.constants.Messages;
import com.tickets.model.Day;
import com.tickets.model.Price;
import com.tickets.model.Route;
import com.tickets.model.Stop;
import com.tickets.services.RouteService;
import com.tickets.services.StopService;

@Controller("routeController")
@Scope("conversation.manual")
public class RouteController extends BaseController implements Serializable {

    private Route route;
    private ListDataModel routesModel;
    private List<Day> days;
    private Integer[] daysPickList;
    private Map<String, String> dayNames = new HashMap<String, String>();
    private Date hour;
    private int selectedHour;
    private Stop stop;
    private HtmlOrderingList stopsTable;
    private TreeNode<Stop> pricesTreeData;
    private Price price;
    private BigDecimal priceValue;
    private BigDecimal twoWayPriceValue;

    @Autowired
    private StopService stopService;


    @Autowired
    private RouteService routeService;

    @Action
    public String save() {
        routeService.save(route, Arrays.asList(daysPickList));
        refreshList();
        return "routesList";
    }

    @Action
    public String edit(){
        route = (Route) routesModel.getRowData();
        daysPickList = routeService.getDaysList(route);
        refreshTreeModel();
        return "routeScreen";
    }

    @Action
    public String delete() {
        routeService.delete((Route) routesModel.getRowData());
        refreshList();
        return "routesList";
    }

    @Action
    public String newRoute() {
        daysPickList = new Integer[0];
        route = new Route();
        return "routeScreen";
    }

    @Action
    public String addHour() {
        routeService.addHourToRoute(hour, route);
        return "routeScreen";
    }

    @Action
    public String removeHour() {
        routeService.removeHour(selectedHour, route);
        selectedHour = 0;
        return "routeScreen";
    }

    @Action
    public String addStop() {
        stop = new Stop();
        return "stopScreen";
    }

    @Action
    public String editStop() {
        stop = (Stop) stopsTable.getRowData();
        return "stopScreen";
    }

    @Action
    public String saveStop() {
        stopService.addStopToRoute(stop, route);
        listReordered(null);
        return "routeScreen";
    }

    @Action
    public String deleteStop() {
        Stop stop = (Stop) stopsTable.getRowData();
        stopService.delete(stop, route);

        return "routeScreen";
    }

    @Action
    public String cancel() {
        endConversation();
        return "routesList";
    }

    @Action
    public String savePrice() {
        price.setPrice(priceValue);
        price.setTwoWayPrice(twoWayPriceValue);
        return null;
    }

    @SuppressWarnings("unused")
    public void listReordered(ValueChangeEvent evt) {
        //TODO : skip the multiple events!
        stopService.listReoredered(route);
        refreshTreeModel();
    }

    public void nodeSelected(NodeSelectedEvent evt) {
        if (evt.getSource() instanceof HtmlTree) {
            HtmlTree tree = (HtmlTree) evt.getSource();
            ListRowKey rowKey = (ListRowKey) tree.getRowKey();
            if (rowKey.depth() == 1) {
                //TODO: expand
            } else {
                price = stopService.getPrice(rowKey, route);
                priceValue = new BigDecimal(price.getPrice().doubleValue());
                twoWayPriceValue = new BigDecimal(price.getTwoWayPrice().doubleValue());
            }
        }
    }

    public void refreshTreeModel() {
        pricesTreeData = new TreeNodeImpl<Stop>();
        for (int i = 0; i < route.getStops().size() - 1; i ++) {
            TreeNode<Stop> node = new TreeNodeImpl<Stop>();
            node.setData(route.getStops().get(i));
            //the root node is the parrent
            node.setParent(pricesTreeData);
            pricesTreeData.addChild("start" + node.getData().getStopId(), node);

            for (int j = i + 1; j < route.getStops().size(); j ++) {
                TreeNode<Stop> subNode = new TreeNodeImpl<Stop>();
                subNode.setData(route.getStops().get(j));
                subNode.setParent(node);
                node.addChild("end" + subNode.getData().getStopId(), subNode);
            }
        }

    }

    @PostConstruct
    public void init() {
        refreshList();
        route = new Route();
        days = routeService.list(Day.class);
        for (Day day : days) {
            day.setLabel(Messages.getString(day.getName()));
            dayNames.put(day.getName(), day.getLabel());
        }
    }

    private void refreshList() {
        routesModel = new ListDataModel(routeService.list());

        // End the current conversation in case the list of routes
        // is refreshed, but only if the bean has not just been constructed
        if (route != null)
            endConversation();
    }

    private void endConversation() {
        Conversation.getCurrentInstance().invalidate();
    }

    public Route getRoute() {
        return route;
    }

    public void setRoute(Route route) {
        this.route = route;
    }

    public ListDataModel getRoutesModel() {
        return routesModel;
    }

    public void setRoutesModel(ListDataModel routesModel) {
        this.routesModel = routesModel;
    }

    public List<Day> getDays() {
        return days;
    }

    public void setDays(List<Day> days) {
        this.days = days;
    }

    public Integer[] getDaysPickList() {
        return daysPickList;
    }

    public void setDaysPickList(Integer[] daysPickList) {
        this.daysPickList = daysPickList;
    }

    public Map<String, String> getDayNames() {
        return dayNames;
    }

    public void setDayNames(Map<String, String> dayNames) {
        this.dayNames = dayNames;
    }

    public Date getHour() {
        return hour;
    }

    public void setHour(Date hour) {
        this.hour = hour;
    }

    public int getSelectedHour() {
        return selectedHour;
    }

    public void setSelectedHour(int selectedHour) {
        this.selectedHour = selectedHour;
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

    public TreeNode<Stop> getPricesTreeData() {
        return pricesTreeData;
    }

    public void setPricesTreeData(TreeNode<Stop> pricesTreeData) {
        this.pricesTreeData = pricesTreeData;
    }

    public Price getPrice() {
        return price;
    }

    public void setPrice(Price price) {
        this.price = price;
    }

    public BigDecimal getPriceValue() {
        return priceValue;
    }

    public void setPriceValue(BigDecimal priceValue) {
        this.priceValue = priceValue;
    }

    public BigDecimal getTwoWayPriceValue() {
        return twoWayPriceValue;
    }

    public void setTwoWayPriceValue(BigDecimal twoWayPriceValue) {
        this.twoWayPriceValue = twoWayPriceValue;
    }
}
