package com.tickets.mocks;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.mail.Email;
import org.apache.commons.mail.EmailException;
import org.junit.Ignore;

import com.tickets.services.EmailService;
import com.tickets.services.EmailServiceImpl;

@Ignore
public class EmailServiceMock extends EmailServiceImpl implements EmailService {

    private List<EmailListener> listeners = new ArrayList<EmailListener>();

    @Override
    public void send(Email email) throws EmailException {
        for (EmailListener listener : listeners) {
            listener.emailSent(email);
        }
    }

    public boolean addListener(EmailListener e) {
        return listeners.add(e);
    }

    public boolean remove(Object o) {
        return listeners.remove(o);
    }


}
