package com.tickets.model;

import javax.persistence.Column;
import javax.persistence.Embeddable;

@Embeddable
public class SeatSettings {

    @Column
    private boolean startRight;

    @Column
    private boolean lastRowHasFourSeats;

    @Column
    private boolean doubleDecker;

    @Column
    private int numberOfSeatsDownstairs;

    public boolean isStartRight() {
        return startRight;
    }

    public void setStartRight(boolean startRight) {
        this.startRight = startRight;
    }

    public boolean isLastRowHasFourSeats() {
        return lastRowHasFourSeats;
    }

    public void setLastRowHasFourSeats(boolean lastRowHasFourSeats) {
        this.lastRowHasFourSeats = lastRowHasFourSeats;
    }

    public boolean isDoubleDecker() {
        return doubleDecker;
    }

    public void setDoubleDecker(boolean doubleDecker) {
        this.doubleDecker = doubleDecker;
    }

    public int getNumberOfSeatsDownstairs() {
        return numberOfSeatsDownstairs;
    }

    public void setNumberOfSeatsDownstairs(int numberOfSeatsDownstairs) {
        this.numberOfSeatsDownstairs = numberOfSeatsDownstairs;
    }
}
