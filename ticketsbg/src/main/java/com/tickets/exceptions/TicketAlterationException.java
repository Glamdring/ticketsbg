package com.tickets.exceptions;

public class TicketAlterationException extends RuntimeException {

    private int hoursBeforeTravelAlterationAllowed;
    private boolean alreadyAltered = false;

    /**
     * Constructs the exception when the reason is an attempt
     * to alter a ticket for the second time
     */
    public TicketAlterationException() {
        alreadyAltered = true;
    }

    /**
     * Constructs the exception when the deadline for altering
     * has passed
     *
     * @param hoursBeforeTravelAlterationAllowed
     */
    public TicketAlterationException(int hoursBeforeTravelAlterationAllowed) {
        this.hoursBeforeTravelAlterationAllowed = hoursBeforeTravelAlterationAllowed;
    }

    public int getHoursBeforeTravelAlterationAllowed() {
        return hoursBeforeTravelAlterationAllowed;
    }

    public void setHoursBeforeTravelAlterationAllowed(
            int hoursBeforeTravelAlterationAllowed) {
        this.hoursBeforeTravelAlterationAllowed = hoursBeforeTravelAlterationAllowed;
    }

    public boolean isAlreadyAltered() {
        return alreadyAltered;
    }

    public void setAlreadyAltered(boolean alreadyAltered) {
        this.alreadyAltered = alreadyAltered;
    }
}
