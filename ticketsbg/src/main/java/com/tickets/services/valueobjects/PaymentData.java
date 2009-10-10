package com.tickets.services.valueobjects;

import java.io.Serializable;

public class PaymentData implements Serializable {

    private String encoded;
    private String checksum;

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


}
