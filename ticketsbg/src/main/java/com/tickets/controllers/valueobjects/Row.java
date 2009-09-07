package com.tickets.controllers.valueobjects;


public class Row {

    private int id;
    private Seat first;
    private Seat second;
    private Seat third;
    private Seat fourth;
    private Seat middleSeat;

    private boolean separator;

    public Row() {
    }

    public Seat getFirst() {
        return first;
    }

    public void setFirst(Seat first) {
        this.first = first;
    }

    public Seat getSecond() {
        return second;
    }

    public void setSecond(Seat second) {
        this.second = second;
    }

    public Seat getThird() {
        return third;
    }

    public void setThird(Seat third) {
        this.third = third;
    }

    public Seat getFourth() {
        return fourth;
    }

    public void setFourth(Seat fourth) {
        this.fourth = fourth;
    }

    public Seat getMiddleSeat() {
        return middleSeat;
    }

    public void setMiddleSeat(Seat middleSeat) {
        this.middleSeat = middleSeat;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public boolean isSeparator() {
        return separator;
    }

    public void setSeparator(boolean separator) {
        this.separator = separator;
    }
}