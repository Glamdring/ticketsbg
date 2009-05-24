package com.tickets.model;

public class StopPriceHolder {

    private Stop stop;
    private Price price;
    private boolean leaf;

    public StopPriceHolder() {
        super();
    }

    public StopPriceHolder(Stop stop) {
        super();
        this.stop = stop;
    }
    public Stop getStop() {
        return stop;
    }
    public void setStop(Stop stop) {
        this.stop = stop;
    }
    public Price getPrice() {
        return price;
    }
    public void setPrice(Price price) {
        this.price = price;
    }

    public boolean isLeaf() {
        return leaf;
    }

    public void setLeaf(boolean leaf) {
        this.leaf = leaf;
    }
}
