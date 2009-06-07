package com.tickets.model;

import java.io.Serializable;
import java.util.Calendar;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

@Entity
@Table(name = "runs")
@NamedQueries( {
        @NamedQuery(
                name = "Run.getLastRunForRoute",
                query = "SELECT run FROM Run run WHERE route=:route ORDER BY time DESC LIMIT 1"
        ),
        @NamedQuery(
                name = "Run.getLastRuns",
                query = "SELECT DISTINCT new list(route, run, run.time) FROM Route AS route LEFT OUTER JOIN route.runs AS run GROUP BY route HAVING (SELECT MAX(time) FROM route.runs) = run.time OR (SELECT MAX(time) FROM route.runs) IS NULL "
        ),
        //TODO optimize. Now does a query for each run (to match a price)
        @NamedQuery(
                name = "Run.search",
                query = "SELECT DISTINCT new com.tickets.model.SearchResultEntry(run, price) FROM Run run, IN(run.route.prices) price " +
                        "WHERE price.startStop.name=:fromStop AND price.endStop.name=:toStop " +
                        "AND price.price > 0 " +
                        "ORDER BY run.time"
        )
})
public class Run implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int runId;

    @OneToMany(mappedBy = "run")
    private Set<Ticket> tickets = new HashSet<Ticket>();

    @ManyToOne
    @JoinColumn(name = "routeId", nullable = false)
    private Route route;

    @Column
    @Temporal(value = TemporalType.TIMESTAMP)
    private Calendar time;

    public Set<Ticket> getTickets() {
        return tickets;
    }

    public void setTickets(Set<Ticket> tickets) {
        this.tickets = tickets;
    }

    public Run() {
    }

    public int getRunId() {
        return runId;
    }

    public void setRunId(int runId) {
        this.runId = runId;
    }

    public Calendar getTime() {
        return time;
    }

    public void setTime(Calendar time) {
        this.time = time;
    }

    public Route getRoute() {
        return route;
    }

    public void setRoute(Route route) {
        this.route = route;
    }
}
