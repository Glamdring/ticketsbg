package com.tickets.model;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.persistence.JoinTable;

import org.hibernate.annotations.CollectionOfElements;
import org.hibernate.annotations.LazyCollection;
import org.hibernate.annotations.LazyCollectionOption;

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

    /**
     * Ids, starting from left to right, 4 in a row
     */
    @CollectionOfElements(targetElement=Integer.class)
    @JoinTable
    @LazyCollection(LazyCollectionOption.FALSE)
    private List<Integer> missingSeats = new ArrayList<Integer>();

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

    public List<Integer> getMissingSeats() {
        return missingSeats;
    }

    public void setMissingSeats(List<Integer> missingSeats) {
        this.missingSeats = missingSeats;
    }
}
