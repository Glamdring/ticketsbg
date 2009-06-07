package com.tickets.model;

public class RunPriceHolder {

    private Run run;
    private Price price;

    public RunPriceHolder(Run run, Price price) {
        super();
        this.run = run;
        this.price = price;
    }

    public Run getRun() {
        return run;
    }
    public void setRun(Run run) {
        this.run = run;
    }
    public Price getPrice() {
        return price;
    }
    public void setPrice(Price price) {
        this.price = price;
    }

}
