package com.tickets.mocks;

import org.apache.commons.mail.Email;

public interface EmailListener {

    public void emailSent(Email email);
}
