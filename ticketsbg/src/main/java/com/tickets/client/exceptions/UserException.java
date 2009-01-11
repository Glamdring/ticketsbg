package com.tickets.client.exceptions;

import com.google.gwt.user.client.rpc.IsSerializable;

public class UserException extends Exception implements IsSerializable {

    public UserException() {

    }

    public UserException(String msg) {
        super(msg);
    }
}
