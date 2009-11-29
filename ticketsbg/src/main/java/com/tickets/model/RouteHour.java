package com.tickets.model;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

/**
 * RouteHour represents an hour entry
 */
@Entity
@Table(name = "routeHours")
public class RouteHour implements Serializable {

    @Id
    @GeneratedValue(strategy=GenerationType.AUTO)
    private int id;

    @Column
    private int minutes;

    @ManyToOne
    @JoinColumn(name="routeId", nullable=false)
    private Route route;

    public Route getRoute() {
        return route;
    }

    public void setRoute(Route route) {
        this.route = route;
    }

    public RouteHour(){
    }

    public int getMinutes() {
        return minutes;
    }

    public void setMinutes(int minutes) {
        this.minutes = minutes;
    }

    public int getId() {
        return this.id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getDisplayLabel() {
        int hours = minutes / 60;
        int mins = minutes % 60;

        String hoursStr = hours < 10 ? "0" + hours : hours+ "";
        String minsStr = mins < 10 ? "0" + mins : mins + "";

        return hoursStr + ":" + minsStr;
    }

}
