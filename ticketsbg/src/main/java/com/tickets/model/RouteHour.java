package com.tickets.model;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
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

    @ManyToOne(cascade=CascadeType.ALL)
    @JoinColumn(name="routeId", nullable=false)
    private Route route;

    @OneToMany(mappedBy="routeHour")
    private Set<Run> runs = new HashSet<Run>();

    public Route getRoute() {
        return route;
    }

    public void setRoute(Route route) {
        this.route = route;
    }


    public Set<Run> getRuns() {
        return runs;
    }

    public void setRuns(Set<Run> runs) {
        this.runs = runs;
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
        return hours + ":" + mins;
    }

}
