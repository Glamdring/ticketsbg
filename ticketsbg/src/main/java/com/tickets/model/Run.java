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

import org.hibernate.annotations.LazyCollection;
import org.hibernate.annotations.LazyCollectionOption;

@Entity
@Table(name = "runs")
@NamedQueries( {
        @NamedQuery(
                name = "Run.getLastRunForRoute",
                query = "SELECT run FROM Run run WHERE route=:route ORDER BY time DESC LIMIT 1"
        ),
        // The query implies that the last run has also the last runId (hence the MAX)
        // No other decent way of taking the greatest element in group-by queries
        @NamedQuery(
                name = "Run.getLastRuns",
                query = "SELECT DISTINCT new list(route, MAX(run), MAX(run.time)) FROM Route AS route LEFT OUTER JOIN route.runs AS run WHERE (run.manuallyAdded = false OR run.manuallyAdded IS NULL) GROUP BY route"
        ),
        //TODO optimize. Now does a query for each run (to match a price)
        @NamedQuery(
                name = "Run.search",
                query = "SELECT DISTINCT new com.tickets.model.SearchResultEntry(run, price) FROM Run run, IN(run.route.prices) price " +
                        "WHERE price.startStop.name=:fromStop AND price.endStop.name=:toStop " +
                        "AND price.price > 0 " +
                        "AND run.seatsExceeded = false " +
                        "AND run.route.firm.isActive = true " +
                        "ORDER BY run.time, price.price"
        ),
        @NamedQuery(
                name = "Run.searchByFirm",
                query = "SELECT DISTINCT new com.tickets.model.SearchResultEntry(run, price) FROM Run run, IN(run.route.prices) price " +
                        "WHERE price.startStop.name=:fromStop AND price.endStop.name=:toStop " +
                        "AND price.price > 0 " +
                        "AND run.seatsExceeded = false " +
                        "AND run.route.firm=:firm " +
                        "ORDER BY run.time, price.price"
        ),
        @NamedQuery(
                name = "Run.adminSearch",
                query = "SELECT DISTINCT new com.tickets.model.SearchResultEntry(run, price) FROM Run run, User user, IN(run.route.prices) price, Firm firm LEFT OUTER JOIN firm.agents agent " +
                        "WHERE price.startStop.name=:fromStop AND price.endStop.name=:toStop " +
                        "AND price.price > 0 " +
                        "AND run.route.firm = firm " +
                        "AND user=:user " +
                        "AND (user.firm=firm OR user.agent=agent OR agent IS NULL)" +
                        "ORDER BY run.time, price.price"
        ),

        @NamedQuery(
                name = "Run.adminSearchNoEndStop",
                query = "SELECT DISTINCT new com.tickets.model.SearchResultEntry(run, price) FROM Run run, User user, IN(run.route.prices) price, Firm firm LEFT OUTER JOIN firm.agents agent " +
                        "WHERE price.startStop.name=:fromStop " +
                        "AND price.price > 0 " +
                        "AND run.route.firm = firm " +
                        "AND user=:user " +
                        "AND (user.firm=firm OR user.agent=agent OR agent IS NULL)" +
                        "ORDER BY run.time, price.price"
        )
})
public class Run implements Serializable, Comparable<Run> {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int runId;

    @OneToMany(mappedBy = "run")
    @LazyCollection(LazyCollectionOption.FALSE)
    private Set<Ticket> tickets = new HashSet<Ticket>();

    @OneToMany(mappedBy = "returnRun")
    @LazyCollection(LazyCollectionOption.FALSE)
    private Set<Ticket> returnTickets = new HashSet<Ticket>();

    @ManyToOne
    @JoinColumn(name = "routeId", nullable = false)
    private Route route;

    @Column
    @Temporal(value = TemporalType.TIMESTAMP)
    private Calendar time;

    /**
     * This column value is set to true in cases when this system
     * is used in parallel with another system, which handles
     * the cash-desk sales.
     */
    @Column
    private boolean seatsExceeded;

    @Column
    private boolean manuallyAdded;

    public Run() {
    }

    public Set<Ticket> getTickets() {
        return tickets;
    }

    public void setTickets(Set<Ticket> tickets) {
        this.tickets = tickets;
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

    public Set<Ticket> getReturnTickets() {
        return returnTickets;
    }

    public void setReturnTickets(Set<Ticket> returnTickets) {
        this.returnTickets = returnTickets;
    }

    public boolean isSeatsExceeded() {
        return seatsExceeded;
    }

    public void setSeatsExceeded(boolean seatsExceeded) {
        this.seatsExceeded = seatsExceeded;
    }

    public boolean isManuallyAdded() {
        return manuallyAdded;
    }

    public void setManuallyAdded(boolean manuallyAdded) {
        this.manuallyAdded = manuallyAdded;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((route == null) ? 0 : route.getId());
        result = prime * result + ((time == null) ? 0 : time.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        Run other = (Run) obj;
        if (route == null) {
            if (other.route != null)
                return false;
        } else if (!(route.getId() == other.route.getId()))
            return false;
        if (time == null) {
            if (other.time != null)
                return false;
        } else if (!time.equals(other.time))
            return false;
        return true;
    }

    @Override
    public int compareTo(Run r) {
        if (this.time == null || r.getTime() == null) {
            return 0;
        }
        // returning reversed, so that elements are ordered DESC
        return (-1) * this.time.compareTo(r.getTime());
    }


}
