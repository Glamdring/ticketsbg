package com.tickets.services;

import org.apache.commons.mail.Email;
import org.apache.commons.mail.EmailException;

public interface EmailService {

    Email createEmail(boolean html);

    void send(Email email) throws EmailException;
}
