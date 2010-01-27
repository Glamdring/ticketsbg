package com.tickets.controllers;

import java.io.Serializable;
import java.lang.reflect.Method;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;
import javax.faces.model.ListDataModel;
import javax.faces.model.SelectItem;

import org.ajax4jsf.component.UIDataAdaptor;
import org.apache.myfaces.orchestra.conversation.annotations.ConversationName;
import org.richfaces.component.UIOrderingList;
import org.richfaces.component.UITree;
import org.richfaces.component.html.HtmlOrderingList;
import org.richfaces.component.html.HtmlTree;
import org.richfaces.event.NodeSelectedEvent;
import org.richfaces.model.ListRowKey;
import org.richfaces.model.OrderingListDataModel;
import org.richfaces.model.TreeNode;
import org.richfaces.model.TreeNodeImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import com.tickets.annotations.Action;
import com.tickets.constants.Messages;
import com.tickets.controllers.handlers.SeatHandler;
import com.tickets.controllers.security.AccessLevel;
import com.tickets.controllers.users.LoggedUserHolder;
import com.tickets.controllers.valueobjects.Screen;
import com.tickets.model.Day;
import com.tickets.model.Discount;
import com.tickets.model.DiscountType;
import com.tickets.model.GenericDiscount;
import com.tickets.model.Price;
import com.tickets.model.Route;
import com.tickets.model.SeatSettings;
import com.tickets.model.Stop;
import com.tickets.model.StopPriceHolder;
import com.tickets.model.User;
import com.tickets.model.Vehicle;
import com.tickets.services.DiscountService;
import com.tickets.services.RouteService;
import com.tickets.services.StopService;
import com.tickets.utils.SelectItemUtils;

@Controller("routeController")
@Scope("conversation.manual")
@ConversationName("routesAndRuns")
@Action(accessLevel = AccessLevel.FIRM_COORDINATOR)
public class RouteController extends BaseController implements Serializable {

    static final Integer[] DEFAULT_SECOND_DOOR_SEATS = new Integer[] {
            27, 28 };

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

    private List<String> existingStopNames = new ArrayList<String>();

    private List<String> genericDiscountNames = new ArrayList<String>();

    private String currentStopMapAddress = "";

    private Vehicle selectedVehicle;

    private UIOrderingList stopsOrderingList;

    private List<Stop> stopsInitialList;

    @Autowired
    private SeatController seatController;

    @Autowired
    private DiscountService discountService;

    @Autowired
    private StopService stopService;

    @Autowired
    private RouteService routeService;

    @Autowired
    private LoggedUserHolder loggedUserHolder;

    @Action
    public String save() {
        if (route.getSellSeatsTo() == 0) {
            route.setSellSeatsTo(route.getSeats());
        }
        if (route.getSellSeatsFrom() == 0) {
            route.setSellSeatsFrom(1);
        }
        if (route.getSeats() > route.getSellSeatsTo()) {
            route.setSellSeatsTo(route.getSeats());
        }

        routeService.save(route, Arrays.asList(daysPickList));
        refreshList();
        return Screen.ROUTES_LIST.getOutcome();
    }

    @Action
    public String edit() {
        route = (Route) routesModel.getRowData();
        stopsInitialList = new ArrayList<Stop>(route.getStops());
        daysPickList = routeService.getDaysList(route);
        seatController.setSeatHandler(new SeatHandler(route));
        if (route.getPrices().size() == 0) {
            stopService.cascadePrices(route);
        }

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
        stopsInitialList = new ArrayList<Stop>();
        route = new Route();
        route.setFirm(loggedUserHolder.getLoggedUser().getFirm());
        route.setRequireReceiptAtCashDesk(route.getFirm().isRequireReceiptAtCashDesk());
        route.setAllowSeatChoice(!route.getFirm().isHasAnotherTicketSellingSystem());
        route.getSeatSettings().getMissingSeats().addAll(Arrays.asList(DEFAULT_SECOND_DOOR_SEATS));
        return Screen.ROUTE_SCREEN.getOutcome();
    }

    @Action
    public void addHour() {
        routeService.addHourToRoute(hour, route);
    }

    @Action
    public void removeHour() {
        // If removing a persistent entity, call the service
        // else, the supplied selection is a negative number,
        // so just remove the .abs(..) position from the list
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
        currentStopMapAddress = "";
    }

    @Action
    public void saveStop() {
        stopService.addStopToRoute(stop, route);
        stopService.saveMapAddress(stop.getName(),
                currentStopMapAddress,
                loggedUserHolder.getLoggedUser().getFirm());
        listReordered();

        // re-setting, because then, after submission of the form,
        // the Stop objects are checked against the "value" attribute
        // (which evaluates to stopsInitialList), and as the hashCode()
        // and equals() methods are using a business key, the check would
        // fail if a stop was edited
        stopsInitialList = route.getStops();
    }

    @Action
    public void deleteStop() {
        Stop stop = (Stop) stopsTable.getRowData();
        stopService.delete(stop, route);
        //see saveStop comment for more details on this
        stopsInitialList = route.getStops();
    }

    @Action
    public void addDiscount() {
        discount = new Discount();
        discount.setStartStop(route.getStops().get(0).getName());
        discount.setEndStop(route.getStops().get(route.getStops().size() - 1).getName());
    }

    @Action
    public void saveDiscount() {
        if (!route.getDiscounts().contains(discount)) {
            route.addDiscount(discount);
        }
    }

    @Action
    public void deleteDiscount() {
        route.getDiscounts().remove(discount);
    }

    @Action
    public String cancel() {
        endConversation();
        refreshList();
        return Screen.ROUTES_LIST.getOutcome();
    }

    @Action
    public void savePrice() {
        if (price != null) {
            price.setPrice(priceValue);
            price.setTwoWayPrice(twoWayPriceValue);
        }
    }

    //no longer in use
    @Deprecated
    @Action
    public Boolean getExpandedNodes(UITree tree) {
        if (tree.getChildCount() <= 4) {
            return Boolean.TRUE;
        }

        return Boolean.FALSE;
    }

    @Action
    public void stopNameSelected() {
        currentStopMapAddress = stopService.getMapUrl(stop.getName(),
                loggedUserHolder.getLoggedUser().getFirm());
    }

    @Action
    public void genericDiscountSelected() {
        GenericDiscount gd = discountService.findGenericDiscount(
                discount.getName(),
                loggedUserHolder.getLoggedUser().getFirm());
        discount.setDescription(gd.getDescription());
    }

    @SuppressWarnings({ "unchecked", "cast" })
    public void listReordered() {

        // This is a hack. But a working one. Should be cleared
        // whenever there is time to understand the functioning
        // of rich:orderingList better

        System.out.println(stopsTable.getValue());
        Collection<Stop> collectionOfStops = null;
        try {
            Method m = UIDataAdaptor.class.getDeclaredMethod("getExtendedDataModel");
            m.setAccessible(true);
            Object dataModel = m.invoke(stopsTable);
            if (dataModel instanceof OrderingListDataModel) {
                Map model = (Map) ((OrderingListDataModel) dataModel).getWrappedData();
                collectionOfStops = (Collection<Stop>) model.values();
            }

            if (collectionOfStops == null) {
                return;
            }
        } catch (Exception ex) {
            ex.printStackTrace();
            return;
        }
        stopService.listReoredered(route, collectionOfStops);

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
                } catch (Exception ex) {
                    // Ignore
                    // No excepetion is expected to be thrown,
                    // but in previous versions of RichFaces,
                    // an IOException was thrown, so retaining
                    // the clause just in case
                }
            } else {
                price = stopService.getPrice(rowKey, route);
                //cloning
                priceValue = new BigDecimal(price.getPrice().toString());
                twoWayPriceValue = new BigDecimal(price.getTwoWayPrice()
                        .toString());
            }
        }
    }

    public void refreshTreeModel() {
        pricesTreeData = new TreeNodeImpl<StopPriceHolder>();
        for (int i = 0; i < route.getStops().size() - 1; i++) {
            TreeNode<StopPriceHolder> node = new TreeNodeImpl<StopPriceHolder>();
            node.setData(new StopPriceHolder(route.getStops().get(i)));
            // the root node is the parent
            node.setParent(pricesTreeData);
            pricesTreeData.addChild("start"
                    + node.getData().getStop().getStopId(), node);

            for (int j = i + 1; j < route.getStops().size(); j++) {
                TreeNode<StopPriceHolder> subNode = new TreeNodeImpl<StopPriceHolder>();
                StopPriceHolder holder = new StopPriceHolder();
                holder.setStop(route.getStops().get(j));
                holder.setPrice(stopService.getPrice(node.getData().getStop()
                        .getStopId(), holder.getStop().getStopId(), route));

                holder.setLeaf(true);
                subNode.setData(holder);
                subNode.setParent(node);
                node.addChild("end" + subNode.getData().getStop().getStopId(),
                        subNode);
            }
        }

    }

    @PostConstruct
    public void init() {
        if (loggedUserHolder.getLoggedUser() == null) {
            return;
        }

        refreshList();
        route = new Route();
        days = routeService.list(Day.class);
        existingStopNames = stopService.getExistingStopNames(
                loggedUserHolder.getLoggedUser().getFirm());

        List<GenericDiscount> genDiscounts = discountService.getGenericDiscounts(
                loggedUserHolder.getLoggedUser().getFirm());

        genericDiscountNames = new ArrayList<String>(genDiscounts.size());
        for (GenericDiscount gd : genDiscounts) {
            genericDiscountNames.add(gd.getName());
        }

        for (Day day : days) {
            day.setLabel(Messages.getString(day.getName()));
            dayNames.put(day.getName(), day.getLabel());
        }

        discountTypeSelectItems = SelectItemUtils
                .formSelectItems(DiscountType.class);

    }

    private void refreshList() {
        User user = LoggedUserHolder.getUser();
        if (user != null) {
            routesModel = new ListDataModel(routeService.list(user.getFirm()));
        }

        // End the current conversation in case the list of routes
        // is refreshed, but only if the bean has not just been constructed
        if (route != null) {
            endConversation();
        }
    }

    public void copyVehicleSettings() {
        if (selectedVehicle != null) {
            route.setSeats(selectedVehicle.getSeats());
            route.setSeatSettings(selectedVehicle.getSeatSettings());
            seatController.getSeatHandler().refreshRows();
        } else {
            route.setSeats(51);
            route.setSeatSettings(new SeatSettings());
        }
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
        setCurrentStopMapAddress(stopService.getMapUrl(stop.getName(),
                loggedUserHolder.getLoggedUser().getFirm()));
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

    public void setDiscountTypeSelectItems(
            List<SelectItem> discountTypeSelectItems) {
        this.discountTypeSelectItems = discountTypeSelectItems;
    }

    public List<String> getExistingStopNames() {
        return existingStopNames;
    }

    public void setExistingStopNames(List<String> existingStopNames) {
        this.existingStopNames = existingStopNames;
    }

    public String getCurrentStopMapAddress() {
        return currentStopMapAddress;
    }

    public void setCurrentStopMapAddress(String currentStopMapAddress) {
        this.currentStopMapAddress = currentStopMapAddress;
    }

    public List<String> getGenericDiscountNames() {
        return genericDiscountNames;
    }

    public void setGenericDiscountNames(List<String> genericDiscountNames) {
        this.genericDiscountNames = genericDiscountNames;
    }

    public Vehicle getSelectedVehicle() {
        return selectedVehicle;
    }

    public void setSelectedVehicle(Vehicle selectedVehicle) {
        this.selectedVehicle = selectedVehicle;
    }

    public UIOrderingList getStopsOrderingList() {
        return stopsOrderingList;
    }

    public void setStopsOrderingList(UIOrderingList stopsOrderingList) {
        this.stopsOrderingList = stopsOrderingList;
    }

    public List<Stop> getStopsInitialList() {
        return stopsInitialList;
    }

    public void setStopsInitialList(List<Stop> stopsInitialList) {
        this.stopsInitialList = stopsInitialList;
    }

    public List<SelectItem> getStopSelectItems() {
        return SelectItemUtils.formStringSelectItems(route.getStops(), true);
    }
}