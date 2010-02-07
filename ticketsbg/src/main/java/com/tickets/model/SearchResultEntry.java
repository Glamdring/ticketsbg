package com.tickets.model;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Calendar;

import com.tickets.constants.Constants;

public class SearchResultEntry implements Serializable {

    private Run run;
    private Price price;
    private Calendar departureTime;
    private Calendar arrivalTime;

    public SearchResultEntry() {
    }

    // constructor for accommodating two more arguments. required by HSQLDB
    @SuppressWarnings("unused")
    public SearchResultEntry(Run run, Price price, Calendar time, BigDecimal priceValue) {
        this(run, price);
    }

    public SearchResultEntry(Run run, Price price) {
        super();
        this.run = run;
        this.price = price;
    }

    public Run getRun() {
        return run;
    }
    public void setRun(Run run) {
        this.run = run;
    }
    public Price getPrice() {
        return price;
    }
    public void setPrice(Price price) {
        this.price = price;
    }

    public Calendar getDepartureTime() {
        return departureTime;
    }

    public void setDepartureTime(Calendar departureTime) {
        this.departureTime = departureTime;
    }

    public Calendar getArrivalTime() {
        return arrivalTime;
    }

    public void setArrivalTime(Calendar arrivalTime) {
        this.arrivalTime = arrivalTime;
    }

    public int getDuration() {
        return (int) ((arrivalTime.getTimeInMillis() - departureTime
                .getTimeInMillis()) / Constants.ONE_MINUTE);
    }
}
