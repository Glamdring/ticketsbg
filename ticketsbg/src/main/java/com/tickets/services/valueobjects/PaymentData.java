package com.tickets.services.valueobjects;

import java.io.Serializable;

import com.tickets.model.Order;

public class PaymentData implements Serializable {

    private String encoded;
    private String checksum;
    private Order order;

    public String getEncoded() {
        return encoded;
    }
    public void setEncoded(String encoded) {
        this.encoded = encoded;
    }
    public String getChecksum() {
        return checksum;
    }
    public void setChecksum(String checksum) {
        this.checksum = checksum;
    }
    public Order getOrder() {
        return order;
    }
    public void setOrder(Order order) {
        this.order = order;
    }
}
