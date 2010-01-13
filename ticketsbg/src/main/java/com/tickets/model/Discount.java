package com.tickets.model;

import java.io.Serializable;
import java.math.BigDecimal;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(name="discounts")
public class Discount extends DataObject implements Serializable, Selectable {

    @Id
    @GeneratedValue(strategy=GenerationType.AUTO)
    private int discountId;

    @Column
    private String name;

    @Column
    private DiscountType discountType;

    @Column
    private BigDecimal value = BigDecimal.ZERO;

    @Column
    private BigDecimal twoWayValue = BigDecimal.ZERO;

    @ManyToOne
    private Route route;

    @Column
    private String description;

    @ManyToOne
    private Stop startStop;

    @ManyToOne
    private Stop endStop;

    @Column
    private boolean currentDayOnly;

    public int getDiscountId() {
        return discountId;
    }

    public void setDiscountId(int discountId) {
        this.discountId = discountId;
    }

    public DiscountType getDiscountType() {
        return discountType;
    }

    public void setDiscountType(DiscountType discountType) {
        this.discountType = discountType;
    }

    public BigDecimal getValue() {
        return value;
    }

    public void setValue(BigDecimal value) {
        this.value = value;
    }

    public Route getRoute() {
        return route;
    }

    public void setRoute(Route route) {
        this.route = route;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public BigDecimal getTwoWayValue() {
        return twoWayValue;
    }

    public void setTwoWayValue(BigDecimal twoWayValue) {
        this.twoWayValue = twoWayValue;
    }

    @Override
    public String getLabel() {
        return getName();
    }



    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + (currentDayOnly ? 1231 : 1237);
        result = prime * result + ((endStop == null) ? 0 : endStop.hashCode());
        result = prime * result + ((name == null) ? 0 : name.hashCode());
        result = prime * result
                + ((startStop == null) ? 0 : startStop.hashCode());
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
        Discount other = (Discount) obj;
        if (currentDayOnly != other.currentDayOnly)
            return false;
        if (endStop == null) {
            if (other.endStop != null)
                return false;
        } else if (!endStop.equals(other.endStop))
            return false;
        if (name == null) {
            if (other.name != null)
                return false;
        } else if (!name.equals(other.name))
            return false;
        if (startStop == null) {
            if (other.startStop != null)
                return false;
        } else if (!startStop.equals(other.startStop))
            return false;
        return true;
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

    public boolean isCurrentDayOnly() {
        return currentDayOnly;
    }

    public void setCurrentDayOnly(boolean currentDayOnly) {
        this.currentDayOnly = currentDayOnly;
    }
}
