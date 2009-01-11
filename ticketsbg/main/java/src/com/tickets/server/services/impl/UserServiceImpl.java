package com.tickets.server.services.impl;

import java.util.Date;
import java.util.Properties;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Transport;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import javax.xml.bind.ValidationException;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Service;

import com.tickets.client.exceptions.UserException;
import com.tickets.client.model.User;
import com.tickets.client.services.UserService;

@Service("userService")
public class UserServiceImpl implements UserService {

    private static Logger log = Logger.getLogger(UserServiceImpl.class);
    @Override
    public void register(User user) {
        String email = user.getEmail();
    }

    @Override
    public boolean login(String username, char[] password, boolean isStaff) throws UserException {
        log.info(username + " : " + new String(password));
        System.out.println(username + " : " + new String(password));
        if (true) throw new UserException("asd");
        return true;
    }

    private static String EMAIL_HOST = "localhost";
    private static String FROM = "admin@ticketsbg.com";

    private void sendMail(String email, String content, String subject) throws ValidationException {
        try {
            Properties props = System.getProperties();
            // -- Attaching to default Session, or we could start a new one --
            props.put("mail.smtp.host", EMAIL_HOST);
            javax.mail.Session session = javax.mail.Session.getDefaultInstance(props, null);
            // -- Create a new message --
            Message msg = new MimeMessage(session);
            // -- Set the FROM and TO fields --
            msg.setFrom(new InternetAddress(FROM));
            msg.setRecipients(Message.RecipientType.TO,
            InternetAddress.parse(email, false));

            msg.setSubject(subject);
            msg.setText(content);
            // -- Set some other header information --
            msg.setHeader("X-Mailer", "Acacia email");
            msg.setSentDate(new Date());
            // -- Send the message --
            Transport.send(msg);
        }
        catch (Exception ex)
        {
            if (ex instanceof AddressException)
                throw new ValidationException("email.invalid");

            if (ex instanceof MessagingException)
                log.error(ex); // throw validation exception
        }
     }

}