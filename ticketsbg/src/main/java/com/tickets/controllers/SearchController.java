package com.tickets.controllers;

import java.util.Date;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.model.ListDataModel;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import com.tickets.annotations.Action;
import com.tickets.model.Run;
import com.tickets.services.SearchService;

@Controller("searchController")
@Scope("conversation.access")
public class SearchController extends BaseController {

    @Autowired
    private SearchService searchService;

    private String fromStop;
    private String toStop;
    private int fromHour = 0;
    private int toHour = 24;
    private Date date;
    private boolean timeForDeparture = true;

    private ListDataModel resultsModel;
    private List<String> stopNames;
    private Integer[] hoursFrom;
    private Integer[] hoursTo;

    @Action
    public String search() {
        List<Run> result = searchService.search(fromStop, toStop, date,
                fromHour, toHour, timeForDeparture);

        resultsModel = new ListDataModel(result);

        return "searchResults";
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
}
