package com.tickets.services.valueobjects;

import java.io.Serializable;

public class TicketInfo implements Serializable {

    private String routeName;
    private String ticketCode;
    private String departureTime;
    private String returnDepartureTime;
    private String seats;
    private String returnSeats;
    private String firmName;
    private String price;
    private String showTicketAtMessage;

    public String getRouteName() {
        return routeName;
    }
    public void setRouteName(String routeName) {
        this.routeName = routeName;
    }
    public String getTicketCode() {
        return ticketCode;
    }
    public void setTicketCode(String ticketCode) {
        this.ticketCode = ticketCode;
    }
    public String getDepartureTime() {
        return departureTime;
    }
    public void setDepartureTime(String departureTime) {
        this.departureTime = departureTime;
    }
    public String getReturnDepartureTime() {
        return returnDepartureTime;
    }
    public void setReturnDepartureTime(String returnDepartureTime) {
        this.returnDepartureTime = returnDepartureTime;
    }
    public String getSeats() {
        return seats;
    }
    public void setSeats(String seats) {
        this.seats = seats;
    }
    public String getFirmName() {
        return firmName;
    }
    public void setFirmName(String firmName) {
        this.firmName = firmName;
    }
    public String getPrice() {
        return price;
    }
    public void setPrice(String price) {
        this.price = price;
    }
    public String getReturnSeats() {
        return returnSeats;
    }
    public void setReturnSeats(String returnSeats) {
        this.returnSeats = returnSeats;
    }
    public String getShowTicketAtMessage() {
        return showTicketAtMessage;
    }
    public void setShowTicketAtMessage(String showTicketAtMessage) {
        this.showTicketAtMessage = showTicketAtMessage;
    }
}
