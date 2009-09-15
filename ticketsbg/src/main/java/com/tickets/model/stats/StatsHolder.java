package com.tickets.model.stats;

import java.io.Serializable;
import java.math.BigDecimal;

public class StatsHolder implements Serializable, Comparable<StatsHolder> {

    private String period;
    private BigDecimal value;

    public StatsHolder(String period, BigDecimal value) {
        super();
        this.period = period;
        this.value = value;
    }

    public String getPeriod() {
        return period;
    }

    public void setPeriod(String period) {
        this.period = period;
    }

    public BigDecimal getValue() {
        return value;
    }

    public void setValue(BigDecimal value) {
        this.value = value;
    }

    @Override
    public int compareTo(StatsHolder o) {
        if (o == null) {
            return -1;
        }
        if (o.getPeriod().equals(this.getPeriod())) {
            return 0;
        }

        return -1;
    }
}
