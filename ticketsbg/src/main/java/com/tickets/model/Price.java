package com.tickets.model;

import java.io.Serializable;
import java.math.BigDecimal;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

/**
 * The Price object is used to indicate a price between two stops
 * It is often used to mark a journey between two points
 */
@Entity
@Table(name = "prices")
@NamedQueries( {
    @NamedQuery(
            name = "Price.get",
            query = "select p from Price p where p.startStop=:startStop AND "
                + "p.endStop=:endStop AND p.route=:route")
})
public class Price implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int priceId;

    @ManyToOne
    @JoinColumn(name = "routeId", nullable = false)
    private Route route;

    /**
     * Starting stop. Annotated with cascade-all because stops should be
     * persisted before the price itself.
     */
    @ManyToOne
    @JoinColumn(name = "startStopId", nullable = false)
    //@Cascade({CascadeType.ALL, CascadeType.DELETE_ORPHAN})
    private Stop startStop;

    /**
     * End stop. Annotated with cascade-all because stops should be persisted
     * before the price itself.
     */
    @ManyToOne
    @JoinColumn(name = "endStopId", nullable = false)
    //@Cascade({CascadeType.ALL, CascadeType.DELETE_ORPHAN})
    private Stop endStop;

    @Column
    private BigDecimal price = BigDecimal.ZERO;

    @Column
    private BigDecimal twoWayPrice = BigDecimal.ZERO;

    public Price() {
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public int getPriceId() {
        return priceId;
    }

    public void setPriceId(int priceId) {
        this.priceId = priceId;
    }

    public Route getRoute() {
        return route;
    }

    public void setRoute(Route route) {
        this.route = route;
    }

    public Stop getStartStop() {
        return startStop;
    }

    public void setStartStop(Stop startStop) {
        this.startStop = startStop;
    }

    public Stop getEndStop() {
        return endStop;
    }

    public void setEndStop(Stop endStop) {
        this.endStop = endStop;
    }

    public BigDecimal getTwoWayPrice() {
        return twoWayPrice;
    }

    public void setTwoWayPrice(BigDecimal twoWayPrice) {
        this.twoWayPrice = twoWayPrice;
    }
}
