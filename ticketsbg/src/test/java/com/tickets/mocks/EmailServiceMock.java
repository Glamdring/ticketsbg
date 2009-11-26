package com.tickets.mocks;

import org.apache.commons.mail.Email;
import org.apache.commons.mail.EmailException;
import org.junit.Ignore;

import com.tickets.services.EmailService;
import com.tickets.services.EmailServiceImpl;

@Ignore
public class EmailServiceMock extends EmailServiceImpl implements EmailService {

    @Override
    public void send(Email email) throws EmailException {
        // do nothing
    }
}
