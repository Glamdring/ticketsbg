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
import javax.persistence.Table;

/**
 * Runs generated by hbm2java
 */
@Entity
@Table(name = "runs")
public class Run implements java.io.Serializable {

    private int id;

    private int seats;

    private Set<Ticket> tickets = new HashSet<Ticket>();

    private RouteHour routeHour;

    @ManyToOne
    @JoinColumn(name="route_hour_id")
    public RouteHour getRouteHour() {
        return routeHour;
    }

    public void setRouteHour(RouteHour routeHour) {
        this.routeHour = routeHour;
    }

    @OneToMany(mappedBy="run")
    public Set<Ticket> getTickets() {
        return tickets;
    }

    public void setTickets(Set<Ticket> tickets) {
        this.tickets = tickets;
    }

    public Run() {
    }

    public Run(int id, int seats) {
        this.id = id;
        this.seats = seats;
    }

    @Id
    public int getId() {
        return this.id;
    }

    public void setId(int id) {
        this.id = id;
    }

    @Column(name = "seats", nullable = false)
    public int getSeats() {
        return this.seats;
    }

    public void setSeats(int seats) {
        this.seats = seats;
    }


}
