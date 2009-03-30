package com.tickets.model;

public class StopPriceHolder {

    private Stop stop;
    private Price price;

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

}
