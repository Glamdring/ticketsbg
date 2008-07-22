package com.tickets.client.model;

// Generated 2008-1-20 22:59:52 by Hibernate Tools 3.2.0.b9

import javax.persistence.Column;
import javax.persistence.Embeddable;

/**
 * TicketsId generated by hbm2java
 */
@Embeddable
public class TicketsId implements java.io.Serializable {

    private int runId;

    private short seat;

    public TicketsId() {
    }

    public TicketsId(int runId, short seat) {
        this.runId = runId;
        this.seat = seat;
    }

    @Column(name = "run_id", nullable = false)
    public int getRunId() {
        return this.runId;
    }

    public void setRunId(int runId) {
        this.runId = runId;
    }

    @Column(name = "seat", nullable = false)
    public short getSeat() {
        return this.seat;
    }

    public void setSeat(short seat) {
        this.seat = seat;
    }

    @Override
    public boolean equals(Object other) {
        if ((this == other))
            return true;
        if ((other == null))
            return false;
        if (!(other instanceof TicketsId))
            return false;
        TicketsId castOther = (TicketsId) other;

        return (this.getRunId() == castOther.getRunId())
                && (this.getSeat() == castOther.getSeat());
    }

    @Override
    public int hashCode() {
        int result = 17;

        result = 37 * result + this.getRunId();
        result = 37 * result + this.getSeat();
        return result;
    }

}
