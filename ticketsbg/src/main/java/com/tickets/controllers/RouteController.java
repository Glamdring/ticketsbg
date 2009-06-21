package com.tickets.controllers;

import java.io.IOException;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;
import javax.faces.event.ValueChangeEvent;
import javax.faces.model.ListDataModel;
import javax.faces.model.SelectItem;

import org.apache.myfaces.orchestra.conversation.Conversation;
import org.richfaces.component.UITree;
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
import com.tickets.model.Discount;
import com.tickets.model.DiscountType;
import com.tickets.model.Price;
import com.tickets.model.Route;
import com.tickets.model.Stop;
import com.tickets.model.StopPriceHolder;
import com.tickets.services.RouteService;
import com.tickets.services.StopService;
import com.tickets.utils.SelectItemUtils;

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
    private Stop stop = new Stop();
    private Discount discount = new Discount();
    private HtmlOrderingList stopsTable;
    private TreeNode<StopPriceHolder> pricesTreeData;
    private Price price;
    private BigDecimal priceValue;
    private BigDecimal twoWayPriceValue;
    private List<SelectItem> discountTypeSelectItems = new ArrayList<SelectItem>();

    @Autowired
    private StopService stopService;

    @Autowired
    private RouteService routeService;

    @Action
    public String save() {
        routeService.save(route, Arrays.asList(daysPickList));
        refreshList();
        return Screen.ROUTES_LIST.getOutcome();
    }

    @Action
    public String edit(){
        route = (Route) routesModel.getRowData();
        daysPickList = routeService.getDaysList(route);
        refreshTreeModel();
        return Screen.ROUTE_SCREEN.getOutcome();
    }

    @Action
    public String delete() {
        routeService.delete((Route) routesModel.getRowData());
        refreshList();
        return Screen.ROUTES_LIST.getOutcome();
    }

    @Action
    public String newRoute() {
        daysPickList = new Integer[0];
        route = new Route();
        return Screen.ROUTE_SCREEN.getOutcome();
    }

    @Action
    public void addHour() {
        routeService.addHourToRoute(hour, route);
    }

    @Action
    public void removeHour() {
        //If removing a persistent entity, call the service
        //else, the supplied selection is a negative number,
        //so just remove the .abs(..) position from the list
        if (selectedHour > 0) {
            routeService.removeHour(selectedHour, route);
        } else {
            route.getRouteHours().remove(Math.abs(selectedHour));
        }
        selectedHour = 0;
    }

    @Action
    public void addStop() {
        stop = new Stop();
    }

    @Action
    public void saveStop() {
        stopService.addStopToRoute(stop, route);
        listReordered(null);
    }

    @Action
    public void deleteStop() {
        Stop stop = (Stop) stopsTable.getRowData();
        stopService.delete(stop, route);
    }

    @Action
    public void addDiscount() {
        discount = new Discount();
    }

    @Action
    public void saveDiscount() {
        route.addDiscount(discount);
    }

    @Action
    public void deleteDiscount() {
        route.getDiscounts().remove(discount);
    }

    @Action
    public String cancel() {
        endConversation();
        return Screen.ROUTES_LIST.getOutcome();
    }

    @Action
    public void savePrice() {
        price.setPrice(priceValue);
        price.setTwoWayPrice(twoWayPriceValue);
    }

    @Action
    public Boolean getExpandedNodes(UITree tree) {
        if (tree.getChildCount() <= 4)
            return Boolean.TRUE;

        return Boolean.FALSE;
    }

    @SuppressWarnings({ "unused" })
    public void listReordered(ValueChangeEvent evt) {
        //TODO : skip the multiple events!
        //TODO: listener-method is executed before the actual value is set! fix!
        stopService.listReoredered(route);
        refreshTreeModel();
    }

    public void nodeSelected(NodeSelectedEvent evt) {
        if (evt.getSource() instanceof HtmlTree) {
            HtmlTree tree = (HtmlTree) evt.getSource();
            ListRowKey rowKey = (ListRowKey) tree.getRowKey();
            if (rowKey == null)
                return;

            if (rowKey.depth() == 1) {
                try {
                    tree.queueNodeExpand(rowKey);
                } catch (IOException ex) {
                    //Ignore
                }
            } else {
                price = stopService.getPrice(rowKey, route);
                priceValue = new BigDecimal(price.getPrice().doubleValue());
                twoWayPriceValue = new BigDecimal(price.getTwoWayPrice().doubleValue());
            }
        }
    }

    public void refreshTreeModel() {
        pricesTreeData = new TreeNodeImpl<StopPriceHolder>();
        for (int i = 0; i < route.getStops().size() - 1; i ++) {
            TreeNode<StopPriceHolder> node = new TreeNodeImpl<StopPriceHolder>();
            node.setData(new StopPriceHolder(route.getStops().get(i)));
            //the root node is the parent
            node.setParent(pricesTreeData);
            pricesTreeData.addChild("start" + node.getData().getStop().getStopId(), node);

            for (int j = i + 1; j < route.getStops().size(); j ++) {
                TreeNode<StopPriceHolder> subNode = new TreeNodeImpl<StopPriceHolder>();
                StopPriceHolder holder = new StopPriceHolder();
                holder.setStop(route.getStops().get(j));
                holder.setPrice(stopService.getPrice(node.getData().getStop().getStopId(),
                        holder.getStop().getStopId(), route));

                holder.setLeaf(true);
                subNode.setData(holder);
                subNode.setParent(node);
                node.addChild("end" + subNode.getData().getStop().getStopId(), subNode);
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

        SelectItemUtils.formSelectItems(DiscountType.class, discountTypeSelectItems);
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

    public TreeNode<StopPriceHolder> getPricesTreeData() {
        return pricesTreeData;
    }

    public void setPricesTreeData(TreeNode<StopPriceHolder> pricesTreeData) {
        this.pricesTreeData = pricesTreeData;
    }

    public Discount getDiscount() {
        return discount;
    }

    public void setDiscount(Discount discount) {
        this.discount = discount;
    }

    public List<SelectItem> getDiscountTypeSelectItems() {
        return discountTypeSelectItems;
    }

    public void setDiscountTypeSelectItems(List<SelectItem> discountTypeSelectItems) {
        this.discountTypeSelectItems = discountTypeSelectItems;
    }
}