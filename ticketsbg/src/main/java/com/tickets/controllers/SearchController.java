package com.tickets.controllers;

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
import com.tickets.controllers.users.LoggedUserHolder;
import com.tickets.model.Discount;
import com.tickets.model.Price;
import com.tickets.model.Route;
import com.tickets.model.SearchResultEntry;
import com.tickets.model.Ticket;
import com.tickets.model.TicketCount;
import com.tickets.model.User;
import com.tickets.services.SearchService;
import com.tickets.services.ServiceFunctions;
import com.tickets.services.TicketService;
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
    private LoggedUserHolder loggedUserHolder;

    @Autowired
    private PurchaseController purchaseController;

    @Autowired
    private SeatController seatController;

    private static final String TWO_WAY = "twoWay";

    private String fromStop;
    private String toStop;

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

    private List<TicketCount> ticketCounts = new ArrayList<TicketCount>();
    // defaults to 1 for user-friendliness, as this is the most common case
    private int regularTicketsCount = 1;

    private List<Ticket> tickets;
    private boolean proposeCancellation;

    private int currentRunVacantSeats;

    private List<String> currentAvailableTargetStopNames = new ArrayList<String>();

    private UIInput admin;

    @Action
    public String search() {
        if (isAdmin()) {
            return adminSearch();
        }

        return publicSearch();
    }

    private boolean isAdmin() {
        return admin.getValue() != null && admin.getValue().equals(Boolean.TRUE);
    }

    private String publicSearch() {
        //resetting, in case the conversation hasn't ended
        resetSelections();

        List<SearchResultEntry> result = searchService.search(fromStop, toStop, date,
                fromHour, toHour, timeForDeparture);

        resultsModel = new ListDataModel(result);

        if (travelType.equals(TWO_WAY) && returnDate != null) {
            List<SearchResultEntry> returnResult = searchService.search(toStop, fromStop,
                    returnDate, returnFromHour, returnToHour,
                    returnTimeForDeparture);

            returnResultsModel = new ListDataModel(returnResult);
        }

        if (travelType.equals(TWO_WAY) && returnDate == null) {
            addError("choseReturnDate");
        }

        purchaseController.setCurrentStep(Step.SEARCH_RESULTS);

        return Screen.SEARCH_RESULTS.getOutcome();
    }

    @Action
    public void filterToStops() {
        if (fromStop != null && fromStop.length() > 0) {
            toStopNames = searchService.listAllEndStops(fromStop,
                    loggedUserHolder.getLoggedUser());
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
        tickets = new ArrayList<Ticket>();

        int nullsCount = 0;
        int total = 0;

        List<Seat> selectedSeats = seatController.getSeatHandler().getSelectedSeats();
        List<Seat> selectedReturnSeats = null;
        if (seatController.getReturnSeatHandler() != null) {
            selectedReturnSeats = seatController.getReturnSeatHandler().getSelectedSeats();
        }

        for (int i = 0; i < regularTicketsCount; i ++) {
            int seat = -1;
            int returnSeat = -1;
            if (selectedSeats.size() > i) {
                seat = selectedSeats.get(i).getNumber();
            }
            if (selectedReturnSeats != null && selectedReturnSeats.size() > i) {
                returnSeat = selectedReturnSeats.get(i).getNumber();
            }
            if (seat == -1) {
                seat = ticketService.getFirstVacantSeat(selectedEntry);
            }

            Ticket ticket = ticketService.createTicket(selectedEntry, selectedReturnEntry, seat, returnSeat);
            total ++;
            if (ticket == null) {
                nullsCount ++;
            } else {
                tickets.add(ticket);
            }
        }

        for (TicketCount tc : ticketCounts) {
            for (int i = 0; i < tc.getNumberOfTickets(); i++) {
                Ticket tmpTicket = ticketService.createTicket(selectedEntry, selectedReturnEntry, -1, -1, tc.getDiscount());
                total ++;
                if (tmpTicket == null) {
                    nullsCount ++;
                } else {
                    tickets.add(tmpTicket);
                }
            }
        }

        // Validate selected ticket counts
        if (total == 0) {
            addError("mustChooseNonZeroTicketCounts");
            return null;
        }

        // If the ticket isn't created - i.e. another user has just
        // taken the final ticket, redo the search and display an error message
        // else, if there are some failures in ticket purchases, offer an option
        // to either cancel all or continue
        if (nullsCount == total) {
            addError("lastTicketBoughtByAnotherUser");
            return search();
        } else if (nullsCount > 0) {
            addError("someTicketsWerentReserved", new Integer(nullsCount));
            return null;
        }

        purchaseController.getTickets().addAll(tickets);

        // If the user is staff-member, just mark the tickets as sold
        User user = loggedUserHolder.getLoggedUser();
        if (user != null && user.isStaff()) {
            purchaseController.finalizePurchase(user);
            //redo the search
            adminSearch();
            return null; // returning null, because the action is
                         // called from modal window with ajax
        }

        purchaseController.setCurrentStep(Step.PAYMENT);
        return Screen.PAYMENT_SCREEN.getOutcome();
    }

    private Price findPrice(String fromStop, String toStopPerPurchase, Route route) {
        for (Price price : route.getPrices()) {
            if (price.getStartStop().getName().equals(fromStop)
                    && price.getEndStop().getName().equals(toStopPerPurchase)) {

                return price;
            }
        }

        return null;
    }

    public String cancelTickets() {
        return Screen.SEARCH_SCREEN.getOutcome();
    }

    public String confirmPartialPurchase() {
        purchaseController.getTickets().addAll(tickets);
        tickets = null;
        return Screen.PAYMENT_SCREEN.getOutcome();
    }

    private void resetSelections() {
        returnResultsModel = null;
        selectedEntry = null;
        selectedReturnEntry = null;
    }

    public String toSearchScreen() {
        return Screen.SEARCH_SCREEN.getOutcome();
    }

    @PostConstruct
    public void init() {
        User user = loggedUserHolder.getLoggedUser();

        stopNames = searchService.listAllStops(user);
        //toStopNames = searchService.listAllStops(user);

        //Setting a default origin
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
        selectedEntry = ((List<SearchResultEntry>) resultsModel.getWrappedData()).get(selectedId);

        ticketCounts = new ArrayList<TicketCount>(selectedEntry.getRun().getRoute().getDiscounts().size());

        for (Discount discount : selectedEntry.getRun().getRoute().getDiscounts()) {
            TicketCount pd = new TicketCount();
            pd.setDiscount(discount);
            pd.setNumberOfTickets(0);
            ticketCounts.add(pd);
        }
        seatController.setSeatHandler(new SeatHandler(selectedEntry));
    }

    @SuppressWarnings("unchecked")
    public void returnRowSelectionChanged() {
        Integer selectedId = (Integer) returnSelection.getKeys().next();
        selectedReturnRowId = new Long(selectedId);
        selectedReturnEntry = ((List<SearchResultEntry>) returnResultsModel.getWrappedData()).get(selectedId);

        seatController.setReturnSeatHandler(new SeatHandler(selectedReturnEntry));
    }


    /* -------------- ADMIN PANEL METHODS FOLLOW ------------------ */

    private String adminSearch() {
        //Clear the vacant seats cache so that it is recalculated
        ServiceFunctions.clearCache();

        if (toStop != null && toStop.equals("")) {
            toStop = null;
        }

        List<SearchResultEntry> result = searchService.adminSearch(loggedUserHolder.getLoggedUser(),
                fromStop, toStop, date, fromHour, toHour, timeForDeparture);

        resultsModel = new ListDataModel(result);

        return Screen.ADMIN_SEARCH_RESULTS.getOutcome();
    }

    @Action
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

    @Action
    public void purchaseTwoWayTicket() {
        // the same actions as for one way, with additional ones
        purchaseOneWayTicket();

        selectedReturnEntry = null;
        returnResultsModel = null;
        travelType = "TWO_WAY";
    }

    @Action
    public void returnDateSelected() {
        returnResultsModel = new ListDataModel(searchService.adminSearch(loggedUserHolder.getLoggedUser(),
                toStopPerPurchase, fromStop, returnDate, 0, 24, true));
    }

    @Action
    public void toStopSelected() {
        // If the search has been from the admin panel,
        // and no target stop has been chosen, locate the appropriate price
        selectedEntry.setPrice(findPrice(fromStop, toStopPerPurchase, selectedEntry.getRun().getRoute()));
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

    public List<TicketCount> getPurchaseDiscounts() {
        return ticketCounts;
    }

    public void setPurchaseDiscounts(List<TicketCount> purchaseDiscounts) {
        this.ticketCounts = purchaseDiscounts;
    }

    public List<TicketCount> getTicketCounts() {
        return ticketCounts;
    }

    public void setTicketCounts(List<TicketCount> ticketCounts) {
        this.ticketCounts = ticketCounts;
    }

    public int getRegularTicketsCount() {
        return regularTicketsCount;
    }

    public void setRegularTicketsCount(int regularTicketsCount) {
        this.regularTicketsCount = regularTicketsCount;
    }

    public List<Ticket> getTickets() {
        return tickets;
    }

    public void setTickets(List<Ticket> tickets) {
        this.tickets = tickets;
    }

    public boolean isProposeCancellation() {
        return proposeCancellation;
    }

    public void setProposeCancellation(boolean proposeCancellation) {
        this.proposeCancellation = proposeCancellation;
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
}