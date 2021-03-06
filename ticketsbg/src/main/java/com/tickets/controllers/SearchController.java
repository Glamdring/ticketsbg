package com.tickets.controllers;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.component.UIInput;
import javax.faces.context.FacesContext;
import javax.faces.model.ListDataModel;

import org.richfaces.model.selection.SimpleSelection;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import com.tickets.annotations.Action;
import com.tickets.controllers.handlers.GMapHandler;
import com.tickets.controllers.handlers.SeatHandler;
import com.tickets.controllers.handlers.TransliterationHandler;
import com.tickets.controllers.security.AccessLevel;
import com.tickets.controllers.users.LoggedUserHolder;
import com.tickets.controllers.valueobjects.Screen;
import com.tickets.controllers.valueobjects.Step;
import com.tickets.exceptions.TicketCreationException;
import com.tickets.model.Customer;
import com.tickets.model.Discount;
import com.tickets.model.Firm;
import com.tickets.model.PassengerDetails;
import com.tickets.model.Price;
import com.tickets.model.Route;
import com.tickets.model.Run;
import com.tickets.model.SearchResultEntry;
import com.tickets.model.Stop;
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
@Scope("session")
public class SearchController extends BaseController {

    @Autowired
    private transient SearchService searchService;

    @Autowired
    private transient TicketService ticketService;

    @Autowired
    private transient StopService stopService;

    @Autowired
    private LoggedUserHolder loggedUserHolder;

    @Autowired
    private PurchaseController purchaseController;

    @Autowired
    private SeatController seatController;

    private TransliterationHandler transliterationHandler = new TransliterationHandler();

    private static final String TWO_WAY = "twoWay";
    private static final String ONE_WAY = "oneWay";

    private String fromStop;
    private String toStop;

    private GMapHandler mapHandler = new GMapHandler();

    private TicketCountsHolder ticketCountsHolder = new TicketCountsHolder();

    // A variable holding the selected stop in case
    // a purchase is made from the administration panel
    private String toStopPerPurchase;

    private int fromHour = 0;
    private int toHour = 24;
    private Date date = GeneralUtils.createCalendar().getTime();
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
    private String travelType = ONE_WAY;

    private SearchResultEntry selectedEntry;

    private SimpleSelection selection;

    // Needed to be Long by the option-button component
    private Long selectedRowId;

    private SearchResultEntry selectedReturnEntry;
    private SimpleSelection returnSelection;
    private Long selectedReturnRowId;

    private Ticket ticket;

    private int currentRunVacantSeats;

    private List<String> currentAvailableTargetStopNames = new ArrayList<String>();

    private boolean reRenderSeatChoices;

    private transient UIInput admin;

    private Ticket ticketToAlter;

    private boolean renderMaps = false;

    // Used only in admin panel
    private String customerName = "";

    @Action
    public String navigateToSearch() {
        resetSearchFields();
        return Screen.SEARCH_SCREEN.getOutcome();
    }

    @Action
    public String nagivationToAdminSearch() {
        resetSearchFields();
        return Screen.ADMIN_SEARCH_SCREEN.getOutcome();
    }

    @Action
    public String search() {
        // Clear the vacant seats cache so that it is recalculated
        ServiceFunctions.clearCache();

        if (isAdmin()) {
            return adminSearch();
        }

        return publicSearch();
    }

    private boolean isAdmin() {
        return admin != null && admin.getValue() != null
                && (admin.getValue().equals(Boolean.TRUE) || admin.getValue().equals("true"));
    }

    private String publicSearch() {

        if (!validateReturnDate(returnDate)) {
            addError("returnDateMustNotBeBeforeOutbound");
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

        List<SearchResultEntry> returnResult = null;
        if (travelType.equals(TWO_WAY) && returnDate != null) {
            returnResult = searchService.search(toStop,
                    fromStop, returnDate, returnFromHour, returnToHour,
                    returnTimeForDeparture, currentFirm);

            returnResultsModel = new ListDataModel(returnResult);
        }

        if (travelType.equals(TWO_WAY) && returnDate == null) {
            addError("choseReturnDate");
        }

        initilizeSingleSearchResultValues();

        if (result.size() > 0) {
            Run firstResult = result.get(0).getRun();
            Firm firm = firstResult.getRoute().getFirm();
            mapHandler.setFromMapUrl(stopService.getMapUrl(fromStop, firm));
            mapHandler.setToMapUrl(stopService.getMapUrl(toStop, firm));
        }

        purchaseController.setCurrentStep(Step.SEARCH_RESULTS);

        return Screen.SEARCH_RESULTS.getOutcome();
    }

    public BigDecimal getTotalPrice() {
        return ticketService.calculatePrice(selectedEntry,
                selectedReturnEntry,
                ticketCountsHolder);
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
        // Clear the vacant seats cache so that it is recalculated
        ServiceFunctions.clearCache();

        // Validation in the controller, because custom validators
        // for the complex UI would be an overhead

        // If nothing is chosen, return to the results page
        if (selectedEntry == null) {
            addError("mustSelectEntry");
            return null;
        }

        if (TWO_WAY.equals(travelType) && selectedReturnEntry == null) {
            addError("mustSelectReturnEntry");
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

        // If the user is staff-member, i.e. this is admin panel,
        // set the customer name, and mark the tickets as sold
        User user = loggedUserHolder.getLoggedUser();
        if (user != null && user.isStaff() && isAdmin()) {
            ticket.setCustomerInformation(new Customer());
            ticket.getCustomerInformation().setName(customerName);
            // setting to empty values in order to bypass validation on cascade
            ticket.getCustomerInformation().setEmail("");
            ticket.getCustomerInformation().setContactPhone("");

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

        // before redirecting to the payments page, reset the selections
        // in order to have a fresh page if a back button is pressed
        resetSelectionsPartial();
        purchaseController.setCurrentStep(Step.PAYMENT);
        return Screen.PAYMENT_SCREEN.getOutcome();

    }

    private Price findPrice(String fromStop, String toStopPerPurchase,
            Route route) {
        for (Price price : route.getPrices()) {
            if (price.getStartStop().getName().startsWith(fromStop)
                    && price.getEndStop().getName().startsWith(toStopPerPurchase)) {

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
        resetSelectionsPartial();
    }

    private void resetSelectionsPartial() {
        selectedEntry = null;
        selectedReturnEntry = null;
        selectedRowId = null;
        selectedReturnRowId = null;
        selection = null;
        seatController.setSeatHandler(null);
        seatController.setReturnSeatHandler(null);
        ticketCountsHolder = new TicketCountsHolder();
        reRenderSeatChoices = false;
        renderMaps = false;
        initilizeSingleSearchResultValues();
    }

    private void initilizeSingleSearchResultValues() {
        if (resultsModel != null && resultsModel.getRowCount() == 1) {
            selection = new SimpleSelection();
            selection.addKey(0);
            rowSelectionChanged();
        }
    }

    private void resetSearchFields() {
        fromStop = null;
        toStop = null;
        date = GeneralUtils.createCalendar().getTime();
        returnDate = null;
        toHour = 24;
        fromHour = 0;
        returnToHour = 24;
        returnFromHour = 0;
        timeForDeparture = true;
        returnTimeForDeparture = true;
        travelType = ONE_WAY;
        ticketToAlter = null;
        renderMaps = false;
    }

    public void setRenderMaps() {
        renderMaps = true;
    }

    public String toSearchScreen() {
        return Screen.SEARCH_SCREEN.getOutcome();
    }

    @PostConstruct
    public void init() {
        User user = loggedUserHolder.getLoggedUser();

        stopNames = searchService.listAllStops(user, getCurrentFirm());

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
        // resetting returns
        selectedReturnEntry = null;
        returnSelection = null;
        selectedReturnRowId = null;
        seatController.setReturnSeatHandler(null);


        selectedRowId = getSelectedIndex();
        selectedEntry = ((List<SearchResultEntry>) resultsModel
                .getWrappedData()).get(selectedRowId.intValue());

        List<Discount> discounts = searchService.getApplicableDiscounts(selectedEntry, fromStop, toStop);
        ticketCountsHolder.setTicketCounts(new ArrayList<TicketCount>(discounts.size()));

        for (Discount discount : discounts) {
            TicketCount tc = new TicketCount();
            tc.setDiscount(discount);
            tc.setNumberOfTickets(0);
            ticketCountsHolder.getTicketCounts().add(tc);
        }

        // pre-select the first option, for user-friendlyness
        // in case there is nothing for selection (the filter expression
        // leaves no appropriate result, this wouldn't cause problems
        if (TWO_WAY.equals(travelType)) {
            selectedReturnRowId = 0l;
//            returnSelection = new SimpleSelection();
//            returnSelection.addKey(0);
            returnRowSelectionChanged();
        }

        seatController.setSeatHandler(new SeatHandler(selectedEntry,
                ticketCountsHolder));
    }

    /**
     * Included for backward compatibility
     * @return
     */
    private long getSelectedIndex() {
        if (selection != null && selection.getKeys().hasNext()) {
            return new Long((Integer) selection.getKeys().next());
        } else {
            return selectedRowId;
        }
    }

    @SuppressWarnings("unchecked")
    public void returnRowSelectionChanged() {
        if (returnResultsModel == null) {
            return;
        }

        selectedReturnRowId = getSelectedReturnIndex();
        selectedReturnEntry = ((List<SearchResultEntry>) returnResultsModel
                .getWrappedData()).get(selectedReturnRowId.intValue());

        seatController.setReturnSeatHandler(new SeatHandler(
                selectedReturnEntry, ticketCountsHolder));
    }

    /**
     * Included for backward compatibility
     * @return
     */
    private long getSelectedReturnIndex() {
        if (returnSelection != null && returnSelection.getKeys().hasNext()) {
            return new Long((Integer) returnSelection.getKeys().next());
        } else {
            return selectedReturnRowId;
        }
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

        resetSelections();

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

        seatController.setSeatHandler(new SeatHandler(selectedEntry, ticketCountsHolder));

        // If there is a selected end stop for the search,
        // have it displayed in the panel
        toStopPerPurchase = toStop;

        // If there is only one option, and there hasn't been a pre-chosen
        // end stop, select the only option by default
        if (toStopPerPurchase == null && currentAvailableTargetStopNames.size() == 1) {
            toStopPerPurchase = currentAvailableTargetStopNames.get(0);
        }

        travelType = "ONE_WAY";
    }

    @Action(accessLevel = AccessLevel.CASH_DESK)
    public void purchaseTwoWayTicket() {
        // the same actions as for one way, with additional ones
        purchaseOneWayTicket();

        selectedReturnEntry = null;
        returnResultsModel = null;
        returnDate = null;
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

    @Action(accessLevel = AccessLevel.CASH_DESK)
    public void returnSelected() {
        seatController.setReturnSeatHandler(new SeatHandler(selectedReturnEntry, ticketCountsHolder));
    }
    /* ------------------- GETTERS AND SETTERS FOLLOW ---------------- */
    public String getFromStop() {
        return fromStop;
    }

    public void setFromStop(String fromStop) {
        if (needsTransliteration()) {
            fromStop = transliterationHandler.getCyrillicOriginal(fromStop);
        }
        this.fromStop = fromStop;
    }

    public String getToStop() {
        return toStop;
    }

    public void setToStop(String toStop) {
        if (needsTransliteration()) {
            toStop = transliterationHandler.getCyrillicOriginal(toStop);
        }
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
        if (isAdmin()) {
            // in admin panel, refresh
            stopNames = searchService.listAllStops(loggedUserHolder
                    .getLoggedUser(), getCurrentFirm());
        }

        // TODO cache?
        if (needsTransliteration()) {
            stopNames = transliterateStopNames(stopNames);
        }

        return stopNames;
    }

    private List<String> transliterateStopNames(List<String> stopNames) {
        List<String> transliterated = new ArrayList<String>(stopNames.size());
        for (String stopName : stopNames) {
            transliterated.add(transliterationHandler.toLatin(stopName));
        }

        return transliterated;
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
        // If internal extendedDataTable mechanisms trigger a setter with
        // the first row, set it also as selectedId
        if (selectedReturnRowId == null) {
            Iterator keys = returnSelection.getKeys();
            if (keys.hasNext()) {
                selectedReturnRowId = ((Integer) keys.next()).longValue();
            }
        }
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

    public Ticket getTicketToAlter() {
        return ticketToAlter;
    }

    public void setTicketToAlter(Ticket ticketToAlter) {
        this.ticketToAlter = ticketToAlter;
    }

    public boolean isRenderMaps() {
        return renderMaps;
    }

    public void setRenderMaps(boolean renderMaps) {
        this.renderMaps = renderMaps;
    }

    public String getCustomerName() {
        return customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    /**
     * Get the actual stop name for the given start stop. They can differ in
     * cases like "Sofia - central" (actual) and "Sofia" (searched)
     *
     * @return the actual stop name
     */
    public String getActualFromStop() {
        if (selectedEntry != null) {
            Stop stop = ServiceFunctions.getStopByName(fromStop, selectedEntry.getRun().getRoute().getStops());
            return stop.getName();
        }
        return "";
    }

    private boolean needsTransliteration() {
        //todo variable
        return !FacesContext.getCurrentInstance().getViewRoot().getLocale().equals(GeneralUtils.getDefaultLocale());
    }
}