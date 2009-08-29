package com.tickets.model.stats;

import java.io.Serializable;

public class SoldTickets implements Serializable, Comparable<SoldTickets> {

    private String period;
    private int tickets;

    public SoldTickets(String period, int tickets) {
        super();
        this.period = period;
        this.tickets = tickets;
    }

    public String getPeriod() {
        return period;
    }

    public void setPeriod(String period) {
        this.period = period;
    }

    public int getTickets() {
        return tickets;
    }

    public void setTickets(int tickets) {
        this.tickets = tickets;
    }

    @Override
    public int compareTo(SoldTickets o) {
        if (o == null) {
            return -1;
        }
        if (o.getPeriod().equals(this.getPeriod())) {
            return 0;
        }

        return -1;
    }
}
