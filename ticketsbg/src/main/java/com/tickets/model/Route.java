package com.tickets.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OrderBy;
import javax.persistence.Table;

import org.hibernate.annotations.Cascade;
import org.hibernate.annotations.CascadeType;
import org.hibernate.annotations.IndexColumn;
import org.hibernate.annotations.LazyCollection;
import org.hibernate.annotations.LazyCollectionOption;

@Entity
@Table(name = "routes")
public class Route extends DataObject implements Serializable {

    @Id
    @GeneratedValue(strategy=GenerationType.AUTO)
    private int id;

    @ManyToOne
    @JoinColumn(name="firmId", referencedColumnName="firmId")
    private Firm firm;

    @Column
    private String name;

    @Column
    private int seats;

    @OneToMany(mappedBy="route")
    @OrderBy("minutes")
    @LazyCollection(LazyCollectionOption.FALSE)
    @Cascade({CascadeType.ALL, CascadeType.DELETE_ORPHAN})
    private List<RouteHour> routeHours = new ArrayList<RouteHour>();

    @OneToMany(mappedBy="route")
    @OrderBy("day")
    @LazyCollection(LazyCollectionOption.FALSE)
    @Cascade({CascadeType.ALL, CascadeType.DELETE_ORPHAN})
    private List<RouteDay> routeDays = new ArrayList<RouteDay>();

    @OneToMany(mappedBy="route")
    @IndexColumn(name="idx", base=1)
    @LazyCollection(LazyCollectionOption.FALSE)
    @Cascade({CascadeType.ALL, CascadeType.DELETE_ORPHAN})
    private List<Stop> stops = new ArrayList<Stop>();

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

    @Deprecated
    public void setRouteHours(List<RouteHour> routeHours) {
        this.routeHours = routeHours;
    }

    public List<RouteDay> getRouteDays() {
        return routeDays;
    }

    public void setRouteDays(List<RouteDay> routeDays) {
        this.routeDays = routeDays;
    }

    public void addRouteDay(RouteDay rd) {

        rd.setRoute(this);
        routeDays.add(rd);
    }
    public List<Stop> getStops() {
        return stops;
    }

    @Deprecated
    public void setStops(List<Stop> stops) {
        this.stops = stops;
    }

    public void addStop(Stop stop) {

        stop.setRoute(this);
        stops.add(stop);
    }

    public void addHour(RouteHour hour) {

        hour.setRoute(this);
        routeHours.add(hour);
    }

}