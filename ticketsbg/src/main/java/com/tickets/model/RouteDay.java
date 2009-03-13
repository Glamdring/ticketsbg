package com.tickets.model;

// Generated 2008-1-20 22:59:52 by Hibernate Tools 3.2.0.b9

import java.io.Serializable;

import javax.persistence.AttributeOverride;
import javax.persistence.AttributeOverrides;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

/**
 * RoutesDays generated by hbm2java
 */
@Entity
@Table(name = "routes_days")
public class RouteDay implements Serializable {

     @EmbeddedId
        @AttributeOverrides( {
                @AttributeOverride(name = "routeId", column = @Column(name = "route_id", nullable = false)),
                @AttributeOverride(name = "dayId", column = @Column(name = "day_id", nullable = false)) })
    private RouteDayId id;

    @ManyToOne(cascade=CascadeType.ALL)
    @JoinColumn(name="route_id", insertable = false, updatable = false)
    private Route route;


    @ManyToOne
    @JoinColumn(name="day_id", insertable = false, updatable = false)
    private Day day;

    public Route getRoute() {
        return route;
    }

    public void setRoute(Route route) {
        this.route = route;
    }

    public RouteDay() {
    }

    public RouteDay(RouteDayId id) {
        this.id = id;
    }

    public RouteDayId getId() {
        return this.id;
    }

    public void setId(RouteDayId id) {
        this.id = id;
    }

    public Day getDay() {
        return day;
    }

    public void setDay(Day day) {
        this.day = day;
    }
}