package com.tickets.model;

// Generated 2008-1-20 22:59:52 by Hibernate Tools 3.2.0.b9

import java.util.HashSet;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OrderBy;
import javax.persistence.Table;

/**
 * RouteHour generated by hbm2java
 */
@Entity
@Table(name = "runs")
public class RouteHour implements java.io.Serializable {

    private int id;

    private int routeId;

    private int hour;

    private Route route;

    private Set<Run> runs = new HashSet<Run>();

    @ManyToOne
    @JoinColumn(name="FK_routes_hours_routes")
    public Route getRoute() {
        return route;
    }

    public void setRoute(Route route) {
        this.route = route;
    }

    @OneToMany(mappedBy="routeHour")
    public Set<Run> getRuns() {
        return runs;
    }

    public void setRuns(Set<Run> runs) {
        this.runs = runs;
    }

    public RouteHour(){
    }

    public RouteHour(int id, int routeId, int hour) {
        this.id = id;
        this.routeId = routeId;
        this.hour = hour;
    }

    public int getHour() {
        return hour;
    }

    public void setHour(int hour) {
        this.hour = hour;
    }

    public int getRouteId() {
        return routeId;
    }

    public void setRouteId(int routeId) {
        this.routeId = routeId;
    }

    @Id
    @Column(name = "id", nullable = false)
    public int getId() {
        return this.id;
    }

    public void setId(int id) {
        this.id = id;
    }


}
