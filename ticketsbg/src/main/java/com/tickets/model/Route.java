package com.tickets.model;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

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
import javax.persistence.UniqueConstraint;

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
    private Set<RouteHour> routeHours = new HashSet<RouteHour>();

    @OneToMany(mappedBy="route")
    private Set<RouteDay> routeDays = new HashSet<RouteDay>();

    public Route() {
    }

    public Set<RouteHour> getRouteHours(){
        return routeHours;
    }

    public void setRouteHours(Set<RouteHour> routeHours){
        this.routeHours = routeHours;
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

    public Set<RouteDay> getRouteDays() {
        return routeDays;
    }

    public void setRouteDays(Set<RouteDay> routeDays) {
        this.routeDays = routeDays;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
