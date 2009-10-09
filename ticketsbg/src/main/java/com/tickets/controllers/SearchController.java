package com.tickets.controllers;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.component.UIInput;
import javax.faces.model.ListDataModel;

import org.apache.myfaces.orchestra.conversation.annotations.ConversationName;
import org.richfaces.model.selection.SimpleSelection;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import com.tickets.annotations.Action;
import com.tickets.controllers.handlers.GMapHandler;
import com.tickets.controllers.handlers.SeatHandler;
import com.tickets.controllers.security.AccessLevel;
import com.tickets.controllers.users.LoggedUserHolder;
import com.tickets.controllers.valueobjects.Screen;
import com.tickets.controllers.valueobjects.Step;
import com.tickets.exceptions.TicketCreationException;
import com.tickets.model.Discount;
import com.tickets.model.Firm;
import com.tickets.model.PassengerDetails;
import com.tickets.model.Price;
import com.tickets.model.Route;
import com.tickets.model.Run;
import com.tickets.model.SearchResultEntry;
import com.tickets.model.Ticket;
import com.tickets.model.User;
import com.tickets.services.SearchService;
import com.tickets.services.ServiceFunctions;
import com.tickets.services.StopService;
import com.tickets.services.TicketService;
import com.tickets.services.valueobjects.Seat;
import com.tickets.services.valueobjects.TicketCount;
import com.tickets.services.valueobjects.TicketCountsHolder;
import com.tickets.utils.GeneralUtils;

/**
 * Controller which handles the search process.
 *
 * @author Bozhidar Bozhanov
 *
 */
@Controller("searchController")
@Scope("conversation.manual")
@ConversationName("purchaseConversation")
public class SearchController extends BaseController {

    @Autowired
    private SearchService searchService;

    @Autowired
    private TicketService ticketService;

    @Autowired
    private StopService stopService;

    @Autowired
    private LoggedUserHolder loggedUserHolder;

    @Autowired
    private PurchaseController purchaseController;

    @Autowired
    private SeatController seatController;

    private static final String TWO_WAY = "twoWay";

    private String fromStop;
    private String toStop;

    private GMapHandler mapHandler = new GMapHandler();

    private TicketCountsHolder ticketCountsHolder = new TicketCountsHolder();

    // A variable holding the selected stop in case
    // a purchase is made from the administration panel
    private String toStopPerPurchase;

    private int fromHour = 0;
    private int toHour = 24;
    private Date date = GeneralUtils.createEmptyCalendar().getTime();
    private boolean timeForDeparture = true;

    private int returnFromHour = 0;
    private int returnToHour = 24;
    private Date returnDate;
    private boolean returnTimeForDeparture = true;

    private ListDataModel resultsModel;
    private ListDataModel returnResultsModel;

    private List<String> stopNames;
    private List<String> toStopNames;

    private Integer[] hoursFrom;
    private Integer[] hoursTo;
    private String travelType = TWO_WAY;

    private SearchResultEntry selectedEntry;

    private SimpleSelection selection;

    private Long selectedRowId;

    private SearchResultEntry selectedReturnEntry;
    private SimpleSelection returnSelection;
    private Long selectedReturnRowId;

    private Ticket ticket;

    private int currentRunVacantSeats;

    private List<String> currentAvailableTargetStopNames = new ArrayList<String>();

    private boolean reRenderSeatChoices;

    private UIInput admin;

    private UIInput alterTicketIdField;

    private Ticket ticketToAlter;

    @Action
    public String navigateToSearch() {
        resetSearchFields();
        return Screen.SEARCH_SCREEN.getOutcome();
    }

    @Action
    public String search() {
        if (isAdmin()) {
            return adminSearch();
        }

        return publicSearch();
    }

    private boolean isAdmin() {
        return admin.getValue() != null
                && admin.getValue().equals(Boolean.TRUE);
    }

    public Ticket getTicketToAlter() {
        if (alterTicketIdField == null) {
            return null;
        }

        if (ticketToAlter == null && alterTicketIdField.getValue() != null) {
            int id = Integer.parseInt((String) alterTicketIdField.getValue());
            Ticket ticket = ticketService.get(Ticket.class, id);
            ticketToAlter = ticket;
        }
        return ticketToAlter;
    }

    private String publicSearch() {

        if (!validateReturnDate(returnDate)) {
            addError("returnDateMustNotBeBeforeOutward");
            return null;
        }

        // resetting, in case the conversation hasn't ended
        resetSelections();

        Firm currentFirm = getCurrentFirm();

        // In case this was a search for ticket alteration
        // allow only the firm from the original ticket
        Ticket ticketToAlter = getTicketToAlter();
        if (ticketToAlter != null) {
            currentFirm = ticketToAlter.getRun().getRoute().getFirm();
            loadTicketCounts();
        }

        List<SearchResultEntry> result = searchService.search(fromStop, toStop,
                date, fromHour, toHour, timeForDeparture, currentFirm);

        resultsModel = new ListDataModel(result);

        if (travelType.equals(TWO_WAY) && returnDate != null) {
            List<SearchResultEntry> returnResult = searchService.search(toStop,
                    fromStop, returnDate, returnFromHour, returnToHour,
                    returnTimeForDeparture, currentFirm);

            returnResultsModel = new ListDataModel(returnResult);
        }

        if (travelType.equals(TWO_WAY) && returnDate == null) {
            addError("choseReturnDate");
        }

        if (result.size() == 1) {
            selection = new SimpleSelection();
            selection.addKey(0);
            rowSelectionChanged();
        }

        if (result.size() > 0) {
            Run firstResult = result.get(0).getRun();
            Firm firm = firstResult.getRoute().getFirm();
            mapHandler.setFromMapUrl(stopService.getMapUrl(fromStop, firm));
            mapHandler.setToMapUrl(stopService.getMapUrl(toStop, firm));
        }

        purchaseController.setCurrentStep(Step.SEARCH_RESULTS);

        return Screen.SEARCH_RESULTS.getOutcome();
    }

    private void loadTicketCounts() {
        int regularTickets = 0;
        for (PassengerDetails pd : ticketToAlter.getPassengerDetails()) {
            if (pd.getDiscount() == null) {
                regularTickets ++;
            } else {
                TicketCount tc = new TicketCount();
                tc.setDiscount(pd.getDiscount());
                int idx = ticketCountsHolder.getTicketCounts().indexOf(tc);
                if (idx > -1) {
                    tc = ticketCountsHolder.getTicketCounts().get(idx);
                    tc.setNumberOfTickets(tc.getNumberOfTickets() + 1);
                    ticketCountsHolder.getTicketCounts().set(idx, tc);
                } else {
                    tc.setNumberOfTickets(tc.getNumberOfTickets() + 1);
                    ticketCountsHolder.getTicketCounts().add(tc);
                }
            }
        }
        ticketCountsHolder.setRegularTicketsCount(currentRunVacantSeats);
    }

    @Action
    public void filterToStops() {
        if (fromStop != null && fromStop.length() > 0) {
            List<String> result = searchService.listAllEndStops(fromStop,
                    loggedUserHolder.getLoggedUser(), getCurrentFirm());
            if (result.size() > 0) {
                toStopNames = result;
            }
        }
    }

    @Action
    public String proceed() {
        // Validation in the controller, because custom validators
        // for the complex UI would be an overhead

        // If nothing is chosen, return to the results page
        if (selectedEntry == null) {
            addError("mustSelectEntry");
            return null;
        }

        // Creating many ticket instances
        List<Seat> selectedSeats = seatController.getSeatHandler()
                .getSelectedSeats();
        List<Seat> selectedReturnSeats = null;

        if (seatController.getReturnSeatHandler() != null) {
            selectedReturnSeats = seatController.getReturnSeatHandler()
                    .getSelectedSeats();
        }

        try {
            ticket = getTicketToAlter();
            if (ticket == null) {
                ticket = ticketService.createTicket(selectedEntry, selectedReturnEntry,
                    ticketCountsHolder, selectedSeats, selectedReturnSeats);
            } else {

                ticketService.alterTicket(ticket, selectedEntry, selectedReturnEntry,
                        ticketCountsHolder, selectedSeats, selectedReturnSeats);

                if (ticket.getAlterationPriceDifference().compareTo(BigDecimal.ZERO) < 0) {
                    addMessage("priceDiffereceIsSubstracted");
                }
            }
        } catch (TicketCreationException ex) {
            addError(ex.getMessageKey(), ex.getArguments());
            if (ex.isRedoSearch()) {
                return search();
            }
            return null;
        }

        purchaseController.getTickets().add(ticket);

        // If the user is staff-member, just mark the tickets as sold
        User user = loggedUserHolder.getLoggedUser();
        if (user != null && user.isStaff()) {
            purchaseController.finalizePurchase(user);
            // redo the search
            adminSearch();
            return null; // returning null, because the action is
                        // called from modal window with ajax
        }

        if (ticket.isAltered() && ticket.getAlterationPriceDifference().compareTo(BigDecimal.ZERO) == 0) {
            purchaseController.finalizePurchase(user);
            return navigateToSearch();
        }

        purchaseController.setCurrentStep(Step.PAYMENT);
        return Screen.PAYMENT_SCREEN.getOutcome();

    }

    private Price findPrice(String fromStop, String toStopPerPurchase,
            Route route) {
        for (Price price : route.getPrices()) {
            if (price.getStartStop().getName().equals(fromStop)
                    && price.getEndStop().getName().equals(toStopPerPurchase)) {

                return price;
            }
        }

        return null;
    }

    /**
     * Cancellation of partially failed purchases not possible anymore
     */
    @Deprecated
    public String cancelTickets() {
        return Screen.SEARCH_SCREEN.getOutcome();
    }

    /**
     * Partial purchases not possible anymore
     */
    @Deprecated
    public String confirmPartialPurchase() {
        purchaseController.getTickets().add(ticket);
        ticket = null;
        return Screen.PAYMENT_SCREEN.getOutcome();
    }

    private void resetSelections() {
        returnResultsModel = null;
        selectedEntry = null;
        selectedReturnEntry = null;
        selectedRowId = null;
        selectedReturnRowId = null;
        selection = null;
        seatController.setSeatHandler(null);
        seatController.setReturnSeatHandler(null);
        ticketCountsHolder = new TicketCountsHolder();
        reRenderSeatChoices = false;
    }

    private void resetSearchFields() {
        fromStop = null;
        toStop = null;
        date = GeneralUtils.createEmptyCalendar().getTime();
        returnDate = GeneralUtils.createEmptyCalendar().getTime();
        toHour = 24;
        fromHour = 0;
        returnToHour = 24;
        returnFromHour = 0;
        timeForDeparture = true;
        returnTimeForDeparture = true;
        travelType = TWO_WAY;
    }

    public String toSearchScreen() {
        return Screen.SEARCH_SCREEN.getOutcome();
    }

    @PostConstruct
    public void init() {
        User user = loggedUserHolder.getLoggedUser();

        stopNames = searchService.listAllStops(user, getCurrentFirm());
        // toStopNames = searchService.listAllStops(user);

        // Setting a default origin
        if (user != null) {
            fromStop = loggedUserHolder.getLoggedUser().getCity();
            filterToStops();
        }

        hoursFrom = new Integer[24];
        for (int i = 0; i < hoursFrom.length; i++) {
            hoursFrom[i] = i;
        }
        hoursTo = new Integer[24];
        for (int i = 0; i < hoursTo.length; i++) {
            hoursTo[i] = i + 1;
        }
    }

    @SuppressWarnings("unchecked")
    public void rowSelectionChanged() {
        Integer selectedId = (Integer) selection.getKeys().next();
        selectedRowId = new Long(selectedId);
        selectedEntry = ((List<SearchResultEntry>) resultsModel
                .getWrappedData()).get(selectedId);

        ticketCountsHolder.setTicketCounts(new ArrayList<TicketCount>(
                selectedEntry.getRun().getRoute().getDiscounts().size()));

        for (Discount discount : selectedEntry.getRun().getRoute()
                .getDiscounts()) {
            TicketCount tc = new TicketCount();
            tc.setDiscount(discount);
            tc.setNumberOfTickets(0);
            ticketCountsHolder.getTicketCounts().add(tc);
        }

        seatController.setSeatHandler(new SeatHandler(selectedEntry,
                ticketCountsHolder));
    }

    @SuppressWarnings("unchecked")
    public void returnRowSelectionChanged() {
        Integer selectedId = (Integer) returnSelection.getKeys().next();
        selectedReturnRowId = new Long(selectedId);
        selectedReturnEntry = ((List<SearchResultEntry>) returnResultsModel
                .getWrappedData()).get(selectedId);

        seatController.setReturnSeatHandler(new SeatHandler(
                selectedReturnEntry, ticketCountsHolder));
    }

    public boolean validateReturnDate(Date date) {
        if (!travelType.equals(TWO_WAY)) {
            return true;
        }

        if (date == null)
            return true;

        if (date.compareTo(this.date) < 0) {
            return false;
        }

        return true;
    }


    /* -------------- ADMIN PANEL METHODS FOLLOW ------------------ */

    private String adminSearch() {
        // Clear the vacant seats cache so that it is recalculated
        ServiceFunctions.clearCache();

        if (toStop != null && toStop.equals("")) {
            toStop = null;
        }

        List<SearchResultEntry> result = searchService.adminSearch(
                loggedUserHolder.getLoggedUser(), fromStop, toStop, date,
                fromHour, toHour, timeForDeparture);

        resultsModel = new ListDataModel(result);

        return Screen.ADMIN_SEARCH_RESULTS.getOutcome();
    }

    @Action(accessLevel = AccessLevel.CASH_DESK)
    public void purchaseOneWayTicket() {
        currentAvailableTargetStopNames = searchService
                .listAllEndStopsForRoute(fromStop, selectedEntry.getRun()
                        .getRoute());

        seatController.setSeatHandler(new SeatHandler(selectedEntry));

        // If there is a selected end stop for the search,
        // have it displayed in the panel
        toStopPerPurchase = toStop;

        travelType = "ONE_WAY";
    }

    @Action(accessLevel = AccessLevel.CASH_DESK)
    public void purchaseTwoWayTicket() {
        // the same actions as for one way, with additional ones
        purchaseOneWayTicket();

        selectedReturnEntry = null;
        returnResultsModel = null;
        travelType = "TWO_WAY";
    }

    @Action(accessLevel = AccessLevel.CASH_DESK)
    public void returnDateSelected() {
        returnResultsModel = new ListDataModel(searchService.adminSearch(
                loggedUserHolder.getLoggedUser(), toStopPerPurchase, fromStop,
                returnDate, 0, 24, true));
    }

    @Action(accessLevel = AccessLevel.CASH_DESK)
    public void toStopSelected() {
        // If the search has been from the admin panel,
        // and no target stop has been chosen, locate the appropriate price
        selectedEntry.setPrice(findPrice(fromStop, toStopPerPurchase,
                selectedEntry.getRun().getRoute()));
    }

    /* ------------------- GETTERS AND SETTERS FOLLOW ---------------- */
    public String getFromStop() {
        return fromStop;
    }

    public void setFromStop(String fromStop) {
        this.fromStop = fromStop;
    }

    public String getToStop() {
        return toStop;
    }

    public void setToStop(String toStop) {
        this.toStop = toStop;
    }

    public int getFromHour() {
        return fromHour;
    }

    public void setFromHour(int fromHour) {
        this.fromHour = fromHour;
    }

    public int getToHour() {
        return toHour;
    }

    public void setToHour(int toHour) {
        this.toHour = toHour;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public ListDataModel getResultsModel() {
        return resultsModel;
    }

    public void setResultsModel(ListDataModel resultsModel) {
        this.resultsModel = resultsModel;
    }

    public List<String> getStopNames() {
        return stopNames;
    }

    public void setStopNames(List<String> stopNames) {
        this.stopNames = stopNames;
    }

    public boolean isTimeForDeparture() {
        return timeForDeparture;
    }

    public void setTimeForDeparture(boolean isTimeForDeparture) {
        this.timeForDeparture = isTimeForDeparture;
    }

    public Integer[] getHoursFrom() {
        return hoursFrom;
    }

    public void setHoursFrom(Integer[] hoursFrom) {
        this.hoursFrom = hoursFrom;
    }

    public Integer[] getHoursTo() {
        return hoursTo;
    }

    public void setHoursTo(Integer[] hoursTo) {
        this.hoursTo = hoursTo;
    }

    public String getTravelType() {
        return travelType;
    }

    public void setTravelType(String travelType) {
        this.travelType = travelType;
    }

    public int getReturnFromHour() {
        return returnFromHour;
    }

    public void setReturnFromHour(int returnFromHour) {
        this.returnFromHour = returnFromHour;
    }

    public int getReturnToHour() {
        return returnToHour;
    }

    public void setReturnToHour(int returnToHour) {
        this.returnToHour = returnToHour;
    }

    public Date getReturnDate() {
        return returnDate;
    }

    public void setReturnDate(Date returnDate) {
        this.returnDate = returnDate;
    }

    public boolean isReturnTimeForDeparture() {
        return returnTimeForDeparture;
    }

    public void setReturnTimeForDeparture(boolean returnTimeForDeparture) {
        this.returnTimeForDeparture = returnTimeForDeparture;
    }

    public ListDataModel getReturnResultsModel() {
        return returnResultsModel;
    }

    public void setReturnResultsModel(ListDataModel returnResultsModel) {
        this.returnResultsModel = returnResultsModel;
    }

    public SearchService getSearchService() {
        return searchService;
    }

    public void setSearchService(SearchService searchService) {
        this.searchService = searchService;
    }

    public SimpleSelection getSelection() {
        return selection;
    }

    public void setSelection(SimpleSelection selection) {
        this.selection = selection;
    }

    public Long getSelectedRowId() {
        return selectedRowId;
    }

    public void setSelectedRowId(Long selectedRowId) {
        this.selectedRowId = selectedRowId;
    }

    public SimpleSelection getReturnSelection() {
        return returnSelection;
    }

    public void setReturnSelection(SimpleSelection returnSelection) {
        this.returnSelection = returnSelection;
    }

    public Long getSelectedReturnRowId() {
        return selectedReturnRowId;
    }

    public void setSelectedReturnRowId(Long selectedReturnRowId) {
        this.selectedReturnRowId = selectedReturnRowId;
    }

    public SearchResultEntry getSelectedEntry() {
        return selectedEntry;
    }

    public void setSelectedEntry(SearchResultEntry selectedEntry) {
        this.selectedEntry = selectedEntry;
    }

    public SearchResultEntry getSelectedReturnEntry() {
        return selectedReturnEntry;
    }

    public void setSelectedReturnEntry(SearchResultEntry selectedReturnEntry) {
        this.selectedReturnEntry = selectedReturnEntry;
    }

    public List<TicketCount> getTicketCounts() {
        return ticketCountsHolder.getTicketCounts();
    }

    public void setTicketCounts(List<TicketCount> ticketCounts) {
        ticketCountsHolder.setTicketCounts(ticketCounts);
    }

    public int getRegularTicketsCount() {
        return ticketCountsHolder.getRegularTicketsCount();
    }

    public void setRegularTicketsCount(int regularTicketsCount) {
        ticketCountsHolder.setRegularTicketsCount(regularTicketsCount);
    }

    public Ticket getTicket() {
        return ticket;
    }

    public void setTicket(Ticket ticket) {
        this.ticket = ticket;
    }

    public List<String> getToStopNames() {
        return toStopNames;
    }

    public void setToStopNames(List<String> toStopNames) {
        this.toStopNames = toStopNames;
    }

    public int getCurrentRunVacantSeats() {
        return currentRunVacantSeats;
    }

    public void setCurrentRunVacantSeats(int currentRunVacantSeats) {
        this.currentRunVacantSeats = currentRunVacantSeats;
    }

    public UIInput getAdmin() {
        return admin;
    }

    public void setAdmin(UIInput admin) {
        this.admin = admin;
    }

    public List<String> getCurrentAvailableTargetStopNames() {
        return currentAvailableTargetStopNames;
    }

    public void setCurrentAvailableTargetStopNames(
            List<String> currentAvailableTargetStopNames) {
        this.currentAvailableTargetStopNames = currentAvailableTargetStopNames;
    }

    public String getToStopPerPurchase() {
        return toStopPerPurchase;
    }

    public void setToStopPerPurchase(String toStopPerPurchase) {
        this.toStopPerPurchase = toStopPerPurchase;
    }

    public GMapHandler getMapHandler() {
        return mapHandler;
    }

    public void setMapHandler(GMapHandler mapHandler) {
        this.mapHandler = mapHandler;
    }

    public TicketCountsHolder getTicketCountsHolder() {
        return ticketCountsHolder;
    }

    public void setTicketCountsHolder(TicketCountsHolder ticketCountsHolder) {
        this.ticketCountsHolder = ticketCountsHolder;
    }

    public boolean isReRenderSeatChoices() {
        return reRenderSeatChoices;
    }

    public void setReRenderSeatChoices(boolean reRenderSeatChoices) {
        this.reRenderSeatChoices = reRenderSeatChoices;
    }

    public UIInput getAlterTicket() {
        return alterTicketIdField;
    }

    public void setAlterTicket(UIInput alterTicket) {
        this.alterTicketIdField = alterTicket;
    }
}