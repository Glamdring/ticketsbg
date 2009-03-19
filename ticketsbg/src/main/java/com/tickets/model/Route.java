package com.tickets.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OrderBy;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

import org.hibernate.annotations.IndexColumn;

@Entity
@Table(name = "routes", uniqueConstraints = @UniqueConstraint(columnNames = "firm_id"))
public class Route extends DataObject implements Serializable {

    @Id
    @GeneratedValue(strategy=GenerationType.AUTO)
    private int id;

    @ManyToOne
    @JoinColumn(name="firm_id", referencedColumnName="firmId")
    private Firm firm;

    @Column
    private String name;

    @Column
    private int seats;

    @OneToMany(mappedBy="route")
    @OrderBy("hour")
    private List<RouteHour> routeHours;

    @OneToMany(mappedBy="route", fetch=FetchType.EAGER, cascade=CascadeType.ALL)
    @OrderBy("routeDayId")
    private List<RouteDay> routeDays;

    @OneToMany(mappedBy="route", fetch=FetchType.EAGER, cascade=CascadeType.ALL)
    @IndexColumn(name="idx")
    private List<Stop> stops;

    public Route() {
    }

    public int getId() {
        return this.id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Firm getFirm() {
        return firm;
    }

    public void setFirm(Firm firm) {
        this.firm = firm;
    }

    public int getSeats() {
        return this.seats;
    }

    public void setSeats(int seats) {
        this.seats = seats;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<RouteHour> getRouteHours() {
        return routeHours;
    }

    public void setRouteHours(List<RouteHour> routeHours) {
        this.routeHours = routeHours;
    }

    public List<RouteDay> getRouteDays() {
        return routeDays;
    }

    public void setRouteDays(List<RouteDay> routeDays) {
        this.routeDays = routeDays;
    }

    public List<Stop> getStops() {
        return stops;
    }

    @Deprecated
    public void setStops(List<Stop> stops) {
        this.stops = stops;
    }

    public void addStop(Stop stop) {
        if (stops == null)
            stops = new ArrayList<Stop>();

        stop.setRoute(this);
        stops.add(stop);
    }

}