package com.tickets.services;

import java.util.List;

import com.tickets.model.Run;
import com.tickets.services.valueobjects.TravelListSubroute;

public interface TravelListService extends Service {

    List<TravelListSubroute> getTravelList(Run run, boolean onlineOnly);

    void sendSMS(String phoneNumber, Run run);

}
