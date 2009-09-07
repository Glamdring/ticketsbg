package com.tickets.controllers.valueobjects;

public class TicketDisplayInfo implements Comparable<TicketDisplayInfo>{

    private String ticketCode;
    private int seatNumber;
    public String getTicketCode() {
        return ticketCode;
    }
    public void setTicketCode(String ticketCode) {
        this.ticketCode = ticketCode;
    }
    public int getSeatNumber() {
        return seatNumber;
    }
    public void setSeatNumber(int seatNumber) {
        this.seatNumber = seatNumber;
    }
    @Override
    public int compareTo(TicketDisplayInfo tdi) {
        if (this.getTicketCode() == null) {
            return -1;
        }

        return this.getTicketCode().compareTo(tdi.getTicketCode());
    }

}
