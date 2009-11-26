package com.tickets.controllers;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import com.tickets.annotations.Action;
import com.tickets.controllers.security.AccessLevel;
import com.tickets.model.Run;
import com.tickets.services.TravelListService;
import com.tickets.services.valueobjects.TravelListSubroute;

@Controller("travelListController")
@Scope("conversation.access")
@Action(accessLevel=AccessLevel.CASH_DESK)
public class TravelListController extends BaseController {
    private Run run;
    private List<TravelListSubroute> travelList = new ArrayList<TravelListSubroute>();
    private boolean onlineOnly = true;
    private boolean showNames = true;

    private String phoneNumber;

    @Autowired
    private TravelListService service;

    public String openTravelList() {
        travelList = service.getTravelList(run, onlineOnly);

        return "travelList";
    }

    public void sendSMS() {
        service.sendSMS(phoneNumber, run);
    }

    public Run getRun() {
        return run;
    }

    public void setRun(Run run) {
        this.run = run;
    }

    public List<TravelListSubroute> getTravelList() {
        return travelList;
    }

    public void setTravelList(List<TravelListSubroute> travelList) {
        this.travelList = travelList;
    }

    public boolean isOnlineOnly() {
        return onlineOnly;
    }

    public void setOnlineOnly(boolean onlineOnly) {
        this.onlineOnly = onlineOnly;
    }

    public boolean isShowNames() {
        return showNames;
    }

    public void setShowNames(boolean showNames) {
        this.showNames = showNames;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }
}
