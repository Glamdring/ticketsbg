package com.tickets.services;

import org.apache.commons.mail.Email;
import org.apache.commons.mail.EmailException;
import org.springframework.stereotype.Service;

import com.tickets.utils.GeneralUtils;

@Service("emailService")
public class EmailServiceImpl implements EmailService {

    @Override
    public Email createEmail(boolean html) {
        return GeneralUtils.getPreconfiguredMail(html);
    }

    @Override
    public void send(Email email) throws EmailException {
        email.send();
    }

}
