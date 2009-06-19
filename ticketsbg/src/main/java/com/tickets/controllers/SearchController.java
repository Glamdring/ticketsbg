package com.tickets.controllers;

import java.util.Date;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.model.ListDataModel;

import org.apache.myfaces.orchestra.conversation.annotations.ConversationName;
import org.richfaces.model.selection.SimpleSelection;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import com.tickets.annotations.Action;
import com.tickets.model.SearchResultEntry;
import com.tickets.model.Ticket;
import com.tickets.services.SearchService;
import com.tickets.services.TicketService;
/**
 * Controller which handles the whole purchase process.
 * Subcontrollers are included for different tasks
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
    private PurchaseController purchaseController;

    private static final String TWO_WAY = "twoWay";

    private String fromStop;
    private String toStop;

    private int fromHour = 0;
    private int toHour = 24;
    private Date date;
    private boolean timeForDeparture = true;

    private int returnFromHour = 0;
    private int returnToHour = 24;
    private Date returnDate;
    private boolean returnTimeForDeparture = true;

    private ListDataModel resultsModel;
    private ListDataModel returnResultsModel;

    private List<String> stopNames;
    private Integer[] hoursFrom;
    private Integer[] hoursTo;
    private String travelType = TWO_WAY;

    private SearchResultEntry selectedEntry;
    private SimpleSelection selection;
    private Long selectedRowId;

    private SearchResultEntry selectedReturnEntry;
    private SimpleSelection returnSelection;
    private Long selectedReturnRowId;


    @Action
    public String search() {
        //resetting, in case the conversation hasn't ended
        resetSelections();

        List<SearchResultEntry> result = searchService.search(fromStop, toStop, date,
                fromHour, toHour, timeForDeparture);

        resultsModel = new ListDataModel(result);

        if (travelType.equals(TWO_WAY)) {
            List<SearchResultEntry> returnResult = searchService.search(toStop, fromStop,
                    returnDate, returnFromHour, returnToHour,
                    returnTimeForDeparture);

            returnResultsModel = new ListDataModel(returnResult);
        }

        purchaseController.setCurrentStep(Step.SEARCH_RESULTS);

        return "searchResults";
    }

    @Action
    public String proceedToPersonalInformation() {
        //TODO client-side validate selectedEntry != null

        Ticket ticket = ticketService.createTicket(selectedEntry, selectedReturnEntry);
        purchaseController.setTicket(ticket);

        return Screen.PERSONAL_INFORMATION_SCREEN.getOutcome();
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
        stopNames = searchService.listAllStops();
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
    }

    @SuppressWarnings("unchecked")
    public void returnRowSelectionChanged() {
        Integer selectedId = (Integer) returnSelection.getKeys().next();
        selectedReturnRowId = new Long(selectedId);
        selectedReturnEntry = ((List<SearchResultEntry>) returnResultsModel.getWrappedData()).get(selectedId);
    }

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
}
